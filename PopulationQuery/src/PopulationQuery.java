package src;
import java.io.BufferedInputStream;
//import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class queries census data to find population densities in different
 * areas of the US.
 */
public class PopulationQuery {

	/**
	 * For parsing - the number of comma-separated fields on a given line.
	 */
	public static final int TOKENS_PER_LINE = 7;

	/**
	 * For parsing - zero-based index of the field containing the population of
	 * the current census group.
	 */
	public static final int POPULATION_INDEX = 4;

	/**
	 * For parsing - zero-based index of the field containing the latitude of
	 * the current census group.
	 */
	public static final int LATITUDE_INDEX = 5;

	/**
	 * For parsing - zero-based index of the field containing the longitude of
	 * the current census group.
	 */
	public static final int LONGITUDE_INDEX = 6;

	
	public static final int SEQUENTIAL_CUTOFF = 1;
	/**
	 * There should be only one fork/join pool per program, so this needs to be
	 * a static variable.
	 */
	public static ForkJoinPool fjPool = new ForkJoinPool();
	
	/**
	 * Array of census data parsed from the input file.
	 */
	private CensusData data;
	
	private Rectangle boundary;
	
	private int cols;
	private int rows;
	
	public int[][] grid;
	private int versionNumber;

	/**
	 * Initialize the query object by parsing the census data in the given file.
	 * 
	 * @param filename
	 *            name of the census data file
	 */
	public PopulationQuery(String filename) {
		// Parse the data and store it in an array.
		this.data = parse(filename);
		boundary = new Rectangle();
	
	}

	/**
	 * Parse the input file into a large array held in a CensusData object.
	 * 
	 * @param filename
	 *            name of the file to be used as input.
	 * @return CensusData object containing the parsed data.
	 */
	private static CensusData parse(String filename) {
		CensusData result = new CensusData();

		try {
			/*
			File appBase = new File("."); //current directory
			String path = appBase.getAbsolutePath();
			System.out.println(path);
			*/
			BufferedReader fileIn = new BufferedReader(new FileReader(filename));

			/*
			 * Skip the first line of the file. After that, each line has 7
			 * comma-separated numbers (see constants above). We want to skip
			 * the first 4, the 5th is the population (an int) and the 6th and
			 * 7th are latitude and longitude (floats).
			 */

			try {
				/* Skip the first line. */
				String oneLine = fileIn.readLine();

				/*
				 * Read each subsequent line and add relevant data to a big
				 * array.
				 */
				while ((oneLine = fileIn.readLine()) != null) {
					String[] tokens = oneLine.split(",");
					if (tokens.length != TOKENS_PER_LINE)
						throw new NumberFormatException();
					int population = Integer.parseInt(tokens[POPULATION_INDEX]);
					result.add(population,
							Float.parseFloat(tokens[LATITUDE_INDEX]),
							Float.parseFloat(tokens[LONGITUDE_INDEX]));
				}
			} finally {
				fileIn.close();
			}
		} catch (IOException ioe) {
			System.err
					.println("Error opening/reading/writing input or output file.");
			System.exit(1);
		} catch (NumberFormatException nfe) {
			System.err.println(nfe.toString());
			System.err.println("Error in file format");
			System.exit(1);
		}
		return result;
	}

	/**
	 * Preprocess the census data for a run using the given parameters.
	 * 
	 * @param cols
	 *            Number of columns in the map grid.
	 * @param rows
	 *            Number of rows in the map grid.
	 * @param versionNum
	 *            implementation to use
	 */
	public void preprocess(int cols, int rows, int versionNum) {
		this.cols = cols;
		this.rows = rows;
		switch(versionNum)
		{
			case 1: preprocess1(data);
				break;
			case 2: preprocess2(data);
				break;
			case 3: preprocess3(data);
				break;
			case 4: preprocess4(data);
			default: 
				break;
		}
	}
	
	public void preprocess1(CensusData data)
	{
		versionNumber = 1;
		boundary = PreprocessClass1.preprocess(data, 0, data.dataSize);
		
	}
	
	public void preprocess2(CensusData data)
	{
		versionNumber = 2;
		boundary = fjPool.invoke(new PreprocessClass2(data, 0, data.dataSize));
	
		
	
	}
	public void preprocess3(CensusData data)
	{
		versionNumber = 3;
		grid = PreprocessClass3.preprocess(this, data, rows, cols);
		
	}
	public void preprocess4(CensusData data)
	{
		versionNumber = 4;
		Rectangle boundary = fjPool.invoke(new PreprocessClass2(data, 0, data.dataSize));
		DataStorage dataObj = new DataStorage(data, rows, cols, boundary);	
		grid = fjPool.invoke(new PreprocessClass4(this, dataObj, 0, data.dataSize));
		PreprocessClass3.presum(grid, rows, cols);
	}

	/**
	 * Query the population of a given rectangle.
	 * 
	 * @param w
	 *            western edge of the rectangle
	 * @param s
	 *            southern edge of the rectangle
	 * @param e
	 *            eastern edge of the rectangle
	 * @param n
	 *            northern edge of the rectangle
	 * @return pair containing the population of the rectangle and the
	 *         population as a percentage of the total US population.
	 */
	public Pair<Integer, Float> singleInteraction(int w, int s, int e, int n) 
	{
		switch(versionNumber)
		{
			case 1: return singleInteraction1(w, s, e, n); 
			case 2: return singleInteraction2(w, s, e, n); 
			case 3: return singleInteraction3(w,s,e,n);
			case 4: return singleInteraction4(w,s,e,n);
			default:
				break;
		}
		return new Pair<Integer, Float>(0, (float) 0);
	}
	
	public Pair<Integer, Float> singleInteraction1(int w, int s, int e, int n)
	{
		Pair<Integer, Integer> roughAns= SingleInteractionClass1.sum(this, data, 0, data.dataSize, w, s, e, n);
		return new Pair<Integer, Float>(roughAns.getElementA(), roughAns.getElementA()*100.0f /roughAns.getElementB());
	}
	
	public Pair<Integer, Float> singleInteraction2(int w, int s, int e, int n)
	{
		Pair<Integer, Integer> roughAns = fjPool.invoke(new SingleInteractionClass2(this, data, 0, data.dataSize, w, s, e, n));
		return new Pair<Integer, Float>(roughAns.getElementA(), roughAns.getElementA()*100.0f /roughAns.getElementB());
	}
	
	public Pair<Integer, Float> singleInteraction3(int w, int s, int e, int n)
	{
		Pair<Integer, Integer> roughAns= SingleInteractionClass3.sum(this, w, s, e, n);
		return new Pair<Integer, Float>(roughAns.getElementA(), roughAns.getElementA()*100.0f /roughAns.getElementB());
	}
	
	public Pair<Integer, Float> singleInteraction4(int w, int s, int e, int n)
	{
		Pair<Integer, Integer> roughAns= SingleInteractionClass3.sum(this, w, s, e, n);
		return new Pair<Integer, Float>(roughAns.getElementA(), roughAns.getElementA()*100.0f /roughAns.getElementB());
	}
	
	public Pair<Integer, Integer> gridLocation(CensusGroup group)
	{
		Integer column;
		Integer row;
		
		column = (int) ((group.longitude - boundary.left)/((boundary.right-boundary.left)/ cols))+1;
		row = (int) ((group.latitude - boundary.bottom)/((boundary.top-boundary.bottom)/rows)) + 1;
		if (row >rows){
	
			row = row - 1;
			
		}
		if (column >cols){
			column = column - 1;
			
		}
		return new Pair<Integer, Integer>(row, column);			
	}

	// argument 1: file name for input data: pass this to parse
	// argument 2: number of x-dimension buckets
	// argument 3: number of y-dimension buckets
	// argument 4: -v1, -v2, -v3, -v4, or -v5
	public static void main(String[] args) {
		// Parse the command-line arguments.
		String filename;
		int cols, rows, versionNum;
		try {
			filename = args[0];
			cols = Integer.parseInt(args[1]);
			rows = Integer.parseInt(args[2]);
			String versionStr = args[3];
			Pattern p = Pattern.compile("-v([12345])");
			Matcher m = p.matcher(versionStr);
			m.matches();
			versionNum = Integer.parseInt(m.group(1));
		} catch (Exception e) {
			System.out
					.println("Usage: java PopulationQuery <filename> <rows> <cols> -v<num>");
			System.exit(1);
			return;
		}
		
		// Parse the input data.
		PopulationQuery pq = new PopulationQuery(filename);
		
		// Preprocess the input data.
		pq.preprocess(cols, rows, versionNum);
	
		// Read queries from stdin.
		Scanner scanner = new Scanner(new BufferedInputStream(System.in));
		while (true) {
			int w, s, e, n;
			try {
				System.out.print("Query? (west south east north | quit) ");
				String west = scanner.next();
				if (west.equals("quit")) {
					break;
				}
				w = Integer.parseInt(west);
				s = scanner.nextInt();
				e = scanner.nextInt();
				n = scanner.nextInt();
	
				if (w < 1 || w > cols)
					throw new IllegalArgumentException();
				if (e < w || e > cols)
					throw new IllegalArgumentException();
				if (s < 1 || s > rows)
					throw new IllegalArgumentException();
				if (n < s || n > rows)
					throw new IllegalArgumentException();
			} catch (Exception ex) {
				System.out
						.println("Bad input. Please enter four integers separated by spaces.");
				System.out.println("1 <= west <= east <= " + cols);
				System.out.println("1 <= south <= north <= " + rows);
				continue;
			}
	
			// Query the population for this rectangle.
			Pair<Integer, Float> result = pq.singleInteraction(w, s, e, n);
			System.out.printf("Query population: %10d\n", result.getElementA());
			System.out.printf("Percent of total: %10.2f%%\n",
					result.getElementB());
		}
		scanner.close();
	}
}
