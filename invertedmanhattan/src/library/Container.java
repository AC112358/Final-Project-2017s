package library;

import java.io.IOException;

import processing.core.PApplet;

public class Container {
	private PApplet parent;
    private float width, height;
    private float x, y;
    private Plot up, down;
    private int bgColor;
    private float xScale;
    private float yScale;
    private boolean drawLabels;
    private Label title, middleLabel;
    private float middleLabelMargin = 5;
    
    
    public Container(PApplet p, float w, float h, float x, float y, String fName1, String fName2) throws IOException{
    	parent = p;
    	
    	width = w;
    	height = h;
    	this.x = x;
    	this.y = y;
    	
    	xScale = 1;
    	yScale = 1;
    	
    	middleLabel = new Label("-log(p)", 0, 0);
    	middleLabel.angle = (float)(Math.PI);
    	
    	title = new Label("Inverted Manhattan plot", (x+w)/2, y);
    	
    	up = new Plot(1, x, y, width, height/2);
    	down = new Plot(-1, x, y+h, width, height/2);
    	up.pF = new ProcessFile(fName1);
    	down.pF = new ProcessFile(fName2);
    	down.xAxis.isVisible = false;
    	up.xAxis.ticks = ProcessFile.fullLengths;
    	down.xAxis.ticks = up.xAxis.ticks;
    	
    	String[] chrNames = chromosomeNames();
    	
    	up.yAxis.setNTicks(10);
    	down.yAxis.setNTicks(10);
    	
    	up.xAxis.setTickNames(chrNames);
    	down.xAxis.setTickNames(chrNames);
    	
    	
    	makeMiddleLabel();
    	setScales();
    	setUpPoints();
    }
    
    public String[] chromosomeNames(){
    	String[] chrNames = new String[25];
    	for (int i = 0; i < 22; i++){
    		chrNames[i] = "" + (i+1);
    	}
    	chrNames[22] = "X";
    	chrNames[23] = "Y";
    	
    	return chrNames;
    }
    
    public void rejectValWithProb(float reject, float prob) throws IOException{
    	up.pF = new ProcessFile(up.pF.name, reject, prob);
    	down.pF = new ProcessFile(down.pF.name, reject, prob);
    }
   
    public void setUpPoints(){
    	up.addPoints();
    	down.addPoints();
    }
    
    public boolean setPointColors(int[] colors){
    	if (colors.length != ProcessFile.chromosomeLengths.length){
    		return false;
    	}
    	for (Point p : down.points){
    		p.color = colors[p.chromosome-1];
    	}
    	for (Point p : up.points){
    		p.color = colors[p.chromosome-1];
    	}
    	return true;
    }
    
    
    public void setLowerTraitName(String name){
    	down.trait.name = name;
    }
    
    public void setUpperTraitName(String name){
    	up.trait.name = name;
    }
    
    public void moveTrait(float x, float y){
    	down.trait.x = relX(x);
    	down.trait.y = relY(-1 * y);
    	
    	up.trait.x = relX(x);
    	up.trait.y = relY(y);
    }
    
    
    private void makeMiddleLabel(){
		float xLocn = 0;
		float rot = middleLabel.angle;
		if (x - middleLabelMargin < 0){
			xLocn = 0;
			if (x == 0){
				xLocn = x + width + middleLabelMargin;
				rot = (float)(Math.PI + rot);
			}
		}
		middleLabel.angle = rot;
		middleLabel.x = xLocn;
		middleLabel.y = relY(0);
    }
    
    private void setScales(){
    	double totalX = ProcessFile.fullLengths[ProcessFile.fullLengths.length - 1];
    	xScale = (float) (width/totalX);
    	
    	double totalY = Math.max(up.pF.maxYCor(), down.pF.maxYCor());
    	if (totalY != 0){
    		yScale = (float) (height/totalY);
    	}
    }
    
    private float relY(float addY){
    	return (y+height)/2 - yScale*addY;
    }
    
    private float relX(float addX){
    	return x + xScale*addX;
    }
    
    public void drawPoints(){
    	parent.pushMatrix();
    	for (Point p : up.points){
    		int color = p.getColor();
    		parent.fill(color);
    		parent.ellipse(relX(p.xRel) , relY(p.constant * p.yRel), p.getRadius(), p.getRadius());
    	}	
    	parent.popMatrix();
    }
    
    
    public void drawAxis(Axis axis){
    	if (axis.isVisible){
	    	parent.fill(0);
	    	parent.line(axis.x, axis.y, axis.maxX, axis.maxY);
	    	for (float xVal : axis.ticks){
	        	parent.pushMatrix();
	        	parent.fill(0);
	        	parent.translate(relX(xVal), relY(axis.tickLen/2));
	        	parent.rotate(axis.angle);
	    		parent.line(relX(xVal), relY(0), relX(xVal), relY(axis.tickLen));
	    		parent.popMatrix();
	    	}
    	}
    }
    public void drawAxes(){
    	parent.pushMatrix();
    	parent.fill(0);
    	drawAxis(up.xAxis);
    	drawAxis(up.yAxis);
    	drawAxis(down.xAxis);
    	drawAxis(down.yAxis);
    	parent.popMatrix();
    }
    
    public void drawLabels(){
    	drawLabel(middleLabel);
    	drawLabel(title);
    	drawLabel(up.xAxis.name);
    	drawLabel(up.yAxis.name);
    	drawLabel(up.trait);
    	drawLabel(down.xAxis.name);
    	drawLabel(down.yAxis.name);
    	drawLabel(down.trait);
    	for (Label l : up.xAxis.tickNames){
    		drawLabel(l);
    	}
    	for (Label l : down.xAxis.tickNames){
    		drawLabel(l);
    	}
    	for (Label l : up.yAxis.tickNames){
    		drawLabel(l);
    	}
    	for (Label l : down.yAxis.tickNames){
    		drawLabel(l);
    	}
    }
    
    private void drawLabel(Label label){
    	if (label.isVisible() && label.name != null){
	    	parent.pushMatrix();
	    	parent.translate(label.x, label.y);
	    	parent.rotate(label.angle);
	    	parent.textSize(label.size);
	    	if (label.font != null){
	    		parent.textFont(label.font);
	    	}
	    	//System.out.println(label + " " + label.name + " " + label.x + " " + label.y);
	    	parent.text(label.name, label.x, label.y);
	    	parent.popMatrix();
    	}
    }
    
    public void drawVertLines(){
    	for (float xVal : up.xAxis.ticks){
    		parent.line(relX(xVal), -height, relX(xVal), relY(up.xAxis.tickLen));
    	}
    }
    
    public void drawVertLines(int color){
    	parent.pushMatrix();
    	parent.fill(color);
    	drawVertLines();
    	parent.popMatrix();
    }
    
    
    public void drawHorizLine(int yVal){
    	parent.line(x, relY(yVal), x+width, relY(yVal));
    }
    
    public void drawHorizLine(int y, int color){
    	parent.pushMatrix();
    	parent.fill(color);
    	drawHorizLine(y);
    	parent.popMatrix();
    }
    
    public void setBGColor(int color){
    	bgColor = color;
    }
    
    public int getBGColor(){
    	return bgColor;
    }
    
    public void drawPlot(){
    	drawVertLines();
    	drawLabels();
    	drawAxes();
    	drawPoints();
    }
}
