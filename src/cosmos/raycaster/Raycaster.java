package cosmos.raycaster;

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

import cosmos.raycaster.graphics.Screen;
import cosmos.raycaster.input.KeyInput;
import cosmos.raycaster.input.MouseInput;
import cosmos.raycaster.level.Level;
import cosmos.raycaster.level.Player;
import cosmos.raycaster.menu.MainMenu;
import cosmos.raycaster.menu.MenuOption;
import cosmos.raycaster.menu.PauseMenu;

public class Raycaster extends Canvas 
{	
	public static final boolean DEBUG = false;
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public  boolean isInPlay = false;
	public  boolean isPaused = false;
	private boolean timerHasStarted = false;
	
	private Thread thread;
	private boolean isRunning = false;
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
			new MenuOption("Start", 300, 100, () -> { startGame(); isInPlay = true;}),
			new MenuOption("Exit", 300, 100, () ->  { System.exit(0); })
	};
	private Color mainMenuColor = new Color(0.2f, 0.2f, 0.2f, 1.0f);
	
	private MenuOption pauseMenuOptions[] = {
			new MenuOption("Resume", 300, 100, () ->  { isPaused = false; }),
			new MenuOption("Quit", 300, 100, () ->   { isInPlay = false; isPaused = false; })
			};
	
	private MainMenu mainMenu = new MainMenu(mainMenuOptions, WIDTH, HEIGHT);
	private PauseMenu pauseMenu = new PauseMenu(pauseMenuOptions, WIDTH, HEIGHT);
	
	public Raycaster(int raysAmt)
	{
		this.raysAmt = raysAmt;
		d = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(d);
		frame = new JFrame("Game");
		screen = new Screen(WIDTH, HEIGHT);
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		startGame();
		addKeyListener(keyInput);
		addMouseMotionListener(mouseInput);
		addMouseListener(mouseInput);
	
		frame.addWindowListener(new WindowAdapter() {
			@Override
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
	
	public void startGame()
	{
		player = new Player(20, 20, raysAmt);
		level = new Level(15, raysAmt, player, "/maps/Level.png");
		timerHasStarted = false;
	}
	
	public void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		if(isInPlay && !isPaused)
		{
			pauseMenu.hide();
			mainMenu.hide();
			
			screen.clear();
			g.setColor(Color.black);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			
			level.render(screen);
			for(int i = 0; i < pixels.length; i++)
			{
				if( screen.pixels[i] == 0 && i < pixels.length / 2)
					pixels[i] = 0xadd8e6;
				else if(screen.pixels[i] == 0 && i >= pixels.length / 2)
					pixels[i] = 0x00ff00;
				else
					pixels[i] = screen.pixels[i];
			}
			g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);

			g.setFont(timerFont);
			g.drawString(level.getTimerTime(), 40, 70);
		}
		else if(isPaused)
		{
			pauseMenu.show();
			pauseMenu.render(g, null);
		}
		else
		{
			mainMenu.show();
			mainMenu.render(g, mainMenuColor);
		}
		if(DEBUG)
		{
			level.render(g);
			player.render(g);
		}
		g.dispose();
		bs.show();
	}
	
	public void tick() 
	{
		if(isInPlay && !isPaused)
		{
			if(!timerHasStarted)
			{
				level.startTimer();
				timerHasStarted = true;
			}
			level.resumeTimer();
			player.tick(level);
			level.tick(player, this);
			level.timerTick();
			if(level.checkWin(player))
				isInPlay = false;
		}
		else if(isPaused)
		{
			pauseMenu.tick(this);
			level.pauseTimer();
			level.timerTick();
		}
		else
		{
			mainMenu.tick(this);
		}
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
