package de.wiomoc.JLightify.gui;

import java.io.IOException;
import java.net.UnknownHostException;

import de.wiomoc.JLightify.api.LightifyApi;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		LightifyApi api = new LightifyApi("192.168.2.59",true);
		api.getAllGroups().get(1).setOnOff(true);
		api.getAllGroups().get(1).setTemp((short)3000);
		api.end();
	}

}
