package se.joakimbergman.crater;

public class Column {
	private int width;
	private String name;
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Column(String name, int width) {
		this.name = name;
		this.width = width;
	}
}
