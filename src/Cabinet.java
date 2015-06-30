/*
 * Cabinet.java
 *
 * Created on December 10, 2006, 9:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.util.*;

/**
 *
 * @author Rommel
 */
public class Cabinet implements Collection{
    private Stack<Card> cards;
    private int currentRank;
    private String suit;


    /** Creates a new instance of Cabinet */
    public Cabinet(String suit){
        this.suit = suit;
        cards = new Stack<Card>();
    }

    public void addCard(Card card){
        cards.push(card);
        currentRank = card.getRank();
		Solitaire.numberOfMoves++;
    }

    public Card removeLastCard(){
        Card top = (Card)cards.pop();
        return top;
    }

    public Card getLastCard(){
        Card top = (Card)cards.peek();
        return top;
    }

    public int getSize(){
        return currentRank;
    }

    public String getSuit(){
		return suit;
	}

	public static String getSuit(int index){
		switch(index){
			case 0: 	return Card.CLUB;
			case 1: 	return Card.DIAMOND;
			case 2: 	return Card.SPADE;
			case 3:		return Card.HEART;
			default:	return null;
		}
	}

	public static int getIndex(String suit){
		if(suit.equals(Card.CLUB)){
			return 0;
		}else if(suit.equals(Card.DIAMOND)){
			return 1;
		}else if(suit.equals(Card.SPADE)){
			return 2;
		}else{
			return 3;
		}
	}

    public void removeCard(Card card){}
    public LinkedList getCards(){return null;}
}