package adam.Raycaster;

public class Timer
{
	long startTime;
	float deltaTime;
	float pausedTime = 0.0f;
	boolean isPaused;
	
	void start()
	{
		startTime = System.currentTimeMillis();
		deltaTime = startTime;
		isPaused = false;
	}
	void tick()
	{
			float newDelta = (float)(System.currentTimeMillis() - startTime) / 1000.0f; 
		if(isPaused)
		{
			pausedTime += newDelta - deltaTime;
		}
		deltaTime = newDelta;

	}
	
	void pause()
	{
		isPaused = true;
	}
	
	void resume()
	{
		isPaused = false;
	}
	
	public float getTime()
	{
		return deltaTime - pausedTime;
	}
	
	String getFormatedTime()
	{
		int timeMinutes = (int)((getTime() / 60) % 60);
		int timeSeconds = (int)(getTime() % 60);
		String minutesString = timeMinutes < 10 ? "0" + timeMinutes : timeMinutes + "";
		String secondsString = timeSeconds < 10 ? "0" + timeSeconds : timeSeconds + ""; 
		return minutesString + " : " + secondsString;
	}
}
