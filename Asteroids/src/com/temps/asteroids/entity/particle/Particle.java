package com.temps.asteroids.entity.particle;

import java.util.Random;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temprovich.mesa.maths.random.Xorshift128;
import com.temps.asteroids.entity.PhysicsEntity;
import com.temps.asteroids.level.Level;

public abstract class Particle extends PhysicsEntity {

	protected float angle;
	protected float life;
	protected float maxLife;
	protected int color;

	protected ParticleSystem ps;
	protected Random random;

	public Particle(Vector2f position, float maxLife, int color, float initialAngle, ParticleSystem ps, Level level) {
		super(position, level);
		this.maxLife = maxLife;
		this.color = color;
		this.angle = initialAngle;
		this.ps = ps;
		this.random = new Xorshift128();
		init(level);
	}

}
