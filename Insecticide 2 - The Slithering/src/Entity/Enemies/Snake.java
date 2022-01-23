package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;

public class Snake extends Enemy {

	private BufferedImage[] sprites;
	private int PlayerVector;
	private int PlayerPosition;
	

	public Snake(TileMap tm) {

		super(tm);

		moveSpeed = 0.3;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;

		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;

		health = maxHealth = 2;
		damage = 1;

		// load sprites
		try {

			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
							"/Sprites/snake.png"
							)
					);

			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
						i * width,
						0,
						width,
						height
						);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(300);

		right = true;
		facingRight = true;

	}
	
	public boolean isAlert(int playerposition, int playervector) {
		
		PlayerPosition = playerposition;
		PlayerVector = playervector;
		
		if (x - playerposition <= 100 && x - playerposition >= -100) {
			if (x - playerposition <= 100 && x - playerposition >= 0) {
				facingRight = false;
			}
			if (x - playerposition >= -100 && x - playerposition <= 0) {
				facingRight = true;
			}
			return true;
		}
		else {
			return false;
		}
		
	}

	private void getNextPosition() {
		// movement
		if (isAlert(PlayerPosition, PlayerVector) == true && x > PlayerPosition) {
			dx = -PlayerVector;
		}
		
		if (isAlert(PlayerPosition, PlayerVector) == true && x < PlayerPosition) {
			dx = PlayerVector;
		}
		
			// falling
			if(falling) {
				dy += fallSpeed;
			}
		}

	public void update(int playerposition, int playervector) {

		// update position
		isAlert(playerposition, playervector);
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		// check flinching
		if(flinching) {
			long elapsed =
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}

		// if it hits a wall, go other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}

		// update animation
		animation.update();

	}

	public void draw(Graphics2D g) {

		//if(notOnScreen()) return;

		setMapPosition();

		super.draw(g);

	}

}