import java.io.*;
import java.lang.StringBuilder;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class set1 {

  public static void AES(String key, String text) throws Exception{
    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
    byte [] k = key.getBytes();
    SecretKeySpec key1 = new SecretKeySpec(k, "AES");
    cipher.init(Cipher.DECRYPT_MODE, key1);
    String print = new String(cipher.doFinal(text.getBytes()));
    System.out.println(print);
  }
  public static void main(String []args) throws Exception {
    String key = "YELLOW SUBMARINE";
    File file = new File("ch7.txt");
    BufferedReader br = new BufferedReader(new FileReader(file));
    StringBuilder str1 = new StringBuilder();
    String str;
    while((str = br.readLine()) != null)  {
      str1.append(str);
    }
    byte [] b = Base64.decodeBase64((str1.toString()).getBytes());
    str1 = new String(b);
    AES(key, str1.toString());
  }
}
