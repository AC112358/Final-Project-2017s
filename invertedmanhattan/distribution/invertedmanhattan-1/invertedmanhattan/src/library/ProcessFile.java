package library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessFile{
	private File file;
	protected String name;
	/*private ArrayList<Integer> chrs;
	private ArrayList<Integer> posns;
	private ArrayList<String> rsIds;
	private ArrayList<Double> pVals;*/
	protected InfoNodeArray infoNodes;
	private int maxPosn;
	private int index;
	private int tenPow;
	private double minPVal;
	protected boolean verbose;
	protected boolean sortGeneList = false;
	protected static double[] chromosomeLengths = {0, 248956422, 242193529, 198295559, 190214555, 
    		181538259, 170805979, 159345973, 145138636, 138394717, 
    		133797422, 135086622, 133275309, 114364328, 107043718, 
    		101991189, 90338345, 83257441, 80373285, 58617616, 64444167,
    		46709983, 50818468, 156040895, 57227415};// for X & Y
	protected static float[] fullLengths;
	private static final float DIV_BY = (float)Math.pow(10, 8);
	
	
	
  public ProcessFile(){
	    index = 0;
	    minPVal = 1;
	    maxPosn = 0;
	   /* chrs = new ArrayList<Integer>();
	    posns = new ArrayList<Integer>();
	    rsIds = new ArrayList<String>();
	    pVals = new ArrayList<Double>();*/
	    infoNodes = new InfoNodeArray();
	    file = null;
	    fullLengths = new float[25]; //23 if no X & Y
	    fullLengths[0] = 0;
	    for (int i = 1; i < fullLengths.length; i++){
	    	fullLengths[i] = (float) (fullLengths[i-1] + chromosomeLengths[i]/DIV_BY);
	    }
	    verbose = false;
}
 
	public ProcessFile(String fileName, float reject, float prob, boolean showMsgs, boolean sortGenes) throws IOException{
		this();
		name = fileName;
		file = new File(fileName);
		verbose = showMsgs;
		sortGeneList = sortGenes;
		extractInfo(reject, prob);
	}
	
	
	public ProcessFile(String fileName) throws IOException{
		this(fileName, 0, 0, false, false);
	}

	private boolean rejectP(double pVal, float reject, float prob){
		//reject < 0 ==> test if p val < |reject|
		if (prob < 0){
			prob = 0;
		}
		if (prob > 1){
			prob = 1;
		}
		if (reject == 0){
			return false;
		}
		else if (reject > 0){
			if (pVal > reject){
				return Math.random() < prob;
			}else{
				return false;
			}
		}
		else{
			reject *= -1;
			if (pVal < reject){
				return Math.random() < prob;	
			}else{
				return false;	
			}
		}
	}
	
	public void extractInfo(float reject, float prob) throws IOException{
		//System.out.println("extracting info");
		boolean addToLists = true;
		String[] tokens;
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		int numRejected = 0;
		int index = 0;
		while (line != null){
			index++;
			addToLists = true;
			int chr = 0;
			tokens = line.split(" ");
			if (tokens.length < 4){
				addToLists = false;
				if (verbose){
					System.out.println("Rejected SNP at line " + index + ": due to the line being too short: " + tokens.length);
				}
			}
			if (addToLists){
				for (int i = 0; i < 2; i++){
					try{
						Integer.parseInt(tokens[i]);
						if (i == 0){
							chr = Integer.parseInt(tokens[i]);
						}
					}catch(NumberFormatException e){
						if (i == 0 && (tokens[i].equals("X") || tokens[i].equals("Y"))){
							chr = 24;
							if (tokens[i].equals("X")){
								chr = 23;
							}
						}else{
							addToLists = false;
							numRejected++;
							if (verbose){
								System.out.println("Rejected SNP at line " + index + " due to non-integer input ('X' & 'Y' are automatically handled)");
							}
						}
					}
				}
				try{
					Double.parseDouble(tokens[3]);
				}catch(NumberFormatException e){
					addToLists = false;
					numRejected++;
					if (verbose){
						System.out.println("Rejected SNP at line " + index + ": p value is not a double");
					}
				}
				
				double pVal = Double.parseDouble(tokens[3]);
				if (addToLists){
					addToLists = !rejectP(pVal, reject, prob);
					if (!addToLists){
						numRejected++;
					}	
				}
				
				if (pVal <= 0 || pVal > 1){
					addToLists = false;
					if (verbose){
						System.out.println("Rejected SNP at line " + index + ": invalid p val: " + pVal);
					}
					numRejected++;
				}
				
				int posn = Integer.parseInt(tokens[1]);
				
				if (chr < 1 || chr > 24){
					numRejected++;
					addToLists = false;
					if (verbose){
						System.out.println("Rejected SNP at line " + index + ": invalid chromosome: " + chr);
					}
				}
				
				if (posn < 0){
					numRejected++;
					addToLists = false;
					if (verbose){
						System.out.println("Rejected SNP at line " + index + ": invalid position: " + posn);
					}
				}
				
				if (addToLists){
					/*chrs.add(chr);
					posns.add(posn);
					pVals.add(pVal);
					rsIds.add(tokens[2]);*/
					if (sortGeneList){
						infoNodes.add(new InfoNode(chr, posn, tokens[2], pVal));
					}else{
						infoNodes.addUnsorted(new InfoNode(chr, posn, tokens[2], pVal));
					}
					if (posn > maxPosn){
						maxPosn = posn;
					}
					if (pVal < minPVal){
						minPVal = pVal;
					}
				}
				
			}
			line = in.readLine();
		}
		if (verbose){
			System.out.println(numRejected + " SNPs rejected\n");
		}
		in.close();
		/*if (infoNodes != null && infoNodes.size > 0){
			//System.out.println(infoNodes.size);
			System.out.println(getSize() + " " + infoNodes.get(getSize() - 1).rsId);
		}else if(infoNodes.size == 0){
			System.out.println("why");
		}*/
		tenPow = (int)(Math.ceil(Math.log(maxPosn)/Math.log(10)));
	}
	

	public boolean hasNext(){
		return index < infoNodes.size();
	}
	
	public void advanceIndex(){
		index++;
	}
	
	public String getRsId(){
		return infoNodes.get(index).rsId;
	}
	
	public double getPVal(){
		return infoNodes.get(index).pVal;
	}
	
	public int getChromosome(){
		//System.out.println(index + " " + infoNodes.size + " " + infoNodes.get(index).rsId);
		return infoNodes.get(index).chr;
	}
	
	protected int getChromosome(int index){
		//System.out.println(index + " " + infoNodes.size + " " + infoNodes.get(index).rsId);
		return infoNodes.get(index).chr;
	}
	
	public int getPosition(){
		return infoNodes.get(index).posn;
	}


  private float logP(int i){
    return (float)(-Math.log(infoNodes.get(i).pVal)/Math.log(10));
  }
  
  private float logP(double p){
	  return (float)(-Math.log(p)/Math.log(10));
  }
  
  public float getLogP(){
    return logP(index);
  }
  
  
  protected String getRsId(int index){
	  return infoNodes.get(index).rsId;
  }
  
  public float getXPosn(){
    return (float)fullLengths[getChromosome() - 1] + getPosition()/DIV_BY;//(float)((getChromosome() * Math.pow(10, tenPow) + getPosition())/Math.pow(10, tenPow));
  }
  
  public int getSize(){
    return infoNodes.size();
  }
  
  public float maxYCor(){
    return logP(minPVal);
  }
}