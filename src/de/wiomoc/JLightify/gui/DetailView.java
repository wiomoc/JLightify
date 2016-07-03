package de.wiomoc.JLightify.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.wiomoc.JLightify.api.Group;
import de.wiomoc.JLightify.api.Light;

public class DetailView extends JPanel implements ChangeListener {
	public EntityListEntity sel = null;
	JSlider LumiSlider;
	JSlider TempSlider;
	JSlider RSlider;
	JSlider GSlider;
	JSlider BSlider;
	boolean lock = true;
	long timelast = 0;
	JPanel chieldPanel;

	public DetailView() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createLineBorder(Color.black));

		JTextArea LumiLabel = new JTextArea("Luminosity:");
		LumiLabel.setMaximumSize(new Dimension(300, 20));
		this.add(LumiLabel);
		LumiSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		LumiSlider.setPaintLabels(true);
		LumiSlider.addChangeListener(this);
		this.add(LumiSlider);

		JTextArea TempLabel = new JTextArea("Temperature:");
		TempLabel.setMaximumSize(new Dimension(300, 20));
		this.add(TempLabel);
		TempSlider = new JSlider(JSlider.HORIZONTAL, 2000, 6500, 4250);
		TempSlider.setPaintLabels(true);
		TempSlider.addChangeListener(this);
		this.add(TempSlider);

		JTextArea RLabel = new JTextArea("Red:");
		RLabel.setMaximumSize(new Dimension(300, 20));
		this.add(RLabel);
		RSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 127);
		RSlider.setPaintLabels(true);
		RSlider.addChangeListener(this);
		this.add(RSlider);

		JTextArea GLabel = new JTextArea("Green:");
		GLabel.setMaximumSize(new Dimension(300, 20));
		this.add(GLabel);
		GSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 127);
		GSlider.setPaintLabels(true);
		GSlider.addChangeListener(this);
		this.add(GSlider);

		JTextArea BLabel = new JTextArea("Blue:");
		BLabel.setMaximumSize(new Dimension(300, 20));
		this.add(BLabel);
		BSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 127);
		BSlider.setPaintLabels(true);
		BSlider.addChangeListener(this);
		this.add(BSlider);
		
		
		this.chieldPanel = new JPanel();
		this.chieldPanel.setBorder(new EmptyBorder(20,0, 0, 0));
		this.chieldPanel.setLayout(new BoxLayout(this.chieldPanel, BoxLayout.PAGE_AXIS));
		this.add(this.chieldPanel);
	}

	public void update() {
		lock = true;
		LumiSlider.setValue(sel.mEntity.getLuminosity());
		TempSlider.setValue(sel.mEntity.getTemp());
		RSlider.setValue(sel.mEntity.getColorR());
		GSlider.setValue(sel.mEntity.getColorG());
		BSlider.setValue(sel.mEntity.getColorB());
		if (sel.mEntity instanceof Group) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					JPanel view = DetailView.this.chieldPanel;
					view.removeAll();
					JTextArea CLabel = new JTextArea("Members:");
					CLabel.setMaximumSize(new Dimension(300, 20));
					view.add(CLabel);
					ArrayList<Light> chields = ((Group) sel.mEntity).getChildren();
					for (Light l : chields) {
						JTextArea chieldText = new JTextArea(l.getName());
						chieldText.setMaximumSize(new Dimension(300, 20));
						view.add(chieldText);
						view.revalidate();
						view.repaint(); 
					}
				}
			});
		}
		lock = false;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if ((sel == null) || lock)
			return;
		if (((JSlider) e.getSource()).getValueIsAdjusting()) {
			if ((System.currentTimeMillis() - timelast) < 500 || (!Main.api.switchLocalCloud()))
				return;
			timelast = System.currentTimeMillis();
		}
		if (e.getSource() == LumiSlider) {
			sel.mEntity.setLuminosity((char) LumiSlider.getValue());
		} else if (e.getSource() == TempSlider) {
			sel.mEntity.setTemp((short) TempSlider.getValue());
		} else {
			sel.mEntity.setColor((char) RSlider.getValue(), (char) GSlider.getValue(), (char) BSlider.getValue());
		}

	}
}
