package com.temps.asteroids.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class UIManager {

	public List<UIPanel> panels = new ArrayList<UIPanel>();

	public UIManager() {
	}

	public void tick(float deltaTime) {
		for (int i = 0; i < panels.size(); i++) {
			panels.get(i).tick(deltaTime);
		}
	}

	public UIManager add(UIPanel p) {
		panels.add(p);
		return this;
	}

	public void render(Graphics2D g2d) {
		for (int i = 0; i < panels.size(); i++) {
			panels.get(i).render(g2d);
		}
	}
}
