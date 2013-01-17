package src;
import java.util.concurrent.RecursiveTask;

public class PreprocessClass2 extends RecursiveTask<Rectangle>
{
	//static final int SEQUENTIAL_CUTOFF = 100;
	private CensusData data;
	private int begin;
	private int end;
	
	public PreprocessClass2(CensusData zdata, int zbegin, int zend)
	{
		data = zdata;
		begin = zbegin;
		end = zend;
	}
	
	public Rectangle compute()
	{
		if(end-begin<=PopulationQuery.SEQUENTIAL_CUTOFF){
			return PreprocessClass1.preprocess(data, begin, end);
		}else
		{
			PreprocessClass2 left = new PreprocessClass2(data, begin, (begin+end)/2);
			PreprocessClass2 right = new PreprocessClass2(data, (begin+end)/2, end);
			right.fork();
			
			Rectangle leftAns = left.compute();
			Rectangle rightAns = right.join();
			Rectangle ans = new Rectangle();
			if(leftAns.left>rightAns.left){
				ans.left = rightAns.left;
			}else{
				ans.left = leftAns.left;
			}
				
			if(leftAns.right<rightAns.right){
				ans.right = rightAns.right;
			}else{
				ans.right = leftAns.right;
			}
				
			if(leftAns.bottom>rightAns.bottom){
				ans.bottom = rightAns.bottom;
			}else{
				ans.bottom = leftAns.bottom;
			}
				
			if(leftAns.top<rightAns.top){
				ans.top = rightAns.top;
			}else{
				ans.top = leftAns.top;
			}
	
			return ans;
		}
	}
}
