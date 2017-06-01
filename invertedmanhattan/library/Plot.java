package invertedmanhattan;
import processing.core.*;

public class Plot implements PConstants{
    PApplet parent;

    public Plot(PApplet parent, int c, Label t){
	this.parent = parent;
	constant = c;
	trait = t;
    }
    
    private int constant;
    private Label trait;
    private float scale;
}
