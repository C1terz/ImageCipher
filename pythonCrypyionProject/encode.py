import wave
from math import ceil



def hide_message_in_wav(input_wav_path, message_file_path, output_wav_path):

    try:
        with wave.open(input_wav_path, 'rb') as wave_read:
            params = wave_read.getparams()
            frames = wave_read.readframes(params.nframes)
            frames = bytearray(frames)
            print(222)



        message_file_path += "====="
        message_bytes = message_file_path.encode('utf-8')

        if len(message_bytes) * 8 > len(frames):
            raise ValueError("Сообщение слишком длинное для этого аудиофайла.")

        message_bit_index = 0
        for i in range(len(message_bytes)):
            byte = message_bytes[i]
            for j in range(8):
                bit = (byte >> (7 - j)) & 1

                frames[message_bit_index] = (frames[message_bit_index] & 0xFE) | bit

                message_bit_index += 1

        with wave.open(output_wav_path, 'wb') as wave_write:
            wave_write.setparams(params)
            wave_write.writeframes(bytes(frames))

        print(f"Сообщение успешно спрятано в {output_wav_path}")

    except FileNotFoundError:
        print("Ошибка: Один из файлов не найден.")
    except ValueError as e:
        print(f"Ошибка: {e}")
    except Exception as e:
        print(f"Произошла непредвиденная ошибка: {e}")

def transposition_encrypt(message):

    lines=ceil(len(message)/3)

    table = [[' ' for _ in range(3)] for _ in range(lines)]

    text_l=list(message)

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

def text(message_file):
    with open(message_file, 'r', encoding='utf-8') as file:
        message = file.read()
    return message


if __name__ == "__main__":

    input_wav = "original.wav"
    message_file = "message.txt"
    output_wav = "stego.wav"

    text_message=text(message_file)
    ciphrotext=transposition_encrypt(text_message)
    hide_message_in_wav(input_wav, ciphrotext, output_wav)

