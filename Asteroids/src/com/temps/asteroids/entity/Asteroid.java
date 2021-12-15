package com.temps.asteroids.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import com.temprovich.mesa.maths.Maths;
import com.temprovich.mesa.maths.geom.Vector2f;
import com.temprovich.mesa.maths.random.Xorshift128;
import com.temps.asteroids.entity.particle.ParticleSystem;
import com.temps.asteroids.entity.particle.ParticleType;
import com.temps.asteroids.level.Level;
import com.temps.asteroids.util.Polygon;

public class Asteroid extends PhysicsEntity {

	private static final int MIN_BOUNDS0 = 4;
	private static final int MAX_BOUNDS0 = 6;
	private static final int MIN_BOUNDS1 = 9;
	private static final int MAX_BOUNDS1 = 17;

	private static final int MIN_BOUNDS2 = 9;
	private static final int MAX_BOUNDS2 = 11;
	private static final int MIN_BOUNDS3 = 18;
	private static final int MAX_BOUNDS3 = 35;

	private Vector2f oldPosition;
	private Polygon asteroid;
	private Random random;
	private boolean god;
	public boolean small;

	public Asteroid(Vector2f position, Level level, boolean small) {
		super(position, level);
		this.random = new Xorshift128();
		this.small = small;
		this.god = true;
		this.width = (small) ? 20 : 40;
		this.height = width;
		this.acceleration = (float) (((random.nextInt(2) + 1) + random.nextGaussian()) + 0.5f);
		while (acceleration <= 1f) this.acceleration = (float) (((random.nextInt(2) + 1) + random.nextGaussian()) + 0.5f);
		this.asteroid = new Polygon(position, ((small) ? generatePointsSmall() : generatePointsLarge()), angle = Maths.randomSign() * random.nextInt(360));
		this.asteroid.rotation = angle = random.nextInt(360);
	}

	public Vector2f[] generatePointsSmall() {
		return new Vector2f[] { new Vector2f(0, 0), //
				new Vector2f(random.nextInt(width / MIN_BOUNDS0) + MAX_BOUNDS0, //
						-(random.nextInt(width / MIN_BOUNDS0) + MIN_BOUNDS1)),
				new Vector2f(random.nextInt(width / MIN_BOUNDS0) + MAX_BOUNDS1, //
						-(random.nextInt(MAX_BOUNDS0))),
				new Vector2f(random.nextInt(width / MIN_BOUNDS0) + (MIN_BOUNDS1 + MAX_BOUNDS1), //
						(random.nextInt(MAX_BOUNDS0))),
				new Vector2f(random.nextInt(width / MIN_BOUNDS0) + MAX_BOUNDS1, //
						(random.nextInt(width / MIN_BOUNDS0) + MAX_BOUNDS1)),
				new Vector2f(random.nextInt(MAX_BOUNDS0) - MAX_BOUNDS0, //
						(random.nextInt(MAX_BOUNDS0) + MIN_BOUNDS1)) };
	}

	public Vector2f[] generatePointsLarge() {
		return new Vector2f[] { new Vector2f(0, 0), //
				new Vector2f(random.nextInt(width / MIN_BOUNDS2) + MAX_BOUNDS2, //
						-(random.nextInt(width / MIN_BOUNDS2) + MIN_BOUNDS3)),
				new Vector2f(random.nextInt(width / MIN_BOUNDS2) + MAX_BOUNDS3, //
						-(random.nextInt(MAX_BOUNDS2))),
				new Vector2f(random.nextInt(width / MIN_BOUNDS2) + (MIN_BOUNDS3 + MAX_BOUNDS3), //
						(random.nextInt(MAX_BOUNDS2))),
				new Vector2f(random.nextInt(width / MIN_BOUNDS2) + MAX_BOUNDS3, //
						(random.nextInt(width / MIN_BOUNDS2) + MAX_BOUNDS3)),
				new Vector2f(random.nextInt(MAX_BOUNDS2) - MAX_BOUNDS2, //
						(random.nextInt(MAX_BOUNDS2) + MIN_BOUNDS3)) };
	}

	int counter = 0;

	public void tick(float deltaTime) {
		if (god && counter < 16) counter++;
		else {
			god = false;
			counter = 0;
		}

		move();

		if (collision() && !god) dead = true;

		if (dead) {
			level.entities.add(new ParticleSystem(new Vector2f(position), 2020, 0xffffff, random.nextInt(500) + 100, level, ParticleType.DEATH_PARTICLE));
			if (!small) for (int i = 0; i < 4; i++) level.entities.add(new Asteroid(position, level, true));
		}

		asteroid.position = position;
	}

	protected void move() {
		oldPosition = position;

		position = new Vector2f((float) (position.x + acceleration * (Math.sin(angle))), (float) (position.y + acceleration * (Math.cos(angle))));

		if (position.x > 800 && oldPosition.x < position.x) position = new Vector2f(0 - width, position.y);
		else if (position.x < 0 - width && oldPosition.x > position.x) position = new Vector2f(810, position.y);
		if (position.y > 600 && oldPosition.y < position.y) position = new Vector2f(position.x, 0 - height);
		else if (position.y < 0 - height && oldPosition.y > position.y) position = new Vector2f(position.x, 610);
	}

	public boolean collision() {
		for (int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if (e instanceof Bullet && this.getBoundingBox().intersects(e.getBoundingBox())) return true;
		}
		return false;
	}

	public void render(Graphics2D g2d) {
		g2d.setColor(Color.white);
		asteroid.render(g2d);

//		g2d.setColor(Color.blue);
//		g2d.draw(AffineTransform.getRotateInstance(Math.toRadians(angle), position.x + 6, position.y + 6).createTransformedShape(getBoundingBox()));
//		g2d.setColor(Color.red);
//		g2d.draw(getBoundingBox());
	}

	public Rectangle getBoundingBox() {
		return new Rectangle((int) position.x - 6, (int) position.y - 6, width, height);
	}
}
