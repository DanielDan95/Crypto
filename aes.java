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


			byte[] bKey = new byte[key.length() / 2];
				for (int i = 0; i < bKey.length; i++) {
					int index = i * 2;
					int v = Integer.parseInt(key.substring(index, index + 2), 16);
					bKey[i] = (byte) v;
				}

			byte[] bBlock = new byte[blocks.length() / 2];
					for (int i = 0; i < bBlock.length; i++) {
						int index = i * 2;
						int v = Integer.parseInt(blocks.substring(index, index + 2), 16);
						bBlock[i] = (byte) v;
					}

			/*String stringKey = hexKeyToString(key.toUpperCase());
			byte [] bKey = testKey.getBytes();
			String stringBlock = hexBlockToString(blocks.toUpperCase());
			byte [] bBlock = testBlock.getBytes();*/

			SecretKeySpec secKey = new SecretKeySpec(bKey, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, secKey);
			byte [] finalBytes = cipher.doFinal(bBlock);
			String finalStr = new String(finalBytes);
			System.out.print(finalStr);

			/*for(char cc: finalStr.toCharArray())	{
				System.out.println(cc);
			}*/
			/*String stmp = "";
			System.out.println(str);
			for (byte b : finalBytes) {
            stmp += String.format("%02X", b);
      }
			System.out.print(stmp);*/

		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
