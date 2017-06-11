package library;

import java.io.IOException;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PConstants;

public class Container {
	private PApplet parent;
    private float width, height;
    private float x, y;
    protected Plot up, down;
    private int bgColor;
    private float xScale;
    private float yScale;
    //private boolean drawLabels;
    private Label title, middleLabel;
    private float middleLabelMargin = 5;
    private double totalY;
    private static int defaultMarkedColor = 0;
    
    public Container(PApplet p, float w, float h, float x, float y, String fName1, String fName2) throws IOException{
    	this(p, w, h, x, y, fName1, fName2, 0, 0, false, false);
    }
    
    public Container(PApplet p, float w, float h, float x, float y, String fName1, String fName2, boolean showErrorMsgs) throws IOException{
    	this(p, w, h, x, y, fName1, fName2, 0, 0, showErrorMsgs, false);
    }
    
    public Container(PApplet p, float w, float h, float x, float y, String fName1, String fName2, float reject, float prob, boolean showErrorMsgs, boolean sortGeneList) throws IOException{
    	parent = p;
    	
    	width = w;
    	height = h;
    	this.x = x;
    	this.y = y;
    	
    	xScale = 1;
    	yScale = 1;
    	
    	middleLabel = new Label("-log(p)", 0, 0, 1);
    	//middleLabel.angle = (float)(Math.PI);
    	
    	title = new Label("Inverted Manhattan plot", x + w/2, y, 1);//0, 0);//x + w/2, y);
    	title.size = 15;
    	
    	up = new Plot(1, x, y, width, height/2);
    	down = new Plot(-1, x, y + height/2, width, height/2);
    	up.pF = new ProcessFile(fName1, reject, prob, showErrorMsgs, sortGeneList);
    	down.pF = new ProcessFile(fName2, reject, prob, showErrorMsgs, sortGeneList);
    	//down.xAxis.isVisible = false;
    	up.xAxis.setTicks(ProcessFile.fullLengths);
    	down.xAxis.setTicks(ProcessFile.fullLengths);
    	
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
    	initPoints();
    	setHeight((float)(totalY*1.1));
    }
    
    private void moveXLabelsDown(Axis axis, float minDist){
    	float[] ticks = axis.getTicks();
    	if (ticks == null || ticks.length == 0){
    		return;
    	}
    	parent.pushStyle();
    	parent.textSize(8);
    	if (axis.tickNames != null && axis.tickNames.length > 0 && axis.tickNames[0] != null){
    		parent.textSize(axis.tickNames[0].size);
    	}
    	parent.stroke(0);
    	float addToY = Math.max((float) (0.03 * height), 9);
    	float xVal = 0;
    	float xVal2 = 0;
    	float xVal3 = 0;
    	boolean proceed = true;
    	
    	int len = Math.min(axis.tickNames.length, ticks.length) - 1;
    	
    	for (int i = 0; i < len; i += 2){
    		if (Math.abs(ticks[i+1] - ticks[i]) < minDist/xScale){
    			xVal = ticks[i+1];
    			if (!axis.uniformTicks){
    				xVal = relX(ticks[i+1]);
    			}
    			xVal2 = ticks[i];
    			if (!axis.uniformTicks){
    				xVal2 = relX(xVal2);
    			}
    			if (i + 2 < len){
	    			xVal3 = ticks[i+2];
	    			if (!axis.uniformTicks){
	    				xVal3 = relX(xVal3);
	    			}
    			}
    			
    			
    			proceed = xVal2 + parent.textWidth(axis.tickNames[i].name)/2 < xVal;
    			//System.out.println((xVal2 + parent.textWidth(axis.tickNames[i].name)) + " " + xVal);
    			proceed = proceed && (i + 2 >= len || xVal3 + parent.textWidth(axis.tickNames[i+2].name)/2 > xVal);
    			if (proceed){
		        	parent.line(xVal, axis.y, xVal, axis.y + addToY);
    			}
        		axis.tickNames[i+1].y += addToY;
    		}
    		
    	}
    	parent.popStyle();
    }
    
    public void moveXLabelsDown(){
    	moveXLabelsDown(up.xAxis);
    	moveXLabelsDown(down.xAxis);
    }
    
    public void moveXLabelsDown(Axis axis){
    	moveXLabelsDown(axis, 15);
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
    	up.pF = new ProcessFile(up.pF.name, reject, prob, up.pF.verbose, up.pF.sortGeneList);
    	down.pF = new ProcessFile(down.pF.name, reject, prob, down.pF.verbose, up.pF.sortGeneList);
    	up.points = null;
    	down.points = null;
    	setScales();
    	initPoints();
    	setHeight((float)(totalY*1.1));
    }
   
    public void initPoints(){
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
    
    public void setUpXAxisIntervals(float pVal){
    	setAxisIntervals(up.xAxis, pVal);
    }
    public void setDownXAxisIntervals(float pVal){
    	setAxisIntervals(down.xAxis, pVal);
    }
    public void setUpYAxisIntervals(float pVal){
    	setAxisIntervals(up.yAxis, pVal);
    }
    public void setDownYAxisIntervals(float pVal){
    	setAxisIntervals(down.yAxis, pVal);
    }
    public void setYAxisIntervals(float pVal){
    	setUpYAxisIntervals(pVal);
    	setDownYAxisIntervals(pVal);
    }
    public void setXAxisIntervals(float pVal){
    	setUpXAxisIntervals(pVal);
    	setDownXAxisIntervals(pVal);
    }
    
    public void setAxisIntervals(Axis axis, float logPVal){
    	//System.out.println(logPVal + " vs " + totalY);
    	setHeight(axis.setIntervals(logPVal, (float)totalY));
    	axis.setNonuniformTickNamesToTickVal();
    }
    
    private void markPoint(Plot plot, String rsID){
    	if (plot.pF.sortGeneList){
    		int index = InfoNodeArray.binarySearch(plot.pF.infoNodes, rsID);
	    	if (index >= 0 && index < plot.points.length && rsID.equals(plot.pF.getRsId(index))){
	    		plot.points[index].visibility = 1;
	    		//System.out.println(plot.pF.getChromosome(index));
	    	}
    	}else{
    		for (int i = 0; i < plot.pF.getSize(); i++){
    			if (plot.pF.getRsId(i).equals(rsID)){
    				plot.points[i].visibility = 1;
    			}
    		}
    	}
    }
    
    public void markUpperPoint(String rsID){
    	markPoint(up, rsID);
    }
    
    public void markLowerPoint(String rsID){
    	markPoint(down, rsID);
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
    	
    	totalY = Math.max(up.pF.maxYCor(), down.pF.maxYCor());
    	updateYScale();
    }
    
    private void updateYScale(){
    	if (totalY != 0){
    		yScale = (float) (0.5 * height/totalY);
    	}
    }
    
    private float relY(float addY){
    	return y + height/2 - yScale*addY;
    }
    
    private float relX(float addX){
    	return x + xScale*addX;
    }
    
    private float revRelY(float tag, int constant){
    	return ((y + height/2) - tag)/(constant*yScale);
    }
    
    private float revRelX(float tag, int constant){
    	return (tag - x)/(constant * xScale);
    }
    
    private void drawPoints(Plot plt){
    	parent.pushStyle();
    	parent.noStroke();
    	for (Point p : plt.points){
    		if (p.visibility == 0){
	    		int color = p.getColor();
	    		parent.fill(color);
	    		parent.ellipse(relX(p.xRel), relY(p.constant * p.yRel), p.getRadius(), p.getRadius());
    		}
    		else if (p.visibility == 1){
    			//parent.pushStyle();
        		/*parent.fill(0xFF0000);
        		parent.rect(relX(p.xRel) - p.getRadius(), relY(p.constant * p.yRel) + p.constant*3, 
        				relX(p.xRel) + p.getRadius(), relY(p.yRel) - p.constant*3);*/
        		//System.out.print((relX(p.xRel) - p.getRadius()) + "  " + relY(p.constant * p.yRel) + p.constant*3);
        		//System.out.println(" " + (relX(p.xRel) + p.getRadius()) + " " + (relY(p.yRel) - p.constant*3));
        		/*parent.rect(relX(p.xRel) - 3, relY(p.yRel) - p.getRadius(), 
        				relX(p.xRel) + 3, relY(p.yRel) + p.getRadius());*/
    			parent.fill(defaultMarkedColor);
    			float rt3 = (float) Math.sqrt(3);
    			float pX = relX(p.xRel);
    			float pY = relY(p.constant*p.yRel);
    			parent.triangle(pX - 1.5f*(p.getRadius()*rt3/2), pY + 1.5f*p.getRadius()/2, 
    					pX + 1.5f*(p.getRadius()*rt3/2), pY + 1.5f*p.getRadius()/2, pX, pY - p.getRadius()*1.5f);
    			//System.out.println(x - p.getRadius()*rt3 + " " +  y + p.getRadius()/2);
    			//System.out.println(x + p.getRadius()*rt3 + " " + );
    			
        		//parent.popStyle();
        		//parent.ellipse(relX(p.xRel), relY(p.constant * p.yRel), 100, 100);
        		//System.out.println(p.getRadius());
        		//parent.rect(relX(p.xRel) - p.getRadius(), relY(p.constant * p.yRel), relX(p.xRel) + p.getRadius(), relY(p.constant*p.yRel));
        		
    		}
    	}	
    	parent.popStyle();
    }
    
    public void drawPoints(){
    	drawPoints(up);
    	drawPoints(down);
    }
    
    public static void setMarkedColor(int color){
    	defaultMarkedColor = color;
    }
    
    
    public void setXAxisName(String name){
    	down.xAxis.name.name = name;
    }
    
    public void drawXAxis(Axis axis){
		parent.pushStyle();
    	if (axis.showTicks){
    		parent.fill(0);
    		for (float xVal : axis.getTicks()){
	    		if (axis.uniformTicks){
	        		parent.line(xVal, axis.y, xVal, axis.y - axis.tickLen);
	        		//System.out.println(xVal + " " + relY(0) + " " + (axis.y + axis.tickLen) + " " + axis.tickLen);
		        }else{
	        		parent.line(relX(xVal), axis.y, relX(xVal), axis.y - axis.tickLen);
	        		//System.out.println(relY(0) + " " + (axis.y - axis.tickLen) + " " + axis.tickLen);
	        	}
    		}
    	}
    	parent.popStyle();
    }
    
    public void drawYAxis(Axis axis){
		parent.pushStyle();
    	if (axis.showTicks){
    		parent.fill(0);
    		for (float yVal : axis.getTicks()){
	    		if (axis.uniformTicks){
	        		parent.line(axis.x, yVal, axis.x + axis.tickLen, yVal);
	        		//System.out.println(axis.x + " " + yVal + " " + (axis.x + axis.tickLen) + " " + yVal);
		        }else{
	        		parent.line(axis.x, relY(axis.constant*yVal), axis.x + axis.tickLen, relY(axis.constant*yVal));
	        		//System.out.println(relY(yVal) + " " + yVal);
	        		//System.out.println(relY(0) + " " + (axis.y - axis.tickLen) + " " + axis.tickLen);
	        	}
    		}
    	}
    	parent.popStyle();
    }
    
    public void drawAxis(Axis axis){
    	if (axis.isVisible){
	    	parent.fill(0);
	    	parent.line(axis.x, axis.y, axis.maxX, axis.maxY);
    	}
    	if (axis.isXAxis){
    		drawXAxis(axis);
    	}else{
    		drawYAxis(axis);
    	}
    }
    
    public void setTitle(String name){
    	title.name = name;
    }
    
    public void roundToNthPlace(int n){
       	float toRound = (float) (Math.pow(10, n)/20.0);
    	Axis.roundVal = toRound;
    }
    
    /*
    public void drawAxis(Axis axis){
    	if (axis.isVisible){
    		parent.pushMatrix();
    		parent.translate(axis.x, axis.y);
        	parent.rotate(axis.angle);
        	parent.translate(-axis.x, -axis.y);
	    	parent.fill(0);
	    	parent.line(axis.x, axis.y, axis.maxX, axis.maxY);
	    	//System.out.println("Axis line : " + axis.x + " to " + axis.maxX + " " + axis.y + " @ angle " + axis.angle);
	    	parent.popMatrix();
	    	if (axis.showTicks){
		    	for (float xVal : axis.getTicks()){
		        	parent.pushMatrix();
		        	parent.fill(0);
		        	parent.translate(xVal, axis.y + axis.tickLen/2);
		        	parent.rotate(axis.angle);
		        	parent.translate(-xVal, -axis.y - axis.tickLen/2);
		        	if (axis.isXAxis){
			        	if (axis.uniformTicks){
			        		parent.line(xVal, relY(0), xVal, axis.y + axis.tickLen);
			        		//System.out.println(xVal + " " + relY(0) + " " + (axis.y + axis.tickLen) + " " + axis.tickLen);
				        }else{
			        		parent.line(relX(xVal), axis.y, relX(xVal), axis.y - axis.tickLen);
			        		//System.out.println(relY(0) + " " + (axis.y - axis.tickLen) + " " + axis.tickLen);
			        	}
		        	}else{
		        		if (axis.uniformTicks){
			        		parent.line(axis.x, xVal, axis.x + axis.tickLen, xVal);
			        		//System.out.println(xVal + " " + relY(0) + " " + (axis.y + axis.tickLen) + " " + axis.tickLen);
				        }else{
			        		parent.line(relX(xVal), axis.y, relX(xVal), axis.y - axis.tickLen);
			        		//System.out.println(relY(0) + " " + (axis.y - axis.tickLen) + " " + axis.tickLen);
			        	}
		        	}
		    		parent.popMatrix();
		    	}
	    	}
    	}
    }*/
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
    	/*for (Label l : up.xAxis.tickNames){
    		drawLabel(l, up.xAxis.uniformTicks, true);
    	}*/
    	for (Label l : down.xAxis.tickNames){
    		drawLabel(l, down.xAxis.uniformTicks, true, down.xAxis.useCoordsAsNames);
    	}
    	for (Label l : up.yAxis.tickNames){
    		drawLabel(l, up.yAxis.uniformTicks, false, up.yAxis.useCoordsAsNames);
    	}
    	for (Label l : down.yAxis.tickNames){
    		drawLabel(l, down.yAxis.uniformTicks, false, down.yAxis.useCoordsAsNames);
    	}
    }
    
    private void drawLabel(Label label, boolean uniform, boolean isX, boolean useCoordsAsNames){
    	if (label.isVisible() && label.name != null){
	    	parent.pushMatrix();
	    	parent.translate(label.x, label.y);
	    	parent.rotate(label.angle);
	    	parent.translate(-label.x, -label.y);
	    	parent.textSize(label.size);
	    	if (label.font != null){
	    		parent.textFont(label.font);
	    	}
	    	//System.out.println(label.name + " " + label.x + " " + label.y);
	    	if (uniform){
	    		if (!useCoordsAsNames){
	    			parent.text(label.name, label.x, label.y);
	    		}else{
	    			if (isX){
	    				parent.text(Axis.customRound(revRelX(label.x, label.constant)), centeredLabel(label, label.x, isX), label.y);
	    			}else{
	    				parent.text(Axis.customRound(revRelY(label.y, label.constant)), centeredLabel(label, label.x, isX), label.y);
	    				//System.out.println(Axis.customRound(revRelY((float)Double.parseDouble(label.name), label.constant)) + " " + label.constant + " " + ((float)Double.parseDouble(label.name)-(y+height/2)));
	    			}
	    		}
	    		//System.out.println(label.name + " " + label.x + " " + label.y);
	    	}else{
	    		 //private float centeredLabel(Label l, float val, boolean isX){
	    		if (isX){
		    		parent.text(label.name, centeredLabel(label, relX(label.x), isX), label.y);
		    	}else{
		    		parent.text(label.name, label.x, centeredLabel(label, relY(label.constant * label.y), isX));
		    		//System.out.println(label.name + " " + label.x + " " + label.y + " " + relY(label.y));
		    	}
	    		//System.out.println(label.name + " " + relX(label.x) + " " + relY(label.y));
	    	}
	    	parent.popMatrix();
    	}
    }
    
    public Axis getUpperXAxis(){
    	return up.xAxis;
    }
    public Axis getLowerXAxis(){
    	return down.xAxis;
    }
    public Axis getLowerYAxis(){
    	return down.yAxis;
    }
    public Axis getUpperYAxis(){
    	return up.yAxis;
    }
    
    
    
    private void drawLabel(Label label){
    	parent.pushMatrix();
    	parent.translate(label.x, label.y);
    	parent.rotate(label.angle);
    	parent.translate(-label.x, -label.y);
    	parent.textSize(label.size);
    	parent.text(label.name, label.x, label.y);
    	parent.popMatrix();
    }
    
   
    
    public void drawVertLines(){
    	for (float xVal : up.xAxis.getTicks()){
    		parent.line(relX(xVal), y + height, relX(xVal), y);
    	}
    }
    
    
    public void drawVertLines(int color){
    	parent.pushStyle();
    	parent.stroke(color);
    	drawVertLines();
    	parent.popStyle();
    }
    
    
    public void drawHorizLine(float yVal, boolean labelYAxis){
    	parent.line(x, relY(yVal), x + width, relY(yVal));
    	if (labelYAxis){
    		Label horizName = new Label(Axis.customRound(yVal), x - Axis.yAxisTickNameMargin, relY(yVal), 0);
    		drawLabel(horizName);
    	}
    }
    
    public void drawHorizLine(float yVal){
    	drawHorizLine(yVal, false);
    }
    
    public void drawHorizLine(float y, int color){
    	drawHorizLine(y, color, false);
    }
    
    public void drawHorizLine(float y, int color, boolean labelYAxis){
    	parent.pushMatrix();
    	parent.stroke(color);
    	drawHorizLine(y, labelYAxis);
    	parent.popMatrix();
    }
    
    private float centeredLabel(Label l, float val, boolean isX){
    	parent.pushStyle();
    	parent.textSize(l.size);
    	int constant = -1;
    	if (isX){
    		constant = 1;
    	}
    	parent.popStyle();
    	return (float) (val - constant*parent.textWidth(l.name)/2.);
    }
    
    
    public void setBGColor(int color){
    	bgColor = color;
    }
    
    public int getBGColor(){
    	return bgColor;
    }
    
    public void setHeight(float height){
    	totalY = height;
    	updateYScale();
    }
    
    
    public void drawPlot(boolean moveXLabelsDown){
    	parent.pushMatrix();
    	parent.fill(bgColor);
    	parent.noStroke();
    	parent.rect(x, y, x + width, y + height, -3);
    	parent.stroke(0);
    	
    	drawVertLines(255);
    	/*parent.pushStyle();
    	parent.stroke(0);
    	parent.fill(0);
    	drawLabels();
    	drawAxes();
    	drawPoints();
    	drawAxes();
    	parent.popStyle();*/
    	parent.fill(0);
    	if (moveXLabelsDown){
    		moveXLabelsDown(down.xAxis);
    	}
    	drawLabels();

    	parent.stroke(0);
    	drawAxes();
    	drawPoints();
    	drawAxes();
    	parent.popMatrix();
    }
    
    public void drawPlot(){
    	drawPlot(true);
    }
}
