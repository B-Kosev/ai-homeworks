package main.java.homework3;

public class City {
	private int id;
	private String name;
	private double x;
	private double y;

	public City(int id, double x, double y, String name) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ID: " + id + " - " + name + " - " + "(" + x + ", " + y + ")";
	}
}
