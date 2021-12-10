import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Main 
{
	//default value of 
	private static int numSeats = 20;
	private static int numRows = 10;

	public static void main(String[] args) throws IOException, seatAllocationError
	{

		//Get input file
		System.out.println("Enter input file path");
		Scanner userInput = new Scanner(System.in);
		String path = userInput.next();
		File file = new File(path);
		Scanner sc = new Scanner(file);
		
		//Create theater object and read in input
		theaterSeatingInterface theaterSeating = new theaterSeating(populateTheaterMap());
		while (sc.hasNextLine()) 
		{
			theaterSeating.parseInputFile(sc.nextLine());
		}
		sc.close();
		userInput.close();
		System.out.println(theaterSeating.createFile());
	}

	
	private static ConcurrentHashMap<Character, ArrayList<Integer>> populateTheaterMap()
	{
		//create a default seating chart and place a temp value in each seat
		ConcurrentHashMap<Character, ArrayList<Integer>> theaterLayout = new ConcurrentHashMap<Character, ArrayList<Integer>>();
		char temp = 'A';
		
		//Iterate over each seat and put a temp value in its place
		for (int i = 0; i < numRows; i++) 
		{
			theaterLayout.put(temp, populateRows());
			temp++;
		}
		return theaterLayout;
	}

	private static ArrayList<Integer> populateRows() 
	{
		ArrayList<Integer> seats = new ArrayList<Integer>();
		for (int i = 0; i < numSeats; i++)
		{
			seats.add(i + 1);
		}
		return seats;
	}
}