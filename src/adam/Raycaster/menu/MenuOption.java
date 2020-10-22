package adam.Raycaster.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import adam.Raycaster.Raycaster;
import adam.Raycaster.input.MouseInput;

public class MenuOption {

	private String text;
	private Rectangle rect;
	private int width, height;
	public boolean hasBeenPressed = false;
	public boolean alreadyPressed  = false;
	
	public MenuOption(String text, int width, int height)
	{
		this.width = width;
		this.height = height;
		this.text = text;
		rect = new Rectangle(0, 0, width, height);
	}
	
	public void setPosition(int x, int y)
	{
		rect.x = x;
		rect.y = y;
	}
	
	public void render(Graphics g, int stringWidth, int stringHeight)
	{
		if(isHovered() && hasBeenPressed)
			g.setColor(Color.LIGHT_GRAY);
		else if(isHovered())
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.BLACK);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		if(isHovered())
			g.setColor(Color.BLACK);
		else 
			g.setColor(Color.WHITE);
		g.drawString(text, (rect.x + rect.width / 2) - stringWidth / 2, 
				(rect.y + rect.height / 2) + stringHeight / 4);
	}
	
	public void tick(Raycaster game)
	{
		boolean isLeftButtonPressed = MouseInput.getButtonState(MouseEvent.BUTTON1);
		if(isHovered() && isLeftButtonPressed && !hasBeenPressed && !alreadyPressed)
		{
			hasBeenPressed = true;
		}
		if(!isHovered())
		{
			hasBeenPressed = false;
		}
		if(isHovered() && !isLeftButtonPressed && hasBeenPressed)
		{
			if(text == "Start")
			{
				game.startGame();
				game.isInPlay = true;
				hasBeenPressed = false;

			}
			else if(text == "Exit")
			{
				System.exit(0);
			}
			else if(text == "Resume")
			{
				game.isPaused = false;
				hasBeenPressed = false;
			}
			else if(text == "Quit")
			{
				game.isInPlay = false;
				game.isPaused = false;
				hasBeenPressed = false;

			}
		}
		alreadyPressed = MouseInput.getButtonState(MouseEvent.BUTTON1);
	}
	
	public boolean isHovered()
	{
		int x = MouseInput.getX();
		int y = MouseInput.getY();
		return x > rect.x && x < rect.x + rect.width &&
				y > rect.y && y < rect.y + rect.height;
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public String getText() { return text; }
 }
