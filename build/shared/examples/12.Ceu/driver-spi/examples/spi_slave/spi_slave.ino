// http://www.gammon.com.au/forum/?id=10892

// If Master is reset, Slaves get de-synchronized.
// If Slave is reset, everything works fine.
// See also:
// https://www.avrfreaks.net/forum/spi-reset-problem

#include <SPI.h>

void setup (void) {
  Serial.begin(9600);
  SPCR |= bit (SPE);
  pinMode(MISO, OUTPUT);  
  SPI.attachInterrupt();
}

void loop (void) { }

ISR (SPI_STC_vect) {
  byte c = SPDR;
  Serial.println(c);
}

