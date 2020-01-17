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
	Memory rom;
	Memory ram;
	Processor cpu;
	LogListener log;
	
	Window window;
	IntBuffer screen;
	
	int fps		= 10;
	int cycles 	= fps * 100;  // 1000 Hz
	Process loop;
	
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
		jasm8.setParserVerbose(true);
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
		log.save("V:\\Development\\Assembly\\workspace\\jasm\\video\\video.log");
		
		/*
		 * run program
		 */
		loop = new Process(this, fps);
		loop.start();
	}
	
	// ==============================================================
	
	public void onOUT(int output)
	{
		int offset = 512;
		int x = 0;
		int y = 0;
		for (int i=0;i<192;i++)
		{
			int address = i+offset;
			byte pixel = ram.read(address);
			
			
			
			x++;
		}
		
		Log.out("out: "+output);
	}
	
	public void onEND(int x)
	{
		loop.stop();
	}
	
	// ==============================================================
	
	@Override
	public void onInit()
	{
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
