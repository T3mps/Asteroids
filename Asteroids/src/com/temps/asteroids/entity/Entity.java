package com.temps.asteroids.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temps.asteroids.level.Level;
import com.temps.asteroids.util.UUID;

public abstract class Entity {

	public Vector2f position;
	protected UUID uuid;
	protected boolean dead;
	protected Level level;

	public Entity(Vector2f position) {
		this.position = position;
		this.uuid = new UUID();
		System.out.println(uuid);
	}

	public Entity() {
		this.uuid = new UUID();
		System.out.println(uuid);
	}

	public abstract void tick(float deltaTime);

	public abstract void render(Graphics2D g2d);

	public boolean isDead() {
		return dead;
	}

	public String toString() {
		return "position: " + position.toString() + ", dead: " + dead + ", UUID: " + uuid.toString();
	}

	public void init(Level level) {
		this.level = level;
	}

	public abstract Rectangle getBoundingBox();

}
