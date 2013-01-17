package src;


import java.util.concurrent.RecursiveTask;

public class PreprocessClass4 extends RecursiveTask<int[][]> {

	DataStorage dataObj;
	int lo, hi;
	PopulationQuery pQuery;
	
	public PreprocessClass4(PopulationQuery pQuery, DataStorage dataObj, int lo, int hi){
		this.dataObj = dataObj;
		this.lo = lo;
		this.hi = hi;
		this.pQuery = pQuery;
		
	}
	
	
	@Override
	public int[][] compute() {
		int[][] grid = new int[dataObj.cols][dataObj.rows];
		if (hi - lo <= PopulationQuery.SEQUENTIAL_CUTOFF){
			Pair<Integer, Integer> coord;
			for (int i=lo;i<hi;i++){
				coord = pQuery.gridLocation(dataObj.cdata.data[i]);
				grid[coord.getElementB()-1][coord.getElementA()-1] += dataObj.cdata.data[i].population; 
			}
			return grid;
		}else{
			PreprocessClass4 left = new PreprocessClass4(pQuery, dataObj, lo, (lo+hi)/2);
			PreprocessClass4 right = new PreprocessClass4(pQuery, dataObj, (lo+hi)/2, hi);
			right.fork();
			grid = left.compute();
			int[][] grid2 = right.join();
			GridSummation gridSum = new GridSummation(grid, grid2, 0, dataObj.rows, 0, dataObj.cols);
			PopulationQuery.fjPool.invoke(gridSum);
			
			return grid;
		
		}
		
	}
	
	

}