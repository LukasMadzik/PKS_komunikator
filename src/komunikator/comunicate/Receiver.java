package komunikator.comunicate;

import komunikator.view.View;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by Lukas on 30.10.2015.
 */
public class Receiver extends Thread{
    byte[] data;
    private View gui;

    public Receiver(View gui){
        this.gui = gui;
    }

    public void run(){

        data = new byte[2048];

        try{
            InetAddress add = InetAddress.getByName(Informer.getMyIp());
            DatagramSocket serverSocket = new DatagramSocket(Informer.getPort(), add);
            DatagramPacket datagram = new DatagramPacket(data, data.length);
            Resovle resolve = new Resovle(gui);
            while(true) {
                serverSocket.receive(datagram);
                resolve.run(datagram);
            }
        }catch(Exception e){
            System.out.printf("%s",e);
        }
    }

    private void decode(List<byte[]> recievedFrags) {
        String str = "";
        for(int i = 0; i<recievedFrags.size();i++){
            String string = new String(recievedFrags.get(i));
            string = string.substring(16);
            str+=string;
        }
        gui.getTextArea_chat().add(str);
    }

    public static int arrayToInt(byte []b, int index)
    {
        return   b[index+3] & 0xFF |
                (b[index+2] & 0xFF) << 8 |
                (b[index+1] & 0xFF) << 16 |
                (b[index+0] & 0xFF) << 24;
    }

    private void toByte(int index, int num, byte[] array){
        array[index+3] = (byte) (num & 0xff);
        array[index+2] = (byte) ((num>>8) & 0xff);
        array[index+1] = (byte) ((num>>16) & 0xff);
        array[index] = (byte) ((num>>24) & 0xff);
    }

    private static int crc(byte[] buf, int len){
        int crc = 0xFFFF;
        for (int pos = 0; pos < len; pos++) {
            crc ^= (int)buf[pos] & 0xFF;
            for (int i = 8; i != 0; i--) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                }
                else
                    crc >>= 1;
            }
        }
        return crc;
    }
}
