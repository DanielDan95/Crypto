class KeyManager {
    private static final int[] roundConstants = { 1, 2, 4, 8, 16, 32, 64, 128, 27, 54 };


    static int[][] getNextKey(int[][] prevKey, int keyRound){
        int[][] nextKey = new int[4][4];
        int[] rcon = new int[]{roundConstants[keyRound], 0, 0, 0};

        int[] col0 = AESutils.getColumn(prevKey, 0);
        int[] col3 = RotWord(AESutils.getColumn(prevKey, 3));
        int[] w4 = SubBytes(col3, AESutils.sBox);

        for(int i=0; i<4; i++){
            nextKey[i][0] = w4[i] ^ rcon[i] ^ col0[i];
        }
        for(int r=0; r<4; r++){
            for(int c=1; c<4; c++){
                nextKey[r][c] = nextKey[r][c-1] ^ prevKey[r][c];
            }
        }
        return nextKey;
    }

    private static int[] RotWord(int[] word){
        int[] rotWord = new int[word.length];
        for(int i=0; i<word.length-1; i++){
            rotWord[i] = word[i+1];
        }
        rotWord[word.length-1] = word[0];
        return rotWord;
    }


    private static int[] SubBytes(int[] word, int[][] sBox){
        for(int i=0; i< word.length; i++){
                int hexVal = word[i];
                String hex = Integer.toHexString(hexVal);
                int len = hex.length();
                String b1 = "0";
                String b2 = "0";

                if(len == 1){
                    b2 = hex.substring(0, 1);
                }
                else if(len ==2){
                    b1 = hex.substring(0, 1);
                    b2 = hex.substring(1, 2);
                }
                else if(len > 2){
                    b1 = hex.substring(len-2, len-1);
                    b2 = hex.substring(len-1);
                }
                int row = Integer.parseInt(b1, 16);
                int col = Integer.parseInt(b2, 16);
                word[i] = sBox[row][col];
                //word[i] = sBox[hexVal/16][hexVal%16];
        }
        return word;
    }
}
