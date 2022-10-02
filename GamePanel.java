package snakeGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener
{
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int pumpkinsEaten;
	int pumpkinX;
	int pumpkinY;
	int bombX;
	int bombY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	GamePanel()
	{
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.gray);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		start();
	}
	public void start()
	{
		newPumpkin();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g)
	{
		if(running)
		{	
			g.setColor(new Color(255,104,0));
			g.fillOval(pumpkinX,pumpkinY , UNIT_SIZE, UNIT_SIZE);
			g.setColor(Color.black);
			g.fillOval(bombX,bombY , UNIT_SIZE, UNIT_SIZE);
			for(int i = 0; i<bodyParts; i++)
			{
				if(i==0)
				{
					g.setColor(new Color(244,244,244));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else
				{
					g.setColor(Color.white);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + pumpkinsEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + pumpkinsEaten))/2, g.getFont().getSize());
		}
		else
		{
			gameOver(g);
		}
	}
	public void newPumpkin()
	{
		pumpkinX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		pumpkinY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
	}
	public void newBomb()
	{
		bombX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		bombY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		
	}
	public void move()
	{
		for(int i = bodyParts; i>0;i--)
		{
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		switch(direction)
		{
		case'U':
			y[0]=y[0]-UNIT_SIZE;
			break;
		case'D':
			y[0]=y[0]+UNIT_SIZE;
			break;
		case'L':
			x[0]=x[0]-UNIT_SIZE;
			break;
		case'R':
			x[0]=x[0]+UNIT_SIZE;
			break;
		}
	}
	public void checkPumpkin()
	{
		if((x[0]==pumpkinX)&&(y[0]==pumpkinY))
		{
			bodyParts++;
			pumpkinsEaten++;
			newPumpkin();
		}
		for(int i = bodyParts; i>0; i--)
		{
			if((x[i] == pumpkinX)&&(y[i] == pumpkinY))
				newPumpkin();
		}
	}
	public void checkCollision()
	{
		for(int i = bodyParts; i>0; i--)
		{
			if((x[0] == x[i])&&(y[0] == y[i]))
				running = false;
		}
		if(x[0]<0) 
			running = false;
		if(x[0]> SCREEN_WIDTH)
			running = false;
		if(y[0]<0)
			running = false;
		if(y[0]> SCREEN_HEIGHT)
			running = false;
		if(!running)
			timer.stop();
		
	}
	public void gameOver(Graphics g) 
	{
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + pumpkinsEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + pumpkinsEaten))/2, g.getFont().getSize());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running)
		{
			move();
			checkPumpkin();
			checkCollision();
		}
		repaint();
		
	}
	public class MyKeyAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e) 
		{
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_LEFT:
				if(direction !='R')
					direction= 'L';
				break;
			case KeyEvent.VK_RIGHT:
				if(direction !='L')
					direction= 'R';
				break;
			case KeyEvent.VK_UP:
				if(direction !='D')
					direction= 'U';
				break;
			case KeyEvent.VK_DOWN:
				if(direction !='U')
					direction= 'D';
				break;
			}
		}
	}
	
}
