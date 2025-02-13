
import wave
import os

def hide_message_in_wav(input_wav_path, message_file_path, output_wav_path):


    try:
        with wave.open(input_wav_path, 'rb') as wave_read:
            params = wave_read.getparams()
            frames = wave_read.readframes(params.nframes)
            frames = bytearray(frames)

        with open(message_file_path, 'r', encoding='utf-8') as file:
            message = file.read()

        message += "====="
        message_bytes = message.encode('utf-8')

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

        decoded_message = bytearray(extracted_bytes).decode('utf-8', errors='ignore')

        end_marker = "====="
        end_index = decoded_message.find(end_marker)

        if end_index != -1:
            return decoded_message[:end_index]
        else:
            print("Маркер конца сообщения не найден.  Возможно, сообщение повреждено или файл не содержит стеганографию.")
            return None

    except FileNotFoundError:
        print("Ошибка: Файл не найден.")
        return None
    except Exception as e:
        print(f"Произошла ошибка при извлечении сообщения: {e}")
        return None


if __name__ == "__main__":
    input_wav = "original.wav"
    message_file = "message.txt"
    output_wav = "stego.wav"

    if not os.path.exists(input_wav):
        with wave.open(input_wav, 'w') as wave_write:
            wave_write.setnchannels(1)
            wave_write.setsampwidth(2)
            wave_write.setframerate(44100)
            wave_write.writeframes(b'\0' * 44100)

    if not os.path.exists(message_file):
        with open(message_file, "w") as f:
            f.write("Это секретное сообщение для стеганографии!")


    hide_message_in_wav(input_wav, message_file, output_wav)


    extracted_message = extract_message_from_wav(output_wav)

    if extracted_message:
        print("Извлеченное сообщение:", extracted_message)
