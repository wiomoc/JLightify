package de.wiomoc.JLightify.api;

import java.util.ArrayList;
import java.util.Arrays;

public class Group implements LightEntity {
	private LightifyApi mApiInstance;
	private char mID;
	private String mName;
	private ArrayList<Light> mChildren = null;
	Group(LightifyApi ApiInstance,char ID,String name){
		this.mApiInstance = ApiInstance;
		this.mID = ID;
		this.mName = name;
		System.out.println("Group: ID:"+(int)mID+" Name:"+mName);
		this.getChildren();
	}
	public ArrayList<Light> getChildren(){
		if(mChildren!=null)return mChildren;
		mChildren = new ArrayList<Light>();
		byte [] res = mApiInstance.sendCmd(new byte[]{0x08,0x00,0x00,0x26,0x00,0x00,0x00,0x00,(byte) this.mID,0x00});
		for(int i = 27;i<res.length;i+=8){
			Light tmp = mApiInstance.getLightByID((res[i+0] & 0xFF) << 16 | (res[i+1] & 0xFF) << 8 | (res [i+2] & 0xFF));
			
			System.out.println("	Child: ID:"+tmp.getID()+" Name:"+tmp.getName());
		}
		return mChildren;
		
	}
	@Override
	public boolean setColor(char R, char G, char B) {
		mApiInstance.sendCmd(new byte[]{0x14,0x00,0x02,0x36,0x00,0x00,0x00,0x00,(byte) mID,0x00,0x00,0x00,0x00,0x26,0x18,(byte) 0x84,(byte) R,(byte) G,(byte) B,(byte) 0xff,0x00,0x00});
		return false;
	}

	@Override
	public boolean setTemp(char Temp) {
		mApiInstance.sendCmd(new byte[]{0x12,0x00,0x02,0x33,0x00,0x00,0x00,0x00,(byte) mID,0x00,0x00,0x00,0x00,0x26,0x18,(byte) 0x84,0x00,(byte) Temp,0x00,0x00});
		return false;
	}

	@Override
	public boolean setLuminosity(char Lumi) {
		mApiInstance.sendCmd(new byte[]{0x11,0x00,0x02,0x31,0x00,0x00,0x00,0x00,(byte) mID,0x00,0x00,0x00,0x00,0x26,0x18,(byte) 0x84,(byte)Lumi,0x00,0x00});
		return false;
	}

	@Override
	public boolean setOnOff(boolean On) {
		mApiInstance.sendCmd(new byte[]{0x0f,0x00,0x02,0x32,0x00,0x00,0x00,0x00,(byte) mID,0x00,0x00,0x00,0x00,0x26,0x18,(byte) 0x84,(byte) (On?0x01:0x00)});
		return false;
	}

	@Override
	public int getID() {
		return this.mID;
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

}
