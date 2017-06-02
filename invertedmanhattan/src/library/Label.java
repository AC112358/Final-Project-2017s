package library;

import processing.core.PApplet;
import processing.core.PFont;

public class Label {
	PApplet parent;
	private String name;
	private float rotation;
	private float x, y;
	private boolean visible;
	private PFont font;
	
	public Label(PApplet p, String n, float x, float y){
		parent = p;
		name = n;
		this.x = x;
		this.y = y;
	}
}
