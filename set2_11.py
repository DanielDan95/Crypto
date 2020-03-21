from Crypto.Cipher import AES
import base64
import random
import string

key = b'0'

def pkcs7Pad(text, padlen):
    #Block size pad needed
    needed = padlen - (len(text) % padlen)
    for x in range(0, needed):
        text += bytes([needed])
    return text

def randomKey():
    #16 bytes random key
    randomKey = ""
    for x in range(0,16):
        randomKey += random.choice(string.ascii_letters)
    randomKey = randomKey.encode()
    return randomKey

def randomAdd(text):
    add = b""
    #before
    rnd = random.randint(5,10)
    for x in range(0, rnd):
        add += bytes([rnd])
    text = add + text
    #after
    add = b""
    rnd = random.randint(5,10)
    for x in range(0, rnd):
        add += bytes([rnd])
    return (text + add)

def ecb_encrypt(key, text):
    cipher = AES.new(key, AES.MODE_ECB)
    m = cipher.encrypt(text)
    return (m)

def cbc_encrypt(key, text):
    wholeCipher = b""
    IV_vector = []
    for x in range(len(key)):
        IV_vector.append(random.randint(0,9))
    IV_vector = bytes(IV_vector)

    pastBlock = b""
    for x in range(0, len(text), 16):
        #First XOR
        block = text[x:x+16]
        newBlock = b""
        if x == 0:
            for i in range(0, len(block)):
                tmp = int(block[i]) ^ int(IV_vector[i])
                newBlock += bytes([tmp])
            block = ecb_encrypt(key, newBlock)
        else:
            for i in range(0, len(block)):
                tmp = int(block[i]) ^ int(pastBlock[i])
                newBlock += bytes([tmp])
            block = ecb_encrypt(key, newBlock)
        pastBlock = block
        wholeCipher += block
    return wholeCipher

def encryption_oracle(text):
    key = randomKey()
    text = randomAdd(text.encode())
    text = pkcs7Pad(text, 16)
    mode = random.randint(0,1)
    encrypted = ""
    #0 = ECB
    if mode == 0:
        encrypted = ecb_encrypt(key, text)
        print("USED ECB")
    #1 = CBC
    elif mode == 1:
        encrypted = cbc_encrypt(key, text)
        print("USED CBC")
    #print(encrypted)
    bbTest(encrypted)

def bbTest(encrypted):
    chunks = [encrypted[i*16:(i+1)*16] for i in range(int(len(encrypted)/16))]
    duplicates = len(chunks) - len(set(chunks))
    if duplicates > 0:
        print("Guessing for ECB")
    else:
        print("Guessing for CBC")

def ecb_encryption_oracle(text):
    unknown =   b'Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg'\
                b'aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq'\
                b'dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg'\
                b'YnkK'
    unknown = base64.b64decode(unknown)
    global key
    text = b"".join([text, unknown])
    text = pkcs7Pad(text, 16)
    encrypted = ecb_encrypt(key, text)
    return encrypted

def decrypt_ecb_noKey():
        blocksize = 0
        global key
        key = randomKey()
        #Testing for len (WHY?)
        for i in range(1,17):
            text = b'D'*i*2
            testval = ecb_encryption_oracle(text)
            block = testval[i:2*i]
            if testval[:i] == block:
                blocksize = i
                print("Found block size: " + str(i))
                break
        #Check if ECB(Why?)
        #Get repeat
        testText = b'A'*blocksize*2
        bbTest(ecb_encryption_oracle(testText))
        #One byte short
        blockBreaker = bytearray(b'A'*(blocksize-1))
        dicti = dict()
        for b in range(256):
            lastByte = ecb_encryption_oracle(blockBreaker + bytes([b]))[:16]
            dicti[lastByte] = bytes([b])
        one_byte_short = ecb_encryption_oracle(b'A'*15)[:16]
        first_unknown_byte = dicti[one_byte_short]
        print("First byte is " + str(first_unknown_byte))
        #Try all bytes of a block
        inBlock = b'A'*blocksize
        blockString = b''
        for x in range(blocksize):
            inBlock = inBlock[1:]
            tmpDict = {}
            for b in range(256):
                lastByte = ecb_encryption_oracle(inBlock + bytes([b]))[:blocksize]
                tmpDict[lastByte] = bytes([b])
            one_byte_short = ecb_encryption_oracle(b'A'*(blocksize-x))[:blocksize]
            last = tmpDict[one_byte_short]
            inBlock += last
            blockString += last
        print("First block is " + str(blockString))
        #Do everything
        #Find len
        cipher_len = len(ecb_encryption_oracle(b''))
        full_len = 0
        i = 1
        while True:
            tmp = b'A'*i
            newCipherLen = len(ecb_encryption_oracle(tmp))
            if cipher_len != newCipherLen:
                full_len = newCipherLen -i
                break
            i += 1
        print("Found len " + str(full_len))
        blocks = int(full_len / blocksize)
        print("This amount of blocks: " + str(blocks))
        full_str = b''
        inBlock = b'A'*blocksize
        for bl in range(blocks):
            blockString = b''
            for x in range(blocksize):
                inBlock = inBlock[1:]
                tmpDict = {}
                for b in range(256):
                    lastByte = ecb_encryption_oracle(inBlock + bytes([b]))[:blocksize]
                    tmpDict[lastByte] = bytes([b])
                one_byte_short = ecb_encryption_oracle(b'A'*(blocksize-x))[blocksize*bl:blocksize*(bl+1)]
                if one_byte_short not in tmpDict:
                    break
                last = tmpDict[one_byte_short]
                inBlock += last
                blockString += last
            full_str += blockString
            inBlock = blockString

        print("Whole string: " + full_str.decode()[1:])

def ecb_encryption_oracle2(text, unknown):
    unknown = base64.b64decode(unknown)
    global key
    text = b"".join([text, unknown])
    text = pkcs7Pad(text, 16)
    encrypted = ecb_encrypt(key, text)
    return encrypted

def decrypt_ecb_noKey2(unkown):
        blocksize = 0
        global key
        key = randomKey()
        #Testing for len (WHY?)
        for i in range(1,17):
            text = b'D'*i*2
            testval = ecb_encryption_oracle2(text, unkown)
            block = testval[i:2*i]
            if testval[:i] == block:
                blocksize = i
                print("Found block size: " + str(i))
                break
        #Check if ECB(Why?)
        #Get repeat
        testText = b'A'*blocksize*2
        bbTest(ecb_encryption_oracle2(testText, unkown))
        #One byte short
        blockBreaker = bytearray(b'A'*(blocksize-1))
        dicti = dict()
        for b in range(256):
            lastByte = ecb_encryption_oracle2(blockBreaker + bytes([b]), unkown)[:16]
            dicti[lastByte] = bytes([b])
        one_byte_short = ecb_encryption_oracle2(b'A'*15, unkown)[:16]
        first_unknown_byte = dicti[one_byte_short]
        print("First byte is " + str(first_unknown_byte))
        #Try all bytes of a block
        inBlock = b'A'*blocksize
        blockString = b''
        for x in range(blocksize):
            inBlock = inBlock[1:]
            tmpDict = {}
            for b in range(256):
                lastByte = ecb_encryption_oracle2(inBlock + bytes([b]), unkown)[:blocksize]
                tmpDict[lastByte] = bytes([b])
            one_byte_short = ecb_encryption_oracle2(b'A'*(blocksize-x), unkown)[:blocksize]
            last = tmpDict[one_byte_short]
            inBlock += last
            blockString += last
        print("First block is " + str(blockString))
        #Do everything
        #Find len
        cipher_len = len(ecb_encryption_oracle2(b''), unkown)
        full_len = 0
        i = 1
        while True:
            tmp = b'A'*i
            newCipherLen = len(ecb_encryption_oracle2(tmp), unkown)
            if cipher_len != newCipherLen:
                full_len = newCipherLen -i
                break
            i += 1
        print("Found len " + str(full_len))
        blocks = int(full_len / blocksize)
        print("This amount of blocks: " + str(blocks))
        full_str = b''
        inBlock = b'A'*blocksize
        for bl in range(blocks):
            blockString = b''
            for x in range(blocksize):
                inBlock = inBlock[1:]
                tmpDict = {}
                for b in range(256):
                    lastByte = ecb_encryption_oracle2(inBlock + bytes([b]), unkown)[:blocksize]
                    tmpDict[lastByte] = bytes([b])
                one_byte_short = ecb_encryption_oracle2(b'A'*(blocksize-x), unkown)[blocksize*bl:blocksize*(bl+1)]
                if one_byte_short not in tmpDict:
                    break
                last = tmpDict[one_byte_short]
                inBlock += last
                blockString += last
            full_str += blockString
            inBlock = blockString

        print("Whole string: " + full_str.decode()[1:])

def parsing_routine(query):
    #make JSON
    json = {}
    objects = query.split("&")
    for o in objects:
        tmpobj = o.split("=")
        json[tmpobj[0]] = tmpobj[1]
    #print(json)
    return json

def profile_for(email):
    #Could also do throw error upon encounter
    email = email.replace("&" , "")
    email = email.replace("=" , "")
    #obj = parsing_routine("email=" + email + "&" + "id=10" + "&" "role=user")
    encoded_str = "email=" + str(email) + "&uid=10&role=user"
    return encoded_str

def encryptUser(encoded_user):
    global key
    padded = pkcs7Pad(encoded_user.encode(), 16)
    encrypted = ecb_encrypt(key, padded)
    return encrypted

def decUser(enc):
    global key
    c = AES.new(key, AES.MODE_ECB)
    dec = c.decrypt(enc)
    return (unpad(dec)).decode()

def unpad(paddedStr):
    paddingVal = paddedStr[-1]
    #print(paddingVal)
    return paddedStr[:-paddingVal]

def hacking():
    global key
    stoneSet = "email=&uid=10&role="
    adm_mail = b'A'*(16- len("email=")) + pkcs7Pad(b'admin',16)
    blocks = int(len(stoneSet)/16) + 1
    usr_mail = b'A' * (blocks*16 - len(stoneSet))
    admin_email = profile_for(adm_mail.decode())
    admin_email_prof = encryptUser(admin_email)
    user_email = profile_for(usr_mail.decode())
    user_email_prof = encryptUser(user_email)
    adminProf = user_email_prof[:blocks*16] + admin_email_prof[16:16*2]
    print(parsing_routine(decUser(adminProf)))


def ecb_cutPaste():
    print(parsing_routine("foo=bar&baz=qux&zap=zazzle"))
    print(profile_for("foo@bar.com"))
    #profile_for("foo@bar.com&role=admin")
    global key
    key = randomKey()
    encryptedUser = encryptUser(profile_for("foo@bar.com"))
    decryptedUser = decUser(encryptedUser)
    print(parsing_routine(decryptedUser))
    hacking()

def pkcs_val(paddedCheck):
    ints = paddedCheck[-1]
    for x in range(len(paddedCheck), len(paddedCheck) - ints, -1):
        if paddedCheck[x-1] != ints:
             raise Exception("WRONG PADDING")
    print("correct pad")

if __name__ == '__main__':
    #pcksPad("YELLOW SUBMARINE".encode(), 20)
    #do_aes_cbc("ch10.txt", "YELLOW SUBMARINE")
    #randomKey_encrypt("hej")
    #encryption_oracle("ppkk" * 90)
    #ecb_encryption_oracle("ppkk" * 90)
    #decrypt_ecb_noKey()
    #ecb_cutPaste()
    pkcs_val(b'ICE ICE BABY\x04\x04\x04\x04')
    pkcs_val(b'ICE ICE BABY\x01\x02\x03\x04')
