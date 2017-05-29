import grafica.*;

public GPlot plot1, plot2;
import java.io.FileNotFoundException;
String[] chromosomes; 
final int BOX_COLOR = 245;
final int POINT_SIZE = 4;
GPointsArray points1, points2;
float[] logTicksW, logTicksM;
//GPlot plots[] = {plot1, plot2};
int[] colors = {unhex("1b9e77"), unhex("d95f02"), unhex("7570b3"), unhex("e7298a")};
/*int[] colors = {Integer.parseInt(""+color(#1b9e77)), Integer.parseInt(""+color(#d95f02)), 
Integer.parseInt(""+color(#7570b3)), Integer.parseInt(""+color(#d95f02))};*/


public void setUpPlot(GPlot plots){
    plots.setHorizontalAxesTicksSeparation(4);
    plots.getXAxis().setNTicks(22);
    plots.getYAxis().setNTicks(100);
    plots.getYAxis().getAxisLabel().setText("-log(p value)");
    plots.setLogScale("xy");
    plots.setBgColor(255);
    plots.setBoxBgColor(BOX_COLOR);
    plots.setLineColor(BOX_COLOR);
  }

  
void setup(){
  chromosomes = new String[22];
  /*for(int i:colors){
    println(i);
  }*/
  for (int i = 1; i <= 22; i++){
    chromosomes[i-1] = "" + i;
  }
  size(1000, 900);
  surface.setResizable(true);
  chromosomes = new String[22];
  ProcessFile pfM = null;
  ProcessFile pfW = null;
  pfM = new ProcessFile(loadStrings("HIP_MEN_chr_pos_rs_pval.txt"));
  //pfM = new ProcessFile(loadStrings("someHIP_MEN.txt"));
  //pfW = new ProcessFile(loadStrings("someHIP_MEN.txt"));
  pfW = new ProcessFile(loadStrings("HIP_WOMEN_chr_pos_rs_pval.txt"));
  // pfM = new ProcessFile("ManhattanPlot_java.pde");
   //pfW = new ProcessFile("ProcessFile.java");
  for (int i = 0; i < chromosomes.length; i++){
    chromosomes[i] = (i + 1) + "";
  }
  points1 = new GPointsArray(pfM.getSize());
  points2 = new GPointsArray(pfW.getSize());
  int div1 = pfM.getSize()/1000;
  int div2 = pfW.getSize()/1000;
  println(div1 + " " + div2);
  logTicksM = new float[pfM.getSize()/div1];
  logTicksW = new float[pfW.getSize()/div2];
  float maxWYCor = pfW.maxYCor();
  float maxMYCor = pfM.maxYCor();
  //println(pfM.hasNext());
  for (int i = 0; i < pfM.getSize(); i++){
    points1.add(pfM.getXPosn(), pfM.getLogP(), pfM.getRsId());
    if (i % div1 == 0  && i/div1 < logTicksM.length){
      logTicksM[i/div1] = (int)(pfM.getLogP());//(pfM.getLogP()*1000 - (pfM.getLogP()*1000) % 1000)/1000;
    }
    pfM.advanceIndex();
  }
   // println("added a point");

  for (int i = 0; i < pfW.getSize(); i++){
    points2.add(pfW.getXPosn(), maxWYCor - pfW.getLogP(), pfW.getRsId());
    if (i % div2 == 0 && i/div2 < logTicksW.length){
      logTicksW[i/div2] = (int) (maxWYCor - pfW.getLogP()); //((maxWYCor -pfW.getLogP())*1000 - (pfW.getLogP()*1000) % 1000)/1000;
    }
    pfW.advanceIndex();
  }
  float[] maxTicks = logTicksM;
  if (logTicksW[logTicksW.length-1] > logTicksM[logTicksM.length-1]){
    maxTicks = logTicksW;
  }
  float maxY = (float)(Math.max(maxWYCor, maxMYCor));
  println("added points");
  plot1 = new GPlot(this);
  plot1.getXAxis().setDrawTickLabels(false);
  plot1.setPos(0, 0);
  plot1.setDim(500, (float)(400/maxY * maxMYCor)); //CHANGE BUT SCALE RELATIVE TO THE MAX Y COR
  //plot1.setTicksLength(4);
  plot1.setYLim(0, maxY);
 
  plot2 = new GPlot(this);
  plot2.setPos(plot1.getPos()[0], plot1.getPos()[1] + plot1.getDim()[1]);
  //plot2.setDim(plot1.getDim());
  plot2.setDim(500, (float)(400/maxY * maxWYCor));
  //plot2.setTicksLength(4);
  plot2.setYLim(0, maxY);
  
  plot2.getXAxis().getAxisLabel().setText("Chromosome");
  
  plot1.setPoints(points1);
  plot2.setPoints(points2);
  
  plot1.getTitle().setText("Manhattan plot");
  
  //plot1.getYAxis().setTicks(logTicksM);
 // plot2.getYAxis().setTicks(logTicksW);
  
  println("set the plots");
  setUpPlot(plot1);
  setUpPlot(plot2);
  
  plot1.getYAxis().setAxisLabelText("-log(p value)");
  plot2.getYAxis().setAxisLabelText("-log(p value)");
  
  plot1.getRightAxis().setAxisLabelText("HIP men");
  plot2.getRightAxis().setAxisLabelText("HIP women");
  
  plot2.getXAxis().setTickLabels(chromosomes);
  
  println("set the stuff up");
  //plot.activatePointLabels();
  plot1.defaultDraw();
  plot2.defaultDraw();
  
  println("default draw!");
  /*for (int i = 0; i < points1.getNPoints(); i++){
    plot.drawPoint(points1.get(i), -1 * colors[(int)(points1.get(i).getX())%4], POINT_SIZE); 
  }*/
  noLoop();
}

public void draw(){
  background(255);
  plot1.beginDraw();
    plot1.drawBox();
    plot1.drawXAxis();
    plot1.drawYAxis();
    plot1.drawTitle();
    plot1.drawRightAxis();
    plot1.drawGridLines(GPlot.VERTICAL);
    for (int i = 0; i < points1.getNPoints(); i++){
      plot1.drawPoint(points1.get(i), -1 * colors[(int)(points1.get(i).getX())%4], POINT_SIZE); 
    }
  plot1.endDraw();
  
  plot2.beginDraw();
    plot2.drawBox();
    plot2.drawXAxis();
    plot2.drawYAxis();
    plot2.drawTitle();
    plot2.drawRightAxis();
    plot2.drawGridLines(GPlot.VERTICAL);
    for (int i = 0; i < points2.getNPoints(); i++){
      plot2.drawPoint(points2.get(i), -1 * colors[(int)(points2.get(i).getX())%4], POINT_SIZE); 
    }
    plot2.endDraw();
  
}