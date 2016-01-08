package de.wiomoc.JLightify.api;

public class LightEntity {

	protected LightifyApi mApiInstance;
	protected int mID;
	protected String mName;
	protected char mR;
	protected char mG;
	protected char mB;
	protected short mTemp;
	protected char mLumi;
	protected boolean mOnOff;
	LightEntity(LightifyApi ApiInstance,int ID,String name){
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
		mApiInstance.sendCmd(new byte[]{0x14,0x00,(byte) ((this instanceof Group)?0x02:0x00),0x36,0x00,0x00,0x00,0x00,(byte) (mID&0xFF),(byte) ((mID>>8)&0xFF),(byte) ((mID>>16)&0xFF),0x00,0x00,0x26,0x18,(byte) 0x84,(byte) R,(byte) G,(byte) B,(byte) 0xff,0x00,0x00});
		this.mR = R;
		this.mG = G;
		this.mB = B;
		System.out.println(this instanceof Group);
		return false;
	}

	/**
	 * @param Temp Lighttemperature 2000k-6500k
	 * @return Success
	 */
	public boolean setTemp(short Temp) {
		mApiInstance.sendCmd(new byte[]{0x12,0x00,(byte) ((this instanceof Group)?0x02:0x00),0x33,0x00,0x00,0x00,0x00,(byte) (mID&0xFF),(byte) ((mID>>8)&0xFF),(byte) ((mID>>16)&0xFF),0x00,0x00,0x26,0x18,(byte) 0x84,(byte)(Temp&0xFF),(byte)((Temp>>8)&0xFF),0x00,0x00});
		this.mTemp = Temp;
		return false;
	}

	/**
	 * @param Lumi 0-100 Luminosity in percent
	 * @return Success
	 */
	public boolean setLuminosity(char Lumi) {
		mApiInstance.sendCmd(new byte[]{0x11,0x00,(byte) ((this instanceof Group)?0x02:0x00),0x31,0x00,0x00,0x00,0x00,(byte) (mID&0xFF),(byte) ((mID>>8)&0xFF),(byte) ((mID>>16)&0xFF),0x00,0x00,0x26,0x18,(byte) 0x84,(byte)Lumi,0x00,0x00});
		this.mLumi = Lumi;
		return false;
	}

	/**
	 * @param On On:true Off:false
	 * @return
	 */
	public boolean setOnOff(boolean On) {
		mApiInstance.sendCmd(new byte[]{0x0f,0x00,(byte) ((this instanceof Group)?0x02:0x00),0x32,0x00,0x00,0x00,0x00,(byte) (mID&0xFF),(byte) ((mID>>8)&0xFF),(byte) ((mID>>16)&0xFF),0x00,0x00,0x26,0x18,(byte) 0x84,(byte) (On?0x01:0x00)});
		this.mOnOff = On;
		return false;
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


	public int getID() {
		return this.mID;
	}

}
