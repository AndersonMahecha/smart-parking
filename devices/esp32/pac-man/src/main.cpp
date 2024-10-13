#include <Arduino.h>
#include <Wire.h>
#include <Adafruit_SSD1306.h>
#include <Adafruit_GFX.h>
#include <map>

#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels
#define UP_BUTTON 23
#define DOWN_BUTTON 18
#define LEFT_BUTTON 5
#define RIGHT_BUTTON 19

void welcomeMessage();
void printlnCentered(String text);
void IRAM_ATTR updateDirection();
void drawLevel();
unsigned char *applyRotation(const unsigned char *bitmap, uint8_t rotation);
bool isPacman(uint8_t piece);
bool isWall(uint8_t piece);
uint8_t *getNewPosition(uint8_t x, uint8_t y, uint8_t direction);
uint8_t applyPacmanAnimation(uint8_t piece);

uint8_t level[1][8][16] = {
    {{0, 4, 4, 4, 6, 4, 10, 31, 31, 11, 4, 6, 4, 4, 4, 2},
     {5, 31, 31, 31, 5, 31, 31, 31, 31, 31, 31, 5, 31, 31, 31, 5},
     {9, 31, 8, 31, 9, 31, 0, 31, 31, 2, 31, 9, 31, 8, 31, 9},
     {31, 31, 9, 31, 31, 31, 5, 96, 97, 5, 31, 31, 31, 9, 31, 31},
     {8, 31, 31, 31, 8, 31, 1, 4, 4, 3, 31, 8, 31, 31, 31, 8},
     {5, 31, 11, 4, 3, 31, 31, 31, 31, 31, 31, 1, 4, 10, 31, 5},
     {5, 90, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 31, 5},
     {1, 4, 4, 4, 4, 4, 10, 31, 31, 11, 4, 4, 4, 4, 4, 3}}};

// 'L', 8x8px
const unsigned char L_pieceL[] PROGMEM = {
    0xdb, 0xd9, 0xdc, 0xdf, 0xcf, 0xe0, 0xff, 0xff};
// 'I', 8x8px
const unsigned char L_pieceI[] PROGMEM = {
    0xff, 0xff, 0x00, 0xff, 0xff, 0x00, 0xff, 0xff};
// 'T', 8x8px
const unsigned char L_pieceT[] PROGMEM = {
    0xff, 0xff, 0x00, 0xff, 0xff, 0x18, 0xdb, 0xdb};
// '+', 8x8px
const unsigned char L_piece_[] PROGMEM = {
    0xdb, 0xdb, 0x18, 0xff, 0xff, 0x18, 0xdb, 0xdb};
// 'gosh', 8x8px
const unsigned char L_piecegosh[] PROGMEM = {
    0xff, 0xc3, 0x81, 0x14, 0x36, 0x00, 0x48, 0x48};
// 'pacman_c', 8x8px
const unsigned char L_piecepacman_c[] PROGMEM = {
    0xc3, 0x81, 0x20, 0x00, 0x00, 0x00, 0x81, 0xc3};
// 'pacman_o', 8x8px
const unsigned char L_piecepacman_o[] PROGMEM = {
    0xc3, 0x81, 0x27, 0x0f, 0x0f, 0x07, 0x81, 0xc3};
// 'corner', 8x8px
const unsigned char L_piececorner[] PROGMEM = {
    0xff, 0xff, 0x0f, 0xf7, 0xf7, 0x0f, 0xff, 0xff};
// 'empty', 8x8px
const unsigned char L_pieceempty[] PROGMEM = {
    0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff};

// Array of all bitmaps for convenience. (Total bytes used to store images in PROGMEM = 288)
const int L_pieceallArray_LEN = 9;
const unsigned char *L_pieceallArray[9] = {
    L_pieceI,
    L_pieceL,
    L_pieceT,
    L_piece_,
    L_piececorner,
    L_pieceempty,
    L_piecegosh,
    L_piecepacman_c,
    L_piecepacman_o};

// map level parts to pieces and rotation
// for example 0 -> L_pieceL rotated 1 times.
// 4 -> L_pieceI rotated 0 times.
// 6 -> L_pieceT rotated 0 times.
// all rotations are 90 degrees clockwise.
std::map<uint8_t, std::pair<const unsigned char *, uint8_t>> levelMap = {
    {0, {L_pieceL, 1}},
    {1, {L_pieceL, 0}},
    {2, {L_pieceL, 2}},
    {3, {L_pieceL, 3}},
    {4, {L_pieceI, 0}},
    {5, {L_pieceI, 1}},
    {6, {L_pieceT, 0}},
    {8, {L_piececorner, 3}},
    {9, {L_piececorner, 1}},
    {10, {L_piececorner, 0}},
    {11, {L_piececorner, 2}},
    {31, {L_pieceempty, 0}},
    {90, {L_piecepacman_o, 0}},
    {91, {L_piecepacman_o, 1}},
    {92, {L_piecepacman_o, 2}},
    {93, {L_piecepacman_o, 3}},
    {96, {L_piecegosh, 0}},
    {97, {L_piecegosh, 1}},
    {100, {L_piecepacman_c, 0}},
    {101, {L_piecepacman_c, 1}},
    {102, {L_piecepacman_c, 2}},
    {103, {L_piecepacman_c, 3}}};

// direction 0 is right, 1 is down, 2 is left, 3 is up
uint8_t direction = 0;
bool isMoving = false;
bool pacmanMoved = false;
bool gameover = false;
uint8_t goshDirection_1 = 3;
bool goshMoved_1 = false;
uint8_t goshDirection_2 = 3;
bool goshMoved_2 = false;

Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, -1);

void setup()
{
  if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C))
  {
    Serial.println(F("SSD1306 allocation failed"));
    for (;;)
      ;
  }

  pinMode(UP_BUTTON, INPUT_PULLUP);
  pinMode(DOWN_BUTTON, INPUT_PULLUP);
  pinMode(LEFT_BUTTON, INPUT_PULLUP);
  pinMode(RIGHT_BUTTON, INPUT_PULLUP);

  delay(2000);

  welcomeMessage();

  attachInterrupt(digitalPinToInterrupt(UP_BUTTON), updateDirection, CHANGE);
  attachInterrupt(digitalPinToInterrupt(DOWN_BUTTON), updateDirection, CHANGE);
  attachInterrupt(digitalPinToInterrupt(LEFT_BUTTON), updateDirection, CHANGE);
  attachInterrupt(digitalPinToInterrupt(RIGHT_BUTTON), updateDirection, CHANGE);
}

void loop()
{

  for (int i = 0; i < 8; i++)
  {
    for (int j = 0; j < 16; j++)
    {
      uint8_t piece = level[0][i][j];
      if (isPacman(piece))
      {
        level[0][i][j] = applyPacmanAnimation(piece);
      }
      if (isWall(piece))
      {
        continue;
      }

      if (!pacmanMoved && isPacman(piece) && isMoving)
      {
        level[0][i][j] = applyPacmanAnimation(piece);
        pacmanMoved = true;
        uint8_t *newPos = getNewPosition(i, j, direction);
        uint8_t x = newPos[0];
        uint8_t y = newPos[1];

        if (level[0][x][y] == 31)
        { // empty space
          level[0][x][y] = piece;
          level[0][i][j] = 31;
        }

        if (level[0][x][y] == 96)
        { // gosh_1
          level[0][x][y] = 96;
          level[0][i][j] = 31;
          gameover = true;
          break;
        }

        if (level[0][x][y] == 97)
        { // gosh_2
          level[0][x][y] = 97;
          level[0][i][j] = 31;
          gameover = true;
          break;
        }
      }

      if (!goshMoved_1 && (piece == 96))
      {
        uint8_t *newPos = getNewPosition(i, j, goshDirection_1);
        uint8_t x = newPos[0];
        uint8_t y = newPos[1];

        if (random(0, 100) < 50)
        {
          goshDirection_1 = random(0, 4);
        }

        if (isPacman(level[0][x][y]))
        { // pacman
          level[0][x][y] = 96;
          level[0][i][j] = 31;
          gameover = true;
          goshMoved_1 = true;
          break;
        }
        else if (level[0][x][y] == 31)
        { // empty space
          level[0][x][y] = piece;
          level[0][i][j] = 31;
          goshMoved_1 = true;
        }
        else
        {
          goshDirection_1 = random(0, 4);
        }
      }

      if (!goshMoved_2 && (piece == 97))
      {
        uint8_t *newPos = getNewPosition(i, j, goshDirection_2);
        uint8_t x = newPos[0];
        uint8_t y = newPos[1];

        if (random(0, 100) < 50)
        {
          goshDirection_2 = random(0, 4);
        }

        if (isPacman(level[0][x][y]))
        { // pacman
          level[0][x][y] = 97;
          level[0][i][j] = 31;
          gameover = true;
          goshMoved_2 = true;
          break;
        }
        else if (level[0][x][y] == 31)
        { // empty space
          level[0][x][y] = piece;
          level[0][i][j] = 31;
          goshMoved_2 = true;
        }
        else
        {
          goshDirection_2 = random(0, 4);
        }
      }
    }
  }
  delay(200);
  pacmanMoved = false;
  goshMoved_1 = false;
  goshMoved_2 = false;
  display.clearDisplay();
  if (!gameover)
  {
    drawLevel();
  }
  else
  {
    display.setTextSize(2);
    display.setTextColor(WHITE);
    display.setCursor(0, 0);
    display.println("Game Over");
    display.setTextSize(1);
    display.println("Press reset");
  }
  display.display();
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

uint8_t *getNewPosition(uint8_t x, uint8_t y, uint8_t localDir)
{
  static uint8_t newPosition[2];
  if (localDir == 0)
  {
    y++;
    if (y == 16)
    {
      y = 0;
    }
  }
  else if (localDir == 1)
  {
    x++;
    if (x == 8)
    {
      x = 0;
    }
  }
  else if (localDir == 2)
  {
    y--;
    if (y == 0)
    {
      y = 15;
    }
  }
  else if (localDir == 3)
  {
    x--;
    if (x == 0)
    {
      x = 7;
    }
  }
  newPosition[0] = x;
  newPosition[1] = y;
  return newPosition;
}
void printlnCentered(String text)
{
  int textWidth = text.length() * 6;
  int x = (SCREEN_WIDTH - textWidth) / 2;
  int y = display.getCursorY();
  display.setCursor(x, y);
  display.println(text);
}

uint8_t applyPacmanAnimation(uint8_t piece)
{
  if (piece == 90 || piece == 91 || piece == 92 || piece == 93)
  {
    return 100 + direction;
  }

  if (piece == 100 || piece == 101 || piece == 102 || piece == 103)
  {
    return 90 + direction;
  }
  return piece;
}

// can only go in 1 direction at a time
void IRAM_ATTR updateDirection()
{
  if (digitalRead(UP_BUTTON) == LOW)
  {
    direction = 3;
    isMoving = true;
  }
  else if (digitalRead(DOWN_BUTTON) == LOW)
  {
    direction = 1;
    isMoving = true;
  }
  else if (digitalRead(LEFT_BUTTON) == LOW)
  {
    direction = 2;
    isMoving = true;
  }
  else if (digitalRead(RIGHT_BUTTON) == LOW)
  {
    direction = 0;
    isMoving = true;
  }
  else
  {
    isMoving = false;
  }
}

bool isPacman(uint8_t piece)
{
  return piece == 90 ||
         piece == 91 ||
         piece == 92 ||
         piece == 93 ||
         piece == 100 ||
         piece == 101 ||
         piece == 102 ||
         piece == 103;
}

bool isWall(uint8_t piece)
{
  return piece == 0 ||
         piece == 1 ||
         piece == 2 ||
         piece == 3 ||
         piece == 4 ||
         piece == 5 ||
         piece == 6 ||
         piece == 8 ||
         piece == 9 ||
         piece == 10 ||
         piece == 11 ||
         piece == 31;
}

void drawLevel()
{
  for (int i = 0; i < 8; i++)
  {
    for (int j = 0; j < 16; j++)
    {
      uint8_t piece = level[0][i][j];
      unsigned char *pieceData = applyRotation(levelMap[piece].first, levelMap[piece].second);
      display.drawBitmap(j * 8, i * 8, pieceData, 8, 8, WHITE);
      // clean up memory
      delete[] pieceData;
    }
  }
}

unsigned char *applyRotation(const unsigned char *bitmap, uint8_t rotation)
{
  unsigned char *rotatedBitmap = new unsigned char[8];
  for (int i = 0; i < 8; i++)
  {
    rotatedBitmap[i] = bitmap[i];
  }

  for (int i = 0; i < rotation; i++)
  {
    unsigned char temp[8];
    for (int j = 0; j < 8; j++)
    {
      temp[j] = rotatedBitmap[j];
    }

    for (int j = 0; j < 8; j++)
    {
      rotatedBitmap[j] = 0;
      for (int k = 0; k < 8; k++)
      {
        rotatedBitmap[j] |= ((temp[k] >> (7 - j)) & 1) << k;
      }
    }
  }

  return rotatedBitmap;
}
