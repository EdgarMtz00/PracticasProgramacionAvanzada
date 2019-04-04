import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        Socket soyCliente;
        try {
            soyCliente = new Socket("192.168.84.91", 5000);
            String y = "";
            for (int i = 0; i < 4; i++){
                y += (char)soyCliente.getInputStream().read();
            }
            System.out.println(y);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
