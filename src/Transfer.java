/*
 * Transfer.java
 *
 * Created on December 24, 2006, 6:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.*;
import java.util.*;

/**
 *
 * @author Rommel
 */
public class Transfer {
    private Stack<Card> cards;
	private Pile srcPile;

    private Card source;
    private Card target;
	private Card card;


    /** Creates a new instance of Transfer */
    public Transfer(Pile srcPile, int index, Card target) {
        this.srcPile = srcPile;
		this.target = target;
        cards = new Stack<Card>();

        for(int i = srcPile.getSize()-1; i >= index && i >= 0; i--){
			card = (Card)srcPile.getCards().get(i);
			cards.push(card);
		}

		if(!cards.isEmpty()){
			source = (Card)cards.peek();
		}
   	}

    public Transfer(Card source, Card target){
		cards = new Stack<Card>();
		this.source = source;
		this.target = target;
	}

    public boolean isAllowed(int blankPile){
		if(target == null){
			return isTransferKing(blankPile);
		}else{
			if(source == null){
				return false;
			}
			else{
				return isTransferSuit();
			}
		}
    }

    public boolean isAllowed(){
		return isTransferSuit();
	}

	private boolean isTransferSuit(){
		int rankSource = source.getRank();
		int rankDest = target.getRank();

		return (rankDest-1 == rankSource && source.getColor() != target.getColor());
	}

	private boolean isTransferKing(int blankPile){
		if(source.getRank() == 13){
			if(target == null && blankPile != Pile.NONE){
				return true;
			}else{
				return false;
			}
		}else if(cards.isEmpty()){
			return false;
		}else{
			if(blankPile == Pile.NONE){
				return false;
			}else{
				throw new TransferException(source);
			}
		}
	}

    public Pile moveCards(Pile destination){
		if(destination.getCards().indexOf(target) != destination.getSize()-1){
			throw new TransferException(destination.getSize());
		}

		while(!cards.isEmpty()){
			card = (Card)cards.pop();
			destination.addCard(card);
			srcPile.removeCard(card);
		}
		return destination;
	}

	public Pile getCardsLeft(){
		return srcPile;
	}
}