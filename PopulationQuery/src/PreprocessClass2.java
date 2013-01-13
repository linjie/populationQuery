package src;
import java.util.concurrent.RecursiveTask;

public class PreprocessClass2 extends RecursiveTask<float[]>
{
	static final int SEQUENTIAL_CUTOFF = 250;
	private CensusData data;
	private int begin;
	private int end;
	
	public PreprocessClass2(CensusData zdata, int zbegin, int zend)
	{
		data = zdata;
		begin = zbegin;
		end = zend;
	}
	
	public float[] compute()
	{
		if(end-begin<=SEQUENTIAL_CUTOFF)
			return PreprocessClass1.preprocess(data, begin, end);
		else
		{
			PreprocessClass2 left = new PreprocessClass2(data, begin, (begin+end)/2);
			PreprocessClass2 right = new PreprocessClass2(data, (begin+end)/2, end);
			right.fork();
			float[] leftAns = left.compute();
			float[] rightAns = right.join();
			if(leftAns[0]>rightAns[0])
				leftAns[0]=rightAns[0];
			if(leftAns[1]<rightAns[1])
				leftAns[1]=rightAns[1];
			if(leftAns[2]>rightAns[2])
				leftAns[2]=rightAns[2];
			if(leftAns[3]<rightAns[3])
				leftAns[3]=rightAns[3];
			return leftAns;
		}
	}
}
