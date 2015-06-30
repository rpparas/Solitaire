/*
 * HowTo.java
 *
 * Created on January 16, 2007, 10:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/**
 *
 * @author Rommel
 */

public class HowTo{
	private JEditorPane jep;
	private JScrollPane scrollPane;
	private JFrame frame;

	public HowTo(){
		jep = new JEditorPane();
		jep.setEditable(false);
		jep.addHyperlinkListener(new HyperlinkHandler());
	}

	public void createPane(){
		boolean successful = false;
		char drive = 'C';

		while(drive != 'H'){
			try{
				jep.setPage("file:///"+(drive++)+":/Solitaire/howTo/interface.html");
				successful = true;
				break;
			}catch(IOException e){}
		}

		if(!successful){
			try{
				jep.setPage("http://rolincenter.tripod.com/Solitaire/howTo/interface.html");
			}catch(IOException e){
				jep.setContentType("text/html");
				jep.setText("<html>Could not load <b>How To...</b> page. Please \n"+
							"check your internet connection settings. </html>");
			}
		}

		scrollPane = new JScrollPane(jep);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

	public void show(){
	    frame = new JFrame("How To...");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(scrollPane);
		frame.setLocation(20, 20);
		frame.setSize(730, 520);
		frame.setVisible(true);
	}

	private class HyperlinkHandler implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent evt) {
			if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
				jep = (JEditorPane)evt.getSource();
				try{
					jep.setPage(evt.getURL());
				}catch(IOException e){
					frame.setVisible(false);
				}
			}
		}
    }
}