
public class Timer 
{
	long startTimer;
	long endTimer;

	public void stop()
	{
		endTimer = System.currentTimeMillis();
	}
	
	public long getTime()
	{
		return(endTimer-startTimer);
	}

	public void start() 
	{
		startTimer = System.currentTimeMillis();
	}
}
