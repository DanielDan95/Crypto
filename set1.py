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
    return highestProbSentence, bestScore

#4
def singleCharXOR():
    bestScore = 0
    bestString = ""
    file = open("strings.txt", "r").read().splitlines()
    for line in file:
        try:
            tmpString, tmpScore = findXOR(line)
            if tmpScore > bestScore:
                bestScore = tmpScore
                bestString = tmpString
        except:
            continue
    print(bestString)

if __name__ == '__main__':
    #hexToBase()
    #fixedXOR("1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965")
    #findXOR("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")
    singleCharXOR()
