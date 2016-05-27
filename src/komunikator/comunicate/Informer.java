package komunikator.comunicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukas on 30.10.2015.
 */
public class Informer {
    private static Informer ourInstance = new Informer();

    public static String myIp;
    public static String targetIp;
    public static int port;
    public static int received;
    public static int count;
    public static int awaiting;
    public static int size;
    public static List<byte[]> recievedFrags = new ArrayList<>();
    public static String sprava = "";
    public static int alive=5;
    public static int type;
    
    public static int getType() {
		return type;
	}


	public static void setType(int type) {
		Informer.type = type;
	}


	public static int getAlive() {
		return alive;
	}


	public static void setAlive(int alive) {
		Informer.alive = alive;
	}


	public static void addFrag(int index, byte[] data){
    	byte[] data2 = new byte[2048];
    	data2 = data.clone();
    	recievedFrags.add(index, data2);
    }


    public static int getPort() {
		return port;
	}

	public static void setPort(int port) {
		Informer.port = port;
	}

	public static String getSprava() {
        return sprava;
    }

    public static void setSprava(String sprava) {
        Informer.sprava = sprava;
    }

    public static List<byte[]> getRecievedFrags() {
        return recievedFrags;
    }

    public static void setRecievedFrags(List<byte[]> recievedFrags) {
        Informer.recievedFrags = recievedFrags;
    }

    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        Informer.size = size;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Informer.count = count;
    }

    public static int getAwaiting() {
        return awaiting;
    }

    public static void setAwaiting(int awaiting) {
        Informer.awaiting = awaiting;
    }

    public static int getReceived() {
        return received;
    }

    public static void setReceived(int received) {
        Informer.received = received;
    }

    public static String getMyIp() {
        return myIp;
    }

    public static void setMyIp(String myIp) {
        Informer.myIp = myIp;
    }

    public static String getTargetIp() {
        return targetIp;
    }

    public static void setTargetIp(String targetIp) {
        Informer.targetIp = targetIp;
    }

    public static Informer getInstance() {
        return ourInstance;
    }

    private Informer() {
    }
}
