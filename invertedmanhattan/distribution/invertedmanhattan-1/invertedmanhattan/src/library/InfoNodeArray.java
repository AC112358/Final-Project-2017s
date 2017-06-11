package library;
class InfoNodeArray{
	private InfoNode[] nodes;
	private int size = 0;
		
	private void grow(){
		InfoNode[] temp = new InfoNode[nodes.length*2];
		for (int i = 0; i < nodes.length; i++){
			temp[i] = nodes[i];
		}
		nodes = temp;
	}
	
	protected static int binarySearch(InfoNodeArray arr, String insert){
//		/InfoNode(int chromosome, int position, String rsID, double pValue){
		return binarySearch(arr.nodes, 0, arr.size, new InfoNode(1, 1, insert, 1));
	}
		
	private static int binarySearch(InfoNode[] arr, int s, int e, InfoNode insert){
		//[s, e)
		while (s < e){
			int mid = (s + e)/2;
			if (e - s == 1){
				if (insert.compareTo(arr[s]) >= 0){
					if (insert.compareTo(arr[e]) <= 0){
						return e;
					}
					return e + 1;
				}
				return s;
			}
			if (arr[mid].compareTo(insert) >= 0){
				e = mid;
			}else{
				s = mid;
			}
				//System.out.println(s + " " + mid + " " + e);
		}
		return s;
			
	}
		
		protected void add(InfoNode n){
			if (nodes == null || nodes.length == 0){
				nodes = new InfoNode[1];
				nodes[0] = n;
				return;
			}
			//System.out.println(nodes.length);
			int index = binarySearch(nodes, 0, size, n);
			
			
			size++;
			
			if (index >= nodes.length || size >= nodes.length){
				grow();
			}
			
			/*for (int i = 0; i < size; i++){
				System.out.print(nodes[i].rsId + " ");
			}
			System.out.println("\n" + n.rsId + " " + index + " " + nodes.length + " " + size + " " + "\n");
			*/
			//System.out.println(nodes.length + " "  + index + "  " + (index+size));
			
			for (int i = size; i >= index && i >= 1; i--){
				nodes[i] = nodes[i-1];
			}
			nodes[index] = n;
		}
		
		public void addUnsorted(InfoNode n){
			if (nodes == null || nodes.length == 0){
				nodes = new InfoNode[1];
				nodes[0] = n;
				return;
			}
			size++;
			
			if (size >= nodes.length){
				grow();
			}

			
			nodes[size-1] = n;
		}
		
		public InfoNode get(int i){
			return nodes[i];
		}
		
		public int size(){
			return size;
		}
		
	}
	