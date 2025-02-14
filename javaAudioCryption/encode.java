import javax.sound.sampled.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class encode {

    public static void hideMessageInWav(String inputWavPath, String messageFilePath, String outputWavPath) {
        try {

            AudioInputStream waveRead = AudioSystem.getAudioInputStream(new File(inputWavPath));
            AudioFormat format = waveRead.getFormat();
            byte[] frames = new byte[(int) waveRead.getFrameLength() * format.getFrameSize()];
            waveRead.read(frames);


            messageFilePath += "=====";
            byte[] messageBytes = messageFilePath.getBytes(StandardCharsets.UTF_8);

            if (messageBytes.length * 8 > frames.length) {
                throw new IllegalArgumentException("Сообщение слишком длинное для этого аудиофайла.");
            }

            int messageBitIndex = 0;
            for (byte b : messageBytes) {
                for (int j = 0; j < 8; j++) {
                    int bit = (b >> (7 - j)) & 1;
                    frames[messageBitIndex] = (byte) ((frames[messageBitIndex] & 0xFE) | bit);
                    messageBitIndex++;
                }
            }

            AudioInputStream waveWrite = new AudioInputStream(new ByteArrayInputStream(frames), format, frames.length / format.getFrameSize());

            AudioSystem.write(waveWrite, AudioFileFormat.Type.WAVE, new File(outputWavPath));
            System.out.println(45);
            waveWrite.close();


            System.out.println("Сообщение успешно спрятано в " + outputWavPath);

        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: Один из файлов не найден.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла непредвиденная ошибка: " + e.getMessage());
        }
    }

    public static String transpositionEncrypt(String message) {
        int lines = (int) Math.ceil((double) message.length() / 3);
        char[][] table = new char[lines][3];

        char[] textL = message.toCharArray();
        int index = 0;
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < 3; j++) {
                if (index < textL.length) {
                    table[i][j] = textL[index];
                    index++;
                } else {
                    table[i][j] = ' ';
                }
            }
        }

        StringBuilder ciphertext = new StringBuilder();
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < lines; i++) {
                ciphertext.append(table[i][j]);
            }
        }

        return ciphertext.toString();
    }

    public static String text(String messageFile) throws IOException {
        BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(messageFile), StandardCharsets.UTF_8));
        StringBuilder message = new StringBuilder();
        String line;
        while ((line = file.readLine()) != null) {
            message.append(line);
        }
        file.close();
        return message.toString();
    }

    public static void main(String[] args) {
        String inputWav = "C:\\Users\\nepip\\IdeaProjects\\encode\\src\\original.wav";//your file path
        String messageFile = "C:\\Users\\nepip\\IdeaProjects\\encode\\src\\message.txt";//your file path
        String outputWav = "C:\\Users\\nepip\\IdeaProjects\\encode\\src\\stego.wav";//your file path

        try {
            String textMessage = text(messageFile);
            String ciphertext = transpositionEncrypt(textMessage);
            hideMessageInWav(inputWav, ciphertext, outputWav);
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

