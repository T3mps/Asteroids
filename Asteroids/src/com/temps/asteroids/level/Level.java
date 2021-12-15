package com.temps.asteroids.level;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temprovich.mesa.maths.random.Xorshift128;
import com.temps.asteroids.Game;
import com.temps.asteroids.entity.Asteroid;
import com.temps.asteroids.entity.Entity;

public class Level {

	public int width, height;

	public List<Entity> entities = new ArrayList<Entity>();
	private Random random = new Xorshift128();

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void tick(float deltaTime) {
		if (getNumberOfAsteroids(entities) < 7) {
			int rng = random.nextInt(4);
			entities.add(new Asteroid(new Vector2f((rng == 0 || rng == 1 ? 0 : Game.WIDTH), (rng == 0 || rng == 1 ? 0 : Game.HEIGHT)), this, false));
		}

		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick(deltaTime);
		}
		removalCheck();
	}

	private void removalCheck() {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).isDead()) entities.remove(i);
		}
	}

	public int getNumberOfAsteroids(List<Entity> list) {
		int count = 0;
		Map<Entity, Integer> map = new HashMap<Entity, Integer>();

		for (Entity i : list) {
			Integer j = map.get(i);
			map.put(i, (j == null) ? 1 : j + 1);
		}

		for (Map.Entry<Entity, Integer> value : map.entrySet()) {
			if (value.getKey() instanceof Asteroid) count++;
		}
		return count;
	}

	public void render(Graphics2D g2d) {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render(g2d);
		}
	}

}
