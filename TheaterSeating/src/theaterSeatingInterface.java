import java.io.IOException;

public interface theaterSeatingInterface 
{
	String parseInputFile(String s) throws seatAllocationError;
	String createFile() throws IOException;
}
