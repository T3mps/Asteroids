package com.temps.asteroids.entity.particle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temps.asteroids.Game;
import com.temps.asteroids.level.Level;

public class DeathParticle extends Particle {

	public DeathParticle(Vector2f position, float maxLife, int color, float initialAngle, ParticleSystem ps, Level level) {
		super(position, maxLife, color, initialAngle, ps, level);
		this.width = 2;
		this.height = 2;
		this.life = 16 + random.nextInt(4);
	}

	public void tick(float deltaTime) {
		life++;
		if (maxLife >= life) dead = true;
		move();
	}

	protected void move() {
		position = new Vector2f((float) (position.x + 2 * (Math.sin(Math.toRadians(angle)))), (float) (position.y + 2 * (Math.cos(Math.toRadians(angle)))));
	}

	public void render(Graphics2D g2d) {
		g2d.setColor(new Color(color));
		g2d.fillRect((int) position.x, (int) position.y, width, height);
	}

	public Rectangle getBoundingBox() {
		return new Rectangle((int) position.x, (int) position.y, width, height);
	}

}
