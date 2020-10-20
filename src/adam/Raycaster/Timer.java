package adam.Raycaster;

import java.text.DecimalFormat;

public class Timer
{
	long startTime;
	float deltaTime;
	boolean stopped;
	
	void start()
	{
		startTime = System.currentTimeMillis();
		deltaTime = startTime;
		stopped = false;
		System.out.println(startTime);
	}
	void tick()
	{
		if(!stopped)
		{
			deltaTime = (float)(System.currentTimeMillis() - startTime) / 1000.0f;
		}
	}
	
	void stop()
	{
		stopped = true;
	}
	
	String getFormatedTime()
	{
		System.out.println(deltaTime);
		int timeMinutes = (int)(deltaTime / 60);
		int timeSeconds = (int)(deltaTime % 60);
		String minutesString = timeMinutes < 10 ? "0" + timeMinutes : timeMinutes + "";
		String secondsString = timeSeconds < 10 ? "0" + timeSeconds : timeSeconds + ""; 
		return minutesString + " : " + secondsString;
	}
}
