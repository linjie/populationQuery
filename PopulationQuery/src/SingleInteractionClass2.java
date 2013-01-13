package src;

import java.util.concurrent.RecursiveTask;

public class SingleInteractionClass2 extends RecursiveTask<Pair<Integer, Integer>>
{
	private PopulationQuery popQuery;
	private CensusData data;
	private int begin;
	private int end;
	private int w;
	private int s;
	private int e;
	private int n;
	static final int SEQUENTIAL_CUTOFF = 250;
	
	public SingleInteractionClass2(PopulationQuery zpopQuery, CensusData zdata, int zbegin, int zend, int zw, int zs, int ze, int zn)
	{
		popQuery = zpopQuery;
		data = zdata;
		begin = zbegin;
		end = zend;
		w = zw;
		s = zs;
		e = ze;
		n = zn;
	}
	
	public Pair<Integer, Integer> compute()
	{
		Pair<Integer, Integer> leftAns;
		Pair<Integer, Integer> rightAns;
		if((end-begin)<=SEQUENTIAL_CUTOFF)
			return SingleInteractionClass1.sum(popQuery, data, begin, end, w, s, e, n);
		else
		{
			SingleInteractionClass2 left = new SingleInteractionClass2(popQuery, data, begin, (begin+end)/2, w, s, e, n);
			SingleInteractionClass2 right = new SingleInteractionClass2(popQuery, data, (begin+end)/2, end, w, s, e, n);
			right.fork();
			leftAns = left.compute();
			rightAns = right.join();
			return new Pair<Integer, Integer>(leftAns.getElementA()+rightAns.getElementA(), leftAns.getElementB()+rightAns.getElementB());
		}
	}
}
