package adam.Raycaster.menu;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import adam.Raycaster.input.MouseInput;

public abstract class  Menu
{
	protected boolean isVisible = false;
	protected MenuOption options[];
	protected final int SCREEN_WIDTH, SCREEN_HEIGHT;
	protected Font font = new Font("LucidaSans", Font.BOLD, 40);
		
	public Menu(MenuOption options[], int screenWidth, int screenHeight)
	{
		SCREEN_WIDTH = screenWidth;
		SCREEN_HEIGHT = screenHeight;
		this.options = options; 
		init(screenWidth, screenHeight);
	}
	
	public void init(int screenWidth, int screenHeight)
	{
		int size = options.length;
		for(int i = 0; i < size; i++)
		{
			options[i].setPosition(screenWidth / 2 - options[i].getWidth() / 2,
					(screenHeight / size / 2 + screenHeight / size * i) - options[i].getHeight() / 2);
		}
	}
	
	public void show() { isVisible = true; }
	
	public void hide() { isVisible = false; }
	
	public void render(Graphics g)
	{
		if(isVisible)
		{
			g.setFont(font);
			for(int i = 0; i < options.length; i++)
			{
				Rectangle2D stringBounds = font.getStringBounds(options[i].getText(), g.getFontMetrics().getFontRenderContext());
				options[i].render(g, (int)stringBounds.getWidth(), (int)stringBounds.getHeight());
			}
		}
	}
		
	public boolean isVisible()
	{
		return isVisible;
	}
	
	public void tick()
	{
		if(isVisible)
		{
			
			for(int i = 0; i < options.length; i++)
			{				
				options[i].tick();
			}
		}
	}

}
