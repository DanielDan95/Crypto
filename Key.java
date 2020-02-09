public class Key  {
  int [][] myKey;

  public Key(int [][] key)  {
    myKey = new int [4][4];

    for(int i = 0; i < 4; i ++) {
      for(int j = 0; j < 4; j++)  {
        this.myKey[i][j] = key[i][j];
      }
    }
  }

  public int [][] giveKey() {
    return this.myKey;
  }
}
