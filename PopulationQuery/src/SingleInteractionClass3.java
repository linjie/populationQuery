package src;

public class SingleInteractionClass3 {
	public static Pair<Integer, Integer> sum(PopulationQuery pQuery, int w, int s, int e, int n){
		int[][] grid = pQuery.grid;
		int population=grid[e-1][s-1];
		if (e>0 && n<grid.length){
			population -= grid[e-1][n];
		}
		if (w>1 && s>0){
			population -= grid[w-2][s-1];
		
		}
		if (w>1 && n<grid.length){
			population += grid[w-2][n];
		}
		System.out.println("Population = "+population+", Total Population=" + grid[grid.length-1][0]);
		return new Pair<Integer, Integer>(population,grid[grid.length-1][0]);
	}	
	public static Pair<Integer, Integer> test(int[][] grid, int w, int s, int e, int n){
		
		int population=grid[e-1][s-1];
		if (e>0 && n<grid.length){
			population -= grid[e-1][n];
		}
		if (w>1 && s>0){
			population -= grid[w-2][s-1];
		
		}
		if (w>1 && n<grid.length){
			population += grid[w-2][n];
		}
		System.out.println("Population = "+population+", Total Population=" + grid[grid.length-1][0]);
		return new Pair<Integer, Integer>(population,grid[grid.length-1][0]);
	}	
	public static void main(String[] args){
		int[] row1 = {9,2,1,0};
		int[] row2 = {1,2,7,11};
		int[] row3 = {1,0,4,1};
		int[] row4 = {1,0,3,9};
		int[][] grid = {row1, row2, row3, row4};
		
		grid = PreprocessClass3.test(grid, 4, 4);
		
		for (int j=4-1;j>=0;j--){
			for (int i=0; i<4;i++){			
				System.out.print(grid[i][j]+ "   ");
				
			}	
			System.out.println();
		}
		Pair<Integer, Integer> result = test(grid, 3,2,4,3);
		System.out.println("population: "+result.getElementA()+" total population:"+result.getElementB());
	}
	
}