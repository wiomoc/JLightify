package de.wiomoc.JLightify.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import de.wiomoc.JLightify.api.Light;
import de.wiomoc.JLightify.api.LightEntity;

public class EntityListEntity extends JPanel implements MouseListener, ActionListener {
	LightEntity mEntity;
	DetailView mDetail;
	public EntityListEntity(LightEntity ent,DetailView det) {
		super();
		this.mEntity = ent;
		this.mDetail = det;
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		JToggleButton onoff = new JToggleButton("ON");
		onoff.setSelected(ent.getOnOff());
		onoff.addActionListener(this);		
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setMaximumSize(new Dimension(300,50));
		String name = this.mEntity.getName();
		if(this.mEntity instanceof Light&&(!((Light) this.mEntity).IsOnline())){
			name += " (Offline)";
		}
		JTextArea textField = new JTextArea(name);
		textField.setEditable(false);
		textField.setBackground(new Color(0,0,0,0));
		textField.setHighlighter(null);
		textField.addMouseListener(this);
		this.add(textField);
		this.add(onoff);
		this.addMouseListener(this);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.mDetail.sel!=null)this.mDetail.sel.setBackground(Color.WHITE);
		this.setBackground(Color.GRAY);
		this.mDetail.sel = this;
		this.mDetail.update();
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
			
	}
	@Override
	public void mouseReleased(MouseEvent e) {
				
	}
	@Override
	public void mouseEntered(MouseEvent e) {
				
	}
	@Override
	public void mouseExited(MouseEvent e) {
				
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		mEntity.setOnOff(((JToggleButton)e.getSource()).isSelected());
	}

}
