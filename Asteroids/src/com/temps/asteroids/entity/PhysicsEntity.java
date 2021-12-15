package com.temps.asteroids.entity;

import java.awt.Graphics2D;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temps.asteroids.level.Level;

public abstract class PhysicsEntity extends Entity {

	protected int width, height;
	protected float acceleration;
	protected float angle;

	public PhysicsEntity(Vector2f position, Level level) {
		super(position);
		init(level);
	}

	public PhysicsEntity(Level level) {
		init(level);
	}

	public abstract void tick(float deltaTime);

	protected abstract void move();

	public abstract void render(Graphics2D g2d);

	public boolean collision() {
		for (int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if (e != this && e.getBoundingBox().intersects(this.getBoundingBox())) return true;
		}

		return false;
	}

}
