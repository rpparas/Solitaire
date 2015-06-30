/*
 * TopPanel.java
 *
 * Created on December 14, 2006, 10:08 AM
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


public class TopPanel extends JPanel implements Runnable{
    private final int WIDTH = 75;
    private final int HEIGHT = 100;
    private final int XSTART = 101;
    private final int YSTART = 12;
    private final int XDIFF = 20;

    private boolean hasDrawnCards;
    private boolean hasChosen;
    private boolean isDrawImage;
    private boolean isDragged;
	private boolean timerStarted;
	private int column, index, draw;
    private int x, y;

	private Solitaire sol;
    private Stock stock;
    private Card card;
    private Card[] drawn;
    private Pile[] pile;
    private Cabinet[] cabinet;

	private ImageIcon icon;
	private JButton drawButton;
    private JButton[] cabButton;


    /** Creates a new instance of TopPanel */
    public TopPanel(Solitaire sol) {
		this.sol = sol;
        this.stock = sol.getStock();
        this.cabinet = sol.getCabinet();
        this.pile = sol.getPile();

        setBackground(new Color(41, 136, 251));
        setFocusable(true);
        setLayout(new FlowLayout(1, 1, 5));

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

		index = sol.getCardAddedIndex();
		if(index != Solitaire.NO_ACTION){
			try{
				Card source = cabinet[index].getLastCard();
				icon = new ImageIcon("../deck/"+source.getSuit()+source.getRank()+".png");
				cabButton[index].setIcon(icon);
				sol.resetCabinetIndex();
			}catch(Exception e){}
		}

		if(hasDrawnCards){
			drawn = stock.getDrawn();
		}

		if(drawn == null || drawn.length == 0){
			return;
		}

		int size = (isDrawImage || hasChosen ? drawn.length - 1 : drawn.length);
		for(int i = 0, x = XSTART, y = YSTART; i < size; i++){
			try{
				icon = new ImageIcon("../deck/" + drawn[i].getSuit() + drawn[i].getRank() + ".png");
				g.drawImage(icon.getImage(), x, y, this);
			}catch(Exception e){
			}finally{
				x+=XDIFF;
			}
		}

		if(hasChosen){
			hasChosen = false;
		}

		if(isDrawImage){
			Card card = drawn[size-1];
			icon = new ImageIcon("../deck/"+card.getSuit()+card.getRank()+".png");
			g.drawImage(icon.getImage(), x, y, this);
		}

		hasDrawnCards = false;
		toggleButton();
    }

    private void toggleButton(){
		if(hasDrawnCards){
			drawButton.setIcon(new ImageIcon("../images/empty.png"));
		}else{
			drawButton.setIcon(new ImageIcon("../images/deck.jpg"));
		}
	}

    public void createStock(){
        JPanel stock = new JPanel();
        stock.add(createDrawButton());
        stock.add(createDrawnCards());
	    stock.setBackground(new Color(0, 0, 255));
        add(stock);
    }

    private JButton createDrawButton(){
        drawButton = new JButton();
        drawButton.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        drawButton.setToolTipText("DRAW");
        drawButton.setActionCommand("DRAW");
        drawButton.addActionListener(new ButtonHandler());
        toggleButton();
        return drawButton;
    }

    private JLabel createDrawnCards(){
		int index = (int)(Math.random()*4) + 1;
		icon = new ImageIcon("../images/drawn"+index+".jpg");
		JLabel background = new JLabel(icon);
        background.setPreferredSize(new Dimension(WIDTH+70, HEIGHT));
        return background;
    }

    public void createCabinet(){
        JPanel cabinet = new JPanel();
        cabinet.setBackground(new Color(0, 0, 255));
        cabButton = new JButton[4];

        for(int i = 0; i < cabButton.length; i++){
			String suit = Cabinet.getSuit(i);
      		icon = new ImageIcon("../images/"+suit+".png");
	        cabButton[i] = new JButton(icon);
			cabButton[i].setPreferredSize(new Dimension(WIDTH, HEIGHT));
			cabButton[i].setToolTipText(suit.toUpperCase());
			cabButton[i].setActionCommand(suit.toUpperCase());
			cabButton[i].addActionListener(new ButtonHandler());
			cabinet.add(cabButton[i]);
		}
		add(cabinet);
    }

    private class ButtonHandler implements ActionListener{
		private Object MESSAGE = "Designate the destination PILE for this card.";
		private String TITLE = "Select Pile";
        private String destPile;

        public void actionPerformed(ActionEvent event){
			String source = event.getActionCommand().toLowerCase();
            if(source.equals("draw")){
                stock.draw();
                hasDrawnCards = true;
            }else{
				Object[] choices = sol.findTargetPile(source);
				if(choices == null || choices.length == 0){
					return;
				}else{
					if(choices.length == 1){
						destPile = (String)choices[0];
					}else{
						destPile = (String)JOptionPane.showInputDialog(null, MESSAGE, TITLE, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
					}

					if(destPile != null){ // move from Cabinet to Pile
						modifyPile(destPile, source);// check
						modifyCabinet(source);
					}
				}
			}
			repaint();
        }
    }

    private class MouseClickHandler extends MouseAdapter{
		private String destPile;

		public void mouseClicked(MouseEvent event){
			x = event.getX();
			y = event.getY();
			card = identifyCard();

			if(card == null){
				return;
			}else if(card.equals(drawn[drawn.length-1])){
				if(event.getClickCount() == 2){
					try{
						if(sol.uploadCard(card)){ // move from Drawn to Cabinet
							modifyCabinet(null);
							modifyDrawn(card);
						}
					}catch(GameException e){
						e.show();
						mouseReleased(event);
					}
				}else{
					mouseReleased(event);
				}
			}
			isDrawImage = false;
			repaint();
		}

		public void mouseReleased(MouseEvent event){
			isDrawImage = false;
			if(isDragged && card != null){
				Object MESSAGE = "Designate the destination PILE for this card.";
				String TITLE = "Select Pile";
				Object[] choices = sol.findTargetPile(card);

				if(choices == null || choices.length == 0){
					return;
				}else if(choices.length == 1){
					destPile = (String)choices[0];
				}else{
					hasChosen = true;
					destPile = (String)JOptionPane.showInputDialog(null, MESSAGE, TITLE, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
				}

				if(destPile != null){ // move from Drawn to Pile
					modifyPile(destPile, "Drawn");
					modifyDrawn(card);
				}
			}
			isDragged = false;
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
				card = identifyCard();
				isDragged = true;
			}
			if(card == null){
				isDrawImage = false;
			}else{
				isDrawImage = true;
			}
			repaint();
		}
	}

	private void modifyCabinet(String source){
		if(source == null){
			sol.addToCabinet(identifyCard());
		}else{
			int index = Cabinet.getIndex(source);
			try{
				cabinet[index].removeLastCard();
				Card card = cabinet[index].getLastCard();
				icon = new ImageIcon("../deck/"+card.getSuit()+card.getRank()+".png");
			}catch(Exception e){
				String suit = Cabinet.getSuit(index);
				icon = new ImageIcon("../images/"+suit.toLowerCase()+".png");
			}finally{
				cabButton[index].setIcon(icon);
			}
		}
	}

	private void modifyPile(String destPile, String source){
		int col = Integer.parseInt(destPile.substring(5, 6))-1;
		if(source.equals("Drawn")){
			pile[col].addCard(drawn[drawn.length-1]);
		}else{
			int index = Cabinet.getIndex(source);
			pile[col].addCard(cabinet[index].getLastCard());
		}
	}

	private void modifyDrawn(Card card){
		Card[] temp = drawn.clone();
		drawn = new Card[temp.length-1];
		for(int i = 0; i < drawn.length; i++){
			drawn[i] = temp[i];
		}
		stock.removeCard(card);
	}

	private Card identifyCard(){
		Card card = null;
		try{
			if(isWithinBounds()){
				try{
					card = drawn[draw];
				}catch(Exception e){
					draw = drawn.length - 1;
					card = drawn[draw];
				}
			}
		}catch(Exception e){
		}finally{
			return card;
		}
	}

	private boolean isWithinBounds(){
		draw = (x - XSTART) / XDIFF;
		// Computations: h = XSTART + XDIFF*(size-1) + Card.WIDTH
		// v = YSTART + Card.HEIGHT
		boolean horizontal = x >= XSTART && x <= 162 + XDIFF*(drawn.length-1);
		boolean vertical = y >= YSTART && y <= 108;
		return horizontal && vertical;
	}

	public boolean isStartTimer(){
		return timerStarted;
	}
}