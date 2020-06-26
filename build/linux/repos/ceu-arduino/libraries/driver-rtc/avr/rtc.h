#ifndef DS3231_h
#define DS3231_h

#define DS3231_ADDRESS              (0x68)

#define DS3231_REG_TIME             (0x00)
#define DS3231_REG_ALARM_1          (0x07)
#define DS3231_REG_ALARM_2          (0x0B)
#define DS3231_REG_CONTROL          (0x0E)
#define DS3231_REG_STATUS           (0x0F)
#define DS3231_REG_TEMPERATURE      (0x11)

typedef enum
{
    DS3231_1HZ          = 0x00,
    DS3231_4096HZ       = 0x01,
    DS3231_8192HZ       = 0x02,
    DS3231_32768HZ      = 0x03
} DS3231_sqw_t;

typedef enum
{
    DS3231_EVERY_SECOND   = 0b00001111,
    DS3231_MATCH_S        = 0b00001110,
    DS3231_MATCH_M_S      = 0b00001100,
    DS3231_MATCH_H_M_S    = 0b00001000,
    DS3231_MATCH_DT_H_M_S = 0b00000000,
    DS3231_MATCH_DY_H_M_S = 0b00010000
} DS3231_alarm1_t;

typedef enum
{
    DS3231_EVERY_MINUTE   = 0b00001110,
    DS3231_MATCH_M        = 0b00001100,
    DS3231_MATCH_H_M      = 0b00001000,
    DS3231_MATCH_DT_H_M   = 0b00000000,
    DS3231_MATCH_DY_H_M   = 0b00010000
} DS3231_alarm2_t;

uint8_t bcd2dec(uint8_t bcd)
{
    return ((bcd / 16) * 10) + (bcd % 16);
}

uint8_t dec2bcd(uint8_t dec)
{
    return ((dec / 10) * 16) + (dec % 10);
}

const uint8_t daysArray [] PROGMEM = { 31,28,31,30,31,30,31,31,30,31,30,31 };
const uint8_t dowArray[] PROGMEM = { 0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4 };

uint8_t dow(uint16_t y, uint8_t m, uint8_t d)
{
    uint8_t dow;

    y -= m < 3;
    dow = ((y + y/4 - y/100 + y/400 + pgm_read_byte(dowArray+(m-1)) + d) % 7);

    if (dow == 0)
    {
        return 7;
    }

    return dow;
}

#endif