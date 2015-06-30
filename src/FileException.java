/*
 * FileException.java
 *
 * Created on January 14, 2007, 10:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Rommel
 */

public class FileException extends GameException{
	private static final long serialVersionUID = 1;

    public FileException(){
		message = "The storage medium maybe read-only.";
		title = "File Not Created";
    }

    public void show(){
        show(message, title);
    }
}
