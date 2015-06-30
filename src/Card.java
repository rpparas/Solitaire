/*
 * Card.java
 *
 * Created on December 10, 2006, 9:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Rommel
 */

import java.awt.Color;

public class Card {
    final static int WIDTH = 71;
    final static int HEIGHT = 96;

    final static String CLUB = "club";
    final static String DIAMOND = "diamond";
    final static String HEART = "heart";
    final static String SPADE = "spade";

    private String suit;
    private Color color;
    private int rank;
    private boolean isFaceUp;


    public Card(String suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        isFaceUp = true;
        setColor();
    }

	private void setColor(){
		if(suit.equals(Card.DIAMOND) || suit.equals(Card.HEART)){
			color = Color.RED;
		}else{
			color = Color.BLACK;
		}
	}

    public boolean isFaceUp(){
		return isFaceUp;
	}

    public void setFaceUp(boolean value){
        isFaceUp = value;
    }

    public Color getColor(){
		return color;
	}

    public String getSuit(){
        return suit;
    }

    public int getRank(){
        return rank;
    }
}