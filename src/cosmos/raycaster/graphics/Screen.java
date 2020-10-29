package cosmos.raycaster.graphics;

public class Screen {

	public int pixels[];
	private int width, height;
	
	public Screen(int w, int h)
	{
		width = w;
		height = h;
		pixels = new int[width * height];
	}
	public void clear()
	{
		for(int i = 0; i < pixels.length; i++)
		{
			pixels[i] = 0;
		}
	}
}
