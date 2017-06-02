package library;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class ProcessFile{
	private File file;
	private ArrayList<Integer> chrs;
	private ArrayList<Integer> posns;
	private ArrayList<String> rsIds;
	private ArrayList<Double> pVals;
  private int maxPosn;
  private Scanner scan;
	private int index;
  private int tenPow;
  private int minPValIndex;

  public ProcessFile(){
    index = 0;
    minPValIndex = 0;
    maxPosn = 0;
    chrs = new ArrayList<Integer>();
    posns = new ArrayList<Integer>();
    rsIds = new ArrayList<String>();
    pVals = new ArrayList<Double>();
    file = null;
  }
	public ProcessFile(String fileName) throws FileNotFoundException{
    this();
    file = new File(fileName);
    scan = new Scanner(file);
		extractInfo();
	}

  public ProcessFile(String[] lines){
    this();
    //System.out.println(lines.length);
    scan = new Scanner(String.join("\n", lines));
    //System.out.println(scan.hasNext());
    extractInfo();
  }
	
	public void extractInfo(){
		boolean addToLists = true;
		String[] tokens;
		while (scan.hasNext()){
			tokens = scan.nextLine().split(" ");
      //System.out.println("PROCESS INFO " + tokens[0]);
			addToLists = true;
			for (int i = 0; i < 2; i++){
				try{
					Integer.parseInt(tokens[i]);
				}catch(NumberFormatException e){
					addToLists = false;
				}
			}
			try{
				Double.parseDouble(tokens[3]);
			}catch(NumberFormatException e){
				addToLists = false;
			}
			if (addToLists){
				chrs.add(Integer.parseInt(tokens[0]));
        posns.add(Integer.parseInt(tokens[1]));
        pVals.add(Double.parseDouble(tokens[3]));
				rsIds.add(tokens[2]);
        if (posns.get(posns.size()-1) > maxPosn){
          maxPosn = posns.get(posns.size()-1);
        }
        if (pVals.get(pVals.size()-1) < pVals.get(minPValIndex)){
          minPValIndex = posns.size()-1;
        }
			}
		}
		scan.close();
    tenPow = (int)(Math.ceil(Math.log(maxPosn)/Math.log(10)));
	}
	
	
	public boolean hasNext(){
		return index < rsIds.size();
	}
	public void advanceIndex(){
		index++;
	}
	
	public String getRsId(){
		return rsIds.get(index);
	}
	
	public double getPVal(){
		return pVals.get(index);
	}
	
	public int getChromosome(){
		return chrs.get(index);
	}
	
	public int getPosition(){
		return posns.get(index);
	}


  private float logP(int i){
    return (float)(-Math.log(pVals.get(i))/Math.log(10));
  }
  public float getLogP(){
    return logP(index);
  }
  
  
  public float getXPosn(){
    return (float)((getChromosome() * Math.pow(10, tenPow) + getPosition())/Math.pow(10, tenPow));
  }
  
  public int getSize(){
    return posns.size();
  }
  
  public float maxYCor(){
    return logP(minPValIndex);
  }
}