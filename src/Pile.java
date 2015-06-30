/*
 * Pile.java
 *
 * Created on December 11, 2006, 9:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.util.*;
/**
 *
 * @author Rommel
 */
public class Pile implements Collection{
    private Vector<Card> deck;
    private LinkedList<Card> cards;
    private int size;
    private int number;


    /** Creates a new instance of Pile */
    public Pile(int number, Vector<Card> deck){
        this.number = number;
        this.deck = deck;
        cards = new LinkedList<Card>();
    }

    public void init(){
        Card card;
        for(int i = 0; i < number; i++){
            card = shuffle();
            if(i+1 < number){
                card.setFaceUp(false);
            }
            cards.add(card);
        }
    }

    private Card shuffle(){
        int index = (int)(Math.random() * deck.size());
        Card card = (Card)deck.elementAt(index);
        deck.removeElementAt(index);
        return card;
    }

    public void addCard(Card card){
		Solitaire.numberOfMoves++;
		cards.add(card);
	}

	public void removeCard(Card card){
		cards.remove(card);
	}

    public void setFaceUp(int index){
		Card card = (Card)cards.get(index);
		card.setFaceUp(true);
		cards.set(index, card);
		Solitaire.numberOfMoves++;
	}

    /**Returns the remaining cards in the deck after
     * having been distributed to the current pile.
     * @return Vector
     */
    public Vector<Card> getCardsLeft(){
        deck.trimToSize();
        return deck;
    }

    /**Returns the existing cards in this pile.
     *
     * @return LinkedList
     */
    public LinkedList getCards(){
        return cards;
    }

    /**Returns the lowest-ranked card in this pile.
     *
     * @return Card
     */
    public Card getLastCard(){
        return (Card)cards.getLast();
    }

    /**Returns the number of cards in this pile.
     *
     * @return int
     */
    public int getSize(){
		return cards.size();
	}

    /**Removes the last card in in this pile.
     *
     * @return Card
     */
	public Card removeLastCard(){
		return null;
	}
}