/*
 * Solitaire.java
 *
 * Created on December 10, 2006, 9:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;

/**
 *
 * @author Rommel
 */
public class Solitaire {
    protected static final int DECK_SIZE = 52;
    protected static final int NO_ACTION = -1;
    protected static final int NONE = 0;

    private Vector<Card> deck;
	private Card source, target;
    private static Stock stock;
    private static Pile[] pile;
    private static Cabinet[] cabinet;

	private static BestPlayers bestPlayers;
    private static int index;
    protected static int numberOfMoves;


    /** Creates a new instance of Solitaire */
    public Solitaire() {
        deck = new Vector<Card>(DECK_SIZE);
        pile = new Pile[7];
        index = NO_ACTION;
        numberOfMoves = 0;
    }

	public void loadFile() throws FileException{
		try{
			FileInputStream fileIn = new FileInputStream("players.dat");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			bestPlayers = (BestPlayers)in.readObject();
			in.close();
		}catch(IOException e){
			bestPlayers = new BestPlayers();
			saveFile();
		}catch(ClassNotFoundException e){}
	}

	public void saveFile() throws FileException{
		try{
			FileOutputStream fileOut = new FileOutputStream("players.dat");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(bestPlayers);
			out.close();
		}catch(IOException ex){
			throw new FileException();
		}
	}

    public void createDeck(){
        for(int rank = 1; rank <= 13; rank++){
            deck.add(new Card(Card.CLUB, rank));
            deck.add(new Card(Card.DIAMOND, rank));
            deck.add(new Card(Card.HEART, rank));
            deck.add(new Card(Card.SPADE, rank));
        }
    }

    public void createCabinet(){
		cabinet = new Cabinet[4];
        cabinet[0] = new Cabinet(Card.CLUB);
		cabinet[1] = new Cabinet(Card.DIAMOND);
		cabinet[2] = new Cabinet(Card.SPADE);
		cabinet[3] = new Cabinet(Card.HEART);
    }

    public void deal(){
        for(int i = 0; i < 7; i++){
            pile[i] = new Pile(i+1, deck);
            pile[i].init();
            deck = pile[i].getCardsLeft();
        }
        stock = new Stock(deck);
        stock.init();
        deck = null;
    }

	public void addToCabinet(Card card){
		index = Cabinet.getIndex(card.getSuit());
		cabinet[index].addCard(card);
	}

	public void resetCabinetIndex(){
		index = NO_ACTION;
	}

	public int findBlankPile(){
		for(int i = 0; i < pile.length; i++){
			if(pile[i].getSize() == 0){
				return i;
			}
		}
		return Pile.NONE;
	}

	public Object[] findTargetPile(String src){ // from Cabinet to Pile
		index = Cabinet.getIndex(src);
		try{
			Card source = cabinet[index].getLastCard();
			return findTargetPile(source);
		}catch(Exception e){
			return null;
		}
	}

	public Object[] findTargetPile(Card source){ //from Drawn to Pile
		Transfer transfer = null;
		TList tList = new TList();

		for(int i = 0; i < pile.length; i++){
			try{
				target = pile[i].getLastCard();
			}catch(Exception e){
				target = null;
			}finally{
				try{
					transfer = new Transfer(source, target);
					if(transfer.isAllowed(i)){
						tList.addEntry("Pile "+(i+1));
					}
				}catch(GameException e){
					e.show();
				}
			}
		}
		return tList.getList();
	}

	public boolean uploadCard(Card card){
		String suit = card.getSuit();
		int index = Cabinet.getIndex(suit);

		try{
			Card top = cabinet[index].getLastCard();
			if(card.getRank() == top.getRank() + 1){
				return true;
			}else{
				throw new UploadException(card);
			}
		}catch(Exception e){
			if(card.getRank() == 1){
				return true;
			}else{
				throw new UploadException(card);
			}
		}
	}

	public Stock getStock(){
        return stock;
    }

    public Pile[] getPile(){
        return pile;
    }

    public Cabinet[] getCabinet(){
		return cabinet;
	}

	public int getCardsLeft(){
		int num = DECK_SIZE;
		for(int i = 0; i < cabinet.length; i++){
			if(cabinet[i] != null){
				num -= cabinet[i].getSize();
			}
		}
		return num;
	}

	public int getCardAddedIndex(){
		return index;
	}

	public BestPlayers getBestPlayers(){
		return bestPlayers;
	}
}