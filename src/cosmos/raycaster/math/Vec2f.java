package cosmos.raycaster.math;

import java.awt.Color;
import java.awt.Graphics;

public class Vec2f {

	public float x;
	public float y;
	
	public Vec2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void add(Vec2f v) {
		x += v.x;
		y += v.y;
	}
	public static Vec2f add(Vec2f v1, Vec2f v2) {
		return new Vec2f(v1.x + v2.x, v1.y + v2.y);
	}
	public void sub(Vec2f v) 
	{
		x -= v.x;
		y -= v.y;
	}
	public static Vec2f sub(Vec2f v1, Vec2f v2)
	{
		return new Vec2f(v1.x - v2.x, v1.y - v2.y);
	}
	public void mult(float n)
	{
		x *= n;
		y *= n;
	}
	public static Vec2f mult(Vec2f v, float n)
	{
		return new Vec2f(v.x * n, v.y * n);
	}
	public float getMag() 
	{
		return (float) (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
	}
	public void normalize()
	{
		float mag = getMag();
		x /= mag;
		y /= mag;
	}
	public static Vec2f normalize(Vec2f v) 
	{
		float mag = v.getMag();
		float xn = v.x / mag;
		float yn = v.y /=mag;
		return new Vec2f(xn, yn);
		
	}
	public void setMag(float n) 
	{
		normalize();
		mult(n);
	}
	public float dot(Vec2f v)
	{
		return x * v.x + y * v.y;
	}
	public void render(Graphics g)
	{
		g.setColor(Color.white);
		g.fillArc((int) x - 5, (int) y, 10, 10, 0, 360);
	}
	public void print() 
	{
		System.out.println("X: " + x + "   Y: " + y);
	}
}

