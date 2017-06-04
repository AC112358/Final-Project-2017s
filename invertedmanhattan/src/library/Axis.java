package library;


public class Axis {
	protected Label name;
	protected float x, y, maxX, maxY; //assume all Axes are horizontal: will be rotated if not
	protected float[] ticks;
	protected Label[] tickNames;
	protected float angle;
	protected float tickLen;
	protected float marginX, marginY;
	protected boolean isXAxis;
	protected float xScale, yScale;
	protected boolean isVisible;
	
	protected static float axisLabelMargin = 4;
	protected static float axisTickNameMargin = 1;

	public Axis(float marginX, float marginY, float x1, float y1, float x2, float y2, float angle){
		isVisible = true;
		x = x1;
		y = y1;
		maxX = x2;
		maxY = y2;
		name = new Label("", (x1+x2+marginX)/2, (y1+y2+marginY)/2);
		this.angle = angle;
		name.angle = angle;
		isXAxis = y1 == y2;
	}
	
	public boolean setNTicks(int n){
		if (n <= 0){
			return false;
		}
		ticks = new float[n];
		float increments = (x + maxX)/n;
		for (int i = 0; i < n; i++){
			ticks[i] = increments * i;
		}
		
		if (tickNames == null){
			setTickNamesToTickVal();
		}
		return true;
	}
	
	public void setTickNamesToTickVal(){
		tickNames = new Label[ticks.length];
		for (int i = 0; i < ticks.length; i++){
			tickNames[i] = makeTickName(i, customRound(ticks[i]));
		}
	}
	
	private Label makeTickName(int i, String n){
		//System.out.println(i + " " + tickNames[i]);
		return new Label(n, getXVal(ticks[i]), getYVal(ticks[i]));
	}
	
	private String customRound(float f){
		return "" + (int)Math.floor(f + 0.05);
	}
	
	private float getXVal(float tickVal){
		if (isXAxis){
			return x + tickVal - Axis.axisTickNameMargin;
		}
		return x;
	}
	
	private float getYVal(float tickVal){
		if (!isXAxis){
			return y + tickVal - Axis.axisTickNameMargin;
		}
		return y;
	}
	
	
	public void setTickNames(String[] names){
		int i = 0;
		while (i < names.length && i < ticks.length){
			i++;
		}
		tickNames = new Label[i];
		
		int j = 0;
		while (j < i){
			tickNames[j] = makeTickName(j, names[j]);
			j++;
		}
	}
}
