from math import ceil
def transposition_encrypt(plaintext):


    lines=ceil(len(plaintext)/3)

    table = [[' ' for _ in range(3)] for _ in range(lines)]

    text_l=list(plaintext)

    index=0
    for i in range(lines):
        for j in range(3):
            table[i][j]=text_l[index]
            index+=1
            if index==len(text_l):
                break


    ciphertext = ''
    for j in range(3):
         for i in range(lines):
            ciphertext += table[i][j]

    return ciphertext


def transposition_decrypt(ciphertext):
    converted=[]
    lines = ceil(len(ciphertext)/3)
    text_l=list(ciphertext)
    table = [[' ' for _ in range(3)] for _ in range(lines)]
    index=0
    for i in range (3):
        for j in range(lines):
            table[j][i]=text_l[index]
            index+=1
    for i in table:
        union=''.join(i)
        converted.append(union)
    decrypted_text = ''.join(converted)
    return decrypted_text

plaintext = "ATTACKATDAW"
columns = 3

ciphertext = transposition_encrypt(plaintext)
print(f"Исходный текст: {plaintext}")
print(f"Шифрованный текст: {ciphertext}")


decrypted_text = transposition_decrypt(ciphertext)
print(f"Дешифрованный текст: {decrypted_text}")


