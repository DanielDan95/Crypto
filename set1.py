import codecs
import base64
from string import ascii_letters, digits
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
def makeFreqTable():
    table = {
            'a': .08167, 'b': .01492, 'c': .02782, 'd': .04253,
            'e': .12702, 'f': .02228, 'g': .02015, 'h': .06094,
            'i': .06094, 'j': .00153, 'k': .00772, 'l': .04025,
            'm': .02406, 'n': .06749, 'o': .07507, 'p': .01929,
            'q': .00095, 'r': .05987, 's': .06327, 't': .09056,
            'u': .02758, 'v': .00978, 'w': .02360, 'x': .00150,
            'y': .01974, 'z': .00074, ' ': .13000
            }
    return table
def findXOR(string1):
    text = bytes.fromhex(string1)
    bestScore = 0
    highestProbSentence = ""
    theChar = ""
    table = makeFreqTable()
    regex = re.compile(r'[`@_!#$%^&*+,()<>?/\|}{~:]')
    regex2 = re.compile(r'[^A-Fa-f]')

    #Check each letter
    for c in ascii_letters + digits:
        xor = bytes(a ^ ord(c) for a in text)
        orgtheString = codecs.decode(xor)
        theString = orgtheString.lower()#.rstrp()
        freq_string = {}
        for char in theString:
            if regex.search(char) is None:# and regex2.search(char) is None:
                if char in freq_string:
                    freq_string[char] += 1
                else:
                    freq_string[char] = 1
        #Check ratio with table
        tmpScore = 0
        for i in freq_string.keys():
            if i in table:
                score = table[i.lower()]
                tmpScore += score
        if tmpScore >= bestScore:
            bestScore = tmpScore
            highestProbSentence = orgtheString
            theChar = str(c)
    #print("The char %s made: %s"% (theChar, highestProbSentence))
    return highestProbSentence, bestScore, theChar

#4
def singleCharXOR():
    bestScore = 0
    bestString = ""
    char = ""
    file = open("strings.txt", "r").read().splitlines()
    for line in file:
        try:
            tmpString, tmpScore, char = findXOR(line)
            if tmpScore > bestScore:
                bestScore = tmpScore
                bestString = tmpString
        except:
            continue
    print(bestString)

#5
def repeating_keyXOR():
    plaintext = b'Burning \'em, if you ain\'t quick and nimble \nI go crazy when I hear a cymbal'
    key = b'ICE'
    string = ""
    for x in range(0, len(plaintext)):
        hexa = (hex(key[x%3] ^ plaintext[x]))[2:]
        hexa = str(hexa)
        if len(hexa) < 2:
            hexa = '0' + hexa
        string += hexa
    print(string)
#
def repeating_keyXOR_newLine():
    plaintext = """Burning 'em, if you ain't quick and nimble
I go crazy when I hear a cymbal"""
    key = b'ICE'
    byteText = plaintext.encode()
    counter = 0
    string = ""
    for b in byteText:
        xor = b ^ key[counter%3]
        hexa = str(hex(xor))[2:]
        if len(hexa) < 2:
            hexa = '0' + hexa
        string += hexa
        counter += 1
    print(string)

#6
def hamming_distance(String1, String2):
    sum = 0
    for x in range(0, len(String1)):
        int_1 = ord(String1[x])
        int_2 = ord(String2[x])
        for i in range(31, -1, -1):
            b1 = int_1 >> i&1
            b2 = int_2 >> i&1
            sum += not(b1==b2)
    #print(sum)
    return sum

def singleCharXOR1(String):
    bestScore = 0
    bestString = ""
    char = ""
    tmpString, tmpScore, char = findXOR(String)
    if tmpScore > bestScore:
        bestScore = tmpScore
        bestString = tmpString
    return char

def break_reapeat_XOR():
    #test
    #hamming_distance("this is a test","wokka wokka!!!")
    file = open("ch6.txt", "r").read()
    text_after_decode_b64 = base64.b64decode(file)
    text_after_decode_byte = text_after_decode_b64.decode()
    min_distance = []
    min_distance.insert(0, 99999999)
    for keysize in range(2, 40):
        sum = 0
        firstBlock = text_after_decode_byte[0:keysize]
        secondBlock = text_after_decode_byte[keysize:keysize*2]
        sum = hamming_distance(firstBlock, secondBlock)
        sum = (sum / keysize)
        for x in range(0,len(min_distance)):
            if(min_distance[x] > sum):
                print(str(keysize) + " " + str(sum))
                min_distance.insert(x, sum)
                break

    print(min_distance)
    #blocks =  []
    #newBlocks = {}
    #for i in range(0, int(len(text_after_decode_byte)/min_distance), min_distance):
    #    blocks.append(text_after_decode_byte[i:i+min_distance])
    #for str in blocks:
    #    for x in range(0, min_distance):
    #        if not (x in newBlocks):
    #            newBlocks[x] = str[x]
    #        else:
    #            newBlocks[x] += str[x]
    #
    #for i in range(0,min_distance):
    #    c = newBlocks[i]
    #    print(c)
    #    c = singleCharXOR1(c)
    #    print(c)


if __name__ == '__main__':
    #hexToBase()
    #fixedXOR("1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965")
    #findXOR("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")
    #singleCharXOR()
    #repeating_keyXOR()
    #repeating_keyXOR_newLine()
    break_reapeat_XOR()
