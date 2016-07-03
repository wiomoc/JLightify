package de.wiomoc.JLightify.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import de.wiomoc.JLightify.api.LightifyApi;

public class Main {
	static LightifyApi api;
	public static void main(String[] args) throws UnknownHostException, IOException  {
		
		api  = new LightifyApi(true);
		final JFrame frame = new JFrame();
        frame.setTitle("JLightify");
        frame.setSize(new Dimension(300,360));
        JPanel dialog = new JPanel();
        dialog.setLayout(new BoxLayout(dialog, BoxLayout.PAGE_AXIS));
        
        JLabel usernamelabel = new JLabel("Username:");
        final JTextField usernamefield = new JTextField();
        usernamefield.setMaximumSize(new Dimension(300,25));
        dialog.add(usernamelabel);
        dialog.add(usernamefield);
        
        JLabel passwordlabel = new JLabel("Password:");
        final  JPasswordField passwordfield = new  JPasswordField();
        passwordfield.setMaximumSize(new Dimension(300,25));
        dialog.add(passwordlabel);
        dialog.add(passwordfield);
        
		JButton loginbutton = new JButton("Login");
		loginbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(api.logIn(usernamefield.getText(), new String(passwordfield.getPassword()))){
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					init();
				}
				
			}
		});
		dialog.add(loginbutton);
		
		JLabel addresslabel = new JLabel("Gateway IP:");
	    final JTextField addressfield = new JTextField();
	    addressfield.setMaximumSize(new Dimension(300,25));
	    dialog.add(addresslabel);
	    dialog.add(addressfield);
	    
	    
	    JButton connectbutton = new JButton("Connect");
	    
		connectbutton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				api.setGatewayAddress(addressfield.getText());
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				init();
				
				
			}
		});
		dialog.add(connectbutton);

		frame.add(dialog);
		frame.setVisible(true);
	}
	private static void init(){
		api.fetchAll();
		JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        DetailView det = new DetailView();
        frame.setSize(new Dimension(600, 480));
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            	api.end();
            	System.exit(0);
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
