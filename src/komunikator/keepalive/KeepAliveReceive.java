package komunikator.keepalive;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import komunikator.comunicate.Informer;
import komunikator.comunicate.Resovle;
import komunikator.view.View;

public class KeepAliveReceive extends Thread{
	byte[] data;
    private View gui;

    public KeepAliveReceive(View gui) {
		this.gui = gui;
	}

    public void run(){

        data = new byte[4];

        try{
            InetAddress add = InetAddress.getByName(Informer.getMyIp());
            DatagramSocket serverSocket = new DatagramSocket(Informer.getPort()+1, add);
            DatagramPacket datagram = new DatagramPacket(data, data.length);
            Resovle resolve = new Resovle(gui);
            while(true) {
                serverSocket.receive(datagram);
                if(arrayToInt(data, 0)==1){
                	Informer.setAlive(1);
                }else{
                	if(arrayToInt(data, 0)==2){
                		Informer.setAlive(2);
                	}
                }
            }
        }catch(Exception e){
            System.out.printf("%s",e);
        }
        //return data;
    }

    public static int arrayToInt(byte []b, int index)
    {
        return   b[index+3] & 0xFF |
                (b[index+2] & 0xFF) << 8 |
                (b[index+1] & 0xFF) << 16 |
                (b[index+0] & 0xFF) << 24;
    }

}
