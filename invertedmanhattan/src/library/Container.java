package library;

import processing.core.PApplet;

public class Container {
	private PApplet parent;
    private float width, height;
    private float x, y;
    private Plot up, down;
    private int r, g, b;
    private float scale;
    private float maxHeight;
    private boolean drawLabels;
    private ProcessFile pF;
    
    
    public Container(PApplet p, float w, float h, float x, float y){
    	parent = p;
    	width = w;
    	height = h;
    	this.x = x;
    	this.y = y;
    }
    
    private float relY(float addY){
    	return (y+height)/2 - addY;
    }
    
    public void drawPoints(){
    	for (Point p : up.points){
    		int[] colors = p.getColor();
    		parent.fill(colors[0], colors[1], colors[2]);
    		parent.ellipse(x + p.xRel, relY(p.constant * p.yRel), p.getRadius(), p.getRadius());
    	}
    	
    }
}
