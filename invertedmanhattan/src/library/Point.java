package library;

public class Point {
	protected float xRel, yRel;
	private int r, g, b;
	private float radius;
	private Label label;
	protected int constant;
	
	public Point(float x, float y){
		xRel = x;
		yRel = y;
		radius = 3;
		constant = 1;
	}
	
	public Point(float x, float y, int red, int green, int blue, int size, int c){
		this(x, y);
		r = red;
		g = green;
		b = blue;
		constant = c;
	}
	
	public Point(float x, float y, int red, int green, int blue, int size, int c, Label name){
		this(x, y, red, green, blue, size, c);
		label = name;
	}
	
	public int[] getColor(){
		int[] colors = {r, g, b};
		return colors;
	}
	
	public float getRadius(){
		return radius;
	}
	public void setRadius (float r){
		radius = r;
	}
}
