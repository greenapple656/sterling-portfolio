package shimeji;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

import javax.swing.event.*;

import java.awt.image.*;


public class Mascot extends JFrame {

	private static final long serialVersionUID = 1L;
	private BufferedImage image = new BufferedImage(1, 1,
			BufferedImage.TYPE_INT_ARGB);
	private boolean stopped = false;

	// constructors
	public Mascot() {try {init();} catch (NoSuchMethodException e) {e.printStackTrace();}}

	// gets and sets
	public void setImage(BufferedImage bi) {
		if (!bi.equals(null)) {
			ImageIcon pic = new ImageIcon(bi);
			JLabel back = new JLabel(pic);
			this.setContentPane(back);
			this.setVisible(true);
		}
	}
	public void setStopped(boolean b){stopped=b;}
	/*temporary*/ public boolean getStopped(){return stopped;}

	// methods
	public void init() throws NoSuchMethodException {
		Actions.init();
		image = Actions.actionList[0].getImageSet()[0];
		this.setSize(image.getWidth(), image.getHeight());
		this.setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds().width / 2, GraphicsEnvironment
				.getLocalGraphicsEnvironment().getMaximumWindowBounds().height
				- image.getHeight());
		this.setContentPane(new JLabel(new ImageIcon(image)));

		MouseInputListener listener = new MouseInputListener();
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		
		// gets rid of the borders, title bar, etc.
		setUndecorated(true);
		// gives it a transparent background, but doesn't transparentize the
		// image
		setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.repaint();
	}

	public void nextAction() {
		while (!stopped) {
			Random random = new Random();
			int num = random.nextInt(3);
			Actions.setCurrentMascot(this);
			Actions.actionList[num].setContinue(1);
			while(Actions.actionList[num].getContinue()!=0){Actions.actionList[num].go();}
		}
		while(stopped)
		{
			for(Action a : Actions.actionList){a.setContinue(0);}
		}
	}
	
	
	class MouseInputListener extends MouseInputAdapter
	{
		int x;
		int y;
		public void mousePressed(MouseEvent e)
		{
			x=e.getX();
			y=e.getY();
			Mascot.this.setStopped(true);
			System.out.println("Pressed");
		}
		public void mouseDragged(MouseEvent e)
		{
			Mascot.this.setLocation(e.getXOnScreen()-x,e.getYOnScreen()-y);
		}
		public void mouseReleased(MouseEvent e)
		{
			System.out.println("Released");
			Mascot.this.setStopped(false);
			Actions.fall();
			Mascot.this.nextAction();
		}
	}
}
