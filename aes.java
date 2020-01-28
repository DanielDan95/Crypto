import java.io.*;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class aes
{

	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try	{
			//System.out.println(System.in.read());
			//String s = br.readLine();
			//System.out.println(s);
			int c;
			int counter = 0;
			String key = "";
			String blocks = "";
			while((c=System.in.read()) != -1)	{
				//System.out.print(Integer.toHexString(c));
				String tmp = Integer.toHexString(c);
				if(tmp.length() < 2)	{
					tmp = "0" + tmp;
				}
				if(counter < 16)	{
					key += tmp;
				} else	{
					blocks += tmp;
				}
				counter += 1;
			}
			byte [] bKey = key.getBytes();
			byte [] bBlock = blocks.getBytes();
			//System.out.println(key);
			//System.out.println(blocks);
			SecretKeySpec secKey = new SecretKeySpec(bKey, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secKey);
			System.out.println(cipher.doFinal(bBlock));

		}
		catch(Exception e) {
			System.out.println("fel");
		}
	}
}
