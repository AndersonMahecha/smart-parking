#include <SPI.h>
#include <MFRC522.h>

#define RST_PIN  9
#define SS_PIN  10
MFRC522 mfrc522(SS_PIN, RST_PIN);
char incomingByte = 0;
bool read_tag = false;
void setup() {
  Serial.begin(9600);
  SPI.begin();
  mfrc522.PCD_Init();
  pinMode(8, OUTPUT);
}

void loop() {

  if (Serial.available() > 0) {
    incomingByte = Serial.read();
    if (incomingByte == '*') {
      read_tag = true;
      digitalWrite(8, 1);
    }
  }


  if (read_tag) {
    if ( ! mfrc522.PICC_IsNewCardPresent()) {
      return;
    }
    if ( ! mfrc522.PICC_ReadCardSerial()) {
      return;
    }

    char str[32] = "";
    array_to_string(mfrc522.uid.uidByte, mfrc522.uid.size, str);
    Serial.println(str);
    mfrc522.PICC_HaltA();
    read_tag = false;
    digitalWrite(8, 0);
  }

}

void array_to_string(byte array[], unsigned int len, char buffer[])
{
  for (unsigned int i = 0; i < len; i++)
  {
    byte nib1 = (array[i] >> 4) & 0x0F;
    byte nib2 = (array[i] >> 0) & 0x0F;
    buffer[i * 2 + 0] = nib1  < 0xA ? '0' + nib1  : 'A' + nib1  - 0xA;
    buffer[i * 2 + 1] = nib2  < 0xA ? '0' + nib2  : 'A' + nib2  - 0xA;
  }
  buffer[len * 2] = '\0';
}
