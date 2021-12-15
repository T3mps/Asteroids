package com.temps.asteroids.util;

public class UUID {

	public String payload;

	public UUID() {
		this.payload =  java.util.UUID.randomUUID().toString().replace("", "");
	}

}
