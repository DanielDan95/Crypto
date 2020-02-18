from Crypto.Cipher import AES
import base64

def pcksPad(block, padlength):
    needed = padlength - len(block)
    block = bytearray(block)
    for x in range(0, needed):
        block.append(4)
    block = bytes(block)
    print(block)

def aes_ecb(key, blocks):
    cipher = AES.new(key, AES.MODE_ECB)
    m = cipher.decrypt(blocks)
    #print(m.decode())
    return (m)

#unpad PKCS7
def unPad(block):
    #print(block)
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
    file = open(file).read()
    file = base64.b64decode(file)
    IV_vector = []
    for x in range(len(key)):
        IV_vector.append(0)
    IV_vector = bytes(IV_vector)
    aes_cbc_decrypt(key, file, 16, IV_vector)

if __name__ == '__main__':
    #pcksPad("YELLOW SUBMARINE".encode(), 20)
    do_aes_cbc("ch10.txt", "YELLOW SUBMARINE")
