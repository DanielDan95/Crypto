from Crypto.Cipher import AES
import base64
import random
import string

def pcksPad(block, padlength):
    needed = padlength - len(block)
    block = bytearray(block)
    for x in range(0, needed):
        block.append(needed)
    block = bytes(block)
    return block

def aes_ecb(key, blocks):
    cipher = AES.new(key, AES.MODE_ECB)
    m = cipher.decrypt(blocks)
    #print(m.decode())
    return (m)

#unpad PKCS7
def unPad(block):
    #print(block)
    return block

#TEXT not key
def randomAppendandPrepend(text):
    rnd = random.randint(5,10)
    #print(rnd)
    block = text.encode()
    add = b""
    for x in range(0, rnd):
        add += bytes([rnd])
    block = add + block + add
    return block

def aes_cbc_decrypt(key, text, size, iv):
    #For each block
    plainText = ""
    for x in range(0, len(text), size):
        block = aes_ecb(key, text[x:x+size])
        tmpString = ""
        #IV XOR
        if x == 0:
            for b in block:
                tmpString += chr(int(b) ^ 0)
        #regular XOR with eventual Padded block
        else:
            pot_padded_block = text[x-size:x]
            unpaded_block = unPad(pot_padded_block)
            for x in range(0, len(unpaded_block)):
                tmpString += chr(unpaded_block[x] ^ int(block[x]))
        plainText += tmpString

    print(plainText)

def do_aes_cbc(file, key):
    #file = open(file).read()
    #file = base64.b64decode(file)
    IV_vector = []
    for x in range(len(key)):
        IV_vector.append(0)
    IV_vector = bytes(IV_vector)
    aes_cbc_decrypt(key, file, 16, IV_vector)

def aes_ecb_encrypt(key, blocks):
    cipher = AES.new(key, AES.MODE_ECB)
    m = cipher.encrypt(blocks)
    #print(m.decode())
    return (m)

def encryption_oracle(text):
    #Generate randomKey
    randomKey = ""
    for x in range(0,16):
        randomKey += random.choice(string.ascii_letters)
    #RandomByte append and Prepend
    text = randomAppendandPrepend(text)
    #See if pad needed
    testPad = len(text) % 16
    if testPad != 0:
        oldText = text[:-testPad]
        newPadText = pcksPad(text[-testPad:],16)
        text = oldText + newPadText
    #String obj

    #Random mode
    randomMode = random.randint(0,0)
    #0 = ECBprint("CBC")
    if randomMode == 0:
        encrypted = b""
        for x in range(0, len(text), 16):
            encrypted += aes_ecb_encrypt(randomKey, text[x:x+16])
        print(encrypted)
    #1 = CBC
    elif randomMode == 1:
        do_aes_cbc(text, randomKey)

if __name__ == '__main__':
    #pcksPad("YELLOW SUBMARINE".encode(), 20)
    #do_aes_cbc("ch10.txt", "YELLOW SUBMARINE")
    #randomKey_encrypt("hej")
    encryption_oracle("hejkkk")
