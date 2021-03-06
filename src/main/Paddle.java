package main;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/**
 *
 * 
 * 
 */

public class Paddle extends Dimensions implements Boundaries {
	String paddle = "img/paddle.png";
	int dx;
	ImageIcon myImageicon;

	public Paddle() {
		myImageicon = new ImageIcon(getClass().getClassLoader().getResource(
				paddle));
		image = myImageicon.getImage();
		width = image.getWidth(null);
		height = image.getHeight(null);
		resetState();
	}

	public Paddle(boolean flag) {
		if (flag) {
			myImageicon = new ImageIcon(getClass().getClassLoader()
					.getResource(paddle));
			image = myImageicon.getImage();
			width = image.getWidth(null);
			height = image.getHeight(null);
		}
		resetState();
	}

	public void move() {

		if (x <= Boundaries.PADDLELEFT) {
			x = Boundaries.PADDLELEFT;
		}
		if (x >= Boundaries.PADDLERIGHT - width) {
			x = Boundaries.PADDLERIGHT - width;
		}
		x += dx;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT) {
			dx = -2;
		}

		if (key == KeyEvent.VK_RIGHT) {
			dx = 2;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT) {
			dx = 0;
		}

		if (key == KeyEvent.VK_RIGHT) {
			dx = 0;
		}
	}

	public void resetState() {
		x = (Constants.WINDOWWIDTH / 2) - 30;
		y = 460;
	}
}
