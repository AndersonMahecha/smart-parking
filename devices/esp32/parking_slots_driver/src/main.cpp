#include <Arduino.h>
#include <WiFi.h>
#include <WiFiClientSecure.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>
#include <WebSocketsClient.h>

// definicion de tipos de mensaje
#define MESSAGE_TYPE_STATUS 0
#define MESSAGE_TYPE_COMMAND 1
#define MESSAGE_TYPE_RESPONSE 2
#define MESSAGE_TYPE_ERROR 3
#define MESSAGE_TYPE_ENTRY 4
#define MESSAGE_TYPE_EXIT 5

// definicion de tipos de dispositivo
#define DEVICE_TYPE_BARRIER 0
#define DEVICE_TYPE_SENSOR 1

#define SENSE_UMBRAL 1900
#define SENSE_COUNT 4

#define ssid "parqueadero"
#define password "parqueadero123"

void webSocketEvent(WStype_t type, uint8_t *payload, size_t length);

uint8_t SENSOR_PINS[SENSE_COUNT] = {
    36,
    39,
    34,
    35};

uint8_t SLOT_STATUS_PIN[SENSE_COUNT] = {
    26,
    25,
    33,
    32};

int SENSOR_ADJUSTMENT[SENSE_COUNT] = {
    -200,
    0,
    0,
    -50};


bool SENSOR_STATES[SENSE_COUNT] = {false};
unsigned long lastChangeTime[SENSE_COUNT] = {0};
const unsigned long debounceTime = 1000; // Tiempo en milisegundos

WiFiMulti WiFiMulti;
WebSocketsClient webSocket;

void buildMessage();

void setup()
{
  Serial.begin(9600);

  Serial.println("Starting WiFi");
  Serial.print("[WiFi] Connecting to ");
  Serial.println(ssid);

  WiFiMulti.addAP(ssid, password);

  for (int i = 0; i < SENSE_COUNT; i++)
  {
    pinMode(SENSOR_PINS[i], INPUT);
  }

  for (int i = 0; i < SENSE_COUNT; i++)
  {
    pinMode(SLOT_STATUS_PIN[i], OUTPUT);
  }

  while (WiFiMulti.run() != WL_CONNECTED)
  {
    delay(100);
  }

  String ip = WiFi.localIP().toString();
  Serial.printf("[SETUP] WiFi Connected %s\n", ip.c_str());

  webSocket.begin("192.168.10.50", 3500, "");

  // event handler
  webSocket.onEvent(webSocketEvent);

  // try ever 5000 again if connection has failed
  webSocket.setReconnectInterval(1000);
}

void loop()
{
  webSocket.loop();

  bool changeDetected = false;

  for (int i = 0; i < SENSE_COUNT; i++)
  {

    bool currentState = analogRead(SENSOR_PINS[i]) > SENSE_UMBRAL + SENSOR_ADJUSTMENT[i];

    if (currentState != SENSOR_STATES[i] && millis() - lastChangeTime[i] > debounceTime)
    {
      lastChangeTime[i] = millis();
      SENSOR_STATES[i] = currentState;
      changeDetected = true;
    }
  }

  if (changeDetected)
  {
    for (int i = 0; i < SENSE_COUNT; i++)
    {
      digitalWrite(SLOT_STATUS_PIN[i], SENSOR_STATES[i]);
    }
    buildMessage();
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

    break;
  case WStype_ERROR:
    break;
  case WStype_FRAGMENT_TEXT_START:
    break;
  case WStype_FRAGMENT_BIN_START:
    break;
  case WStype_FRAGMENT:
    break;
  case WStype_FRAGMENT_FIN:
    break;
  }
}

void hexdump(const void *mem, uint32_t len, uint8_t cols = 16)
{
  const uint8_t *src = (const uint8_t *)mem;
  Serial.printf("\n[HEXDUMP] Address: 0x%08X len: 0x%X (%d)", (ptrdiff_t)src, len, len);
  for (uint32_t i = 0; i < len; i++)
  {
    if (i % cols == 0)
    {
      Serial.printf("\n[0x%08X] 0x%08X: ", (ptrdiff_t)src, i);
    }
    Serial.printf("%02X ", *src);
    src++;
  }
  Serial.printf("\n");
}

// Definicion de protocolo de comunicacion
// 3 bytes de longitud
// 1 byte para el tipo de mensaje
// 1 byte para el tipo de dispositivo
// 1 byte para el estado de los sensores

void buildMessage()
{
  u8_t message[7];
  message[0] = MESSAGE_TYPE_STATUS;
  message[1] = DEVICE_TYPE_SENSOR;
  // write the sensor states to the message
  for (int i = 0; i < SENSE_COUNT; i++)
  {
    message[i + 2] = SENSOR_STATES[i];
  }
  hexdump(message, 7);
  webSocket.sendBIN(message, 7);
}
