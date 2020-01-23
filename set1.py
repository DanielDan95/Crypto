import codecs
import base64
from string import ascii_letters
import re

#1
def hexToBase():
    string = '49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d'
    hex = codecs.decode(string, 'hex')
    print(hex(hex))
    enc = base64.b64encode(hex)
    print((enc))
    return

#2
def fixedXOR(string1, string2):
    string1 = (int(string1, 16))
    string2 = (int(string2, 16))
    finalString = string1 ^ string2
    print(hex(finalString))
    return

#3
def findXOR(string1):
    text = bytes.fromhex(string1)
    #print(text)
    regex = re.compile(r"[^a-zA-Z\.?!:\n' ]")

    for c in ascii_letters:
        xor = bytes(a ^ ord(c) for a in text)
        
    return


if __name__ == '__main__':
    #hexToBase()
    #fixedXOR("1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965")
    findXOR("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")
