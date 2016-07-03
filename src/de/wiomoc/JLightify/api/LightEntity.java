package de.wiomoc.JLightify.api;

public class LightEntity {

	protected LightifyApi mApiInstance;
	protected long mID;
	protected String mName;
	protected char mR;
	protected char mG;
	protected char mB;
	protected short mTemp;
	protected char mLumi;
	protected boolean mOnOff;
	LightEntity(LightifyApi ApiInstance,long ID,String name){
		this.mApiInstance = ApiInstance;
		this.mID = ID;
		this.mName = name;
	}
	/**
	 * @param R Red
	 * @param G Green
	 * @param B Blue
	 * @return Success
	 */
	public boolean setColor(char R, char G, char B) {
		if(mApiInstance.switchLocalCloud()){
			byte [] res = sendCmd((byte)0x36,new byte[]{(byte) R,(byte) G,(byte) B,(byte) 0xff,0x00,0x00});
			if(res==null||res[6]!=0)return false;
		}else{
			if(!sendAction("rgbw,"+(int)R+","+(int)G+","+(int)B+",255,00"))return false;
		}
		this.mR = R;
		this.mG = G;
		this.mB = B;
		return true;
	}

	/**
	 * @param Temp Lighttemperature 2000k-6500k
	 * @return Success
	 */
	public boolean setTemp(short Temp) {
		if(mApiInstance.switchLocalCloud()){
			byte [] res = sendCmd((byte)0x33,new byte[]{(byte)(Temp&0xFF),(byte)((Temp>>8)&0xFF),0x00,0x00});
			if(res==null||res[6]!=0)return false;
		}else{
			if(!sendAction("cct,"+Temp+",00"))return false;
		}
		this.mTemp = Temp;
		return true;
	}

	/**
	 * @param Lumi 0-100 Luminosity in percent
	 * @return Success
	 */
	public boolean setLuminosity(char Lumi) {
		if(mApiInstance.switchLocalCloud()){
			byte [] res = sendCmd((byte)0x31,new byte[]{(byte)Lumi,0x00,0x00});
			if(res==null||res[6]!=0)return false;
		}else{
			if(!sendAction((this.mOnOff?"on":"off")+","+(int)Lumi))return false;
		}
		this.mLumi = Lumi;
		return true;
	}

	/**
	 * @param On On:true Off:false
	 * @return
	 */
	public boolean setOnOff(boolean On) {
		if(mApiInstance.switchLocalCloud()){
			byte [] res = sendCmd((byte)0x32,new byte[]{(byte) (On?0x01:0x00)});
			if(res==null||res[6]!=0)return false;
		}else{
			if(!sendAction(On?"on":"off"))return false;
		}
		this.mOnOff = On;
		return true;
	}
	public short getTemp() {
		return this.mTemp;
	}


	public char getLuminosity() {
		// TODO Auto-generated method stub
		return this.mLumi;
	}


	public boolean getOnOff() {
		return this.mOnOff;
	}


	public char getColorR() {
		return this.mR;
	}
	
	public char getColorG() {
		return this.mG;
	}
	

	public char getColorB() {
		return this.mB;
	}
	public String getName() {
		return this.mName;
	}


	public long getID() {
		return this.mID;
	}
	
	public byte[] sendCmd(byte inst,byte [] pay){
		byte[] cmd = new byte[pay.length+16];
		cmd[0] = (byte) ((cmd.length-2)&0xFF);
		cmd[1] = (byte) (((cmd.length-2)>>8)&0xFF);
		cmd[2] = (byte) ((this instanceof Group)?0x02:0x00);
		cmd[3] = inst;
		cmd[8] = (byte) (mID&0xFF); 
		cmd[9] = (byte) ((mID>>8)&0xFF);
		cmd[10] = (byte) ((mID>>16)&0xFF);
		cmd[11] = (byte) ((mID>>24)&0xFF);
		cmd[12] = (byte) ((mID>>32)&0xFF);
		cmd[13] = (byte) ((mID>>40)&0xFF);
		cmd[14] = (byte) ((mID>>48)&0xFF);
		cmd[15] = (byte) ((mID>>56)&0xFF);
		System.arraycopy(pay, 0, cmd, 16, pay.length);
		return mApiInstance.sendCmd(cmd);
		
		
	}
	private boolean sendAction(String act){
		if(this instanceof Group)return mApiInstance.setAttribute("DeviceAction","grou"+((this.mID<10)?"p0":'p')+(int)this.mID+','+act );
		else return mApiInstance.setAttribute("DeviceAction","devic"+((((Light)this).mIndex<10)?"e0":'e')+(int)((Light)this).mIndex+','+act );
	}
  

}
