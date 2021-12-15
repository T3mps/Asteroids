package com.temps.asteroids.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temps.asteroids.Game;

public class UITextField extends UIComponent {

	private String text;
	private Font font;

	public UITextField(Vector2f position, String defaultText, Font font, int color, UIPanel panel, Game game) {
		super(position, panel, game);
		this.text = defaultText;
		this.font = font;
		this.color = color;
	}

	public void tick(float deltaTime) {
		if (game.getInput().isKeyTyped()) {
			if (text.length() > 0 && game.getInput().getKeyTyped() == KeyEvent.VK_BACK_SPACE) text = text.substring(0, text.length() - 1);
			else if (game.getInput().getKeyTyped() != KeyEvent.VK_BACK_SPACE && game.getInput().getKeyTyped() != KeyEvent.VK_ESCAPE && game.getInput().getKeyTyped() != KeyEvent.VK_DELETE) text += game.getInput().getKeyTyped();
		}
	}

	public void render(Graphics2D g2d) {
		g2d.setColor(new Color(color));
		g2d.setFont(font);
		g2d.drawString(text, (int) position.x, (int) position.y);
	}

}
