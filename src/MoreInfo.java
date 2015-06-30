/*
 * MoreInfo.java
 *
 * Created on January 17, 2007, 8:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * @ original code provided by J. Marinacci & C. Adamson in Swing Hacks
 * @ this version was edited by Rommel Paras
 */

public class MoreInfo extends JPanel{
	private Component tc, bc;
	private SpinWidget spinWidget;
	private final int SW_HEIGHT = 14;

	public MoreInfo(Component tc, Component bc){
		this.tc = tc;
		this.bc = bc;
		spinWidget = new SpinWidget();
		doMyLayout();
	}

	protected void doMyLayout(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(tc);
		add(spinWidget);
		add(bc);
		resetBottomVisibility();
	}

	protected void resetBottomVisibility(){
		if(bc == null || spinWidget == null){
			return;
		}

		bc.setVisible(spinWidget.isOpen());
		revalidate();

		if(isShowing()){
			Container ancestor = getTopLevelAncestor();
			if(ancestor != null && ancestor instanceof Window){
				((Window)ancestor).pack();
			}
			repaint();
		}
	}

	public void showBottom(boolean b){
		spinWidget.setOpen(b);
	}

	public boolean isBottomShowing(){
		return spinWidget.isOpen();
	}

	class SpinWidget extends JPanel{
		final int HALF_SW_HEIGHT = SW_HEIGHT / 2;
		int[] openXPoints = {1, HALF_SW_HEIGHT, SW_HEIGHT-1};
		int[] openYPoints = {HALF_SW_HEIGHT, SW_HEIGHT-1, HALF_SW_HEIGHT};
		int[] closedXPoints = {1, 1, HALF_SW_HEIGHT};
		int[] closedYPoints = {1, SW_HEIGHT-1, HALF_SW_HEIGHT};

		Polygon openTriangle = new Polygon(openXPoints, openYPoints, 3);
		Polygon closedTriangle = new Polygon(closedXPoints, closedYPoints, 3);
		Dimension mySize = new Dimension(SW_HEIGHT, SW_HEIGHT);

		boolean open;

		public SpinWidget(){
			setOpen(false);
			addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent event){
					handleClick();
				}
			});
		}

		public void handleClick(){
			setOpen(!isOpen());
		}

		public boolean isOpen(){
			return open;
		}

		public void setOpen(boolean o){
			open = o;
			resetBottomVisibility();
		}

		public Dimension getMinimumSize(){
			return mySize;
		}

		public Dimension getPreferredSize(){
			return mySize;
		}

		public void paint(Graphics g){
			if(isOpen()){
				g.fillPolygon(openTriangle);
			}else{
				g.fillPolygon(closedTriangle);
			}
		}
	}
}