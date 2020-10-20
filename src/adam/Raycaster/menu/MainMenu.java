package adam.Raycaster.menu;

import java.awt.event.MouseEvent;

import adam.Raycaster.input.MouseInput;

public class MainMenu extends Menu {

	public MainMenu(MenuOption options[], int screenWidth, int screenHeight) 
	{
		super(options, screenWidth, screenHeight);
	}

	@Override
	public void tick() 
	{
		if(isVisible)
		{
			for(int i = 0; i < options.length; i++)
			{
				boolean isLeftButtonPressed = MouseInput.getButtonState(MouseEvent.BUTTON1);
				if(options[i].isHovered() && isLeftButtonPressed && !options[i].hasBeenPressed)
				{
					options[i].hasBeenPressed = true;
				}
				if(options[i].isHovered() && !isLeftButtonPressed && options[i].hasBeenPressed)
				{
					System.out.println("clicked: " + options[i].getText());
					options[i].hasBeenPressed = false;
				}
			}
		}
	}

	
}
