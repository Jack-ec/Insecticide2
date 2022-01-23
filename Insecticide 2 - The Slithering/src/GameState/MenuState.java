package GameState;

import TileMap.Background;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

	private Background bg;

	private int currentChoice = 0;
	private String[] options = {
			"Start",
			"Quit",
	};

	private Color titleColor;
	private Font titleFont;

	private Font font;

	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;

		try {
			bg = new Background("/Backgrounds/menu.png", 1);

			titleColor = new Color(0,0,128);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			font = new Font("Arial", Font.PLAIN, 12);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {}

	public void draw(Graphics2D g) {
		//draw bg
		bg.draw(g);
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Insecticide 2:", 80, 70);
		g.drawString("The Slithering", 80, 100);

		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.BLUE);
			}
			else {
				g.setColor(Color.GREEN);
			}
			g.drawString(options[i], 145, 140 + i * 15);
		}
	}
	private void select() {
		if(currentChoice == 0) {
			// start
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		// quit
		if(currentChoice == 1) {
			System.exit(0);
		}
	}
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER) {
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}

	public void update() {}
}
