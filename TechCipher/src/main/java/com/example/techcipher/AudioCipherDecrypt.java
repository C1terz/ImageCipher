package com.example.techcipher;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioCipherDecrypt {

    public static String transpositionDecrypt(String ciphertext) {
        List<String> converted = new ArrayList<>();
        int lines = (int) Math.ceil((double) ciphertext.length() / 3);
        char[] textL = ciphertext.toCharArray();
        char[][] table = new char[lines][3];
        int index = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < lines; j++) {
                if (index < textL.length) {
                    table[j][i] = textL[index];
                    index++;
                }
            }
        }

        for (char[] row : table) {
            StringBuilder union = new StringBuilder();
            for (char c : row) {
                union.append(c);
            }
            converted.add(union.toString());
        }

        StringBuilder decryptedText = new StringBuilder();
        for (String str : converted) {
            decryptedText.append(str);
        }
        return decryptedText.toString();
    }

    public static String extractMessageFromWav(String inputWavPath) {
        try {
            File wavFile = new File(inputWavPath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
            byte[] frames = audioInputStream.readAllBytes();
            List<Integer> extractedBits = new ArrayList<>();

            for (byte b : frames) {
                extractedBits.add(b & 1);
            }

            List<Integer> extractedBytes = new ArrayList<>();
            int currentByte = 0;

            for (int i = 0; i < extractedBits.size(); i += 8) {
                if (i + 8 > extractedBits.size()) {
                    break;
                }

                for (int j = 0; j < 8; j++) {
                    currentByte = (currentByte << 1) | extractedBits.get(i + j);
                }
                extractedBytes.add(currentByte);
                currentByte = 0;
            }

            String decodedMessage1 = new String(toByteArray(extractedBytes));

            String endMarker = "=====";
            int endIndex = decodedMessage1.indexOf(endMarker);

            if (endIndex != -1) {
                decodedMessage1 = decodedMessage1.substring(0, endIndex);
            } else {
                System.out.println("Маркер конца сообщения не найден.  Возможно, сообщение повреждено или файл не содержит стеганографию.");
                return null;
            }
            return transpositionDecrypt(decodedMessage1);

        } catch (IOException | UnsupportedAudioFileException e) {
            System.out.println("Ошибка: Файл не найден или недопустимый формат.");
            return null;
        } catch (Exception e) {
            System.out.println("Произошла ошибка при извлечении сообщения: " + e.getMessage());
            return null;
        }
    }

    private static byte[] toByteArray(List<Integer> extractedBytes) {
        byte[] byteArray = new byte[extractedBytes.size()];
        for (int i = 0; i < extractedBytes.size(); i++) {
            byteArray[i] = (byte) (int) extractedBytes.get(i);
        }
        return byteArray;
    }

    public String decrypt(String outputWav) {
        return extractMessageFromWav(outputWav);
    }
}


