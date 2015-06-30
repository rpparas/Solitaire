/*
 * MenuBar.java
 *
 * Created on January 9, 2007, 9:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 *
 * @author Rommel
 */

public class MenuBar extends JMenuBar{
	private Solitaire sol;
	private BestPlayers bestPlayers;

	private JMenu[] menu;
	private boolean dealAgain;


	public MenuBar(Solitaire sol){
		menu = new JMenu[2];
		this.sol = sol;
		this.bestPlayers = sol.getBestPlayers();
	}

	public void build(){
		menu[0] = createGameMenu(4);
		menu[1] = createHelpMenu(2);

		for(int i = 0; i < menu.length; i++){
			add(menu[i]);
		}
	}

	private JMenu createGameMenu(int num){
		JMenu menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_G);

		JMenuItem[] item = new JMenuItem[num];
		item[0] = new JMenuItem("Deal", KeyEvent.VK_D);
		item[1] = new JMenuItem("Best Times", KeyEvent.VK_B);
		item[2] = new JMenuItem("Fast Moves", KeyEvent.VK_F);
		item[3] = new JMenuItem("Exit", KeyEvent.VK_X);

		item[0].setAccelerator(KeyStroke.getKeyStroke('D'));
		item[1].setAccelerator(KeyStroke.getKeyStroke('B'));
		item[2].setAccelerator(KeyStroke.getKeyStroke('F'));
		item[3].setAccelerator(KeyStroke.getKeyStroke('X'));

		for(int i = 0; i < num; i++){
			item[i].addActionListener(new MenuHandler(getChar(item[i])));
			menu.add(item[i]);
			if(i == 0 || i == 2){
				menu.addSeparator();
			}
		}
		return menu;
	}

	private JMenu createHelpMenu(int num){
		JMenu menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);

		JMenuItem[] item = new JMenuItem[num];
		item[0] = new JMenuItem("How To...", KeyEvent.VK_T);
		item[1] = new JMenuItem("About...", KeyEvent.VK_A);

		item[0].setAccelerator(KeyStroke.getKeyStroke('T'));
		item[1].setAccelerator(KeyStroke.getKeyStroke('A'));

		for(int i = 0; i < num; i++){
			item[i].addActionListener(new MenuHandler(getChar(item[i])));
			menu.add(item[i]);
			if(i == 0){
				menu.addSeparator();
			}
		}
		return menu;
	}

	private char getChar(JMenuItem item){
		return item.getText().charAt(0);
	}

	public boolean isDealAgain(){
		return dealAgain;
	}

	private class MenuHandler implements ActionListener{
		private char src;

		public MenuHandler(char src){
			this.src = src;
		}

		public void actionPerformed(ActionEvent event){
			switch(src){
				case 'D':	dealAgain = true;
							break;
				case 'B':	showBestTimes();
							break;
				case 'F':	showFastMoves();
							break;
				case 'E':	if(confirmExit()){
								try{
									sol.saveFile();
								}catch(GameException e){
									e.show();
								}finally{
									System.exit(0);
								}
							}
				case 'H':	showHowTo();
							break;
				case 'A':	showAbout();
							break;
				default:
			}
		}
	}

	private boolean confirmExit(){
		int option = JOptionPane.showConfirmDialog(null, "Are you sure you're quitting?", "Confirm Exit",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		return option == JOptionPane.YES_OPTION;
	}

	private void showBestTimes(){
		String title = "Best Times";
		String[][] bestTimes = bestPlayers.getBestTimes();
		String message = "Rank            Time               Player \n";

		for(int i = 0; i < bestTimes.length; i++){
			message += insertSpaces(new Integer(i+1).toString());
			if(bestTimes[i][0] == null){
				message += "\n";
			}else{
				message += insertSpaces(bestTimes[i][1]) +
							insertSpaces(bestTimes[i][0]) + "\n";
			}
		}
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	private String insertSpaces(String prev){
		int gap = 20;
		int spaces = gap - prev.length();
		String spacesText = prev + "";

		for (int i = 0; i < spaces; i++){
			spacesText += " ";
		}
		return spacesText;
	}

	private void showFastMoves(){
		String title = "Fast Moves";
		String[][] fastMoves = bestPlayers.getFastMoves();
		String message = "Rank           Moves           Player \n";

		for(int i = 0; i < fastMoves.length; i++){
			message += insertSpaces(new Integer(i+1).toString());
			if(fastMoves[i][0] == null){
				message += "\n";
			}else{
				message += insertSpaces(fastMoves[i][1]) +
							insertSpaces(fastMoves[i][0]) + "\n";
			}
		}
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	private void showHowTo(){
		HowTo howTo = new HowTo();
		howTo.createPane();
		howTo.show();
	}

	private void showAbout(){
		ImageIcon icon = new ImageIcon("../images/avatar2.gif");
		String title = "About";
		String message = "Java Solitaire Beta was developed \n"+
						 "by Rommel Paras in partial fulfilment \n"+
						 "of the requirements in CMSC 123.";

		JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE,
								JOptionPane.DEFAULT_OPTION, icon);
		JDialog dialog = pane.createDialog(null, title);
		Container container = dialog.getContentPane();

		MoreInfo mi = new MoreInfo(container, showHiddenMessage());
		dialog.setContentPane(mi);
		dialog.pack();
		dialog.setVisible(true);

		//JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, icon);
	}

	private JScrollPane showHiddenMessage(){
		String message = "I'd like to thank my sponsors: God, my parents, my friends and my pc. \n\n"+
						 "Credits go to Sir Kurt, thanks teach. I'd try mobile next time.\n\n"+
						 "To someone special? Neah... don't wanna think about that now.\n"+
						 "To my bestfriends -- one, too focused on his studies,\n"+
						 "the other, too eager to explore his new world.\n\n"+
						 "And to YOU, Time Magazine's Person of the Year, who"+
						 "looked at the road ahead and understood it was necessary.";

		JTextArea textArea = new JTextArea(message, 8, 35);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JScrollPane scroller = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
									ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return scroller;
	}
}