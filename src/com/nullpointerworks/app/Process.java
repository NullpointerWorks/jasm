package com.nullpointerworks.app;

import com.nullpointerworks.game.LoopListener;

public class Process implements Runnable
{
	private final long NANO 		= 1_000_000_000;// 10^9
	private final double inv_NANO 	= 1.0 / NANO;	// 10^-9
	
	private Thread thread;
	private boolean running 	= false;
	private double UPDATE_CAP 	= 1d / 60d;
	private int SLEEP_TIME 		= (int)(UPDATE_CAP*1000d);
	private LoopListener e;
	
	/**
	 * Creates a new {@code AsapLoop} object that drives the provided {@code LoopListener} event methods.
	 * @param looplistener - the loop event listener to listen
	 * @param fps - the desired frames per second
	 * @since 1.0.0
	 */
	public Process(LoopListener looplistener, int fps)
	{
		e = looplistener;
		setTargetFPS(fps);
	}
	
	/**
	 * Set the desired frames per second.
	 * @param fps - the desired frames per second
	 * @since 1.0.0
	 */
	public void setTargetFPS(int fps) 
	{
		UPDATE_CAP	= 1d / (double)fps;
		SLEEP_TIME	= (int)(UPDATE_CAP*1000d) - 1;
	}
	
	/**
	 * Start the game loop in a new thread.
	 * @since 1.0.0
	 */
	public void start()
	{
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Stops the main game loop thread.
	 * @since 1.0.0
	 */
	public void stop()
	{
		running = false;
	}
	
	@Override
	public void run()
	{
		running = true;
		double d_Time = 0; // delta time
		double s_Time = 0; // spare unprocessed time
		double c_Time = 0; // current time
		double p_Time = System.nanoTime() * inv_NANO; // previous time
		e.onInit();
		
		while(running)
		{
			c_Time = System.nanoTime() * inv_NANO;
			d_Time = c_Time - p_Time;
			p_Time = c_Time;
			s_Time += d_Time;
			
			while(s_Time >= UPDATE_CAP)
			{
				s_Time -= UPDATE_CAP;
				e.onUpdate(UPDATE_CAP);
			}
			
			try 
			{
				Thread.sleep(SLEEP_TIME);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		
		e.onDispose();
	}
}