package adam.Raycaster;

import java.awt.Graphics;

import adam.Raycaster.math.Vec2f;

public class Ray 
{
	
	public Vec2f pos;
	public Vec2f dir;
	public float slope;
	public float angle;
	public boolean touchingVert = false;

	
	public Ray(Player player, float xDir, float yDir, float slope)
	{
		pos = player.pos;
		this.slope = slope;
		angle = 0;
		dir = new Vec2f(xDir, yDir);
		dir.add(pos);
		
	}
	public void render(Graphics g)
	{
		g.drawLine((int) pos.x, (int) pos.y, (int) dir.x, (int) dir.y);
	}
	public void tick(Player player)
	{	
		Vec2f cameraLeft = Vec2f.sub(player.cameraLeft, player.dir);
		Vec2f playerDir = Vec2f.sub(player.dir, player.pos);
		angle = (float) (player.angle + Math.atan((float) slope));
		angle = (float) (angle % (Math.PI * 2));
		
		dir = Vec2f.add(Vec2f.mult(cameraLeft, slope),  playerDir);
		dir.add(player.pos);
	}
}
