package de.wiomoc.JLightify.api;

import java.util.ArrayList;

public class Group extends LightEntity {

	ArrayList<Light> mChildren = null;
	Group(LightifyApi ApiInstance,char ID,String name){
		super(ApiInstance, ID, name);
		if(mApiInstance.DEBUG)System.out.println("Group: ID:"+(int)mID+" Name:"+mName);
	}
	public ArrayList<Light> getChildren(){
		if(mChildren!=null)return mChildren;
		mChildren = new ArrayList<Light>();
		byte [] res = mApiInstance.sendCmd(new byte[]{0x08,0x00,0x00,0x26,0x00,0x00,0x00,0x00,(byte) this.mID,0x00});
		for(int i = 26;i<res.length;i+=8){
			long id = 0;
			for (char x = 0; x < 8; x++){
			   id |= ((long) res[x+i] & 0xffL) << (8 * x);
			}
			//FIXME
			Light tmp = mApiInstance.getLightByID(id);
			mChildren.add(tmp);
			tmp.getName();
			if(mApiInstance.DEBUG)System.out.println("Child: ID:"+Long.toHexString(tmp.getID())+" Name:"+tmp.getName());
		}
		return mChildren;
		
	}
}
