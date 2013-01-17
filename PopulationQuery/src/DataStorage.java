package src;

public class DataStorage {

	
		public CensusData cdata;
		public int cols;
		public int rows;
		public Rectangle boundary;
		public DataStorage(CensusData data, int rows, int cols, Rectangle boundary){
			this.cdata = data;
			this.cols = cols;
			this.rows = rows;
			this.boundary = boundary;
			
		}
		
		
	
}
