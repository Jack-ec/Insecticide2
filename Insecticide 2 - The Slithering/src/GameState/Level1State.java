package GameState;

import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState {

	private TileMap tileMap;
	private Background bg;

	private Player player;

	private ArrayList<Snake> snakes;
	private ArrayList<Enemy> sluggers;
	private ArrayList<Explosion> explosions;

	private HUD hud;

	private AudioPlayer bgMusic;

	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	public void init() {

		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.png");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);

		bg = new Background("/Backgrounds/junglebg.png", 0.1);

		player = new Player(tileMap);
		player.setPosition(100, 100);

		populateAnts();
		populateSnakes();

		explosions = new ArrayList<Explosion>();

		hud = new HUD(player);

		bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		bgMusic.play();

	}

	private void populateAnts() {

		sluggers = new ArrayList<Enemy>();

		Ants s;
		Point[] points = new Point[] {
				new Point(300, 100),
				new Point(860, 200),
				new Point(1400, 100),
				new Point(1300, 100),
		};
		for(int i = 0; i < points.length; i++) {
			s = new Ants(tileMap);
			s.setPosition(points[i].x, points[i].y);
			sluggers.add(s);
		}

	}

	private void populateSnakes() {
		snakes = new ArrayList<Snake>();

		Snake c;
		Point[] points = new Point[] {
				new Point(300, 200),
				new Point(700, 50),
				new Point(1700, 200),
		};
		for(int i = 0; i < points.length; i++) {
			c = new Snake(tileMap);
			c.setPosition(points[i].x, points[i].y);
			snakes.add(c);
		}
	}

	public void update() {

		// update player
		player.update();
		tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getx(),
				GamePanel.HEIGHT / 2 - player.gety()
				);

		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());

		// attack enemies
		player.checkAttack(sluggers);
		player.checkAttack1(snakes);

		// update all enemies
		for(int i = 0; i < snakes.size(); i++) {
			Snake e = snakes.get(i);
			e.update(player.getx(), player.getdx());
			if(e.isDead()) {
				snakes.remove(i);
				i--;
				explosions.add(
						new Explosion(e.getx(), e.gety()));
			}

		}
		for(int i = 0; i < sluggers.size(); i++) {
			Enemy e = sluggers.get(i);
			e.update();
			if(e.isDead()) {
				sluggers.remove(i);
				i--;
				explosions.add(
						new Explosion(e.getx(), e.gety()));
			}
		}

		// update explosions
		for(int i1 = 0; i1 < explosions.size(); i1++) {
			explosions.get(i1).update();
			if(explosions.get(i1).shouldRemove()) {
				explosions.remove(i1);
				i1--;
			}
		}
	}


	public void draw(Graphics2D g) {

		// draw bg
		bg.draw(g);

		// draw tilemap
		tileMap.draw(g);

		// draw player
		player.draw(g);

		// draw enemies
		for(int i = 0; i < snakes.size(); i++) {
			snakes.get(i).draw(g);
		}
		
		for(int i = 0; i < sluggers.size(); i++) {
			sluggers.get(i).draw(g);
		}

		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
					(int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}

		// draw hud
		hud.draw(g);

	}

	public void keyPressed(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_UP) player.setJumping(true);
		if(k == KeyEvent.VK_X) player.setScratching();
		if(k == KeyEvent.VK_Z) player.setFiring();
	}

	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_W) player.setJumping(false);
	}

}