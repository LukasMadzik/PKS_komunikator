package komunikator.comunicate;

import komunikator.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukas on 31.10.2015.
 */
public class Resovle extends Thread {
    private DatagramPacket datagram;
    private View gui;
    byte[] data;

    public Resovle(View gui){
        this.gui = gui;
    }

    public void run(DatagramPacket datagram){
        this.datagram = datagram;
        data = new byte[2048];
        data = datagram.getData();
        int first = arrayToInt(data, 0);

        switch(first){
            case 1:{
                System.out.printf("Hello\n");
                Sender sender = new Sender(gui.getTextField_TargetIP().getText());
                sender.send(2);
                break;
            }
            case 2:{
                System.out.printf("Hi\n");
                Informer.setReceived(2);
                break;
            }
            case 3:{
                System.out.printf("Init\n");
                Informer.addFrag(0, data);
                Sender sender = new Sender(gui.getTextField_TargetIP().getText());
                sender.send(4);
                Informer.setType(1);
                break;

            }
            case 4:{
                System.out.printf("Next\n");
                Informer.setReceived(4);
                Informer.setAwaiting(Informer.getAwaiting() + 1);
                break;
            }
            case 5:{
                System.out.printf("Repeat\n");
                Informer.setReceived(5);
                break;
            }
            case 6:{
                System.out.printf("OK\n");
                Informer.setReceived(6);
                
                break;
            }
            case 7:{
                System.out.printf("File\n");
                Informer.addFrag(0, data);
                Sender sender = new Sender(gui.getTextField_TargetIP().getText());
                sender.send(4);
                Informer.setType(2);
                break;
            }
            default:{
                System.out.printf("Fragment\n");
                if (chckCrc(data)){
                	Informer.addFrag(arrayToInt(data, 4)+1,data);
                	Sender sender = new Sender(gui.getTextField_TargetIP().getText());
                
	                if(arrayToInt(data, 4)+1 == arrayToInt(data, 8)){
	                	sender.send(6);
	                	if(Informer.getType()==1){
	                		gui.getTextArea_chat().add("On: "+decode());
	                	}else{
	                		try {
								decodeFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
	                	}
	                	Informer.getRecievedFrags().clear();
	                }else{
	                	sender.send(4);
	                }
                }else{
                	Sender sender = new Sender(gui.getTextField_TargetIP().getText());
                	sender.send(5);
                }

                
                


                break;
            }
        }

    }

    private boolean chckCrc(byte[] data2) {
		int crc = arrayToInt(data2, 12);
    	toByte(12, 0, data2);
    	if(crc == crc(data2, arrayToInt(data2, 0))){
    		return true;
    	}else
		return false;
	}
    
    private void decodeFile() throws IOException {
    	int count = arrayToInt(Informer.getRecievedFrags().get(0), 4);
    	int size = arrayToInt(Informer.getRecievedFrags().get(0), 8);
        byte[] file = new byte[size*count];
        List<byte[]> recievedFrags = new ArrayList<>();
        for(int i = 1; i<Informer.getRecievedFrags().size();i++){
        	for(int j=0;j<size;j++){
        		file[(i-1)*size+j]=Informer.getRecievedFrags().get(i)[j];
        	}
        }
        String str = "";
        for(int i = 12;i<Informer.getRecievedFrags().get(0).length-12;i++){
        	str+=Informer.getRecievedFrags().get(0)[i];
        }
        
        File f = new File(str);

	    FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(f);
			fileInputStream.read(file);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	private String decode() {
    	int count = arrayToInt(Informer.getRecievedFrags().get(0), 4);
    	int size = arrayToInt(Informer.getRecievedFrags().get(0), 8);
        String str = "";
        List<byte[]> recievedFrags = new ArrayList<>();
        for(int i = 1; i<Informer.getRecievedFrags().size();i++){
            String string = new String(Informer.getRecievedFrags().get(i));
            string = string.substring(16);
            str+=string;
            int nullindex = str.indexOf("\u0000");
            str = str.substring(0,nullindex);
        }
        return str;
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
