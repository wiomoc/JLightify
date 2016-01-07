package de.wiomoc.JLightify.api;

public class Light implements LightEntity {
	private LightifyApi mApiInstance;
	private int mID;
	private String mName;
	Light(LightifyApi ApiInstance,int ID,String name){
		this.mApiInstance = ApiInstance;
		this.mID = ID;
		this.mName = name;
		System.out.println("Light: ID:"+mID+" Name:"+mName);
	}
	@Override
	public boolean setColor(char R, char G, char B) {
		mApiInstance.sendCmd(new byte[]{0x14,0x00,0x00,0x36,0x00,0x00,0x00,0x00,(byte) ((mID>>16)&0xFF),(byte) ((mID>>8)&0xFF),(byte) (mID&0xFF),0x00,0x00,0x26,0x18,(byte) 0x84,(byte) R,(byte) G,(byte) B,(byte) 0xff,0x00,0x00});
		return false;
	}

	@Override
	public boolean setTemp(char Temp) {
		mApiInstance.sendCmd(new byte[]{0x12,0x00,0x00,0x33,0x00,0x00,0x00,0x00,(byte) ((mID>>16)&0xFF),(byte) ((mID>>8)&0xFF),(byte) (mID&0xFF),0x00,0x00,0x26,0x18,(byte) 0x84,0x00,(byte) Temp,0x00,0x00});
		return false;
	}

	@Override
	public boolean setLuminosity(char Lumi) {
		mApiInstance.sendCmd(new byte[]{0x11,0x00,0x00,0x31,0x00,0x00,0x00,0x00,(byte) ((mID>>16)&0xFF),(byte) ((mID>>8)&0xFF),(byte) (mID&0xFF),0x00,0x00,0x26,0x18,(byte) 0x84,(byte)Lumi,0x00,0x00});
		return false;
	}

	@Override
	public boolean setOnOff(boolean On) {
		mApiInstance.sendCmd(new byte[]{0x0f,0x00,0x00,0x32,0x00,0x00,0x00,0x00,(byte) ((mID>>16)&0xFF),(byte) ((mID>>8)&0xFF),(byte) (mID&0xFF),0x00,0x00,0x26,0x18,(byte) 0x84,(byte) (On?0x01:0x00)});
		return false;
	}

	@Override
	public char getTemp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getLuminosity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getOnOff() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getColor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return this.mName;
	}

	@Override
	public int getID() {
		return this.mID;
	}

}
