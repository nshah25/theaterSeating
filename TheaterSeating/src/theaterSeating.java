import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class theaterSeating implements theaterSeatingInterface
{

	private ConcurrentHashMap<Character, ArrayList<Integer>> theaterLayout = new ConcurrentHashMap<Character, ArrayList<Integer>>();
	private HashMap<String, Integer> reservationIdentifersList = new HashMap<String, Integer>();
	private LinkedHashMap<String, ArrayList<String>> reservationDetails = new LinkedHashMap<String, ArrayList<String>>();
	private HashSet<String> noReservation = new HashSet<String>();
	private int numSeatsInRow = 20;
	private int numRows = 10;
	private int numSeatsAvailable = numSeatsInRow * numRows;

	//constructor
	public theaterSeating(ConcurrentHashMap<Character, ArrayList<Integer>> populateTheaterMap)
	{
		theaterLayout = populateTheaterMap;
	}

	@Override
	public String parseInputFile(String s) throws seatAllocationError
	{
		//Split the input on each space and send it to be validated
		String split[] = s.split(" ");
		return validateRequest(split);
	}

	private String validateRequest(String split[]) throws seatAllocationError 
	{
		String name = "";
		int numSeats = 0;
		if (split.length == 2) 
		{
			//get the parameters of the input and feed it to the validators
			name = split[0];
			numSeats = Integer.parseInt(split[1]);
			reservationIdentifersList.put(name, numSeats);
			return searchForBestSeats(numSeats, name);
		}
		//too many seats
		if (split.length > 2 || split.length < 2)
		{
			throw new seatAllocationError("Parameters incorrect");
		}
		
		//too few seats
		if (numSeats <= 0) 
		{
			throw new seatAllocationError("Number of seat requested is 0 or less");
		}

		//identifier is present
		if (name == null || name.isEmpty()) 
		{
			throw new seatAllocationError("Name is empty");
		}
		
		return "";
	}

	private String searchForBestSeats(int numSeats, String name) 
	{
		String result = "";
		//check number of seats availale and number of seats requested
		if (numSeats > numSeatsAvailable || numSeats > numSeatsInRow) 
		{
			result = name.concat(" Reservation cannot be made, too many seats requested");
			noReservation.add(result);
			return result;
		}
		//Find the closet unoccupied seat
		Character row = closestFit(numSeats);
		ArrayList<Integer> availableSeats = theaterLayout.get(row);
		//Insert them into the return value
		reservationDetails.put(name, printSeats(availableSeats, numSeats, row));
		//Update the available seats
		updateSeats(row, numSeats);
		numSeatsAvailable -= numSeats;
		return new String(name.concat(reservationDetails.get(name).toString()));
	}

	private void updateSeats(Character row, int numSeats)
	{
		//get the row requested and remove them from the available seats
		ArrayList<Integer> availableSeats = theaterLayout.get(row);
		for (int i = 0; i < numSeats; i++)
		{
			availableSeats.remove(0);
		}
	}

	private ArrayList<String> printSeats(ArrayList<Integer> availableSeats, int numSeats, Character rowId) {
		//Create return values made up of the row and seats and insert them into the retval
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < numSeats; i++) {
			result.add(Character.toString(rowId) + availableSeats.get(i));
		}
		return result;
	}

	private Character closestFit(int numSeats)
	{
		int minSize = Integer.MAX_VALUE;
		Character row = Character.MIN_VALUE;
		//If the number of seats requested is small enough the be satisfied, get the key and value and update the template
		for (ConcurrentHashMap.Entry<Character, ArrayList<Integer>> entry : theaterLayout.entrySet()) 
		{
			if ((entry.getValue().size() - numSeats) < minSize && (entry.getValue().size() - numSeats) >= 0)
			{
				minSize = entry.getValue().size() - numSeats;
				row = entry.getKey();
			}
		}
		return row;
	}

	
	@Override
	public String createFile() throws IOException {
		String content = "";
		String newContent = "";
		//create output.txt and fill it will the resulting values
		for (Map.Entry<String, ArrayList<String>> entry : reservationDetails.entrySet())
		{
			content += entry.getKey() + " " + entry.getValue() + System.lineSeparator();
			newContent = content.replace("[", "").replace("]", "");
		}
		//create output.txt
		String path = createOutputFile("output.txt", newContent);
		return path;
	}

	private String createOutputFile(String fileName, String content) throws IOException
	{
		Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
		File f = new File(fileName);
		return f.getCanonicalPath();
	}

}