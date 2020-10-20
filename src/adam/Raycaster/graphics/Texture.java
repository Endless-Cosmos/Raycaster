package adam.Raycaster.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture
{
	public int pixels[];
	public int size;
	
	public static Texture wall = new Texture("/textures/Wall.png");
	
	private Texture(String filePath)
	{
		loadTexture(filePath);
	}
	
	public void loadTexture(String filePath)
	{
		BufferedImage image;
		try 
		{
			image = ImageIO.read(Texture.class.getResource(filePath));
			int w = size = image.getWidth();
			int h = image.getHeight();
			pixels = new int[w * h];
			image.getRGB(0, 0, w, h, pixels, 0, w);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
