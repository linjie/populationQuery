package src;

public class PreprocessClass1 
{
	public static Rectangle preprocess(CensusData data, int begin, int end)
	{
		//minLon, maxLon, minLat, maxLat
		Rectangle result = new Rectangle();
		for(int i = begin; i < end; i++)
		{
			if(data.data[i].longitude < result.left)
				result.left = data.data[i].longitude;
			if(data.data[i].longitude > result.right)
				result.right = data.data[i].longitude;
			if(data.data[i].latitude < result.bottom)
				result.bottom = data.data[i].latitude;
			if(data.data[i].latitude > result.top)
				result.top = data.data[i].latitude;
		}
		return result;
	}
}
