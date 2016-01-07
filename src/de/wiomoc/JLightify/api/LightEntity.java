package de.wiomoc.JLightify.api;

public interface LightEntity {
	boolean setColor(char R,char G,char B);
	boolean setTemp(char Temp);
	boolean setLuminosity(char Lumi);
	boolean setOnOff(boolean On);
	int		getID();
	char	getTemp();
	char	getLuminosity();
	boolean getOnOff();
	int		getColor();
	String	getName();
}
