package library;
class InfoNode implements Comparable<InfoNode>{
	int chr;
	int posn;
	String rsId;
	double pVal;

	InfoNode(int chromosome, int position, String rsID, double pValue){
		chr = chromosome;
		posn = position;
		rsId = rsID;
		pVal = pValue;
	}
		
	public int compareTo(InfoNode other){
		return rsId.compareTo(other.rsId);
	}
		
	}