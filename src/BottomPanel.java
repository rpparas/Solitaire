/*
 * BottomPanel.java
 *
 * Created on December 27, 2006, 10:41 AM
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
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class BottomPanel extends JPanel implements Runnable{
    private Solitaire sol;
    private BestPlayers bestPlayers;
    private javax.swing.Timer timer;

    private String time, timeStop;
    private int option, cardsLeft, moves;
    private boolean dealAgain;


    public BottomPanel(Solitaire sol){
		this.sol = sol;
		this.cardsLeft = sol.getCardsLeft();
		this.bestPlayers = sol.getBestPlayers();
		option = Solitaire.NO_ACTION;

		timeStop = null;
		time = new String("00:00");
		timer = new javax.swing.Timer(1000, new TimeHandler());
		timer.setInitialDelay(100);
	}

	public void run(){
		while(true){
			cardsLeft = sol.getCardsLeft();
			moves = sol.numberOfMoves;
			repaint();

			if(cardsLeft == Solitaire.NONE){
				checkPerformance();
				cardsLeft = Solitaire.DECK_SIZE;
				dealAgain = true;
				break;
			}

			try{
				Thread.sleep(300);
			}catch(InterruptedException e){
				System.out.println("thread terminated");
			}
		}
	}

	public void paint(Graphics g){
		super.paint(g);

		g.setColor(Color.BLUE);
		g.drawString("Number of Cards Left: ", 5, 18);
		g.drawString("Time Elapsed: ", 345, 18);
		g.drawString("Moves: ", 495, 18);

		g.setColor(Color.RED);
		g.drawString(cardsLeft+"", 130, 18);

		if(cardsLeft == Solitaire.NONE){
			if(timeStop == null){
				timeStop = time;
			}
			g.drawString(timeStop, 440, 18);
		}else{
			g.drawString(time, 440, 18);
		}

		g.drawString(moves+"", 545, 18);
	}

	public void startTimer(){
		if(time.equals("00:00")){
			timer.start();
		}
	}

	private synchronized void checkPerformance(){
		String TITLE = "Congratulations!";
		Object[] choices = {"Deal Again", "Close Solitaire"};
		ImageIcon icon = new ImageIcon("../labels/congrats.gif");

		option = JOptionPane.showOptionDialog(null, displayMessage(), TITLE,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				icon, choices, choices[0]);

		if(bestPlayers.isBestTime(timeStop) || bestPlayers.isFastMove(moves)){
			String name = (String)JOptionPane.showInputDialog(null, "Enter Player's name",
							"For The Record", JOptionPane.QUESTION_MESSAGE);
			if(name == null){
				name = new String("Anonymous");
			}

			bestPlayers.addBestTime(name, timeStop);
			bestPlayers.addFastMove(name, moves);

			try{
				sol.saveFile();
			}catch(GameException e){
				e.show();
			}
		}

		if(option == 1){
			System.exit(0);
		}
	}

	private String displayMessage(){
		if(timeStop == null){
			timeStop = time;
		}
		String message1 = "You've successfully completed\n";
		String message2 = "a game of Solitaire in " + moves + " moves\n";
		String message3 = "in just " + timeStop + " minutes.";
		return message1 + message2 + message3;
	}

	public class TimeHandler implements ActionListener{
		private Calendar calendar;
		private boolean nextRun;
		private int startHour, startMin, startSec;
		private int currentHour, currentMin, currentSec;

		public TimeHandler(){
			calendar = Calendar.getInstance();
		}

		public void actionPerformed(ActionEvent event){
			calendar = Calendar.getInstance();
			if(!nextRun){
				startHour = calendar.get(Calendar.HOUR);
				startMin = calendar.get(Calendar.MINUTE);
				startSec = calendar.get(Calendar.SECOND);
				nextRun = true;
			}

			currentHour = calendar.get(Calendar.HOUR);
			currentMin = calendar.get(Calendar.MINUTE);
			currentSec = calendar.get(Calendar.SECOND);

			formatTime(computeElapsed());
		}

		private int computeElapsed(){
			int startTime = startHour * 3600 + startMin * 60 + startSec;
			int currentTime = currentHour * 3600 + currentMin * 60 + currentSec;
			return currentTime - startTime;
		}

		private void formatTime(int timeElapsed){
			int min = timeElapsed / 60;
			int sec = timeElapsed % 60;

			DecimalFormat twoDigits = new DecimalFormat("00");
			time = new String(twoDigits.format(min)+":"+twoDigits.format(sec));
		}
	}

	public boolean isDealAgain(){
		return dealAgain;
	}

	public void resetDeal(){
		option = Solitaire.NO_ACTION;
	}
}