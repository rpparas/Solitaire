/*
 * Collection.java
 *
 * Created on December 26, 2006, 9:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Rommel
 */

import java.util.*;

public interface Collection{
	static final int NONE = -1;


	public void addCard(Card card);

	public void removeCard(Card card);

	public int getSize();

	public LinkedList getCards();

	public Card getLastCard();

	public Card removeLastCard();
}