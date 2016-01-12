package de.wiomoc.JLightify.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DetailView extends JPanel implements ChangeListener {
	public EntityListEntity sel = null;
	JSlider LumiSlider;
	JSlider TempSlider;
	JSlider RSlider;
	JSlider GSlider;
	JSlider BSlider;
	boolean lock = true;
	long timelast =0;
	public DetailView() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JTextArea LumiLabel = new JTextArea("Luminosity:");
		LumiLabel.setMaximumSize(new Dimension(300,20));
		this.add(LumiLabel);
		LumiSlider = new JSlider(JSlider.HORIZONTAL,0, 100, 50);
		LumiSlider.setPaintLabels(true);
		LumiSlider.addChangeListener(this);
		this.add(LumiSlider);
		
		JTextArea TempLabel = new JTextArea("Temperature:");
		TempLabel.setMaximumSize(new Dimension(300,20));
		this.add(TempLabel);
		TempSlider = new JSlider(JSlider.HORIZONTAL,2000, 6500, 4250);
		TempSlider.setPaintLabels(true);
		TempSlider.addChangeListener(this);
		this.add(TempSlider);
		
		JTextArea RLabel = new JTextArea("Red:");
		RLabel.setMaximumSize(new Dimension(300,20));
		this.add(RLabel);
		RSlider = new JSlider(JSlider.HORIZONTAL,0, 255, 127);
		RSlider.setPaintLabels(true);
		RSlider.addChangeListener(this);
		this.add(RSlider);
		
		JTextArea GLabel = new JTextArea("Green:");
		GLabel.setMaximumSize(new Dimension(300,20));
		this.add(GLabel);
		GSlider = new JSlider(JSlider.HORIZONTAL,0, 255, 127);
		GSlider.setPaintLabels(true);
		GSlider.addChangeListener(this);
		this.add(GSlider);
		
		JTextArea BLabel = new JTextArea("Blue:");
		BLabel.setMaximumSize(new Dimension(300,20));
		this.add(BLabel);
		BSlider = new JSlider(JSlider.HORIZONTAL,0, 255, 127);
		BSlider.setPaintLabels(true);
		BSlider.addChangeListener(this);
		this.add(BSlider);
	}
	public void update(){
		lock=true;
		LumiSlider.setValue(sel.mEntity.getLuminosity());
		TempSlider.setValue(sel.mEntity.getTemp());
		RSlider.setValue(sel.mEntity.getColorR());
		GSlider.setValue(sel.mEntity.getColorG());
		BSlider.setValue(sel.mEntity.getColorB());
		lock=false;
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		if((sel==null)||lock)return;
		if(((JSlider)e.getSource()).getValueIsAdjusting()){
			if((System.currentTimeMillis()-timelast)<500||(!Main.api.switchLocalCloud()))return;
			timelast = System.currentTimeMillis();
		}
		if(e.getSource()==LumiSlider){
			sel.mEntity.setLuminosity((char) LumiSlider.getValue());
		}else if(e.getSource()==TempSlider){
			sel.mEntity.setTemp((short) TempSlider.getValue());
		}else{
			sel.mEntity.setColor((char)RSlider.getValue(), (char)GSlider.getValue(), (char)BSlider.getValue());
		}
		
	}
}
