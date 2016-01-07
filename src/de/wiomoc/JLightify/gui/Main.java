package de.wiomoc.JLightify.gui;

import java.io.IOException;
import java.net.UnknownHostException;

import de.wiomoc.JLightify.api.LightifyApi;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		LightifyApi api = new LightifyApi("192.168.2.59");
		api.getAllLights();
		api.getAllGroups().get(1).setLuminosity((char)100);
		api.getAllGroups().get(1).setOnOff(true);
		api.getAllGroups().get(1).setColor((char) 0,(char)255,(char)0);
		api.end();
	}

}
