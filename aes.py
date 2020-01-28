import codecs
import base64
from Crypto.Cipher import AES
import sys

def aes():
    file = open("aes_sample.in", "rb").read()
    print(file.hex())
    #file2 = open("aes_sample.ans", "rb").read()
    #print(file2)
    #for line in sys.stdin.buffer:
        #file = bytes(line)
    key = file[:16]
    rest = file
    aes = AES.new(key, AES.MODE_ECB)
    numberofBlocks = len(rest) / 32
    #print(rest)
    cipher = b''
    for x in range(0, int(numberofBlocks)):
        theBlock = rest[x*32:(x+1)*32]
        #print(theBlock)
        cipher += aes.encrypt(theBlock)
    hex = codecs.decode(cipher.hex()[32:], 'hex')
    print(hex)
if __name__ == '__main__':
    aes()
