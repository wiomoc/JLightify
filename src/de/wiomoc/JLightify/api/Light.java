package de.wiomoc.JLightify.api;

public class Light extends LightEntity {
	private boolean mIsOnline;
	Light(LightifyApi ApiInstance, int ID, String name) {
		super(ApiInstance, ID, name);
		if(mApiInstance.DEBUG)System.out.println("Light: ID:"+mID+" Name:"+mName);
	}
	
	public boolean fetch(){
		byte[] res = mApiInstance.sendCmd(new byte[]{0x0f,0x00,0x00,0x68,0x00,0x00,0x00,0x00,(byte) (mID&0xFF),(byte) ((mID>>8)&0xFF),(byte) ((mID>>16)&0xFF),0x00,0x00,0x26,0x18,(byte) 0x84,0x00});
		if(res[17]!=(byte)0x0){
			if(mApiInstance.DEBUG)System.out.println("Status: Offline");
			this.mIsOnline = false;
			return true;
		}
		this.mIsOnline = true;
		this.mOnOff = res[19]==1?true:false;
		this.mLumi = (char) res[20];
		this.mTemp = (short) (((res[22]&0xFF)<<8)|(res[21]&0xFF));
		this.mR = (char) res[23];
		this.mG = (char) res[24];
		this.mB = (char) res[25];
		if(mApiInstance.DEBUG)System.out.println("Status: On:"+this.mOnOff+" Luminosity:"+(int)this.mLumi+"% Temperature:"+(int)this.mTemp+"K Color: R:"+(int)(this.mR&0xFF)+" G:"+(int)(this.mG&0xFF)+" B:"+(int)(this.mB&0xFF));
		return true;
	}
	public boolean IsOnline(){
		return this.mIsOnline;
	}

}
