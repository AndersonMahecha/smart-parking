#include <Arduino.h>
#include <ESP32Servo.h>
#include <Wire.h>
#include <Adafruit_SSD1306.h>
#include <Adafruit_GFX.h>

#define SERVO_PIN 13
#define BUTTON_PIN_A 19
#define BUTTON_PIN_B 5
#define CLOSE_ANGLE 155
#define OPEN_ANGLE 60
#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels
#define WAIT_TIME 5 // in seconds

int targetAngle = 0;
int currentAngle = 0;
bool requireUpdate = false;

hw_timer_t *closeTimmer = NULL;

void IRAM_ATTR updateTarget();
void IRAM_ATTR closeWithDelay();
void printlnCentered(String text);
void welcomeMessage();

Servo servo;
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);

void setup()
{
  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C))
  {
    Serial.println(F("SSD1306 allocation failed"));
    for (;;)
      ;
  }
  delay(2000);

  welcomeMessage();

  servo.attach(SERVO_PIN, 500, 2400);
  pinMode(BUTTON_PIN_A, INPUT_PULLUP);
  pinMode(BUTTON_PIN_B, INPUT_PULLUP);

  pinMode(2, OUTPUT);

  targetAngle = CLOSE_ANGLE;
  currentAngle = 90;
  servo.write(currentAngle);

  delay(5000);

  closeTimmer = timerBegin(0, 80, true);
  timerAttachInterrupt(closeTimmer, &closeWithDelay, true);

  attachInterrupt(digitalPinToInterrupt(BUTTON_PIN_A), updateTarget, CHANGE);
  attachInterrupt(digitalPinToInterrupt(BUTTON_PIN_B), updateTarget, CHANGE);

  display.clearDisplay();
  display.display();
}

void loop()
{

  if (currentAngle < targetAngle)
  {
    digitalWrite(2, LOW);
    currentAngle++;
    display.setCursor(0, 0);
    display.clearDisplay();
    printlnCentered("Clossing barrier");
  }
  else if (currentAngle > targetAngle)
  {
    digitalWrite(2, LOW);
    currentAngle--;
    display.setCursor(0, 0);
    display.clearDisplay();
    printlnCentered("Opening barrier");
  }
  else
  {
    if (!requireUpdate)
    {
      if (targetAngle == CLOSE_ANGLE)
      {
        display.setCursor(0, 0);
        display.clearDisplay();
        printlnCentered("Barrier Closed");
      }
      else
      {
        display.setCursor(0, 0);
        display.clearDisplay();
        printlnCentered("Barrier Opened");
      }
      requireUpdate = true;
    }
    digitalWrite(2, HIGH);
  }
  servo.write(currentAngle);
  if (requireUpdate)
  {
    requireUpdate = false;
    display.display();
  }
  delay(10);
}

void IRAM_ATTR updateTarget()
{
  if (digitalRead(BUTTON_PIN_A) == HIGH && digitalRead(BUTTON_PIN_B) == HIGH)
  {
    timerRestart(closeTimmer);
    timerAlarmWrite(closeTimmer, WAIT_TIME*1000000, true);
    timerAlarmEnable(closeTimmer);
    timerStart(closeTimmer);
  }
  else if (digitalRead(BUTTON_PIN_B) == LOW)
  {
    timerStop(closeTimmer);
    targetAngle = OPEN_ANGLE;
  }

  requireUpdate = true;
}

void IRAM_ATTR closeWithDelay()
{
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
