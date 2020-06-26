#include <SPI.h>
#include <nRF24L01.h>
#include <RF24.h>

const uint64_t pipe =  0xAABBCCDDEELL;
RF24 radio(8,7);

void setup() {
    Serial.begin(9600);
    Serial.println("C - nrf-rx");

    radio.begin();
    //radio.disableCRC();
    radio.setPayloadSize(1);
    radio.setAutoAck(false);              // true by default
    radio.setDataRate(RF24_1MBPS);
    //radio.setDataRate(RF24_2MBPS);
    radio.openReadingPipe(1,pipe);
    radio.startListening();
}

void loop() {
    if (radio.available()) {
        byte v;
        radio.read(&v, 1);
        Serial.print("> ");
        Serial.println(v);
    }
}
