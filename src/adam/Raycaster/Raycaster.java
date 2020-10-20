package adam.Raycaster;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import adam.Raycaster.graphics.Screen;
import adam.Raycaster.input.KeyInput;
import adam.Raycaster.input.MouseInput;
import adam.Raycaster.menu.MainMenu;
import adam.Raycaster.menu.MenuOption;

public class Raycaster extends Canvas 
{	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	private Thread thread;
	private boolean isRunning;
	private JFrame frame;
	private Dimension d;
	private Level level;
	private Player player;
	private int raysAmt;
	private Screen screen;
	private BufferedImage image;
	private int pixels[];
	private Font timerFont = new Font("LucidaSans", Font.BOLD, 50);
	
	private KeyInput keyInput = new KeyInput();
	private MouseInput mouseInput = new MouseInput();

	
	private MenuOption mainMenuOptions[] = {
			new MenuOption("Start", 300, 100),
			new MenuOption("Quit", 300, 100),
		};
	private MainMenu mainMenu = new MainMenu(mainMenuOptions, WIDTH, HEIGHT);
	
	public Raycaster(int raysAmt)
	{
		this.raysAmt = raysAmt;
		d = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(d);
		frame = new JFrame("Game");
		screen = new Screen(WIDTH, HEIGHT);
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		player = new Player(20, 20, raysAmt);
		level = new Level(15, raysAmt, player, "/maps/Level.png");
		addKeyListener(keyInput);
		addMouseMotionListener(mouseInput);
		addMouseListener(mouseInput);
	
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				isRunning = false;
			}
		});
	}
	
	public synchronized void start()
	{
		requestFocus();
		isRunning = true;
		thread = new Thread(() ->
		{
			long lastTime = System.nanoTime();
			long timer = System.currentTimeMillis();
			final double ns = 1000000000.0 / 60.0;
			double delta = 0.0;
			int fps = 0;
			while(isRunning) 
			{
				long now = System.nanoTime();
				delta+= (now - lastTime) / ns;
				lastTime = now;
				while(delta >= 1)
				{
					tick();
					delta--;
				}
				if(System.currentTimeMillis() - timer >= 1000)
				{
					frame.setTitle("FPS: " + fps);
					fps = 0;
					timer += 1000;
				}
				render();
				fps++;
			}
			stop();
		});
		thread.start();
	}
	
	public synchronized void stop()
	{
		try
		{
			thread.join();
		} 
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		screen.clear();
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		level.render(screen);
		for(int i = 0; i < pixels.length; i++)
		{
			if( screen.pixels[i] == 0 && i < pixels.length / 2)
			{
				pixels[i] = 0xadd8e6;
			}
			else if(screen.pixels[i] == 0 && i >= pixels.length / 2)
			{
				pixels[i] = 0x00ff00;
			}
			else
			{
				pixels[i] = screen.pixels[i];
			}
		}
		g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
		//level.render(g);
		//player.render(g);
		
		g.setFont(timerFont);
		g.drawString(level.getTimerTime(), 40, 70);
		
		mainMenu.hide();
		mainMenu.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public void tick() 
	{
		mainMenu.tick();
		player.tick(level);
		level.tick(player);
	}

	public static void main(String[] args)
	{
		Raycaster gameTwo = new Raycaster(WIDTH);
		gameTwo.frame.setResizable(false);
		gameTwo.frame.add(gameTwo);
		gameTwo.frame.pack();
		gameTwo.frame.setLocationRelativeTo(null);
		gameTwo.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameTwo.frame.setVisible(true);
		gameTwo.start();
	}

}
