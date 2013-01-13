package src;

public class PreprocessClass1 
{
	public static float[] preprocess(CensusData data, int begin, int end)
	{
		//minLon, maxLon, minLat, maxLat
		float[] result = {360, -360, 360, -360};
		for(int i = begin; i < end; i++)
		{
			if(data.data[i].longitude < result[0])
				result[0] = data.data[i].longitude;
			if(data.data[i].longitude > result[1])
				result[1] = data.data[i].longitude;
			if(data.data[i].latitude < result[2])
				result[2] = data.data[i].latitude;
			if(data.data[i].latitude > result[3])
				result[3] = data.data[i].latitude;
		}
		return result;
	}
}
