import java.io.*;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class aes
{
	public static byte [] makeByteKey(String key)	{
		byte[] bKey = new byte[key.length() / 2];
			for (int i = 0; i < bKey.length; i++) {
				int index = i * 2;
				int v = Integer.parseInt(key.substring(index, index + 2), 16);
				bKey[i] = (byte) v;
			}
			return bKey;
	}

	public static String convertBlocks(byte  [] bKey, String blocks)	{
		String encrypt = "";
		SecretKeySpec secKey = new SecretKeySpec(bKey, "AES");
		int numberofBlocks = blocks.length() / 32;
		for(int j = 0; j < numberofBlocks; j++)	{
			String hexBlocks = blocks.substring(j*32, j*32+32);
			byte[] bBlock = new byte[16];
					for (int i = 0; i < bBlock.length; i++) {
						int index = i * 2;
						int v = Integer.parseInt(hexBlocks.substring(index, index + 2), 16);
						bBlock[i] = (byte) v;
					}
					try	{
						Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
						cipher.init(Cipher.ENCRYPT_MODE, secKey);
						byte [] finalBytes = cipher.doFinal(bBlock);
						String blockString = new String(finalBytes, "ISO-8859-15");
						encrypt += blockString;
					} catch(Exception e)	{
						System.out.println(e);
					}
		}

		return encrypt;

	}

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

			byte [] bKey = makeByteKey(key);
			String finalStr = convertBlocks(bKey, blocks);

			System.out.print(finalStr);


		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
