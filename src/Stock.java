/*
 * Stock.java
 *
 * Created on December 11, 2006, 10:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.util.*;

/**
 *
 * @author Rommel
 */
public class Stock implements Collection, Cloneable{
    private LinkedList<Card> shuffled;
    private Stack<Card> drawn;
    private Vector<Card> deck;
    private int counter, currentDraw;

    /** Creates a new instance of Stock */
    public Stock(Vector<Card> deck) {
        this.deck = deck;
        drawn = new Stack<Card>();
        shuffled = new LinkedList<Card>();
    }

    public void init(){
        for(int i = 0; !deck.isEmpty(); i++){
            shuffled.add(shuffle());
        }
        deck = null;
    }

    public void draw(){
		if(shuffled.isEmpty()){
			return;
		}

		if(counter == shuffled.size()){
			if(drawn == null){
				drawn = new Stack<Card>();
				counter = 0;
			}else{
				drawn = null;
			}
		}

		currentDraw = 0;
        for(int i = 1, index; i <= 3 && canDraw(); i++){
            index = shuffled.size()-counter-1;
            drawn.push(shuffled.get(index));
            currentDraw++;
            counter++;
        }
        Solitaire.numberOfMoves++;
    }

    private Card shuffle(){
        int index = (int)(Math.random() * deck.size());
        Card card = (Card)deck.elementAt(index);
        deck.removeElementAt(index);
        return card;
    }

    private boolean canDraw(){
		boolean isNotEmpty = !shuffled.isEmpty();
		boolean hasMoreCards = counter < shuffled.size();
		return isNotEmpty && hasMoreCards;
	}

    public Card removeLastCard(){
        Card card = (Card)drawn.pop();
        shuffled.remove(counter-1);
        counter--;
        return card;
    }

	public void removeCard(Card card){
		drawn.pop();
		shuffled.remove(card);
        counter--;
	}

    public Card[] getDrawn(){
        Card[] cards = null;
		try{
			Stack<Card> temp = (Stack<Card>)drawn.clone();
			if(currentDraw != 0){
				cards = new Card[currentDraw];
				for(int i = 0; i < cards.length; i++){
					cards[i] = (Card)temp.pop();
				}
			}
		}catch(NullPointerException e){
		}finally{
			return cards;
		}
    }

    public Card getLastCard(){
        Stack<Card> temp = this.drawn;
        return (Card)temp.pop();
	}

    public int getSize(){
		return drawn.size();
	}

	public void addCard(Card card){}
	public LinkedList getCards(){return null;}
}