package library;

import java.io.IOException;

import processing.core.PApplet;
import processing.core.PConstants;

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
    	
    	title = new Label("Inverted Manhattan plot", x, y);//0, 0);//x + w/2, y);
    	
    	up = new Plot(1, x, y, width, height/2);
    	down = new Plot(-1, x, y + height/2, width, height/2);
    	up.pF = new ProcessFile(fName1);
    	down.pF = new ProcessFile(fName2);
    	//down.xAxis.isVisible = false;
    	up.xAxis.setTicks(ProcessFile.fullLengths);
    	down.xAxis.setTicks(ProcessFile.fullLengths);
    	//down.yAxis.isVisible = false;
    	
    	String[] chrNames = chromosomeNames();
    	
    	up.yAxis.setNTicks(10);
    	down.yAxis.setNTicks(10);
    	
    	up.xAxis.setTickNames(chrNames);
    	down.xAxis.setTickNames(chrNames);
    	up.trait.name = "Trait 1";
    	down.trait.name = "Trait 2";
    	
    	
    	bgColor = 209;
    	
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
    		yScale = (float) (.5*height/totalY);
    	}
    }
    
    private float relY(float addY){
    	return y + height/2 - yScale*addY;
    }
    
    private float relX(float addX){
    	return x + xScale*addX;
    }
    
    private void drawPoints(Plot plt){
    	parent.pushMatrix();
    	parent.noStroke();
    	for (Point p : plt.points){
    		int color = p.getColor();
    		parent.fill(color);
    		parent.ellipse(relX(p.xRel), relY(p.constant * p.yRel), p.getRadius(), p.getRadius());
    	}	
    	parent.popMatrix();
    }
    
    public void drawPoints(){
    	drawPoints(up);
    	drawPoints(down);
    }
    
    
    public void drawAxis(Axis axis){
    	if (axis.isVisible){
	    	parent.fill(0);
	    	parent.line(axis.x, axis.y, axis.maxX, axis.maxY);
	    	for (float xVal : axis.getTicks()){
	        	parent.pushMatrix();
	        	parent.fill(0);
	        	parent.translate(xVal, relX(axis.tickLen/2));
	        	parent.rotate(axis.angle + PConstants.PI/2);
	        	if (axis.uniformTicks){
	        		parent.line(xVal, relY(0), xVal, relY(axis.tickLen));
	        	}else{
	        		parent.line(relX(xVal), relY(0), relX(xVal), relY(axis.tickLen));
	        	}
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
    		drawLabel(l, up.xAxis.uniformTicks);
    	}
    	for (Label l : down.xAxis.tickNames){
    		drawLabel(l, down.xAxis.uniformTicks);
    	}
    	for (Label l : up.yAxis.tickNames){
    		drawLabel(l, up.yAxis.uniformTicks);
    	}
    	for (Label l : down.yAxis.tickNames){
    		drawLabel(l, down.yAxis.uniformTicks);
    	}
    }
    
    private void drawLabel(Label label, boolean uniform){
    	if (label.isVisible() && label.name != null){
	    	parent.pushMatrix();
	    	parent.translate(label.x, label.y);
	    	parent.rotate(label.angle);
	    	parent.textSize(label.size);
	    	if (label.font != null){
	    		parent.textFont(label.font);
	    	}
	    	//System.out.println(label + " " + label.name + " " + label.x + " " + label.y);
	    	if (uniform){
	    		parent.text(label.name, label.x, label.y);
	    	}else{
	    		parent.text(label.name, relX(label.x), relY(label.y));
	    	}
	    	parent.popMatrix();
    	}
    }
    
    private void drawLabel(Label label){
    	drawLabel(label, true);
    }
    
   
    
    public void drawVertLines(){
    	for (float xVal : up.xAxis.getTicks()){
    		parent.line(relX(xVal), y + height, relX(xVal), y);
    	}
    }
    
    public void drawVertLines(int color){
    	parent.pushMatrix();
    	parent.stroke(color);
    	drawVertLines();
    	parent.popMatrix();
    }
    
    
    public void drawHorizLine(float yVal){
    	parent.line(x, relY(yVal), x + width, relY(yVal));
    }
    
    public void drawHorizLine(float y, int color){
    	parent.pushMatrix();
    	parent.stroke(color);
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
    	parent.pushMatrix();
    	parent.fill(bgColor);
    	parent.noStroke();
    	parent.rect(x, y, x + width, y + height);
    	parent.stroke(0);
    	
    	drawVertLines(255);
    	parent.fill(0);
    	drawLabels();
    	
    	parent.stroke(0);
    	drawAxes();
    	drawPoints();
    	parent.popMatrix();
    }
}
