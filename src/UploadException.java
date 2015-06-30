/*
 * UploadException.java
 *
 * Created on December 24, 2006, 4:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Rommel
 */

public class UploadException extends GameException{
	private static final long serialVersionUID = 1;

	private String suit;
	private int rank;

    public UploadException(Card card){
		suit = card.getSuit().toUpperCase();
		rank = card.getRank();

		message = "The "+suit.toUpperCase()+" Cabinet must contain "+findPreReq()+" before uploading this.";
		title = "Upload Not Possible";
    }

    public void show(){
        show(message, title);
    }

    private String findPreReq(){
		if(rank == 2){
			return "an ACE";
		}else if(rank == 12){
			return "a JACK";
		}else if(rank == 13){
			return "a QUEEN";
		}else if(rank == 9){
			return "an "+"8-"+suit.charAt(0);
		}else{
			return "a "+(rank-1)+"-"+suit.charAt(0);
		}
	}
}
