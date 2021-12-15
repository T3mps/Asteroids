package com.temps.asteroids.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temprovich.mesa.maths.random.Xorshift128;
import com.temps.asteroids.level.Level;

public class Bullet extends PhysicsEntity {

	public double angle;
	private int maxLife;
	private int life;
	public int score;
	private Random random = new Xorshift128();

	public Bullet(Vector2f position, double angle, Level level) {
		super(position, level);
		this.position = position;
		this.width = 3;
		this.height = 3;
		this.angle = angle;
		this.maxLife = 48 + random.nextInt(4);
		this.life = 0;
	}

	public void tick(float deltaTime) {
		life++;
		if (life >= maxLife || collision()) dead = true;
		move();
	}

	public void move() {
		position.x += ((Math.cos(angle * Math.PI / 180))) * 7;
		position.y += ((Math.sin(angle * Math.PI / 180))) * 7;
	}

	public boolean collision() {
		for (int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if (e instanceof Asteroid && e.getBoundingBox().intersects(this.getBoundingBox())) {
				Asteroid a = (Asteroid) e;
				if (a.small) score = 50;
				else score = 20;
				return true;
			}
		}
		return false;
	}

	public void render(Graphics2D g2d) {
		g2d.setColor(Color.white);
		g2d.fillRect((int) position.x, (int) position.y, width, height);

//		g2d.setColor(Color.red);
//		g2d.draw(getBoundingBox());
	}

	public Rectangle getBoundingBox() {
		return new Rectangle((int) position.x, (int) position.y, width, height);
	}
}
