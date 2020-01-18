package com.nullpointerworks.jasm.app;

import static com.nullpointerworks.jasm.jasm8.Memory.*;

import java.io.FileNotFoundException;

import com.nullpointerworks.jasm.jasm8.Compiler;
import com.nullpointerworks.jasm.jasm8.LogListener;
import com.nullpointerworks.jasm.jasm8.Memory;
import com.nullpointerworks.jasm.jasm8.Monitor;
import com.nullpointerworks.jasm.jasm8.Processor;
import com.nullpointerworks.jasm.jasm8.compiler.CompilerJASM8;
import com.nullpointerworks.jasm.jasm8.compiler.GenericLog;
import com.nullpointerworks.jasm.jasm8.processor.InstructionsJASM8;
import com.nullpointerworks.jasm.jasm8.processor.Memory8bit;
import com.nullpointerworks.jasm.jasm8.processor.ProcessorJASM8;
import com.nullpointerworks.jasm.loop.Process;

import com.nullpointerworks.color.ColorFormat;
import com.nullpointerworks.color.Colorizer;
import com.nullpointerworks.core.Window;
import com.nullpointerworks.core.buffer.IntBuffer;
import com.nullpointerworks.game.LoopAdapter;
import com.nullpointerworks.util.Log;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class MainDisplay
extends LoopAdapter
implements InstructionsJASM8, Monitor
{
	private Memory rom;
	private Memory ram;
	private Processor cpu;
	private LogListener log;
	
	private Window window;
	private IntBuffer screen;
	private Colorizer rgb = Colorizer.getColorizer(ColorFormat.RGB);
	final int WHITE = rgb.toInt(255,255,255);
	final int DGREY = rgb.toInt(50,50,50);
	final int BLACK = rgb.toInt(0,0,0);
	
	private int fps		= 10;
	private int cycles 	= fps * 100;  // 1000 Hz
	private Process loop;
	
	public static void main(String[] args) {new MainDisplay(args);}

	/* 
	 * 32 kiB = 1024 * 32 = 32768
	 * 64 kiB = 1024 * 64 = 65536
	 * 
	 * address range
	 * 16 bit = 2^16 = 65536 = 0xFFFF = 0b1111 1111 1111 1111
	 * 64 kiB max
	 * 
	 */
	public MainDisplay(String[] args)
	{
		/*
		 * load text file
		 */
		TextFile tf = null;
		try
		{
			tf = TextFileParser.file("V:\\Development\\Assembly\\workspace\\jasm\\video\\video.jasm");
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		if (tf == null) return;
		
		/*
		 * make log
		 */
		log = new GenericLog();
		
		/*
		 * compile into jasm8
		 */
		Compiler jasm8 = new CompilerJASM8();
		jasm8.setParserVerbose(false);
		jasm8.setPreprocessorVerbose(true);
		jasm8.setCompilerVerbose(true);
		jasm8.setIncludesPath("V:\\Development\\Assembly\\workspace\\jasm\\video");
		jasm8.setLogListener(log);
		byte[] program = jasm8.parse(tf.getLines());

		/*
		 * setup virtual machine
		 */
		rom = new Memory8bit(   kiloByte ).load(program);
		ram = new Memory8bit( 4*kiloByte );
		cpu = new ProcessorJASM8(this, rom, ram);
		
		/*
		 * save log
		 */
		//log.save("V:\\Development\\Assembly\\workspace\\jasm\\video\\video.log");
		
		/*
		 * run program
		 */
		loop = new Process(this, fps);
		loop.start();
	}
	
	// ==============================================================
	
	public void onOUT(int output)
	{
		int x = 0;
		int y = 0;
		for (int i=0;i<768;i++)
		{
			int address = i+512;
			int pixel = ram.read(address)&0xff;
			
			if (pixel > 0)
			{
				rect20(x,y,WHITE);
			}
			else
			{
				rect20(x,y,BLACK);
			}
			
			x++;
			if (x>31) 
			{ y++; x=0; }
		}
		
		Log.out("refresh");
		window.swap(screen.content());
	}
	
	private void rect10(int x, int y, int col)
	{
		x*=10;
		y*=10;
		int sx = 0;
		int sy = 0;
		for (int i=0;i<100;i++)
		{
			screen.plot(sx+x, sy+y, col);
			sx++;
			if (sx>9) 
			{ sy++; sx=0; }
		}
	}
	
	private void rect20(int x, int y, int col)
	{
		x*=20;
		y*=20;
		int sx = 0;
		int sy = 0;
		for (int i=0;i<400;i++)
		{
			screen.plot(sx+x, sy+y, col);
			sx++;
			if (sx>19) 
			{ sy++; sx=0; }
		}
	}
	
	public void onEND(int x)
	{
		loop.stop();
	}
	
	// ==============================================================
	
	@Override
	public void onInit()
	{
		screen = new IntBuffer(640,480);
		window = new Window(640,480,"Display Playground JASM");
		window.setVisible(true);
	}
	
	@Override
	public void onUpdate(double time)
	{
		for (int i=0; i<cycles; i++) cpu.cycle();
	}
	
	@Override
	public void onRender(double interpolation) { }
	
	@Override
	public void onDispose() { }
}
