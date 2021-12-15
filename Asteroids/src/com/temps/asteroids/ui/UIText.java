package com.temps.asteroids.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temps.asteroids.Game;

public class UIText extends UIComponent {

	private String text;
	private Font font;

	public UIText(Vector2f position, String text, Font font, int color, UIPanel panel, Game game) {
		super(position, panel, game);
		this.text = text;
		this.color = color;
		this.font = font;
	}

	public void tick(float deltaTime) {
	}

	public void setText(String text) {
		this.text = text;
	}

	public void render(Graphics2D g2d) {
		g2d.setColor(new Color(color));
		g2d.setFont(font);
		g2d.drawString(text, (int) position.x, (int) position.y);
	}

}