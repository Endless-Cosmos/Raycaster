package cosmos.raycaster.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {
	
	private static boolean leftIsPressed = false;
	private  static int mouseX = 0, mouseY = 0;
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
			leftIsPressed = true;
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
			leftIsPressed = false;
	}
	public static int getX()
	{
		return mouseX;
	}
	
	public static int getY()
	{
		return mouseY;
	}
	public static boolean getButtonState(int button)
	{
		switch(button)
		{
			case MouseEvent.BUTTON1: return leftIsPressed;
		}
		return false;
	}
}
