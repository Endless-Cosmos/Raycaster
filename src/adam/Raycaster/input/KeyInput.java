package adam.Raycaster.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.BitSet;

import adam.Raycaster.Level;
import adam.Raycaster.Player;
import adam.Raycaster.Vec2f;

public class KeyInput extends KeyAdapter 
{
	private static BitSet keyStates = new BitSet(70000);
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		 keyStates.set(e.getKeyCode(), true);
	}
	
	@Override
	public void keyReleased(KeyEvent e) 
	{
		 keyStates.set(e.getKeyCode(), false);
	}
	
	public static boolean getKeyState(int keyCode)
	{
		return keyStates.get(keyCode);
	}
}
