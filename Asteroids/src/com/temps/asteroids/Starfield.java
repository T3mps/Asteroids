package com.temps.asteroids;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.temprovich.mesa.maths.Maths;
import com.temprovich.mesa.maths.geom.Vector2f;

public class Starfield {

	private int starfieldWidth = Game.WIDTH;
	private int starfieldHeight = Game.HEIGHT;

	private List<Vector2f> starPositions = new LinkedList<Vector2f>();
	private List<Integer> starSizes = new ArrayList<Integer>();

	public Starfield(int amount) {
		for (int i = 0; i < amount; i++) {
			this.starPositions.add(new Vector2f(Maths.random(0, starfieldWidth), Maths.random(0, starfieldHeight)));
			this.starSizes.add(-1);
		}
	}

	public void render(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		int rng = -1;
		int index = 0;
		for (Vector2f v : starPositions) {
			rng = Maths.random(100);
			if (rng == 1) starSizes.set(index, 2);
			else if (rng == 2) starSizes.set(index, 2);
			else if (rng == 4) starSizes.set(index, 2);
			else if (rng == 8) starSizes.set(index, 2);
			else if (rng == 16) starSizes.set(index, 2);
			else if (rng == 32) starSizes.set(index, 2);
			else if (rng == 64) starSizes.set(index, 2);
			else starSizes.set(index, 1);

			g2d.fillRect((int) v.x, (int) v.y, starSizes.get(index), starSizes.get(index));
			index++;
		}
	}

}
