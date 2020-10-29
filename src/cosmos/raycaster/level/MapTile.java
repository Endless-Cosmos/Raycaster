package cosmos.raycaster.level;

import java.awt.Color;
import java.awt.Graphics;

import cosmos.raycaster.math.Vec2f;

public class MapTile 
{

	public Vec2f[] points;
	public boolean isBlock = false;
	public boolean[] neighbors = new boolean[4];
	
	public MapTile(float xPos1, float xPos2, float yPos1, float yPos2) 
	{
		points = new Vec2f[4];
		init(xPos1, xPos2, yPos1, yPos2);
	}
	public void init(float xPos1, float xPos2, float yPos1, float yPos2) 
	{
		points[0] = new Vec2f(xPos1, yPos1);
		points[1] = new Vec2f(xPos2, yPos1);
		points[2] = new Vec2f(xPos1, yPos2);
		points[3] = new Vec2f(xPos2, yPos2);
	}
	public void print() {
		System.out.println("X: " + points[0].x + "    Y: " + points[0].y);
		System.out.println("X: " + points[1].x + "    Y: " + points[1].y);
		System.out.println("X: " + points[2].x + "    Y: " + points[2].y);
		System.out.println("X: " + points[3].x + "    Y: " + points[3].y);
		System.out.println("-----------------");
	}
	public void render(Graphics g, float xPos1, float xPos2, float yPos1, float yPos2) 
	{
		if(isBlock) {
			g.setColor(Color.blue);
			g.fillRect((int) xPos1, (int) yPos1, Level.tileSize, Level.tileSize);
			g.setColor(Color.green);
			g.drawRect((int) xPos1, (int) yPos1, Level.tileSize, Level.tileSize);
		} else {
			g.setColor(Color.red);
			g.drawRect((int) xPos1, (int) yPos1, Level.tileSize, Level.tileSize);
		}
		g.setColor(Color.white);
		g.fillArc((int) points[0].x, (int) points[0].y, 3, 3, 0, 360);
		g.fillArc((int) points[1].x, (int) points[1].y, 3, 3, 0, 360);
		g.fillArc((int) points[2].x, (int) points[2].y, 3, 3, 0, 360);
		g.fillArc((int) points[3].x, (int) points[3].y, 3, 3, 0, 360);
	}
}
