package com.bb1.tub.api.world;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public final class Location {
	
	private final World world;
	private final double x;
	private final double y;
	private final double z;
	
	public Location(World world, double x, double y, double z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Location(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	public World getWorld() {
		return world;
	}
	
	public Block getBlock() {
		return this.world.getBlockAt(this);
	}
	
	public Chunk getChunk() {
		return this.world.getChunk(getChunkCoordOf(this.x), getChunkCoordOf(this.z));
	}
	
	private static int getChunkCoordOf(double coord) {
		int floor = (int) coord;
		int value = floor == coord ? floor : floor - (int) (Double.doubleToRawLongBits(coord) >>> 63);
		return value >> 4;
	}
	
	@Override
	public String toString() {
		return new GsonBuilder().create().toJson(toJsonObject());
	}
	
	public JsonObject toJsonObject() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("world", getWorld().getWorldName());
		jsonObject.addProperty("x", getX());
		jsonObject.addProperty("y", getY());
		jsonObject.addProperty("z", getZ());
		return jsonObject;
	}
	
}
