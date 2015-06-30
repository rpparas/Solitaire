/*
 * BodyPanel.java
 *
 * Created on December 15, 2006, 2:18 PM
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


public class BodyPanel extends JPanel implements Runnable{
    private final int XSTART = 12;
    private final int YSTART = 15;
    private final int XDIFF = 80;
    private final int YDIFF = 15;

    private boolean isReleased;
    private boolean isDragged;
    private boolean isDrawImage;
    private boolean timerStarted;
    private int column, index;
    private int x, y;

	private Solitaire sol;
	private Card source, target;
    private Pile[] pile;
    private Cabinet[] cabinet;

	private Color color;
	private ImageIcon icon;


    /** Creates a new instance of BodyPanel */
    public BodyPanel(Solitaire sol){
		this.sol = sol;
        this.pile = sol.getPile();
        this.cabinet = sol.getCabinet();
        column = Pile.NONE;
        index = Pile.NONE;

    	setFocusable(true);
        setLayout(new FlowLayout(1, 1, 1));
        addMouseListener(new MouseClickHandler());
        addMouseMotionListener(new MouseDragHandler());
    }

	public void run(){
		while(true){
			repaint();
			try{
				Thread.sleep(500);
			}catch(Exception e){}
		}
	}

    public void paint(Graphics g){
		super.paint(g);
		icon = new ImageIcon("../images/wall2.jpg");
		g.drawImage(icon.getImage(), 0, 0, this);

		for(int col = 0; col < 7; col++){
			createPile(g, col);
		}

		if(isDrawImage){
			int steadyY = y;
			for(int i = index; i < pile[column].getSize() &&
				column >= 0 && column < pile.length; i++){
				try{
					Card card = (Card)pile[column].getCards().get(i);
					if(card.isFaceUp()){
						icon = new ImageIcon("../deck/"+card.getSuit()+card.getRank()+".png");
					}else{
						icon = new ImageIcon("../images/back.png");
					}
					g.drawImage(icon.getImage(), x, steadyY, this);
				}catch(Exception e){
				}finally{
					if(i+1 != pile[column].getSize()){
						steadyY+=YDIFF;
					}
				}
			}
		}
    }

	private void createPile(Graphics g, int col){
		int x = col*XDIFF+XSTART, y = YSTART;
		Card card;

		for(int i = 0; i < pile[col].getSize(); i++){
			card = (Card)pile[col].getCards().get(i);
			if(card.isFaceUp()){
				icon = new ImageIcon("../deck/" + card.getSuit() + card.getRank() + ".png");
			}else{
				icon = new ImageIcon("../images/back.png");
			}

			if(!isDrawImage || col != column || i < index){
				g.drawImage(icon.getImage(), x, y, this);
				y+=YDIFF;
			}
		}
	}

	private class MouseClickHandler extends MouseAdapter{
		public void mouseClicked(MouseEvent event){
			x = event.getX();
			y = event.getY();
			source = identifyCard();

			if(source == null){
				return;
			}else if(event.getClickCount() == 2 && source.isFaceUp()){
				try{
					if(sol.uploadCard(source)){ // pile to cabinet
						pile[column].removeCard(source);
						sol.addToCabinet(source);
						repaint();
					}
				}catch(GameException e){
					e.show();
				}
			}else if(!source.isFaceUp()){
				if(index == pile[column].getSize()-1){
					pile[column].setFaceUp(index); // flip source
					repaint();
				}
			}
		}

		//insert feature for one-click transfer
		public void mouseReleased(MouseEvent event){
			int initCol = column;
			int initIndex = index;

			x = event.getX();
			y = event.getY();
			target = identifyCard();

			if(isDragged && source != null){ // pile to pile
				try{
					Transfer transfer = new Transfer(pile[initCol], initIndex, target);
					if(transfer.isAllowed(sol.findBlankPile())){
						pile[column] = transfer.moveCards(pile[column]);
						pile[initCol] = transfer.getCardsLeft();
					}
				}catch(GameException e){
					e.show();
				}
			}

			isDragged = false;
			isDrawImage = false;
			repaint();
		}

		public void mousePressed(MouseEvent event){
			timerStarted = true;
		}
	}

    private class MouseDragHandler extends MouseMotionAdapter{
		public void mouseDragged(MouseEvent event){
			x = event.getX();
			y = event.getY();

			if(!isDragged){
				source = identifyCard();
				isDragged = true;
			}
			if(source == null || !source.isFaceUp()){
				isDrawImage = false;
			}else{
				isDrawImage = true;
			}
			repaint();
		}
	}

	private Card identifyCard(){
		Card card = null;
		try{
			if(isWithinBounds()){
				try{
					card = (Card)pile[column].getCards().get(index);
				}catch(Exception e){
					index = pile[column].getSize() - 1;
					card = (Card)pile[column].getCards().get(index);
				}

				if(!card.isFaceUp()){
					color = Color.BLUE;
				}else{
					color = card.getColor();
				}
			}
		}catch(Exception e){
		}finally{
			return card;
		}
	}

	private boolean isWithinBounds(){
		column = (x - XSTART) / XDIFF;
		index = (y - YSTART) / YDIFF;

		// x boundary:= XSTART + XDIFF(size-1) + WIDTH = 15 + 15(n-1) + 96
		// y boundary:= YSTART + YDIFF(size-1) + HEIGHT = 15 + 15(n-1) + 96
		boolean horizontal = x >= 12 && x <= 563 && x <= 80*column+93;
		boolean vertical = horizontal && y >= 15 && y <= pile[column].getSize()*15+96;

		return horizontal && vertical;
	}

	public boolean isStartTimer(){
		return timerStarted;
	}
}