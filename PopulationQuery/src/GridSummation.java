package src;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class GridSummation extends RecursiveAction {

	private int rLo, rHi, cLo, cHi;
	
	int[][] grid1, grid2;
	private static ForkJoinPool fjPool = new ForkJoinPool();
	
	public GridSummation(int[][] grid1, int[][] grid2, int rLo, int rHi, int cLo, int cHi){
		this.grid1 = grid1;
		this.grid2 = grid2;
		this.rLo = rLo;
		this.rHi = rHi;
		this.cLo = cLo;
		this.cHi = cHi;
		
		}
	
	@Override
	public void compute() {
		if (cHi - cLo <= PopulationQuery.SEQUENTIAL_CUTOFF){
			if (rHi - rLo <= PopulationQuery.SEQUENTIAL_CUTOFF){
				for (int i=cLo; i<cHi; i++){
					for (int j=rLo; j<rHi; j++){
						grid1[i][j] = grid1[i][j] + grid2[i][j];
						
						
					}
					
					
				}
		
			}else{
				GridSummation bottom = new GridSummation(grid1, grid2, rLo,(rLo +rHi)/2, cLo, cHi);
				GridSummation top = new GridSummation(grid1, grid2, (rLo +rHi)/2, rHi, cLo, cHi);
				top.fork();
				bottom.compute();
				top.join();
				
				
			}
			
			
			
		}else{
			GridSummation left = new GridSummation(grid1, grid2, rLo, rHi, cLo, (cLo+cHi)/2);
			GridSummation right = new GridSummation(grid1,grid2,rLo, rHi, (cLo+cHi)/2, cHi);
			right.fork();
			left.compute();
			right.join();
			
			
		}
		
	}
	public static void main(String[] args){
		int[] row1 = {9,2,1,0};
		int[] row2 = {1,2,7,11};
		int[] row3 = {1,0,4,1};
		int[] row4 = {1,0,3,9};
		int[][] grid1 = {row1, row2, row3, row4};
		int[][] grid2 = {row1, row2, row3, row4};
		GridSummation gridsum = new GridSummation(grid1, grid2, 0, 4, 0, 4);
		fjPool.invoke(gridsum);
		for (int j=4-1;j>=0;j--){
			for (int i=0; i<4;i++){			
				System.out.print(grid1[i][j]+ "   ");
				
			}	
			System.out.println();
		}
		
	}

}
