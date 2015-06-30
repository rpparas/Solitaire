/*
 * TransferException.java
 *
 * Created on December 24, 2006, 7:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Rommel
 */

public class TransferException extends GameException{
	private static final long serialVersionUID = 1;
	private int column;

    public TransferException(){
		message = "Kindly make sure that target card is in Face Up Position.";
		title = "Transfer Not Possible";
	}

    public TransferException(int column){
		this.column = column;
		message = "You can only drop on the last card of the "+findPile()+" Pile.";
		title = "Transfer Not Possible";
    }

	public TransferException(Card card){
		message = "Only cards of rank KING may be transferred to a blank Pile.";
		title = "Transfer Not Possible";
	}

    public void show(){
        show(message, title);
    }

	private String findPile(){
		if(column == 1){
			return column+"st";
		}else if(column == 2){
			return column+"nd";
		}else if(column == 3){
			return column+"rd";
		}else{
			return column+"th";
		}
	}
}
