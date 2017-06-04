package library;
import java.util.ArrayList;

import processing.core.PConstants;

public class Plot implements PConstants{
	private int constant;
    protected Label trait;
    protected float scale;
    protected Axis xAxis, yAxis;
    protected ArrayList<Point> points;
    protected float x, y, width, height;
    private static float marginX = 3, marginY = 3;
    
    protected ProcessFile pF;
    //public Axis(float marginX, float marginY, int x1, int y1, int x2, int y2, int angle){
    
    public Plot(int c, float x, float y, float w, float h){
    	constant = c;
    	trait = new Label("", (float)(.75*(x+w)), (float)(.75*(y+h)));
    	width = w;
    	height = h;
    	points = new ArrayList<Point>();
    	xAxis = new Axis(marginX, marginY, x, y, x+width, y, 0);
    	yAxis = new Axis(marginX, marginY, x, y, x, y+height, PConstants.PI/2);
    }
    
   /* public Plot(int c, float x, float y, float w, float h, Label t){
    	this(c, x, y, w, h);
    	trait = t;
    }*/
    
    public void addPoint(Point p){
    	points.add(p);
    }
    
    public void addPoints(){
    	while (pF.hasNext()){
    		points.add(new Point(pF.getXPosn(), pF.getLogP()));
    		pF.advanceIndex();
    	}
    	System.out.println("added points");
    }
     
    
    
}
