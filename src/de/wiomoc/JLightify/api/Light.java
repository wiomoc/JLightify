package de.wiomoc.JLightify.api;

public class Light extends LightEntity {
	boolean mIsOnline;
	char mIndex;
	Light(LightifyApi ApiInstance, long ID, String name,char index) {
		super(ApiInstance, ID, name);
		this.mIndex = index;
		if(mApiInstance.DEBUG)System.out.println("Light: ID:"+Long.toHexString(mID)+" Name:"+mName);
	}
	
	public boolean fetchLocal(){
		byte[] res = sendCmd((byte) 0x68,new byte[]{0x00});
		if(res==null||res[6]!=0)return false;
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
