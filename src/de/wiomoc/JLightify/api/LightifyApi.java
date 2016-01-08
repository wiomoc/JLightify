package de.wiomoc.JLightify.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class LightifyApi {
	private Socket sock;
	private DataOutputStream outStream;
	private DataInputStream inStream;
	private ArrayList<Light> Lights = null;
	private ArrayList<Group> Groups = null;
	boolean DEBUG = false;
	public LightifyApi(String ip,boolean debug) throws UnknownHostException, IOException{
		sock = new Socket(ip,4000);
		outStream = new DataOutputStream(sock.getOutputStream());
		inStream = new DataInputStream(sock.getInputStream());
		this.DEBUG = debug;
	}
	public void end(){
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<Light> getAllLights(){
		if(Lights!=null)return Lights;
		Lights = new ArrayList<Light>();
		byte [] res = sendCmd(new byte[]{0x07,0x00,0x00,0x13,0x00,0x00,0x00,0x02,0x01});
		
		for(int i = 10;i<res.length;i+=50){
			int n;
			for(n = i+26;res[n]!='\0';n++);
			String name = new String(Arrays.copyOfRange(res,i+26,n));
			Lights.add(new Light(this,(res[i+2+2] & 0xFF) << 16 | (res[i+2+1] & 0xFF) << 8 | (res [i+2+0] & 0xFF),name));
		}
		
		return Lights;
	}
	public Light getLightByID(int ID){
		getAllLights();
		for(Light light: Lights)if(light.getID()==ID)return light;
		return null;
	}
	public ArrayList<Group> getAllGroups(){
		if(Groups!=null)return Groups;
		Groups = new ArrayList<Group>();
		byte [] res = sendCmd(new byte[]{0x07,0x00,0x00,0x1e,0x00,0x00,0x00,0x02,0x01});
		
		for(int i = 10;i<res.length;i+=18){
			int n;
			for(n = i+2;res[n]!='\0';n++);
			String name = new String(Arrays.copyOfRange(res,i+2,n));
			Groups.add(new Group(this,(char) res[i] ,name));
		}
		
		return Groups;
	}
	public byte[] sendCmd(byte[] data){
		try {
			byte[] buf;
			outStream.write(data);
			buf= new byte[inStream.read()+1];
			inStream.read(buf);
			return buf;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		
	}
	
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

}
