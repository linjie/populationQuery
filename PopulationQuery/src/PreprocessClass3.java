package src;

public class PreprocessClass3{
	public static int[][] preprocess(PopulationQuery pQuery, CensusData data, int rows, int cols)
	{
		
		int[][] grid = new int[cols][rows];
		
		Pair<Integer, Integer> coord;
		for (int i=0;i<data.dataSize;i++){
			coord = pQuery.gridLocation(data.data[i]);
			grid[coord.getElementB()-1][coord.getElementA()-1] += data.data[i].population; 
		}
		
		presum(grid, rows, cols);
		//System.out.println("Version 3, total population:"+grid[grid.length-1][0]);
		
		return grid;
	}
	
	public static void presum(int[][] grid, int rows, int cols){

		for (int i=1;i<cols;i++){
			grid[i][rows-1] += grid[i-1][rows-1];
			
		}
		for (int j=rows-2;j>=0;j--){
			grid[0][j] += grid[0][j+1];
			
		}
		for (int i=1; i<cols;i++){
			for (int j=rows-2;j>=0;j--){
				
				grid[i][j] = grid[i][j] + grid[i-1][j] + grid[i][j+1] - grid[i-1][j+1];
				
			}
		
		}
	}
	
	public static int[][] test(int[][] grid, int rows, int cols)
	{
		
		
		for (int i=1;i<cols;i++){
			grid[i][rows-1] += grid[i-1][rows-1];
			
		}
		for (int j=rows-2;j>=0;j--){
			grid[0][j] += grid[0][j+1];
			
		}
		for (int i=1; i<cols;i++){
			for (int j=rows-2;j>=0;j--){
				
				grid[i][j] = grid[i][j] + grid[i-1][j] + grid[i][j+1] - grid[i-1][j+1];
				
			}
			
			
		}
		
		
		return grid;
	}
	
	public static void main(String[] args){
		int[] row1 = {9,2,1,0};
		int[] row2 = {1,2,7,11};
		int[] row3 = {1,0,4,1};
		int[] row4 = {1,0,3,9};
		int[][] grid = {row1, row2, row3, row4};
		grid = test(grid, 4, 4);
	
		for (int j=4-1;j>=0;j--){
			for (int i=0; i<4;i++){			
				System.out.print(grid[i][j]+ "   ");
				
			}	
			System.out.println();
		}	
	}
}
