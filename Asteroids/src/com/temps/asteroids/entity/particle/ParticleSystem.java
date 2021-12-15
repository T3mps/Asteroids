package com.temps.asteroids.entity.particle;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.temprovich.mesa.maths.Maths;
import com.temprovich.mesa.maths.geom.Vector2f;
import com.temprovich.mesa.maths.random.Xorshift128;
import com.temps.asteroids.entity.Entity;
import com.temps.asteroids.level.Level;

public class ParticleSystem extends Entity {

	protected float life;
	protected int spawningRate;
	protected int amount;
	protected int particleColor;
	protected float particleLife;
	protected int rate;

	protected ParticleSystemType type;
	protected ParticleType particleType;
	protected Random random;

	private List<Particle> particles = new LinkedList<Particle>();

	public ParticleSystem(Vector2f position, float life, int spawingRate, int amount, int particleColor, float particleLife, Level level, ParticleType particleType) {
		super(position);
		this.life = life;
		this.spawningRate = spawingRate;
		this.amount = amount;
		this.particleColor = particleColor;
		this.particleLife = particleLife;
		this.particleType = particleType;
		this.type = ParticleSystemType.CONTINUAL;
		this.random = new Xorshift128();

		init(level);

		for (int i = 0; i < amount; i++) {
			if (particleType == ParticleType.DEATH_PARTICLE) {
				particles.add(new DeathParticle(position, random.nextInt((int) particleLife) + 100, particleColor, (float) Math.toDegrees(Maths.randomSign() * random.nextInt(360)), this, level));
			}
		}
	}

	public ParticleSystem(Vector2f position, int amount, int particleColor, float particleLife, Level level, ParticleType particleType) {
		super(position);
		this.amount = amount;
		this.particleType = particleType;
		this.type = ParticleSystemType.SINGLETON;
		this.random = new Xorshift128();

		init(level);

		for (int i = 0; i < amount; i++) {
			if (particleType == ParticleType.DEATH_PARTICLE) {
				particles.add(new DeathParticle(position, random.nextInt((int) particleLife) + 100, particleColor, (float) Math.toDegrees(Maths.randomSign() * random.nextInt(360)), this, level));
			}
		}
	}

	public void tick(float deltaTime) {
		if (particles.isEmpty() && type == ParticleSystemType.SINGLETON) dead = true;
		if (type == ParticleSystemType.CONTINUAL) {
			rate -= deltaTime;

			if (rate <= 0) {
				for (int i = 0; i < amount; i++) {
					if (particleType == ParticleType.DEATH_PARTICLE) {
						particles.add(new DeathParticle(position, random.nextInt((int) particleLife) + 100, particleColor, (float) Math.toDegrees(Maths.randomSign() * random.nextInt(360)), this, level));
					}
				}
				rate += (spawningRate);
			}
		}

		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).tick(deltaTime);
		}

		removalCheck();
	}

	private void removalCheck() {
		for (int i = 0; i < particles.size(); i++) {
			if (particles.get(i).isDead()) particles.remove(i);
		}
	}

	public void render(Graphics2D g2d) {
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).render(g2d);
		}
	}

	public Rectangle getBoundingBox() {
		return new Rectangle();
	}

}
