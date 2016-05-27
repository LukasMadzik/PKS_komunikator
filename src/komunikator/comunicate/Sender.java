package komunikator.comunicate;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukas on 30.10.2015.
 */
public class Sender {
    private String text;
    private String myIp;
    private String targetIp;
    private int fragSize;
    private boolean error;
    private byte[] init;
    private byte[] file;
    private List<byte[]> fragmentyText = new ArrayList<>();
    private String fileName;

    public Sender(String destIp){
        targetIp = destIp;
    }

    public Sender(String text, String myIp, String targetIp, int fragSize, boolean error) {
        this.text = text;
        this.myIp = myIp;
        this.targetIp = targetIp;
        this.fragSize = fragSize;
        this.error = error;
    }
    
    public Sender(byte[] file, String myIp, String targetIp, int fragSize, boolean error, String fileName) {
        this.file = file;
        this.myIp = myIp;
        this.targetIp = targetIp;
        this.fragSize = fragSize;
        this.error = error;
        this.fileName = fileName;
    }

    public void performSend(){
            Informer.setAwaiting(-2);
            send(1);
            while(true) {
            	try {
                Thread.sleep(5);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            	
                if (Informer.getReceived() == 2) {
                    makeFragments();
                    byte[] init = new byte[12];
                    int i = 0;
                    toByte(0, 3, init);
                    toByte(4, fragmentyText.size(), init);
                    toByte(8, fragSize, init);

                    send(init);
                    while(true) {
                    	try {
                            Thread.sleep(5);
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        if(Informer.getReceived() == 6){
                            break;
                        }
                        if (Informer.getReceived() == 4) {
                        	if(error){
                        		Informer.setReceived(0);
                        		byte[] frag = fragmentyText.get(i).clone();
                        		toByte(12, -5, frag);
                        		send(frag);
                        		i++;
                        	}else{
                        		Informer.setReceived(0);
                            	send(fragmentyText.get(i));
                            	i++;
                        	}
                        }
                        if (Informer.getReceived() == 5){
                    		Informer.setReceived(0);
                        	send(fragmentyText.get(i-1));
                        }
                    }
                }
                break;
            }

    }
    
    public void performSendFile(){
        Informer.setAwaiting(-2);
        send(1);
        while(true) {
        	try {
            Thread.sleep(5);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        	
            if (Informer.getReceived() == 2) {
                makeFileFragments();
                byte[] init = new byte[fileName.length()+12];
                int i = 0;
                toByte(0, 7, init);
                toByte(4, fragmentyText.size(), init);
                toByte(8, fragSize, init);
                for(int i1=0;i1<fileName.length();i1++){
                	init[i1+12]=(byte) fileName.charAt(i1);
                }

                send(init);
                while(true) {
                	try {
                        Thread.sleep(5);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    if(Informer.getReceived() == 6){
                        break;
                    }
                    if (Informer.getReceived() == 4) {
                    	if(error){
                    		Informer.setReceived(0);
                    		byte[] frag = fragmentyText.get(i).clone();
                    		toByte(12, -5, frag);
                    		send(frag);
                    		i++;
                    	}else{
                    		Informer.setReceived(0);
                        	send(fragmentyText.get(i));
                        	i++;
                    	}
                    }
                    if (Informer.getReceived() == 5){
                		Informer.setReceived(0);
                    	send(fragmentyText.get(i-1));
                    }
                }
            }
            break;
        }

    }

    public void send(int type){
        DatagramSocket client;
        try {
            byte[] pole = new byte[4];
            client = new DatagramSocket();
            toByte(0, type, pole);
            DatagramPacket sendpack = new DatagramPacket(pole, pole.length, InetAddress.getByName(targetIp), Informer.getPort());
            client.send(sendpack);
            client.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(byte[] b){
        DatagramSocket client;
        try {
            client = new DatagramSocket();
            DatagramPacket sendpack = new DatagramPacket(b, b.length, InetAddress.getByName(targetIp), Informer.getPort());
            client.send(sendpack);
            client.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] makeFragment(int numOfFrags, int index){
        byte[] fragment = new byte[fragSize];
        toByte(0, fragSize, fragment);
        toByte(4, index, fragment );
        toByte(8, numOfFrags, fragment);
        int j=16;
        for(int i=index*(fragSize-16);i<index*(fragSize-16)+fragSize-16 && i<text.length();i++){
            fragment[j++] = ((byte) text.charAt(i));
        }
        toByte(12, 0, fragment);
        toByte(12, crc(fragment, fragSize), fragment);
        return fragment;
    }

    private void makeFragments(){
        int numOfFrags = (int) Math.ceil(text.length() / (double) (fragSize-16));
        int index=0;
        for(index=0;index<numOfFrags;index++){
            fragmentyText.add(makeFragment(numOfFrags, index));
        }
    }
    
    private byte[] makeFileFragment(int numOfFrags, int index){
        byte[] fragment = new byte[fragSize];
        for(int i=0;i<fragSize;i++){
        	fragment[i]=-1;
        }
        toByte(0, fragSize, fragment);
        toByte(4, index, fragment );
        toByte(8, numOfFrags, fragment);
        int j=16;
        for(int i=index*(fragSize-16);i<index*(fragSize-16)+fragSize-16 && i<text.length();i++){
            fragment[j++] = file[i];
        }
        toByte(12, 0, fragment);
        toByte(12, crc(fragment, fragSize), fragment);
        return fragment;
    }
    
    private void makeFileFragments(){
            int numOfFrags = (int) Math.ceil(file.length / (double) (fragSize-16));
            int index=0;
            for(index=0;index<numOfFrags;index++){
                fragmentyText.add(makeFileFragment(numOfFrags, index));
            }
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
