package gui;

import javax.swing.JButton;

public class CellButton extends JButton {

	/*
	 * Extension of JButton, allows it to also have a location (x, y)
	 */
	
	// Serialisation required by Java
	private static final long serialVersionUID = 5210537194729742976L;

	private int x;
	private int y;
	
	public CellButton(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getXCoord() {
		return x;
	}

	public int getYCoord() {
		return y;
	}
}
