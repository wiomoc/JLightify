package de.wiomoc.JLightify.gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import de.wiomoc.JLightify.api.LightifyApi;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		final LightifyApi api = new LightifyApi("192.168.2.59",true);
		JFrame frame = new JFrame();
        frame.setTitle("JLightify");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        DetailView det = new DetailView();
        frame.setSize(new Dimension(600, 480));
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            	api.end();
            }
        });
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setMaximumSize(new Dimension(300,2000));
        det.setMaximumSize(new Dimension(300,2000));
        tabbedPane.addTab("Light",new JScrollPane(new EntityListView(api.getAllLights(),det)));
        tabbedPane.addTab("Group",new JScrollPane(new EntityListView(api.getAllGroups(),det)));
        //tabbedPane.setSize(new Dimension(300,480));
        panel.add(tabbedPane);
        panel.add(det);
        frame.add(panel);
		frame.setVisible(true);
	}

}
