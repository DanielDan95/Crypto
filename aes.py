import codecs
import base64
from Crypto.Cipher import AES

def aes():
    file = open("aes_sample.in", "rb").read()
    print(file.hex()[:32])

if __name__ == '__main__':
    aes()
