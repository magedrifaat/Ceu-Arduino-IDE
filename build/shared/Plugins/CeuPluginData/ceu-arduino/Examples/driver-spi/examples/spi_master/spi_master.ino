// http://www.gammon.com.au/forum/?id=10892

#include <SPI.h>

void setup (void) {
  Serial.begin(9600);
  digitalWrite(SS, HIGH);
  SPI.begin ();
  //SPI.setClockDivider(SPI_CLOCK_DIV8); // slower
}

char i = 0;

void loop (void) {
  digitalWrite(SS, LOW);
  byte c = SPI.transfer(i++);
  digitalWrite(SS, HIGH);
  Serial.println(c);
  delay(1000);
}

