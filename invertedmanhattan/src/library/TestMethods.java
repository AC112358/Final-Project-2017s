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
		try {
			c = new Container(this, 500, 500, 10, 10, "someHIP_MEN.txt", "someHIP_MEN.txt");
			//System.out.println("done w/setup");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw(){
		c.drawPlot();
		c.drawHorizLine(3);
	}
	
	public static void main(String[] args){
		PApplet.main(new String[] { /*"--present",*/ "library.TestMethods" });
	}

}