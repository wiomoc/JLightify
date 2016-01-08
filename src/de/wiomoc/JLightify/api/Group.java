package de.wiomoc.JLightify.api;

import java.util.ArrayList;

public class Group extends LightEntity {

	private ArrayList<Light> mChildren = null;
	Group(LightifyApi ApiInstance,char ID,String name){
		super(ApiInstance, ID, name);
		if(mApiInstance.DEBUG)System.out.println("Group: ID:"+(int)mID+" Name:"+mName);
	}
	public ArrayList<Light> getChildren(){
		if(mChildren!=null)return mChildren;
		mChildren = new ArrayList<Light>();
		byte [] res = mApiInstance.sendCmd(new byte[]{0x08,0x00,0x00,0x26,0x00,0x00,0x00,0x00,(byte) this.mID,0x00});
		for(int i = 27;i<res.length;i+=8){
			Light tmp = mApiInstance.getLightByID((res[i+0] & 0xFF) << 16 | (res[i+1] & 0xFF) << 8 | (res [i+2] & 0xFF));
			
			if(mApiInstance.DEBUG)System.out.println("Child: ID:"+tmp.getID()+" Name:"+tmp.getName());
		}
		return mChildren;
		
	}
}
