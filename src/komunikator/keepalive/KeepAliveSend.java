/**
 * 
 */
package komunikator.keepalive;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import komunikator.comunicate.Informer;
import komunikator.view.View;

/**
 * @author Lukas
 *
 */
public class KeepAliveSend extends Thread{
	private View gui;
	
	public KeepAliveSend(View gui){
		this.gui = gui;
	}

	public void run(){
		DatagramSocket client;
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
		    try {
		    	if(Informer.getAlive() == 1){
			        byte[] pole = new byte[4];
			        client = new DatagramSocket();
			        toByte(0, 2, pole);
			        DatagramPacket sendpack = new DatagramPacket(pole, pole.length, InetAddress.getByName(Informer.getTargetIp()), Informer.getPort()+1);
			        client.send(sendpack);
			        client.close();
			        Informer.setAlive(0);
			        try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	}else{
		    		if(Informer.getAlive() == 2){
				        byte[] pole = new byte[4];
				        client = new DatagramSocket();
				        toByte(0, 1, pole);
				        DatagramPacket sendpack = new DatagramPacket(pole, pole.length, InetAddress.getByName(Informer.getTargetIp()), Informer.getPort()+1);
				        client.send(sendpack);
				        client.close();
				        Informer.setAlive(0);
				        try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		    			
		    		}else{
			    		if(Informer.getAlive() == 5){
					        byte[] pole = new byte[4];
					        client = new DatagramSocket();
					        toByte(0, 1, pole);
					        DatagramPacket sendpack = new DatagramPacket(pole, pole.length, InetAddress.getByName(Informer.getTargetIp()), Informer.getPort()+1);
					        client.send(sendpack);
					        client.close();
					        Informer.setAlive(0);
					        try {
								Thread.sleep(10000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			    		}else{
				    		Informer.setAlive(Informer.getAlive()-1);
				    		if(Informer.getAlive() == -5){
				    			gui.getTextArea_chat().add("Spojenie prerusene");
				    		}
			    		}
		    		}
		    	}
		    } catch (SocketException e) {
		        e.printStackTrace();
		    } catch (UnknownHostException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}

    private void toByte(int index, int num, byte[] array){
        array[index+3] = (byte) (num & 0xff);
        array[index+2] = (byte) ((num>>8) & 0xff);
        array[index+1] = (byte) ((num>>16) & 0xff);
        array[index] = (byte) ((num>>24) & 0xff);
    }
	

}
