package com.temps.asteroids.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temps.asteroids.Game;

public class UIPanel extends UIComponent {

	private boolean show;
	private int alpha;

	public List<UIComponent> components = new ArrayList<UIComponent>();

	public UIPanel(Vector2f position, int width, int height, boolean show, int color, int alpha, Game game) {
		super(position, game);
		this.width = width;
		this.height = height;
		this.show = show;
		this.color = color;
		this.alpha = alpha;
	}

	public UIPanel add(UIComponent c) {
		c.position = new Vector2f(this.position.x + c.position.x, this.position.y + c.position.y);
		components.add(c);
		return this;
	}

	public void tick(float deltaTime) {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).tick(deltaTime);
		}
	}

	public void render(Graphics2D g2d) {
		if (show) {
			int r = (color & 0xFF0000) >> 16;
			int g = (color & 0xFF00) >> 8;
			int b = (color & 0xFF);

			g2d.setColor(new Color(r, g, b, alpha));
			g2d.fillRect((int) position.x, (int) position.y, width, height);
		}
		for (int i = 0; i < components.size(); i++) {
			components.get(i).render(g2d);
		}
	}

}
