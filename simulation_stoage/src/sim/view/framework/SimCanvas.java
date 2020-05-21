package sim.view.framework;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import map.cavas.DrawObject;
import sim.model.impl.stoage.atc.impl.CrossLandSideATC;
import sim.model.impl.stoage.atc.impl.CrossSeaSideATC;
import sim.model.impl.stoage.atc.impl.TwinLandSideATC;
import sim.model.impl.stoage.atc.impl.TwinSeaSideATC;
import sim.model.impl.stoage.block.Block;
import sim.model.impl.stoage.block.BlockManager;
import sim.view.SimMain;
import sim.view.obj.SimViewATC;
import sim.view.obj.SimViewBlock;

/**
 * @author LDCC
 *
 */
public class SimCanvas extends Canvas implements Runnable {

	protected ArrayList<DrawObject> draw = new ArrayList<DrawObject>();

	public void addDrawObject(DrawObject object) {
		draw.add(object);
	}

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 1920;

	public static final int HEIGHT = WIDTH * 9 / 16;

	public static final String TITLE = "YOUR GAMES NAME";

	public static final int TICKSPERS = 120;

	public static final boolean ISFRAMECAPPED = false;

	private SimMain main;

	private Thread thread;

	private boolean running = false;

	public int frames;

	public int lastFrames;

	public int ticks;

	public static JFrame frame;

	public SimCanvas(SimMain main) {
		this.main = main;

	}

	public synchronized void render() {

		frames++;
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}

		main.render();

		Graphics g = bs.getDrawGraphics();

		//g.setColor(new Color(79, 194, 232));

		g.setColor(Color.lightGray);
		g.fillRect(0, 0, getWidth(), getHeight());
		//Call your render funtions from here

		try {

			//Collections.reverse(draw);
			List<DrawObject> unmodifiableList = Collections.unmodifiableList(draw);

			//;

			for (DrawObject str : unmodifiableList) {
				str.draw(g);
			}

			g.setColor(Color.black);

			g.drawString(frameInfo, 0, getHeight() - 15);

		} catch (Exception e) {
			//e.printStackTrace();
		}

		g.dispose();
		bs.show();
	}

	/**
	 *
	 */
	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this, "Thread");
		thread.start();
	}

	/**
	 *
	 */
	public synchronized void stop(){
		if(!running) return;
		running = false;
		try {
			System.exit(1);
			frame.dispose();
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void init() {

	}

	String frameInfo;

	public void tick() {
	}

	@Override
	public void run() {
		init();
		//Tick counter variable
		long lastTime = System.nanoTime();
		//Nanoseconds per Tick
		double nsPerTick = 1000000000D / TICKSPERS;
		frames = 0;
		ticks = 0;
		long fpsTimer = System.currentTimeMillis();
		double delta = 0;
		boolean shouldRender;
		while (running) {
			shouldRender = !ISFRAMECAPPED;
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			//if it should tick it does this
			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			if (shouldRender) {

				render();
			}
			if (fpsTimer < System.currentTimeMillis() - 1000) {

				frameInfo = ticks + " ticks, " + frames + " frames";
				ticks = 0;
				lastFrames = frames;
				frames = 0;
				fpsTimer = System.currentTimeMillis();
			}
		}
	}

	public void addObject(Object obj) {
		if (obj instanceof TwinSeaSideATC) {

			TwinSeaSideATC atc = (TwinSeaSideATC) obj;
			addDrawObject(new SimViewATC(atc.getAtcID(), atc.getBlockID(), atc.getX(), BlockManager.magin, 0, 0));

		} else if (obj instanceof TwinLandSideATC) {
			TwinLandSideATC atc = (TwinLandSideATC) obj;
			addDrawObject(new SimViewATC(atc.getAtcID(), atc.getBlockID(), atc.getX(), BlockManager.magin, 0, 0));
		}

		else if (obj instanceof CrossLandSideATC) {

			CrossLandSideATC atc = (CrossLandSideATC) obj;
			addDrawObject(new SimViewATC(atc.getAtcID(), atc.getBlockID(), atc.getX(), BlockManager.magin, 0, 0));

		} else if (obj instanceof CrossSeaSideATC) {

			CrossSeaSideATC atc = (CrossSeaSideATC) obj;
			addDrawObject(new SimViewATC(atc.getAtcID(), atc.getBlockID(), atc.getX(), BlockManager.magin, 0, 0));
		}
		else if (obj instanceof Block) {
			Block block = (Block) obj;
			this.addDrawObject(new SimViewBlock(block.getBlockID(), block.getX(), block.getY()));
		}

	}

	public void clear() {
		draw.clear();

	}

	boolean isCountView = true;
	public void setCountView(boolean selected) {
		//Collections.reverse(draw);
		List<DrawObject> unmodifiableList = Collections.unmodifiableList(draw);

		//;

		for (DrawObject str : unmodifiableList) {
			str.setCountView(selected);
		}
	}

}
