package de.wiomoc.JLightify.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class LightifyApi {
	private boolean useLocal;
	private boolean useCloud;
	
	private Socket sock;
	private DataOutputStream outStream;
	private DataInputStream inStream;
	private ArrayList<Light> Lights = null;
	private ArrayList<Group> Groups = null;
	
	private String CLOUDAPI_ENDPOINT_URL = "https://emea-srmprod01-api.arrayent.com:8081/zdk/services/zamapi/";
	private int CLOUDAPI_ENDPOINT_APIID = 2058;
	private int CLOUDAPI_DEVICETYPEID = 377;
	private String secureID;
	private long devID; 
	private long lastCloudUpdate = 0;
	
	boolean DEBUG = false;
	private String mIP;
	public LightifyApi(String ip,boolean debug) throws UnknownHostException, IOException{
		this.mIP = ip;
		this.DEBUG = debug;
		this.useLocal=true;
		connectLocal();
	}
	public LightifyApi(boolean debug) throws UnknownHostException, IOException{
		this.DEBUG = debug;
	}
	public void end(){
		try {
			if(sock!=null)sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean logIn(String username,String password){
		try {
			this.secureID = getXmlElement("securityToken", "userLogin?appId="+CLOUDAPI_ENDPOINT_APIID+"&name="+ URLEncoder.encode(username,"UTF-8")+"&password="+URLEncoder.encode(password,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		if(this.secureID==null)return false;
		this.devID = Integer.parseInt(getXmlElement("devId", "getDeviceList?deviceTypeId="+CLOUDAPI_DEVICETYPEID+"&deviceTypeId=377&secToken="+this.secureID+"&userId="+this.secureID.subSequence(0, this.secureID.indexOf('-'))));
		if(this.devID==0)return false;
		if(this.DEBUG)System.out.println("SecureID:"+this.secureID+" DeviceId:"+this.devID);
		this.useCloud = true;
		return true;
		
	}
	private XMLStreamReader getCloudXml(String operation) throws XMLStreamException, IOException{
		URL url = new URL(CLOUDAPI_ENDPOINT_URL+operation);
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		return factory.createXMLStreamReader(con.getInputStream());	 
		
	}
	private String getXmlElement(String Name,String operation){
		try{
		XMLStreamReader reader = getCloudXml(operation);
		boolean detected = false;
		while ( reader.hasNext() ){
			reader.next();
			switch(reader.getEventType()){
			case XMLStreamConstants.START_ELEMENT:
				if(reader.getLocalName()==Name){
					detected=true;
				}
				break;
			case XMLStreamConstants.CHARACTERS:
				if(detected){
					return reader.getText();
				}
				break;
			}
		}
	} catch (XMLStreamException | IOException e) {
		e.printStackTrace();
	}
	return null;

}
	public boolean fetchAll(){
		if(switchLocalCloud()){
			getAllGroups();
			getAllLights();
			for(Group grp: this.Groups){
				grp.getChildren();
			}
			for(Light lgt: this.Lights){
				lgt.fetchLocal();
			}
		}else{
			try {
				XMLStreamReader reader = getCloudXml("getDeviceAttributesWithValues?devId="+this.devID+"&deviceTypeId=377&secToken="+this.secureID);
				boolean attrdetected = false;
				boolean keydetected = false;
				boolean valuedetected = false;
				String key = null;
				String value = null;
				String [] DeviceName = new String[51];
				String [] DeviceInfo = new String[51];
				String [] GroupName = new String[51];
				long [] DeviceGroupMembers = new long[51];
				String [] DeviceStatus = new String[51];
				while ( reader.hasNext() ){
					reader.next();
					switch(reader.getEventType()){
					case XMLStreamConstants.START_ELEMENT:
						switch(reader.getLocalName()){
							case "attrList":
								attrdetected = true;
								key = null;
								value = null;
								break;
							case "name":
								if(attrdetected)keydetected = true;
								break;
							case "value":
								if(attrdetected)valuedetected = true;
								break;
								
						}
						break;
					case XMLStreamConstants.END_ELEMENT:
	
						switch(reader.getLocalName()){
						case "attrList":
							attrdetected = false;
							if(value==null)break;
							if(this.DEBUG)System.out.println(key+":"+value);
							if(key.startsWith("DeviceName")){
								DeviceName[Integer.parseInt(key.substring(key.length()-2))]=value;
							}else if(key.startsWith("DeviceInfo")){
								DeviceInfo[Integer.parseInt(key.substring(key.length()-2))]=value;
							}else if(key.startsWith("GroupName")){
								GroupName[Integer.parseInt(key.substring(key.length()-2))]=value;
							}else if(key.startsWith("DeviceGroupMembers")){
								DeviceGroupMembers[Integer.parseInt(key.substring(key.length()-2))]=Long.parseLong(value);
							}else if(key.startsWith("DeviceStatus")){
								DeviceStatus[Integer.parseInt(key.substring(key.length()-2))]=value;
							}
							break;
						case "name":
							keydetected = false;
							break;
						case "value":
							valuedetected = false;
							break;
							
						}
						break;
					case XMLStreamConstants.CHARACTERS:
						if(keydetected)key=reader.getText();
						if(valuedetected)value=reader.getText();
					}
				}
				if(this.Groups==null){
					this.Groups = new ArrayList<Group>();
					for(char i = 1;i<GroupName.length;i++){
						if(GroupName[i]==null)continue;
						Group tmp = new Group(this,i,GroupName[i]);
						
						this.Groups.add(tmp);
					}
				}
				if(this.Lights==null){
					this.Lights = new ArrayList<Light>();
					for(char i = 1;i<DeviceInfo.length;i++){
						if(DeviceInfo[i]==null)continue;
						Light tmp = new Light(this,new BigInteger(DeviceInfo[i].substring(0, 16),16).longValue(),DeviceName[i],i);
						long memb = DeviceGroupMembers[i];
						for(char n = 0;memb!=0;n++){
							if((memb&1)==1){
								Group grp = this.Groups.get(n);
								if(grp.mChildren==null)grp.mChildren = new ArrayList<Light>();
								grp.mChildren.add(tmp);
								
							}
							memb>>>=1;
						}
						String [] devStatus = DeviceStatus[i].split(",");
						if(devStatus[1].equals("online"))tmp.mIsOnline=true;
						if(devStatus[3].equals("1"))tmp.mOnOff=true;
						tmp.mLumi=(char) Integer.parseInt(devStatus[4]);
						tmp.mTemp=(short) Integer.parseInt(devStatus[5]);
						int color = (int) Long.parseLong(devStatus[6]);
						tmp.mR=(char) ((color>>>24)&0xFF);
						tmp.mG=(char) ((color>>>16)&0xFF);
						tmp.mB=(char) ((color>>>8)&0xFF);
						this.Lights.add(tmp);
					}
				}
				
			} catch (XMLStreamException |IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
		
	}
	public boolean switchLocalCloud(){
		return this.useLocal;
		//TODO: add Test
	}
	private void connectLocal() throws UnknownHostException, IOException{
		sock = new Socket(this.mIP,4000);
		outStream = new DataOutputStream(sock.getOutputStream());
		inStream = new DataInputStream(sock.getInputStream());
	}
	public ArrayList<Light> getAllLights(){
		if(Lights!=null)return Lights;
		Lights = new ArrayList<Light>();
		byte [] res = sendCmd(new byte[]{0x07,0x00,0x00,0x13,0x00,0x00,0x00,0x02,0x01});
		char num = 1;
		for(int i = 9;i<res.length;i+=50){
			int n;
			for(n = i+26;res[n]!='\0';n++);
			String name = new String(Arrays.copyOfRange(res,i+26,n));
			long id = 0;
			for (char x = 0; x < 8; x++){
			   id |= ((long) res[(i+2+x)] & 0xffL) << (8 * x);
			}
			Lights.add(new Light(this,id ,name,num));
			num++;
		}
		
		return Lights;
	}
	public Light getLightByID(long id){
		getAllLights();
		for(Light light: Lights)if(light.getID()==id)return light;
		return null;
	}
	public ArrayList<Group> getAllGroups(){
		if(Groups!=null)return Groups;
		Groups = new ArrayList<Group>();
		byte [] res = sendCmd(new byte[]{0x07,0x00,0x00,0x1e,0x00,0x00,0x00,0x02,0x01});
		for(int i = 9;i<res.length;i+=18){
			int n;
			for(n = i+2;res[n]!='\0';n++);
			String name = new String(Arrays.copyOfRange(res,i+2,n));
			Groups.add(new Group(this,(char) res[i] ,name));
		}
		
		return Groups;
	}
	public byte[] sendCmd(byte[] data){
		try {
			if(!sock.isConnected())connectLocal();
			byte[] buf;
			outStream.write(data);
			buf= new byte[inStream.read()|inStream.read()<<8];
			inStream.read(buf);
			return buf;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		
	}
	public boolean setAttribute(String key,String value){
		try {
			getCloudXml("setMultiDeviceAttributes2?devId="+this.devID+"&name1="+key+"&secToken="+this.secureID+"&value1="+URLEncoder.encode(value,"UTF-8"));
			System.out.println(value);
		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 3];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 3] = hexArray[v >>> 4];
	        hexChars[j * 3 + 1] = hexArray[v & 0x0F];
	        hexChars[j * 3 + 2] = ' ';
	    }
	    return new String(hexChars);
	}

}
