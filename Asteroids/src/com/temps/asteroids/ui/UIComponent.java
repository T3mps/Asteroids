package com.temps.asteroids.ui;

import java.awt.Graphics2D;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temps.asteroids.Game;

public abstract class UIComponent {

	public Vector2f position;
	public int width, height;
	public boolean selected;
	protected int color;
	protected UIPanel panel;
	protected Game game;

	public UIComponent(Vector2f position, Game game) {
		this.position = position;
		this.game = game;
	}

	public UIComponent(Vector2f position, UIPanel panel, Game game) {
		this.position = position;
		this.panel = panel;
		this.game = game;
	}

	public abstract void tick(float deltaTime);

	public abstract void render(Graphics2D g2d);

}
