public class seatAllocationError extends Exception
{
	//throw generic error to handle unexpected input
	private static final long serialVersionUID = 1L;

	public seatAllocationError(String message)
	{
		super(message);
	}

	public seatAllocationError()
	{
		super();
	}
}