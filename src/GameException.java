/*
 * GameException.java
 *
 * Created on December 10, 2006, 10:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Rommel
 */
public class GameException extends RuntimeException{
    protected Object message;
    protected String title;

    public void show(Object message, String title){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void show(){}
}