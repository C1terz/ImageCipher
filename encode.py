import wave


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



if __name__ == "__main__":
    input_wav = "original.wav"
    message_file = "message.txt"

    hide_message_in_wav(input_wav, message_file, output_wav)

