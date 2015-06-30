/*
 * Main.java
 *
 * Created on December 11, 2006, 10:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Rommel
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class Main extends JFrame implements Runnable{
    private Solitaire sol;
    private boolean firstTime;

    private TopPanel topPanel;
    private BodyPanel bodyPanel;
    private BottomPanel bottomPanel;

    private Container container;
    private MenuBar menuBar;
    private Point location;
    private ImageIcon icon;

    /** Creates a new instance of Main */
    public Main(){
        container = getContentPane();
		addWindowListener(new FrameHandler());
		location = new Point(80, 50);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(575, 460);
        setTitle("Solitaire");
    }

    private void init(){
		sol = new Solitaire();
		sol.createDeck();
		sol.createCabinet();
		sol.deal();

        try{
	        sol.loadFile();
		}catch(GameException e){
			e.show();
		}
	}

	public void run(){
		while(true){
			repaint();
			try{
				Thread.sleep(200);
			}catch(InterruptedException e){
				System.out.println("thread terminated");
			}
		}
	}

    public void build(){
        container.setLayout(new BorderLayout());
        container.add(createTop(), BorderLayout.NORTH);
        container.add(createBody(), BorderLayout.CENTER);
        container.add(createBottom(), BorderLayout.SOUTH);

        setJMenuBar(createMenu());
        setLocation((int)location.getX(), (int)location.getY());
		setVisible(true);
   }

	private void rebuild(){
		location = new Point(getX(), getY());
		bottomPanel.resetDeal();
		init();

		container.remove(topPanel);
		container.remove(bodyPanel);
		container.remove(bottomPanel);
		container = getContentPane();
		build();

		setContentPane(container);
		validate();
	}

    private JMenuBar createMenu(){
		menuBar = new MenuBar(sol);
		menuBar.build();
		return menuBar;
	}

    private JPanel createTop(){
        topPanel = new TopPanel(sol);
        topPanel.createStock();
        topPanel.createCabinet();

        Thread update = new Thread(topPanel);
     	update.setDaemon(true);
        update.start();
        return topPanel;
    }

	private JPanel createBody(){
		bodyPanel = new BodyPanel(sol);

		Thread update = new Thread(bodyPanel);
		update.setDaemon(true);
		update.start();
		return bodyPanel;
	}

	private JPanel createBottom(){
		bottomPanel = new BottomPanel(sol);
		bottomPanel.setPreferredSize(new Dimension(getWidth(), 20));

		Thread status = new Thread(bottomPanel);
		status.setDaemon(false);
		status.start();

		return bottomPanel;
	}

	public void paint(Graphics g){
		super.paint(g);

		if(!firstTime){
			icon = new ImageIcon("../images/avatar.gif");
			g.drawImage(icon.getImage(), 40, 320, this);
			firstTime = true;

			try{
				Thread.sleep(1000);
			}catch(InterruptedException ie){}
		}

		if(bottomPanel.isDealAgain() || menuBar.isDealAgain()){
			rebuild();
		}

		if(topPanel.isStartTimer() || bodyPanel.isStartTimer()){
			bottomPanel.startTimer();
		}
	}

    private class FrameHandler extends WindowAdapter{
		public void windowActivated(WindowEvent event){
			topPanel.repaint();
			bodyPanel.repaint();
			bottomPanel.repaint();
			repaint();
		}

		public void windowClosing(WindowEvent event){
			sol.saveFile();
		}
	}

    public static void main(String[] args){
        Main main = new Main();
        main.init();
        main.build();
        main.run();
    }
}