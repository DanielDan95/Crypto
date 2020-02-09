import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class aes {
    static List<Integer> intArray = new ArrayList<Integer>();
    static List<Byte> byteArray = new ArrayList<Byte>();
    static List<String> tmpArray = new ArrayList<String>();
    //static List<Key> keyRing = new ArrayList<Key>();
    static Key [] keyRing = new Key[11];

    static int[] mdsMatrix =   {
      2,3,1,1,
      1,2,3,1,
      1,1,2,3,
      3,1,1,2,
    };

    static int [] rCon = { 1, 2, 4, 8, 16, 32, 64, 128, 27, 54 };

    public static void getinput() throws Exception {
      int number;
      InputStream in = new DataInputStream(System.in);
      //OutputStream out = new DataOutputStream(System.out);
      while((number = System.in.read()) != -1)  {
        //System.out.println(number);
        intArray.add((int) number);
      }
    }

    public static int [][] initKey() {
      int [][] key = new int[4][4];
      for(int i = 0; i < 4; i++)  {
        for(int j = 0; j < 4; j++)  {
          key[i][j] = intArray.get(i+(j*4));
          //key[i][j] = (int) byteArray.get(i+(j*4));
        }
      }
      return key;
    }

    public static int [][] getBlock(int round) {
      int [][] block = new int[4][4];
      for(int i = 0; i < 4; i++)  {
        for(int j = 0; j < 4; j++)  {
          block[i][j] = intArray.get(round*16+i+j*4);
          //block[i][j] = (int) byteArray.get(round*16+i+j*4);
        }
      }
      return block;
    }

    public static int [][] addRoundKey(int [][] key, int [][] state) {
        int [][] newState = new int [4][4];
        for(int i = 0; i < 4; i++)  {
          for(int j = 0; j < 4; j++)  {
            newState[i][j] =  (int) (key[i][j] ^ state[i][j]);
          }
        }
        return newState;
    }

    public static int [][] keyMake(int [][] oldKey, int round)  {
      int[] sBox = {
              0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
              0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
              0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
              0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
              0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
              0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
              0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
              0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
              0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
              0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
              0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
              0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
              0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
              0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
              0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
              0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16};

      int [][] newKey = new int [4][4];
      int [] tmpRCon = new int[]{rCon[round], 0, 0, 0};
      //First part needs a ROT word
      newKey[0][0] = oldKey[1][3];
      newKey[1][0] = oldKey[2][3];
      newKey[2][0] = oldKey[3][3];
      newKey[3][0] = oldKey[0][3];
      //Sub ints;
      for(int i = 0; i < 4; i++)  {
        String hexString = Integer.toHexString(newKey[i][0]);
        char row;
        char col;
        if(hexString.length() < 2)  {
          row = '0';
          col = hexString.charAt(0);
        } else  {
          col = hexString.charAt(1);
          row = hexString.charAt(0);
        }
        int colValue = Integer.parseInt(String.valueOf(col), 16);
        int rowValue = Integer.parseInt(String.valueOf(row), 16);
        //newKey[i][0] = (int) sBox[rowValue][colValue];
        int elem = rowValue*16+colValue;
        newKey[i][0] = (int) sBox[elem];
      }
      //XOR with rcon w-4
      for(int i = 0; i < 4; i++)  {
        int newVal = newKey[i][0] ^ oldKey[i][0] ^ tmpRCon[i];
        newKey[i][0] = newVal;
      }
      //Rest is just XOR
      for(int i = 1; i < 4; i++)  {
        for(int j = 0; j < 4; j++)  {
          newKey[j][i] = newKey[j][i-1] ^ oldKey[j][i];
        }
      }
      return newKey;
    }

    public static void makeKeyRing(int [][] key) {
        /*Key tmpKey = new Key(key);
        keyRing[0] = tmpKey;
        int [][] tmp = keyMake(key, 0);
        Key newtmp = new Key(tmp);
        keyRing[1] = newtmp;*/

        //First key
        Key firstKey = new Key(key);
        keyRing[0] = firstKey;
        for(int i = 0; i < 10; i++) {
          int [][] tmp = keyMake(keyRing[i].giveKey(),i);
          Key newtmp = new Key(tmp);
          keyRing[i+1] = newtmp;
        }

    }

    public static int [][] subints(int [][] state) {
      int[] sBox = {
              0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
              0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
              0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
              0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
              0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
              0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
              0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
              0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
              0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
              0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
              0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
              0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
              0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
              0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
              0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
              0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16};
      int [][] newState = new int [4][4];
      for(int i = 0; i < 4; i++)  {
        for(int j = 0; j < 4; j++)  {
          String hexString = Integer.toHexString(state[i][j]);
          char col;
          char row;
          if(hexString.length() < 2)  {
            row = '0';
            col = hexString.charAt(0);
          } else  {
            col = hexString.charAt(1);
            row = hexString.charAt(0);
          }
          int colValue = Integer.parseInt(String.valueOf(col), 16);
          int rowValue = Integer.parseInt(String.valueOf(row), 16);
          int elem = rowValue*16+colValue;
          //newState[i][j] = (int) sBox[rowValue][colValue];
          newState[i][j] = (int) sBox[elem];

        }
      }
      return newState;
    }

    public static int[][] shiftRows(int [][] state)  {
      int [][] newState = new int [4][4];
      for(int i = 0; i < 4; i++)  {
        for(int j = 0; j < 4; j++)  {
          int tmp_j;
          switch(i) {
            case 0:
              newState[i][j] = state[i][j];
              break;
            case 1:
              tmp_j = (j+1)%4;
              newState[i][j] = state[i][tmp_j];
              break;
            case 2:
              tmp_j = (j+2)%4;
              newState[i][j] = state[i][tmp_j];
              break;
            case 3:
              tmp_j = (j+3)%4;
              newState[i][j] = state[i][tmp_j];
              break;
          }
        }
      }
      return newState;
    }

    public static int[][] mixCols(int [][] state) {
      int [][] newState = new int [4][4];
      int [] row = new int [4];
      for(int i = 0; i < 4; i++)  {
        row = getColMatrix(state, i);
        for(int j = 0; j < 4; j++)  {
          //System.out.println(row[j]);
          newState[j][i] = row[j];
        }
      }
      return newState;
    }

    public static int[] getColMatrix(int [][] state, int col) {

      int[][] g2Matrix = {
        {0x00,0x02,0x04,0x06,0x08,0x0a,0x0c,0x0e,0x10,0x12,0x14,0x16,0x18,0x1a,0x1c,0x1e},
        {0x20,0x22,0x24,0x26,0x28,0x2a,0x2c,0x2e,0x30,0x32,0x34,0x36,0x38,0x3a,0x3c,0x3e},
        {0x40,0x42,0x44,0x46,0x48,0x4a,0x4c,0x4e,0x50,0x52,0x54,0x56,0x58,0x5a,0x5c,0x5e},
        {0x60,0x62,0x64,0x66,0x68,0x6a,0x6c,0x6e,0x70,0x72,0x74,0x76,0x78,0x7a,0x7c,0x7e},
        {0x80,0x82,0x84,0x86,0x88,0x8a,0x8c,0x8e,0x90,0x92,0x94,0x96,0x98,0x9a,0x9c,0x9e},
        {0xa0,0xa2,0xa4,0xa6,0xa8,0xaa,0xac,0xae,0xb0,0xb2,0xb4,0xb6,0xb8,0xba,0xbc,0xbe},
        {0xc0,0xc2,0xc4,0xc6,0xc8,0xca,0xcc,0xce,0xd0,0xd2,0xd4,0xd6,0xd8,0xda,0xdc,0xde},
        {0xe0,0xe2,0xe4,0xe6,0xe8,0xea,0xec,0xee,0xf0,0xf2,0xf4,0xf6,0xf8,0xfa,0xfc,0xfe},
        {0x1b,0x19,0x1f,0x1d,0x13,0x11,0x17,0x15,0x0b,0x09,0x0f,0x0d,0x03,0x01,0x07,0x05},
        {0x3b,0x39,0x3f,0x3d,0x33,0x31,0x37,0x35,0x2b,0x29,0x2f,0x2d,0x23,0x21,0x27,0x25},
        {0x5b,0x59,0x5f,0x5d,0x53,0x51,0x57,0x55,0x4b,0x49,0x4f,0x4d,0x43,0x41,0x47,0x45},
        {0x7b,0x79,0x7f,0x7d,0x73,0x71,0x77,0x75,0x6b,0x69,0x6f,0x6d,0x63,0x61,0x67,0x65},
        {0x9b,0x99,0x9f,0x9d,0x93,0x91,0x97,0x95,0x8b,0x89,0x8f,0x8d,0x83,0x81,0x87,0x85},
        {0xbb,0xb9,0xbf,0xbd,0xb3,0xb1,0xb7,0xb5,0xab,0xa9,0xaf,0xad,0xa3,0xa1,0xa7,0xa5},
        {0xdb,0xd9,0xdf,0xdd,0xd3,0xd1,0xd7,0xd5,0xcb,0xc9,0xcf,0xcd,0xc3,0xc1,0xc7,0xc5},
        {0xfb,0xf9,0xff,0xfd,0xf3,0xf1,0xf7,0xf5,0xeb,0xe9,0xef,0xed,0xe3,0xe1,0xe7,0xe5},
      };

      int [][] g3Matrix = {
        {0x00,0x03,0x06,0x05,0x0c,0x0f,0x0a,0x09,0x18,0x1b,0x1e,0x1d,0x14,0x17,0x12,0x11},
        {0x30,0x33,0x36,0x35,0x3c,0x3f,0x3a,0x39,0x28,0x2b,0x2e,0x2d,0x24,0x27,0x22,0x21},
        {0x60,0x63,0x66,0x65,0x6c,0x6f,0x6a,0x69,0x78,0x7b,0x7e,0x7d,0x74,0x77,0x72,0x71},
        {0x50,0x53,0x56,0x55,0x5c,0x5f,0x5a,0x59,0x48,0x4b,0x4e,0x4d,0x44,0x47,0x42,0x41},
        {0xc0,0xc3,0xc6,0xc5,0xcc,0xcf,0xca,0xc9,0xd8,0xdb,0xde,0xdd,0xd4,0xd7,0xd2,0xd1},
        {0xf0,0xf3,0xf6,0xf5,0xfc,0xff,0xfa,0xf9,0xe8,0xeb,0xee,0xed,0xe4,0xe7,0xe2,0xe1},
        {0xa0,0xa3,0xa6,0xa5,0xac,0xaf,0xaa,0xa9,0xb8,0xbb,0xbe,0xbd,0xb4,0xb7,0xb2,0xb1},
        {0x90,0x93,0x96,0x95,0x9c,0x9f,0x9a,0x99,0x88,0x8b,0x8e,0x8d,0x84,0x87,0x82,0x81},
        {0x9b,0x98,0x9d,0x9e,0x97,0x94,0x91,0x92,0x83,0x80,0x85,0x86,0x8f,0x8c,0x89,0x8a},
        {0xab,0xa8,0xad,0xae,0xa7,0xa4,0xa1,0xa2,0xb3,0xb0,0xb5,0xb6,0xbf,0xbc,0xb9,0xba},
        {0xfb,0xf8,0xfd,0xfe,0xf7,0xf4,0xf1,0xf2,0xe3,0xe0,0xe5,0xe6,0xef,0xec,0xe9,0xea},
        {0xcb,0xc8,0xcd,0xce,0xc7,0xc4,0xc1,0xc2,0xd3,0xd0,0xd5,0xd6,0xdf,0xdc,0xd9,0xda},
        {0x5b,0x58,0x5d,0x5e,0x57,0x54,0x51,0x52,0x43,0x40,0x45,0x46,0x4f,0x4c,0x49,0x4a},
        {0x6b,0x68,0x6d,0x6e,0x67,0x64,0x61,0x62,0x73,0x70,0x75,0x76,0x7f,0x7c,0x79,0x7a},
        {0x3b,0x38,0x3d,0x3e,0x37,0x34,0x31,0x32,0x23,0x20,0x25,0x26,0x2f,0x2c,0x29,0x2a},
        {0x0b,0x08,0x0d,0x0e,0x07,0x04,0x01,0x02,0x13,0x10,0x15,0x16,0x1f,0x1c,0x19,0x1a},
      };

      int [] getColMatrix = new int [4];
      int tmp = 0;

      for(int i = 0; i < 4; i++)  {
        String hexString = Integer.toHexString(state[0][col]);
        char [] tmpChar = getColandRow(hexString);
        char col1 = tmpChar[1]; //getCol(hexString);
        char row1 = tmpChar[0]; //hexString.charAt(0);
        int colValue1 = Integer.parseInt(String.valueOf(col1), 16);
        int rowValue1 = Integer.parseInt(String.valueOf(row1), 16);
        int value1 = 0;
        //switch(mdsMatrix[i][0]) {
        switch(mdsMatrix[i*4]) {
          case 1:
            value1 = state[0][col];
            break;
          case 2:
            value1 = g2Matrix[rowValue1][colValue1];
            break;
          case 3:
            value1 = g3Matrix[rowValue1][colValue1];
            break;
        }

        String hexString2 = Integer.toHexString(state[1][col]);
        tmpChar = getColandRow(hexString2);
        char col2 = tmpChar[1]; //getCol(hexString2);
        char row2 =  tmpChar[0]; //hexString2.charAt(0);
        int colValue2 = Integer.parseInt(String.valueOf(col2),16);
        int rowValue2 = Integer.parseInt(String.valueOf(row2), 16);
        int value2 = 0;
        //switch(mdsMatrix[i][1]) {
        switch(mdsMatrix[i*4+1]) {
          case 1:
            value2 = state[1][col];
            break;
          case 2:
            value2 = g2Matrix[rowValue2][colValue2];
            break;
          case 3:
            value2 = g3Matrix[rowValue2][colValue2];
            break;
        }

        String hexString3 = Integer.toHexString(state[2][col]);
        tmpChar = getColandRow(hexString3);
        char col3 = tmpChar[1];//getCol(hexString3);
        char row3 = tmpChar[0]; //hexString3.charAt(0);
        int colValue3 = Integer.parseInt(String.valueOf(col3),16);
        int rowValue3 = Integer.parseInt(String.valueOf(row3), 16);
        int value3 = 0;
        //switch(mdsMatrix[i][2]) {
        switch(mdsMatrix[i*4+2]) {
          case 1:
            value3 = state[2][col];
            break;
          case 2:
            value3 = g2Matrix[rowValue3][colValue3];
            break;
          case 3:
            value3 = g3Matrix[rowValue3][colValue3];
            break;
        }

        String hexString4 = Integer.toHexString(state[3][col]);
        tmpChar = getColandRow(hexString4);
        char col4 = tmpChar[1];//getCol(hexString4);
        char row4 = tmpChar[0]; //hexString4.charAt(0);
        int colValue4 = Integer.parseInt(String.valueOf(col4),16);
        int rowValue4 = Integer.parseInt(String.valueOf(row4), 16);
        int value4 = 0;
        //switch(mdsMatrix[i][3]) {
        switch(mdsMatrix[i*4+3]) {
          case 1:
            value4 = state[3][col];
            break;
          case 2:
            value4 = g2Matrix[rowValue4][colValue4];
            break;
          case 3:
            value4 = g3Matrix[rowValue4][colValue4];
            break;
        }
        int totValue = value1 ^ value2 ^ value3 ^ value4;
        getColMatrix[i] = totValue;
      }

      return getColMatrix;
    }

    public static char [] getColandRow(String hexString) {
      char [] colrow = new char [2];
      if(hexString.length() < 2)  {
        colrow[0] = '0';
        colrow[1] = hexString.charAt(0);
      } else  {
        colrow[1] = hexString.charAt(1);
        colrow[0] = hexString.charAt(0);
      }
      return colrow;
    }

    public static void printMatrix(int [][] matrix) {
      /*for(int i = 0; i < 4; i++)  {
        for(int j = 0; j < 4; j++)  {
          System.out.print(matrix[i][j] + " ");
        }
        System.out.println();
      }*/
      //System.out.println("As a hex String: ");
        PrintStream printStream = new PrintStream(System.out);
        for(int i = 0; i < 4; i++)  {
          for(int j = 0; j < 4; j++)  {
            byte [] arr = new byte [1];
            Integer a = new Integer(matrix[j][i]);
            arr[0] = a.byteValue();
            printStream = new PrintStream(System.out);
            try {
              printStream.write(arr[0]);
            }catch(Exception e) {
              System.out.print(e);
            }
            //String st = String.format("%02X", matrix[j][i]);
          }
        }
        printStream.flush();
        //System.out.print(new String(arr));
        //System.out.println();
    }

    public static void main(String[] args) throws Exception {
        //Fix input
        //test input
        /*int [][] key = {
          {43, 40, 171, 9},
          {126, 174, 247, 207},
          {21, 210, 21, 79},
          {22, 166, 136, 60},
        };
        int [][] state = {
          {50, 136, 49, 224},
          {67, 90, 49, 55},
          {246, 48, 152, 7},
          {168, 141, 162, 52},
        };*/
        getinput();
        //init key
        //16 int key
        int [][] key = initKey();
        makeKeyRing(key);
        int round = 1;
        int numOfBlocks = (intArray.size()-16)/16;
        //int numOfBlocks = (byteArray.size()-16)/16;
        while(round <= numOfBlocks)  {
          int [][] state = getBlock(round);
          //printMatrix(state);

          //add firstAddRoundKey
          state = addRoundKey(keyRing[0].giveKey(), state);
          //"9" Rounds
          for(int i = 1; i < 10; i++) {
            //printMatrix(state);
            //check specific Round
            state = subints(state);
            state = shiftRows(state);
            state = mixCols(state);
            state = addRoundKey(keyRing[i].giveKey(), state);
          }
          //printMatrix(state);
          //Last round
          state = subints(state);
          state = shiftRows(state);
          state = addRoundKey(keyRing[10].giveKey(), state);

        //Finished
          printMatrix(state);
          round++;
        }

    }
}
