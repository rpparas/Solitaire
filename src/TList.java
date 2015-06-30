/*
 * TList.java
 *
 * Created on December 26, 2006, 11:44 AM
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
public class TList {
	private LinkedList<String> list;

    /** Creates a new instance of TList */
    public TList() {
		list = new LinkedList<String>();
   	}

    public void addEntry(String entry){
		list.add(entry);
    }

	public Object[] getList(){
		return list.toArray();
	}
}