package adam.Raycaster;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import adam.Raycaster.graphics.Screen;
import adam.Raycaster.graphics.Texture;
import adam.Raycaster.input.KeyInput;

public class Level
{	
	public int map[];
	private int width;
	private int height;
	public static int tileSize;
	public MapTile[] mapTiles;
	private int[] wallsHeight;
	private int wallsAmt;
	private Vec2f currentPlayerTileCoords;
	public Vec2f mapSquarePos;
	private int mapImagePixels[];
	private float rayTexCoords[];
	private Vec2i rayMapCoords[];
	private Player player;
	private Timer timer = new Timer();

	private Texture wall = Texture.wall;
	private Texture end = Texture.end;
	private Texture at = null;
	
	public Level(int width, int height, int tileSize, Player player, int wallsAmt) 
	{
		Level.tileSize = tileSize;
		width = width / tileSize;
		height = height / tileSize;
		map = new int[width * height];
		mapTiles = new MapTile[width  * height];
		this.wallsAmt = wallsAmt;
		rayTexCoords = new float[wallsAmt];
		rayMapCoords = new Vec2i[wallsAmt];
		for(int i = 0; i < rayMapCoords.length; i++)
			rayMapCoords[i] = new Vec2i();
		wallsHeight = new int[wallsAmt];
		mapSquarePos = new Vec2f(0, 0);
		currentPlayerTileCoords = new Vec2f(0, 0);
		this.player = player;
		generateMap();
	}
	
	public Level(int tileSize, int wallsAmt, Player player, String filePath) 
	{
		loadLevel(filePath);
		Level.tileSize = tileSize;
		map = new int[width * height];
		mapTiles = new MapTile[width  * height];
		this.wallsAmt = wallsAmt;
		rayTexCoords = new float[wallsAmt];
		rayMapCoords = new Vec2i[wallsAmt];
		for(int i = 0; i < rayMapCoords.length; i++)
			rayMapCoords[i] = new Vec2i();
		wallsHeight = new int[wallsAmt];
		generateMap();
		mapSquarePos = new Vec2f(0, 0);
		currentPlayerTileCoords = new Vec2f(0, 0);
		this.player = player;
	}
	
	public void loadLevel(String filePath)
	{
		try
		{
			BufferedImage image = ImageIO.read(Level.class.getResource(filePath));
			int w = width = image.getWidth();
			int h = height = image.getHeight();
			mapImagePixels = new int[w * h];
			image.getRGB(0, 0, w, h, mapImagePixels, 0, w);
		}
		catch(IOException e)
		{
			
		}
	}
	
	public void generateMap() {
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++) 
			{
				if(mapImagePixels[x + y * width] == 0xffffffff)
					map[x + y * width] = 1;
				else if(mapImagePixels[x + y * width] == 0x000000ff)
					map[x + y * width] = 2;
			}
		}
	}
	
	public void renderWalls(Graphics g) 
	{
		for(int i = 0; i < wallsHeight.length; i++) 
		{
			int shade = wallsHeight[i] * 3;
			if(shade > 205) {
				shade = 205;
			}
			g.setColor(new Color(50 + shade, 50 + shade, 50 + shade));
			g.fillRect(i, Raycaster.HEIGHT / 2 - wallsHeight[i] , 1 , wallsHeight[i] * 2);
		}
	}
	
	public void renderWallTextures(Screen screen)
	{
		for(int x = 0; x < wallsHeight.length; x++)
		{
			if(rayMapCoords[x].x == 30 && rayMapCoords[x].y == 30)
				at = end;
			else
				at = wall;
			int yy = Raycaster.HEIGHT / 2 - 1;
			for(int y = Raycaster.HEIGHT / 2; y < wallsHeight[x] + Raycaster.HEIGHT / 2; y++)
			{
				if(y > Raycaster.HEIGHT - 1 || yy < 0)  break;	
				int xPix = mapRange(rayTexCoords[x], 0, tileSize, 0, 64) + 1;
				int yPix = mapRange(y, Raycaster.HEIGHT / 2, wallsHeight[x] + Raycaster.HEIGHT / 2, 32, 64);
				int yyPix = mapRange(yy, Raycaster.HEIGHT / 2, -wallsHeight[x] + Raycaster.HEIGHT / 2, 32, 0);
				screen.pixels[x + y * Raycaster.WIDTH] = at.pixels[(xPix & at.size - 1) + (yPix & at.size - 1) * at.size];
				screen.pixels[x  + yy * Raycaster.WIDTH] = at.pixels[(xPix & at.size - 1) + (yyPix & at.size- 1) * at.size];
				yy--;
			}
		}
	}
	
	public int mapRange(float val, int min1,int max1,int min2,int max2)
	{
		return (int) ((val - min1) / (max1 - min1) * (max2 - min2) + min2);
	}
	
	public void calcWalls(Player player)
	{
		for(int i = 0; i < player.rays.length; i++) 
		{

			Vec2f ray = Vec2f.sub(player.pos, player.rays[i].dir);
						
			float projectionPlaneDist = (float) (Raycaster.WIDTH / 2);
			float cos = (float) Math.cos(player.rays[i].angle - player.angle);
			float rayDirX = player.rays[i].dir.x;
			float rayDirY = player.rays[i].dir.y;
			rayTexCoords[i] = player.rays[i].touchingVert ? rayDirY % tileSize : rayDirX % tileSize;
			rayMapCoords[i].x = (int)((rayDirX / tileSize) - 0.0001f);
			rayMapCoords[i].y = (int)((rayDirY / tileSize) + 0.0001f);
			wallsHeight[i] = (int) (tileSize / ray.getMag()  / 2 * projectionPlaneDist / cos);
		}
	}
	
	public void renderMap(Graphics g)
	{
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++) 
			{
				float xPos1 = x * tileSize;
				float xPos2 = (x * tileSize) + tileSize;
				float yPos1 = y * tileSize;
				float yPos2 = (y * tileSize) + tileSize;
				mapTiles[x + y * width] = new MapTile(xPos1, xPos2, yPos1, yPos2);
				if(map[x + y * width] == 1) mapTiles[x + y * width].isBlock = true; 
				mapTiles[x + y * width].render(g, xPos1, xPos2, yPos1, yPos2);
			}
		}
	}
	
	public void render(Screen screen) 
	{
		renderWallTextures(screen);
	}
	
	public void render(Graphics g)
	{
		//renderWalls(g);
		renderMap(g);
	}
	
	public void tick(Player player, Raycaster game)
	{
		if(KeyInput.getKeyState(KeyEvent.VK_SPACE))
			game.isPaused = true;
		
		currentPlayerTileCoords.x = player.pos.x % tileSize;
		currentPlayerTileCoords.y = player.pos.y % tileSize;
		mapSquarePos.x = (int) (player.pos.x / tileSize);
		mapSquarePos.y = (int) (player.pos.y / tileSize);
		player.calcRays(currentPlayerTileCoords, mapSquarePos , this);
		calcWalls(player);
	}
	
	public void timerTick()
	{
		timer.tick();
	}
	
	public void startTimer()
	{
		timer.start();
	}
	
	public void pauseTimer()
	{
		timer.pause();
	}
	
	public void resumeTimer()
	{
		timer.resume();
	}
	
	public String getTimerTime()
	{
		return timer.getFormatedTime();
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
}
