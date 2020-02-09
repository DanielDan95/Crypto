import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;

class aes2  {
  private final static int keyLength = 32;

  public  static String doAES(byte [] key, byte[] blocks) {
    String done = "";
    SecretKeySpec secKey = new SecretKeySpec(key, "AES");
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
      cipher.init(Cipher.ENCRYPT_MODE, secKey);
      byte [] finalBytes = cipher.doFinal(blocks);
      done = new String(finalBytes, "ISO-8859-16");
    } catch (Exception e) {
      System.out.println(e);
    }
    return done;
  }

  //
	public static void main(String args[]) {
    byte [] key = new byte [16];
    List<Byte> rest = new ArrayList<Byte>();
		try {
      //Key fixed length
      for(int i = 0; i < 16; i++) {
        key[i] = (byte) System.in.read();
      }
      int b;
      int i = 0;

      //Blocks
      while((b = System.in.read()) != -1) {
        rest.add((byte)b);
      }
      byte [] blocks = new byte [rest.size()];
      for(int j = 0; j < rest.size(); j++)  {
        blocks[j] = rest.get(j);
      }
      //AES
      String stringPrint = doAES(key, blocks);
      System.out.println(stringPrint);
    } catch(Exception e ) {
      System.out.println(e);
    }
	}
}
