package library;

import java.io.IOException;

import processing.core.PApplet;

public class TestMethods extends PApplet{
	Container c = null;
	
	public void settings(){
		size(700, 700);
	}
	
	public void setup(){
		//PApplet p, float w, float h, float x, float y
		int[] colors = new int[25];
		for (int i = 0; i < colors.length; i++){
			if (i % 3 == 0){
				colors[i] = color(53, 95, 180);
			}else if (i % 3 == 1){
				colors[i] = color(53, 180, 142);
			}else{
				colors[i] = color(180, 70, 53);
			}
		}
		try {
			c = new Container(this, 500, 500, 50, 50, "someHIP_MEN.txt", "someHIP_MEN.txt");
			c.setPointColors(colors);
			//System.out.println(c.up.xAxis.isVisible);
			//c.setBGColor(color(0, 255, 213));
			//System.out.println("done w/setup");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw(){
		c.drawPlot();
		c.drawHorizLine(1, color(180, 70, 53));

		//c.drawVertLines(color(180, 70, 53));
    	//c.down.yAxis.isVisible = false;
    	//c.up.yAxis.isVisible = false;
		//c.up.xAxis.isVisible = false;
		//c.down.xAxis.isVisible = false;
	//	c.drawAxis(c.up.xAxis);
		//c.drawAxis(c.down.yAxis);
		//c.drawAxis(c.up.yAxis);
		//c.drawAxis(c.down.yAxis);
		//c.drawAxes();
	}
	
	public static void main(String[] args){
		PApplet.main(new String[] { /*"--present",*/ "library.TestMethods" });
	}

}
