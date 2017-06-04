package library;

import processing.core.PFont;

public class Label {
	protected String name;
	protected float angle;
	protected float x, y;
	protected boolean visible;
	protected PFont font;
	protected int size;
	
	public Label(String n, float x, float y){
		name = n;
		this.x = x;
		this.y = y;
		visible = true;
		size = 8;
	}
	
	public void setSize(int s){
		size = s;
	}
	
	public void toggleVisibility(){
		visible = !visible;
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void setFont(PFont f){
		font = f;
	}
}
