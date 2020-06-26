# RTC (Real Time Clock)
This driver can be used to communicate to the Real Time Clock via I2C by a microcontroller.

## Summary

- Events

    ```
    code/await Rtc (var on/off v)->none;        // await RTC to begin or stop


    // events to read and write the RTC registers

    code/await Rtc_write_register8 (var u8 reg, var u8 value) -> none;
    code/await Rtc_read_register8 (var u8 reg) -> u8;


    // TIME

    code/await Rtc_get_time (var&[] u16 values) -> none;
    code/await Rtc_set_time (var u16 year,var u8 month, var u8 day, var u8 hour, var u8 minute, var u8 second) -> none;


    // TEMPERATURE

    code/await Rtc_force_conversion (none) -> none;
    code/await Rtc_read_temperature (none) -> r32;


    // ALARMS

    code/await Rtc_get_alarm1 (var&[] u8 values) -> none;
    code/await Rtc_get_alarm_type1(none) -> u16;
    code/await Rtc_arm_alarm1(var bool armed) -> none;
    code/await Rtc_clear_alarm1(none) -> none;
    code/await Rtc_set_alarm1(var u8 dydw, var u8 hour, var u8 minute, var u8 second, var u8 mode, var bool armed) -> none;
    code/await Rtc_is_alarm1(var bool clear) -> bool;
    code/await Rtc_is_armed1 (none) -> bool;
    code/await Rtc_get_alarm2(var&[] u8 values) -> none;
    code/await Rtc_get_alarm_type2(none) -> u8;
    code/await Rtc_arm_alarm2(var bool armed) -> none;
    code/await Rtc_is_armed2(none) -> on/off;
    code/await Rtc_clear_alarm2(none) -> none;
    code/await Rtc_set_alarm2(var u8 dydw,var u8 hour,var u8 minute, var u8 mode, var bool armed) -> none;
    code/await Rtc_is_alarm2(var bool clear) ->  bool;
    ```

- Defines

    ```
    // RTC ADDRESS
    #define DS3231_ADDRESS              (0x68)
    
    // REGISTERS OF RTC
    #define DS3231_REG_TIME             (0x00)
    #define DS3231_REG_ALARM_1          (0x07)
    #define DS3231_REG_ALARM_2          (0x0B)
    #define DS3231_REG_CONTROL          (0x0E)
    #define DS3231_REG_STATUS           (0x0F)
    #define DS3231_REG_TEMPERATURE      (0x11)
    
    // ALARM 1
    typedef enum
    {
        DS3231_EVERY_SECOND   = 0b00001111,
        DS3231_MATCH_S        = 0b00001110,
        DS3231_MATCH_M_S      = 0b00001100,
        DS3231_MATCH_H_M_S    = 0b00001000,
        DS3231_MATCH_DT_H_M_S = 0b00000000,
        DS3231_MATCH_DY_H_M_S = 0b00010000
    } DS3231_alarm1_t;
    
    // ALARM 2
    typedef enum
    {
        DS3231_EVERY_MINUTE   = 0b00001110,
        DS3231_MATCH_M        = 0b00001100,
        DS3231_MATCH_H_M      = 0b00001000,
        DS3231_MATCH_DT_H_M   = 0b00000000,
        DS3231_MATCH_DY_H_M   = 0b00010000
    } DS3231_alarm2_t;
    ```

## Includes

To use RTC driver, a program must always include `rtc.ceu`:

```
#include "rtc.ceu"
```

## Events

Céu-Arduino provides declarations to communicate to RTC via I2C. The API for the ceu driver for RTC is as follows:

- Include the driver after cloning this repository in libraries with #include "rtc.ceu".
- await the Rtc with on to begin the Two Wire Interface and start the RTC by writing to it's register. It has one parameter for turning on/off.
    
    ```
    await Rtc(on);
    ```

- To stop using the RTC driver you can turn it off by awaiting the Rtc with off
    
    ```
    await Rtc(off);
    ```

## Examples

### Time Display

- The following program illustrates the display of time from the RTC.
- The Rtc_set_time sets the time. It has parameters year, month, day, hour, minute and second respectively. We can await Rtc_set_time to set the time. The parameters are u8 type except for year which is u16.
- Rtc_get_time gets the time from the Real Time Clock and sets the parameter passed accordingly.
- The parameter passed should be a vector of length 7 and of type u16.
- the Values respectively are
    - time_data[6] = year
    - time_data[5] = month
    - time_data[4] = day
    - time_data[3] = day of the week
    - time_data[2] = hours
    - time_data[1] = minutes
    - time_data[0] = seconds

```
#include "wclock.ceu"
#include "rtc.ceu"

_Serial.begin(9600);
await Rtc(on);
await Rtc_set_time(2018,2,6,23,58,0);//u16 year, u8 month, u8 day, u8 hour, u8 minute, u8 second)

loop do
    var[7] u16 time_data;
    await Rtc_get_time(&time_data);
    _Serial.print("Date and Time: ");
    _Serial.print(time_data[6]);    _Serial.print("-");
    _Serial.print(time_data[5]);    _Serial.print("-");
    _Serial.print(time_data[4]);    _Serial.print(" ");
    _Serial.print(time_data[2]);    _Serial.print(":");
    _Serial.print(time_data[1]);    _Serial.print(":");
    _Serial.print(time_data[0]);    _Serial.println("");
    await 1s;
end
```

- Refer [rtc_simple.ceu](https://github.com/ceu-arduino/driver-rtc/blob/pre-v0.40/examples/rtc_simple.ceu).


### Temperature

- The following program illustrates getting the temperature from the RTC.
- The temperature registers are updated after every 64-second conversion. If you want to force temperature conversion await Rtc_force_conversion().
- Rtc_read_temperature gets the temperature and escapes with a r32 type value.

```
#include "wclock.ceu"
#include "rtc.ceu"

_Serial.begin(9600);
await Rtc(on);

loop do
    await Rtc_force_conversion();
    var r32 temp = await Rtc_read_temperature();
    _Serial.print("Temperature is: ");
    _Serial.println(temp);
    await 1s;
end
```

- Refer [rtc_temperature.ceu](https://github.com/ceu-arduino/driver-rtc/blob/pre-v0.40/examples/rtc_temperature.ceu).
### Alarm

- The following program illustrates the use of RTC to set, arm and clear alarms in different formats.
- RTC has 2 Alarms of different format for different usages, Alarm1 and Alarm2.
- The event Rtc_is_armedx escapes with a bool that returns true if Alarmx is armed.
- The event Rtc_arm_alarmx arms Alarmx according to the bool parameter passed.
- The event Rtc_clear_alarmx clears Alarmx.
- Set Alarm - Every second.
  DS3231_EVERY_SECOND is available only on Alarm1.
  eg: await Rtc_set_alarm1(0, 0, 0, 20, {DS3231_MATCH_S},true);
- Set Alarm - Every full minute.
  DS3231_EVERY_MINUTE is available only on Alarm2.
  eg: await Rtc_set_alarm2(0, 0, 1, {DS3231_MATCH_M},true);

```
#include "wclock.ceu"
#include "rtc.ceu"

_Serial.begin(9600);
await Rtc(on);

await Rtc_arm_alarm1(false);
await Rtc_arm_alarm2(false);

await Rtc_clear_alarm1();
await Rtc_clear_alarm2();
await Rtc_set_time(2014, 4, 25, 0, 0, 0); // (Year, Month, Day, Hour, Minute, Second)

await Rtc_set_alarm1(0, 0, 0, 20, {DS3231_MATCH_S},true);
await Rtc_set_alarm2(0, 0, 1, {DS3231_MATCH_M},true);

var bool isArmed1= await Rtc_is_armed1();

if isArmed1 then
  _Serial.print("Alarm1 is triggered ");
else
  _Serial.println("Alarm1 is disarmed.");
end

var bool isArmed2= await Rtc_is_armed2();
if isArmed2 then
    _Serial.print("Alarm2 is triggered ");
else
    _Serial.println("Alarm2 is disarmed.");
end

loop do
    var[7] u16 time_data;
    await Rtc_get_time(&time_data);
    _Serial.print("Date and Time: ");
    _Serial.print(time_data[6]);   _Serial.print("-");
    _Serial.print(time_data[5]);  _Serial.print("-");
    _Serial.print(time_data[4]);    _Serial.print(" ");
    _Serial.print(time_data[2]);   _Serial.print(":");
    _Serial.print(time_data[1]); _Serial.print(":");
    _Serial.print(time_data[0]); _Serial.println("");

    var bool isAlarm1 = await Rtc_is_alarm1(true);
    if isAlarm1 then
        _Serial.println("ALARM 1 TRIGGERED!");
    end

    var bool isAlarm2 = await Rtc_is_alarm2(true);
    if isAlarm2 then
        _Serial.println("ALARM 2 TRIGGERED!");
    end
 
    await 1s;
end
```

- Refer [rtc_alarm.ceu](https://github.com/ceu-arduino/driver-rtc/blob/pre-v0.40/examples/rtc_alarm.ceu).