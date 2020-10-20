package adam.Raycaster.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import adam.Raycaster.Level;
import adam.Raycaster.Player;
import adam.Raycaster.Vec2f;

public class KeyInput extends KeyAdapter 
{
	
	public boolean forwards = false;
	public boolean backwards = false;
	public boolean left = false;
	public boolean right = false;
	public boolean turnRight = false;
	public boolean turnLeft = false;
	private Player player;
	private Level level;
	
	public KeyInput(Player player, Level level) 
	{
		this.player = player;
		this.level = level;
	}
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == KeyEvent.VK_W)
		{ 
			forwards = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_S)
		{ 
			backwards = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A)
		{ 
			left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_D)
		{ 
			right = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_R)
		{ 
			turnRight = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_Q)
		{ 
			turnLeft = true;
		}
	}
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_W) 
		{ 
			forwards = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) 
		{ 
			backwards = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A)
		{ 
			left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_D) 
		{ 
			right = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_R)
		{ 
			turnRight = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_Q)
		{ 
			turnLeft = false;
		}
	}
	public void tick()
	{
		if(forwards)
		{
			Vec2f moveForwards = Vec2f.normalize(Vec2f.sub(player.dir, player.pos));
			if(!player.collision(Vec2f.add(player.pos, moveForwards), level))
			{
				player.pos.add(moveForwards);
			}
		}
		if(backwards) 
		{
			Vec2f moveBackwards =  Vec2f.normalize(Vec2f.sub(player.dir, player.pos));
			if(!player.collision(Vec2f.sub(player.pos, moveBackwards), level))
			{
				player.pos.sub(moveBackwards);
			}
		}
		if(left)
		{
			Vec2f moveLeft = Vec2f.normalize(Vec2f.sub(player.cameraLeft, player.dir));
			if(!player.collision(Vec2f.sub(player.pos, moveLeft), level)) 
			{
				player.pos.sub(moveLeft);
			}
		}
		if(right) 
		{
			Vec2f moveRight = Vec2f.normalize(Vec2f.sub(player.cameraRight, player.dir));
			if(!player.collision(Vec2f.sub(player.pos, moveRight), level)) 
			{
				player.pos.sub(moveRight);
			}
		}
		if(turnRight)
		{
			player.angle += .1f;
		}
		if(turnLeft)
		{
			player.angle -= .1f;
		}
	}

}
