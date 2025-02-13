
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        ImageCipher IC = new ImageCipher();
        //IC.encrypt("BLINKBKBEZGAME GG","D:\\\\tocipher\\\\images.jpg\"");
        String fp = "D:\\\\tocipher\\\\images.jpg";
        String[] valuestoOutput = IC.decrypt(fp);
        System.out.println(Arrays.toString(valuestoOutput));



    }
}