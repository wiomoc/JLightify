package de.wiomoc.JLightify.gui;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.wiomoc.JLightify.api.LightEntity;


public class EntityListView extends JPanel {
	public EntityListView(ArrayList<? extends LightEntity> list,DetailView det) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		for(LightEntity ent:list){
			this.add(new EntityListEntity(ent,det));
		}

		this.setVisible(true);
		this.setSize(new Dimension(300,480));
	}

}
