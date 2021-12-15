package com.temps.asteroids;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import com.temprovich.mesa.maths.geom.Vector2f;
import com.temps.asteroids.entity.Player;
import com.temps.asteroids.input.InputHandler;
import com.temps.asteroids.level.Level;
import com.temps.asteroids.ui.UIManager;
import com.temps.asteroids.ui.UIPanel;
import com.temps.asteroids.ui.UIText;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int SCALE = 1;
	private static final String TITLE = "Asteroids";
	private static final int FONT_CONSTANT = 512;

	int fps;
	int tps;

	private Thread thread;
	private JFrame frame;
	private Graphics2D screen;
	private InputHandler input;
	private Level level;
	private Starfield starfield;
	public GameState state;
	private UIManager ui;
	private UIPanel menuPanel;

	public UIText uiScore;
	public UIText uiHighscore;

	private Font[] r_OpenSans;
	private Font[] r_SourceCodePro;
	private Font[] r_VT323;
	private Font[] r_Hyperspace;
	private volatile boolean running = false;

	public Game() {
		Dimension viewPort = new Dimension(WIDTH, HEIGHT);
		setMinimumSize(viewPort);
		setMaximumSize(viewPort);
		setPreferredSize(viewPort);

		this.frame = new JFrame(TITLE);
		this.input = new InputHandler();
		this.level = new Level(WIDTH, HEIGHT);
		this.state = GameState.MENU; // change to intro

		this.r_OpenSans = new Font[FONT_CONSTANT];
		this.r_SourceCodePro = new Font[FONT_CONSTANT];
		this.r_VT323 = new Font[FONT_CONSTANT];
		this.r_Hyperspace = new Font[FONT_CONSTANT];

		try {
			for (int i = 0; i < FONT_CONSTANT; i++) {
				r_OpenSans[i] = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/open_sans_r.ttf")).deriveFont((float) i);
				r_SourceCodePro[i] = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/source_code_pro_r.ttf")).deriveFont((float) i);
				r_VT323[i] = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/VT323_r.ttf")).deriveFont((float) i);
				r_Hyperspace[i] = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Hyperspace.ttf")).deriveFont((float) i);
			}
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		this.ui = new UIManager().add(this.menuPanel = new UIPanel(new Vector2f(0, 0), WIDTH, HEIGHT, true, 0, 0, this));
		this.menuPanel.add(uiScore = new UIText(new Vector2f(8, 32), "00", getFont("r_Hyperspace", 32), 0xffffff, menuPanel, this));
		uiHighscore = new UIText(new Vector2f(Game.WIDTH - 45, 32), "00", getFont("r_Hyperspace", 32), 0xffffff, menuPanel, this);
//		this.menuPanel.add(uiHighscore = new UIText(new Vector2f(Game.WIDTH - 45, 32), "00", getFont("r_Hyperspace", 32), 0xffffff, menuPanel, this));
		this.starfield = new Starfield(48);

		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		addMouseWheelListener(input);
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public void run() {
		this.requestFocus();
		boolean render = false;
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		float deltaTime = 0;
		long timer = System.currentTimeMillis();
		int ticks = 0;
		int frames = 0;

		while (running) {
			render = false;
			long now = System.nanoTime();
			deltaTime += (now - lastTime) / ns;
			lastTime = now;

			while (deltaTime >= 1) {
				render = true;
				tick(deltaTime);
				ticks++;
				deltaTime--;
			}

			if (render) {
				render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				fps = frames;
				tps = ticks;

				frames = 0;
				ticks = 0;
			}
		}
		stop();
	}

	private void tick(float deltaTime) {
		if (state == GameState.GAME || state == GameState.MENU) level.tick(deltaTime);
		ui.tick(deltaTime);
		input.tick();

		if (state == GameState.MENU && input.isKey(KeyEvent.VK_SPACE)) {
			level.entities.add(new Player(new Vector2f((WIDTH / 2) - 12, (HEIGHT / 2) - 12), level, this));
			state = GameState.GAME;
		}
	}

	int blink_timer = 0;
	int delay_timer = 0;

	private void render() {
		if (getBufferStrategy() == null) {
			createBufferStrategy(3);
			return;
		}

		screen = (Graphics2D) getBufferStrategy().getDrawGraphics();

		screen.setColor(Color.black);
		screen.fillRect(0, 0, WIDTH, HEIGHT);

		FontMetrics metrics16 = screen.getFontMetrics(getFont("r_Hyperspace", 16));
		String topText = "00";
		String copyText = "©1979 atari inc";
		String blinkText = "push space";

		if (state == GameState.MENU) {
			if (delay_timer < 100) delay_timer++;
			else delay_timer = 100;
			if (blink_timer < 32 && delay_timer == 100) blink_timer++;
			else blink_timer = 0;
			FontMetrics metrics32 = screen.getFontMetrics(getFont("r_Hyperspace", 32));
			String playText = "1 coin 1 play";
			screen.setFont(getFont("r_Hyperspace", 32));
			screen.setColor((blink_timer <= 16) ? Color.black : Color.white);
			screen.drawString(blinkText, (WIDTH - metrics32.stringWidth(blinkText)) / 2, HEIGHT * 0.25f);
			screen.setColor(Color.white);
			screen.drawString(playText, (WIDTH - metrics32.stringWidth(playText)) / 2, HEIGHT * 0.80f);
		}

		if (state == GameState.GAME || state == GameState.MENU) {
			starfield.render(screen);
			level.render(screen);
			screen.setColor(Color.white);
			screen.setFont(getFont("r_Hyperspace", 16));
			screen.drawString(topText, (WIDTH - metrics16.stringWidth(topText)) / 2, 16);
			screen.drawString(copyText, (WIDTH - metrics16.stringWidth(copyText)) / 2, HEIGHT * 0.90f);
			ui.render(screen);
		}

		screen.dispose();
		getBufferStrategy().show();
	}

	public Font getFont(String fName, int size) {
		switch (fName) {
			case "r_OpenSans":
				return r_OpenSans[size];
			case "r_SourceCodePro":
				return r_SourceCodePro[size];
			case "r_VT323":
				return r_VT323[size];
			case "r_Hyperspace":
				return r_Hyperspace[size];
			default:
				return r_Hyperspace[size];
		}
	}

	public InputHandler getInput() {
		return input;
	}

	public static void main(String[] args) {
		Game game = new Game();

		game.frame.setResizable(false);
		game.frame.setTitle(TITLE);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
	}
}