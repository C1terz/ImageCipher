import wave
from math import ceil

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

def extract_message_from_wav(input_wav_path):

    try:
        with wave.open(input_wav_path, 'rb') as wave_read:
            params = wave_read.getparams()
            frames = wave_read.readframes(params.nframes)
            frames = bytearray(frames)

        extracted_bits = []
        for byte in frames:
            extracted_bits.append(byte & 1)

        extracted_bytes = []
        current_byte = 0
        for i in range(0, len(extracted_bits), 8):
            if i + 8 > len(extracted_bits):
                break

            for j in range(8):
                current_byte = (current_byte << 1) | extracted_bits[i + j]
            extracted_bytes.append(current_byte)
            current_byte = 0

        decoded_message1 = bytearray(extracted_bytes).decode('utf-8', errors='ignore')

        end_marker = "====="
        end_index = decoded_message1.find(end_marker)

        if end_index != -1:
            decoded_message1=decoded_message1[:end_index]
        else:
            print("Маркер конца сообщения не найден.  Возможно, сообщение повреждено или файл не содержит стеганографию.")
            return None
        decoded_message = transposition_decrypt(decoded_message1)

        return decoded_message

    except FileNotFoundError:
        print("Ошибка: Файл не найден.")
        return None
    except Exception as e:
        print(f"Произошла ошибка при извлечении сообщения: {e}")
        return None



if __name__ == "__main__":
    output_wav = "stego.wav"

    extracted_message = extract_message_from_wav(output_wav)

    if extracted_message:
        print("Извлеченное сообщение:\n", extracted_message)
