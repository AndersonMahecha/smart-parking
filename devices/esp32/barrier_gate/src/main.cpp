#include <Arduino.h>
#include <ESP32Servo.h>
#include <Wire.h>
#include <Adafruit_SSD1306.h>
#include <Adafruit_GFX.h>
#include <MFRC522.h>
#include <SPI.h>
#include <WiFi.h>
#include <WiFiClientSecure.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>
#include <WebSocketsClient.h>

#define SERVO_PIN 13
#define BUTTON_PIN_A 14
#define BUTTON_PIN_B 12
#define SS_PIN 4
#define RST_PIN 2

#define CLOSE_ANGLE 155
#define OPEN_ANGLE 60
#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels

#define BARRIER_CLOSED 0
#define BARRIER_OPENED 1
#define BARRIER_CLOSING 2
#define BARRIER_OPENING 3

#define WAIT_TIME 5 // in seconds to close the barrier

#define MAX_PARKING_SPOTS 6

#define DETECTION_VALUE HIGH
#define NO_DETECTION_VALUE LOW

#define ssid "FamiliaMahechaSalamanca"
#define password "Nirvana77"
#define websocket_server "http://192.168.10.25:8080"

int targetAngle = 0;
int currentAngle = 0;
bool requireUpdate = false;
int barrierState;
int currentUsedSpots = 0;

char **rfidTags = new char *[MAX_PARKING_SPOTS];

hw_timer_t *closeTimmer = NULL;

void IRAM_ATTR updateTarget();
void IRAM_ATTR closeWithDelay();
void printlnCentered(String text);
void welcomeMessage();
void showAllData();
void startCloseTimmer();
void webSocketEvent(WStype_t type, uint8_t *payload, size_t length);


Servo servo;
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);
MFRC522 rfid = MFRC522(SS_PIN, RST_PIN);
WiFiMulti WiFiMulti;
WebSocketsClient webSocket;

void setup()

{
  Serial.begin(9600);
  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C))
  {
    Serial.println(F("SSD1306 allocation failed"));
    for (;;)
      ;
  }

  Serial.println("Starting WiFi");
  Serial.print("[WiFi] Connecting to ");
  Serial.println(ssid);

  WiFiMulti.addAP(ssid, password);

  while (WiFiMulti.run() != WL_CONNECTED)
  {
    delay(100);
  }

  String ip = WiFi.localIP().toString();
  Serial.printf("[SETUP] WiFi Connected %s\n", ip.c_str());

  webSocket.begin("192.168.10.25", 3500, "");

  // event handler
  webSocket.onEvent(webSocketEvent);

  // try ever 5000 again if connection has failed
  webSocket.setReconnectInterval(1000);

  SPI.begin();
  rfid.PCD_Init();

  delay(2000);

  welcomeMessage();

  servo.attach(SERVO_PIN, 500, 2400);
  pinMode(BUTTON_PIN_A, INPUT_PULLUP);
  pinMode(BUTTON_PIN_B, INPUT_PULLUP);

  targetAngle = CLOSE_ANGLE;
  currentAngle = OPEN_ANGLE;
  servo.write(currentAngle);

  delay(1000);

  closeTimmer = timerBegin(0, 80, true);
  timerAttachInterrupt(closeTimmer, &closeWithDelay, true);

  attachInterrupt(digitalPinToInterrupt(BUTTON_PIN_A), updateTarget, CHANGE);
  attachInterrupt(digitalPinToInterrupt(BUTTON_PIN_B), updateTarget, CHANGE);

  display.clearDisplay();
  display.display();
}

void loop()
{
  webSocket.loop();

  if (digitalRead(BUTTON_PIN_A) == DETECTION_VALUE || digitalRead(BUTTON_PIN_B) == DETECTION_VALUE)
  {
    if (rfid.PICC_IsNewCardPresent() && rfid.PICC_ReadCardSerial())
    {
      if (currentUsedSpots >= MAX_PARKING_SPOTS)
      {
        return;
      }
      // store the UID in a char* buffer
      char uidString[10];
      for (byte i = 0; i < rfid.uid.size; i++)
      {
        sprintf(uidString + 2 * i, "%02X", rfid.uid.uidByte[i]);
      }

      // check if the UID is already in the array
      bool uidExists = false;
      for (int i = 0; i < currentUsedSpots; i++)
      {
        Serial.println("Comparing UID");
        Serial.println(uidString);
        Serial.println(rfidTags[i]);
        if (strcmp(rfidTags[i], uidString) == 0)
        {
          Serial.println("UID already in array");
          uidExists = true;
        }
      }

      if (!uidExists)
      {
        Serial.println("UID added to array");
        Serial.println(uidString);
        rfidTags[currentUsedSpots] = strdup(uidString);
        currentUsedSpots++;
        requireUpdate = true;
        targetAngle = OPEN_ANGLE;
        startCloseTimmer();
      }

      rfid.PICC_HaltA();
      rfid.PCD_StopCrypto1();
    }
  }
  else
  {
    delay(10);
  }

  if (currentAngle < targetAngle)
  {
    currentAngle++;
    barrierState = BARRIER_CLOSING;
  }
  else if (currentAngle > targetAngle)
  {
    currentAngle--;
    barrierState = BARRIER_OPENING;
  }
  else
  {
    if (!requireUpdate)
    {
      if (targetAngle == CLOSE_ANGLE)
      {
        barrierState = BARRIER_CLOSED;
      }
      else
      {
        barrierState = BARRIER_OPENED;
      }
      requireUpdate = true;
    }
  }
  servo.write(currentAngle);
  if (requireUpdate)
  {
    requireUpdate = false;
    showAllData();
  }
}

void IRAM_ATTR updateTarget()
{
  if (digitalRead(BUTTON_PIN_A) == NO_DETECTION_VALUE && digitalRead(BUTTON_PIN_B) == NO_DETECTION_VALUE)
  {
    startCloseTimmer();
  }
  requireUpdate = true;
}

void startCloseTimmer()
{
  timerRestart(closeTimmer);
  timerAlarmWrite(closeTimmer, WAIT_TIME * 1000000, true);
  timerAlarmEnable(closeTimmer);
  timerStart(closeTimmer);
}

void IRAM_ATTR closeWithDelay()
{
  if (digitalRead(BUTTON_PIN_A) == DETECTION_VALUE || digitalRead(BUTTON_PIN_B) == DETECTION_VALUE)
  {
    timerStop(closeTimmer);
    return;
  }

  targetAngle = CLOSE_ANGLE;
  requireUpdate = true;
}

void printlnCentered(String text)
{
  int textWidth = text.length() * 6;
  int x = (SCREEN_WIDTH - textWidth) / 2;
  int y = display.getCursorY();
  display.setCursor(x, y);
  display.println(text);
}

void welcomeMessage()
{
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.setCursor(0, 0);
  display.setRotation(2);
  printlnCentered("Universidad");
  printlnCentered("Sergio");
  printlnCentered("Arboleda");
  printlnCentered("");
  printlnCentered("Edge Computing");
  printlnCentered("Anderson Mahecha");
  printlnCentered("Nicolas Torres");
  printlnCentered("2024");
  display.display();
}

void showAllData()
{
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.setCursor(0, 0);
  display.setRotation(2);
  switch (barrierState)
  {
  case BARRIER_CLOSED:
    display.println("Barrera Cerrada");
    break;
  case BARRIER_OPENED:
    display.println("Barrera Abierta");
    break;
  case BARRIER_CLOSING:
    display.println("Cerrando Barrera");
    break;
  case BARRIER_OPENING:
    display.println("Abriendo Barrera");
    break;
  default:
    break;
  }
  display.println("");
  display.println("Espacios de parqueo");
  display.println("");
  display.println("Disponibles: " + String(MAX_PARKING_SPOTS - currentUsedSpots));
  display.println("");
  display.println("Ocupados: " + String(currentUsedSpots));

  display.display();
}

void dump_byte_array(byte *buffer, byte bufferSize)
{
  for (byte i = 0; i < bufferSize; i++)
  {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], HEX);
  }
}

void webSocketEvent(WStype_t type, uint8_t *payload, size_t length)
{

  switch (type)
  {
  case WStype_DISCONNECTED:
    Serial.printf("[WSc] Disconnected!\n");
    break;
  case WStype_CONNECTED:
    Serial.printf("[WSc] Connected to url: %s\n", payload);

    // send message to server when Connected
    webSocket.sendTXT("Connected");
    break;
  case WStype_TEXT:
    Serial.printf("[WSc] get text: %s\n", payload);

    // send message to server
    // webSocket.sendTXT("message here");
    break;
  case WStype_ERROR:
  case WStype_FRAGMENT_TEXT_START:
  case WStype_FRAGMENT_BIN_START:
  case WStype_FRAGMENT:
  case WStype_FRAGMENT_FIN:
    break;
  }
}

void hexdump(const void *mem, uint32_t len, uint8_t cols = 16) {
	const uint8_t* src = (const uint8_t*) mem;
	Serial.printf("\n[HEXDUMP] Address: 0x%08X len: 0x%X (%d)", (ptrdiff_t)src, len, len);
	for(uint32_t i = 0; i < len; i++) {
		if(i % cols == 0) {
			Serial.printf("\n[0x%08X] 0x%08X: ", (ptrdiff_t)src, i);
		}
		Serial.printf("%02X ", *src);
		src++;
	}
	Serial.printf("\n");
}
