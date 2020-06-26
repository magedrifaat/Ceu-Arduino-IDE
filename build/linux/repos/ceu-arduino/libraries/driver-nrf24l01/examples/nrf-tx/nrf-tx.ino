#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>

byte V = 0;
const uint64_t pipe =  0xAABBCCDDEELL;
RF24 radio(8,7);

void setup() {
  // put your setup code here, to run once:
    Serial.begin(9600);
    Serial.println("C - nrf-tx");

    radio.begin();
    //radio.disableCRC();
    radio.setPayloadSize(1);
    radio.setAutoAck(false);              // true by default
    radio.setDataRate(RF24_1MBPS);
    //radio.setDataRate(RF24_2MBPS);
    radio.openWritingPipe(pipe);
}

void loop() {
  // put your main code here, to run repeatedly:
    delay(1000);
    Serial.print("< ");
    Serial.println(V);
    radio.write(&V, 1);
    V++;
}
