package com.temps.asteroids.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.temprovich.mesa.maths.Maths;
import com.temprovich.mesa.maths.geom.Vector2f;
import com.temprovich.mesa.maths.random.Xorshift128;
import com.temps.asteroids.Game;
import com.temps.asteroids.GameState;
import com.temps.asteroids.entity.particle.ParticleSystem;
import com.temps.asteroids.entity.particle.ParticleType;
import com.temps.asteroids.level.Level;
import com.temps.asteroids.util.Polygon;

public class Player extends PhysicsEntity {

	private static final float NORMAL_ACCELERATION_SPEED = 3f;
	private static final float MINIMUM_ACCELERATION_AMOUNT = 0.5f;
	private static final float ACCELERATION_INCREMENT = 0.02f;
	private static final float DECELERATION_INCREMENT = 0.01f;
	private static final float ROTATION_INCREMENT = 3f;
	private static final int FIRE_TIME = 12;

	private int score;
	private float currentAngle;
	private boolean god, reset;
	private float fireDelay;
	private int lives = 3;
	private int time = 0;

	private Vector2f[] pos = new Vector2f[3];
	private Polygon[] p = new Polygon[3];

	private Vector2f oldPosition;
	private Polygon ship;
	private Game game;
	public Color color;
	private Random random = new Xorshift128();

	private List<Bullet> bullets = new ArrayList<Bullet>();

	public Player(Vector2f position, Level level, Game game) {
		super(position, level);
		this.width = 28;
		this.height = 28;
		this.angle = Maths.randomSign() * random.nextInt(360);
		this.currentAngle = angle;
		this.god = true;
		this.score = 0;
		this.ship = new Polygon(position, new Vector2f[] { new Vector2f(0, 0), new Vector2f(25, 10), new Vector2f(0, 20) }, angle);
		this.game = game;
		this.color = Color.white;

//		if (Files.exists(Paths.get("resources/data/player_data.tn"))) {
//			try {
//				TNObject playerData = new TNObject("resources/data/player_data.tn");
//				this.highscore = playerData.getInt("highscore");
//				game.uiHighscore.setText("" + highscore);
//			} catch (NullDataException e) {
//				e.printStackTrace();
//			}
//		}

		for (int i = 0; i < p.length; i++) {
			pos[i] = new Vector2f(24 + i * 20, 85);
			this.p[i] = new Polygon(pos[i], ship.getPointsRaw(), -90);
		}
	}

	public void tick(float deltaTime) {
		move();

		if (reset) {
			time++;
			if (time >= 64) {
				reset = false;
				time = 0;
			}
			if (time <= 63) {
				god = true;
			}
		} else god = false;

		if (!bullets.isEmpty()) for (int i = 0; i < bullets.size(); i++) {
			if (bullets.get(i).collision()) {
				score += bullets.get(i).score;
				game.uiScore.setText("" + score);
			}
			if (!level.entities.contains(bullets.get(i))) bullets.remove(bullets.get(i));
		}

		if (game.getInput().isKey(KeyEvent.VK_ENTER)) {
			fireDelay -= deltaTime;

			if (fireDelay <= 0) {
				Bullet b = new Bullet(new Vector2f(position), angle, level);
				bullets.add(b);
				level.entities.add(b);
				fireDelay += (FIRE_TIME);
			}
		} else fireDelay = 0;

		if (collision() && !god) {
			lives--;
			god = true;
			reset = true;
		}

		if (lives <= 0) {
//			if (score > highscore) {
//				try {
//					TNWriter.writeFile("resources/data/player_data", Arrays.asList("highscore: " + score));
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
			dead = true;
			level.entities.add(new ParticleSystem(new Vector2f(position), 2020, 0xffffff, random.nextInt(500) + 100, level, ParticleType.DEATH_PARTICLE));
			game.uiScore.setText("00");
			game.state = GameState.MENU;
		}

		ship.position = position;
		ship.rotation = angle;
	}

	protected void move() {
		oldPosition = position;

		if (game.getInput().isKey(KeyEvent.VK_W)) {
			position = new Vector2f((float) (position.x + (acceleration * Math.cos(Math.toRadians(angle)))), (float) (position.y + (acceleration * Math.sin(Math.toRadians(angle)))));
			currentAngle = angle;

			if (acceleration < NORMAL_ACCELERATION_SPEED) acceleration += ACCELERATION_INCREMENT;

			if (acceleration > NORMAL_ACCELERATION_SPEED) acceleration -= (acceleration > NORMAL_ACCELERATION_SPEED * 0.66 ? DECELERATION_INCREMENT * 2 : DECELERATION_INCREMENT);
		} else {
			if (acceleration > MINIMUM_ACCELERATION_AMOUNT) {
				acceleration -= DECELERATION_INCREMENT;

				if (acceleration < MINIMUM_ACCELERATION_AMOUNT) acceleration = MINIMUM_ACCELERATION_AMOUNT;
			}
			position = new Vector2f((float) (position.x + (acceleration * Math.cos(Math.toRadians(currentAngle)))), (float) (position.y + (acceleration * Math.sin(Math.toRadians(currentAngle)))));
		}

		if (game.getInput().isKey(KeyEvent.VK_A)) angle -= ROTATION_INCREMENT;
		if (game.getInput().isKey(KeyEvent.VK_D)) angle += ROTATION_INCREMENT;

		if (position.x > level.width && oldPosition.x < position.x) position = new Vector2f(0 - width, position.y);
		else if (position.x < 0 - width && oldPosition.x > position.x) position = new Vector2f(810, position.y);
		if (position.y > level.height && oldPosition.y < position.y) position = new Vector2f(position.x, 0 - height);
		else if (position.y < 0 - height && oldPosition.y > position.y) position = new Vector2f(position.x, 610);
	}

	public boolean collision() {
		for (int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if (e instanceof Asteroid && e.getBoundingBox().intersects(this.getBoundingBox())) return true;
		}
		return false;
	}

	int blink_timer = 0;

	public void render(Graphics2D g2d) {
		if (god) {
			blink_timer++;
			if (blink_timer <= 5) g2d.setColor(color = Color.black);
			if (blink_timer > 5) g2d.setColor(color = Color.white);
			if (blink_timer > 10) blink_timer = 0;
		} else g2d.setColor(color = Color.white);

		ship.render(g2d);

		for (int i = 0; i < lives; i++) {
			p[i].render(g2d);
		}

//		g2d.setColor(Color.blue);
//		g2d.draw(AffineTransform.getRotateInstance(Math.toRadians(angle), position.x + 6, position.y + 6).createTransformedShape(getBoundingBox()));
//		g2d.setColor(Color.red);
//		g2d.draw(getBoundingBox());
	}

	public Rectangle getBoundingBox() {
		return new Rectangle((int) position.x - 8, (int) position.y - 8, width, height);
	}

}
