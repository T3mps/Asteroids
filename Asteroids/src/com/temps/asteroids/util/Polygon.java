package com.temps.asteroids.util;

import java.awt.Graphics2D;

import com.temprovich.mesa.maths.geom.Vector2f;

public class Polygon {

	public Vector2f position;
	private Vector2f[] points;
	public double rotation;

	public Polygon(Vector2f position, Vector2f[] points, double rotation) {
		this.position = position;
		this.points = points;
		this.rotation = rotation;

		Vector2f origin = points[0].clone();
		for (Vector2f p : points) {
			if (p.x < origin.x) origin.x = p.x;
			if (p.y < origin.y) origin.y = p.y;
		}

		for (Vector2f p : points) {
			p.x -= origin.x;
			p.y -= origin.y;
		}
	}

	public Vector2f[] getPoints() {
		Vector2f center = findCenter();
		Vector2f[] points = new Vector2f[this.points.length];
		for (int i = 0; i < this.points.length; i++) {
			Vector2f p = this.points[i];
			float x = (float) (((p.x - center.x) * Math.cos(Math.toRadians(rotation))) - ((p.y - center.y) * Math.sin(Math.toRadians(rotation))) + center.x / 2 + position.x);
			float y = (float) (((p.x - center.x) * Math.sin(Math.toRadians(rotation))) + ((p.y - center.y) * Math.cos(Math.toRadians(rotation))) + center.y / 2 + position.y);
			points[i] = new Vector2f(x, y);
		}
		return points;
	}

	public Vector2f[] getPointsRaw() {
		return points;
	}

	public boolean contains(Vector2f point) {
		Vector2f[] points = getPoints();
		double crossingNumber = 0;
		for (int i = 0, j = 1; i < this.points.length; i++, j = (j + 1) % this.points.length) {
			if ((((points[i].x < point.x) && (point.x <= points[j].x)) || ((points[j].x < point.x) && (point.x <= points[i].x))) && (point.y > points[i].y + (points[j].y - points[i].y) / (points[j].x - points[i].x) * (point.x - points[i].x))) {
				crossingNumber++;
			}
		}
		return crossingNumber % 2 == 1;
	}

	public void rotate(int degrees) {
		rotation = (rotation + degrees) % 360;
	}

	private double findArea() {
		double sum = 0;
		for (int i = 0, j = 1; i < points.length; i++, j = (j + 1) % points.length) {
			sum += points[i].x * points[j].y - points[j].x * points[i].y;
		}
		return Math.abs(sum / 2);
	}

	private Vector2f findCenter() {
		Vector2f sum = new Vector2f(0, 0);
		for (int i = 0, j = 1; i < points.length; i++, j = (j + 1) % points.length) {
			sum.x += (points[i].x + points[j].x) * (points[i].x * points[j].y - points[j].x * points[i].y);
			sum.y += (points[i].y + points[j].y) * (points[i].x * points[j].y - points[j].x * points[i].y);
		}
		double area = findArea();
		return new Vector2f((float) (Math.abs(sum.x / (6 * area))), (float) (Math.abs(sum.y / (6 * area))));
	}

	public void render(Graphics2D g2d) {
		int[] xPoint = new int[points.length];
		int[] yPoint = new int[points.length];
		int segments = points.length;
		Vector2f[] points = getPoints();
		for (int i = 0; i < this.points.length; i++) {
			xPoint[i] = (int) points[i].x;
			yPoint[i] = (int) points[i].y;
		}
		g2d.drawPolygon(xPoint, yPoint, segments);
	}

	public boolean intersection(Polygon other) {
		Vector2f[] array = other.getPoints();
		int intersection = 0;
		boolean output = false;
		for (int i = 0; i < array.length; i++) {
			if (this.contains(array[i])) {
				intersection++;
			}
		}
		if (intersection > 0) {
			output = true;
		}
		return output;
	}

}