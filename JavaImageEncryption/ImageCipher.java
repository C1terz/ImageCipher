import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageCipher {
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    public void encrypt(String textToEncrypt,String filePath) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String command = "cmd.exe /c echo "+"$enc"+textToEncrypt+" >> "+filePath;
        rt.exec(command);
    }
    public String[] decrypt(String filePath) throws IOException {
        String flag = "$enc";

        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        byte[] flagBytes = flag.getBytes();

        String hexString = bytesToHex(fileBytes);
        String flagHex = bytesToHex(flagBytes);
        String[] hexSplitted = hexString.split(flagHex);

        String[] output = new String[hexSplitted.length-1];
        for (int i = 0; i < hexSplitted.length; i++) {
            if (i>=1) {

                byte[] byteConversion = new byte[hexSplitted[i].length()/2];
                for (int j = 0; j < byteConversion.length; j++) {
                    int index = j*2;
                    int val = Integer.parseInt(hexSplitted[i].substring(index,index+2), 16);
                    byteConversion[j] = (byte)val;
                }

                String answer = new String(byteConversion, StandardCharsets.UTF_8).trim();
                output[i-1] = answer;
            }
        }
        return output;
    }
}
