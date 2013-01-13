package src;

public class SingleInteractionClass1 
{
	public static Pair<Integer, Integer> 
		sum(PopulationQuery popQuery, 
				CensusData data, 
				int begin, int end, int w, int s, int e, int n)
	{
		Integer pop = 0;
		Integer popUS = 0;
		for(int i = begin; i < end; i++)
		{
			Pair<Integer, Integer> coords = popQuery.gridLocation(data.data[i]);
			if( (s<=coords.getElementA())&&
				(coords.getElementA()<=n) &&
				(w <= coords.getElementB())&&
				(coords.getElementB()<=e)
				  )
				pop += data.data[i].population;
			popUS += data.data[i].population;
		}
		return new Pair<Integer, Integer>(pop, popUS);
	}
}
