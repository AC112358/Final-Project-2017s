package library;
import java.util.ArrayList;

import processing.core.*;

public class Plot implements PConstants{
	private PApplet parent;
	private int constant;
    private Label trait;
    private float scale;
    private Axis xAxis, yAxis;
    private ArrayList<Point> points;
    
    public Plot(PApplet parent, int c, Label t){
    	this.parent = parent;
    	constant = c;
    	trait = t;
    }
    
    public void addPoint(Point p){
    	points.add(p);
    }
    
    
    
}
