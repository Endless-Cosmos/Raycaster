package cosmos.raycaster;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import cosmos.raycaster.input.KeyInput;
import cosmos.raycaster.math.Vec2f;
import cosmos.raycaster.math.Vec2i;

public class Player
{	
	public Vec2f pos;
	public Vec2f dir;
	public Vec2f cameraLeft;
	public Vec2f cameraRight;
	public float angle = 0f;
	public Ray[] rays;
	public Vec2i mapPos;
	
	public Player(float x, float y, int raysAmt) 
	{
		pos = new Vec2f(x, y);
		dir = new Vec2f(1, 0);
		cameraLeft = new Vec2f(0, 0);
		cameraRight = new Vec2f(0, 0);
		rays = new Ray[raysAmt];
		initRays();
	}
	public void render(Graphics g) 
	{
		g.setColor(Color.white);
		g.fillArc((int) pos.x, (int) pos.y, 3, 3, 0, 360);
		g.drawLine((int) pos.x, (int) pos.y, (int) dir.x, (int) dir.y);
		g.drawLine((int) dir.x, (int) dir.y, (int) cameraLeft.x, (int) cameraLeft.y);
		g.drawLine((int) dir.x, (int) dir.y, (int) cameraRight.x, (int) cameraRight.y);
		for(int i = 0; i < rays.length; i++) {
			rays[i].render(g);
		}
	}
	public void tick(Level level) 
	{
		move(level);
		for(int i = 0; i < rays.length; i++) 
		{
			rays[i].tick(this);
		}
	}
	public void move(Level level)
	{
		if(KeyInput.getKeyState(KeyEvent.VK_W))
		{
			Vec2f moveForwards = Vec2f.normalize(Vec2f.sub(dir, pos));
			if(!collision(Vec2f.add(pos, moveForwards), level))
			{
				pos.add(moveForwards);
			}
		}
		if(KeyInput.getKeyState(KeyEvent.VK_S)) 
		{
			Vec2f moveBackwards =  Vec2f.normalize(Vec2f.sub(dir, pos));
			if(!collision(Vec2f.sub(pos, moveBackwards), level))
			{
				pos.sub(moveBackwards);
			}
		}
		if(KeyInput.getKeyState(KeyEvent.VK_A))
		{
			Vec2f moveLeft = Vec2f.normalize(Vec2f.sub(cameraLeft, dir));
			if(!collision(Vec2f.sub(pos, moveLeft), level)) 
			{
				pos.sub(moveLeft);
			}
		}
		if(KeyInput.getKeyState(KeyEvent.VK_D)) 
		{
			Vec2f moveRight = Vec2f.normalize(Vec2f.sub(cameraRight, dir));
			if(!collision(Vec2f.sub(pos, moveRight), level)) 
			{
				pos.sub(moveRight);
			}
		}
		if(KeyInput.getKeyState(KeyEvent.VK_E))
		{
			angle += .1f;
		}
		if(KeyInput.getKeyState(KeyEvent.VK_Q))
		{
			angle -= .1f;
		}
		rotate();
		
	}
	public void rotate()
	{
		Vec2f rotateDir = new Vec2f(0, 0);
		Vec2f rotateCam = new Vec2f(0, 0);
	
		rotateCam.x = (float) Math.cos(angle + Math.PI / 2);
		rotateCam.y = (float) Math.sin(angle + Math.PI / 2);
		
		rotateDir.x = (float) Math.cos(angle);
		rotateDir.y = (float) Math.sin(angle);
				
		rotateDir.setMag(1);
		rotateCam.setMag(1);
		
		dir = Vec2f.add(pos, rotateDir);
		cameraLeft = Vec2f.add(dir, rotateCam);
		cameraRight = Vec2f.sub(dir, rotateCam);
	}
	public void initRays()
	{
		for(int i = 0; i < rays.length; i++)
		{
			float slope = (((float) i / (rays.length - 1)) -.5f) * 2f;
			rays[i] = new Ray(this, 1, 0, slope);
		}
	}
	public void calcRays(Vec2f playerTileCoords, Vec2i mapSquarePos, Level level) 
	{
		for(int i = 0; i < rays.length; i++)
		{
			Vec2f ray = Vec2f.sub(rays[i].dir, pos);
			ray.normalize();
			float slope = (float) Math.tan(rays[i].angle);	
			double dist1 = horizontalIntercepts(playerTileCoords, mapSquarePos, level, ray, slope);
			double dist2 = verticalIntercepts(playerTileCoords, mapSquarePos, level, ray, slope);
			
			
			if(dist1 <= dist2) {
				ray.setMag((float) Math.sqrt(dist1));
				ray.add(pos);
				rays[i].touchingVert = false;
				rays[i].dir = ray;
			} else {
				ray.setMag((float) Math.sqrt(dist2));
				ray.add(pos);
				rays[i].touchingVert = true;
				rays[i].dir = ray;
			}
		}	
	}
	public float horizontalIntercepts(Vec2f playerTileCoords, Vec2i mapSquarePos, Level level, Vec2f ray, float slope) 
	{
		int yCoord = mapSquarePos.y;
		int initialXCoord = mapSquarePos.x; 
		float xOffset;
		int xCoord = initialXCoord;
		float initialYIntercept;
		float initialXIntercept;
		float xDist = 0;
		float yDist = 0;
		float yDelta = Level.tileSize;
		float xDelta = yDelta / Math.abs(slope);
		boolean hit = false;
		if(ray.y < 0) {
			initialYIntercept = -playerTileCoords.y;
			yDelta = - yDelta;
			yDist += initialYIntercept;
			yCoord--;
		}
		else
		{
			initialYIntercept = Level.tileSize - playerTileCoords.y;
			yDist += initialYIntercept;
			yCoord++;
		}
		initialXIntercept = Math.abs(initialYIntercept / slope);
		if(ray.x <= 0) {
			xDist -= initialXIntercept;
			xOffset = playerTileCoords.x - Level.tileSize;
			xCoord = initialXCoord + (int) ((xDist + xOffset) / Level.tileSize);
			xDelta = -xDelta;
			if(xCoord < 0)
			{
				xCoord = 0;
			}
		}
		else
		{
			xDist += initialXIntercept;
			xOffset = playerTileCoords.x;
			xCoord = initialXCoord + (int) ((xDist + xOffset) / Level.tileSize);
			if(xCoord > level.getWidth() - 1) 
			{
				xCoord = level.getWidth() - 1;
			}
		}
		while(!hit) 
		{
			if(level.map[xCoord + yCoord * level.getWidth()] == 1)
			{
				hit = true;
				break;
			}
			yDist += yDelta;
			if(yDist < 0) yCoord--;
			else yCoord++;
			xDist += xDelta;
			xCoord = initialXCoord + (int) ((xDist + xOffset) / Level.tileSize);
			if(xCoord < 0) xCoord = 0;
			if(xCoord > level.getWidth() - 1) xCoord = level.getWidth() - 1;
		}
		return xDist * xDist + yDist * yDist;
	}
	public float verticalIntercepts(Vec2f playerTileCoords, Vec2i mapSquarePos, Level level, Vec2f ray, float slope) 
	{
		int xCoord = mapSquarePos.x;
		int initialYCoord = mapSquarePos.y;
		int yCoord = initialYCoord;
		float yOffset;
		float initialXIntercept;
		float initialYIntercept;
		float xDist = 0;
		float yDist = 0;
		float xDelta = Level.tileSize;
		float yDelta = xDelta * Math.abs(slope);
		boolean hit = false;
		if(ray.x < 0)
		{
			initialXIntercept = -playerTileCoords.x;
			xDelta = - xDelta;
			xDist += initialXIntercept;
			xCoord--;
		} 
		else
		{
			initialXIntercept = Level.tileSize - playerTileCoords.x;
			xDist += initialXIntercept;
			xCoord++;
		}
		initialYIntercept = Math.abs(initialXIntercept * slope);
		if(ray.y < 0) 
		{
			yDist -= initialYIntercept;
			yOffset = playerTileCoords.y - Level.tileSize;
			yCoord = initialYCoord + (int) ((yDist + yOffset) / Level.tileSize);
			yDelta = -yDelta;
			if(yCoord < 0)
			{
				yCoord = 0;
			}
		} 
		else 
		{
			yDist += initialYIntercept;
			yOffset =  playerTileCoords.y;
			yCoord = initialYCoord + (int) ((yDist + yOffset) / Level.tileSize);
			if(yCoord > level.getWidth() - 1) 
			{
				yCoord = level.getWidth() - 1;
			}
		}
		while(!hit)
		{
			if(level.map[xCoord + yCoord * level.getWidth()] == 1) 
			{
				hit = true;
				break;
			}
			xDist += xDelta;
			if(xDist < 0) xCoord--;
			else xCoord++;
			yDist += yDelta;
			yCoord = initialYCoord + (int) ((yDist + yOffset) / Level.tileSize);
			if(yCoord < 0) yCoord = 0;
			if(yCoord > level.getWidth() - 1) yCoord = level.getWidth() - 1;
		}
		return xDist * xDist + yDist * yDist;
	}
	public boolean collision(Vec2f move, Level level)
	{
		int posX = (int) (move.x / Level.tileSize);
		int posY = (int) (move.y / Level.tileSize);
		if(level.map[posX + posY * level.getWidth()] == 1) 
		{
			return true;
		}
		return false;
	}
}
