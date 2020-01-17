package com.nullpointerworks.jasm.app;

import static com.nullpointerworks.jasm.jasm8.Memory.*;

import java.io.FileNotFoundException;

import com.nullpointerworks.game.LoopAdapter;
import com.nullpointerworks.jasm.jasm8.Compiler;
import com.nullpointerworks.jasm.jasm8.Memory;
import com.nullpointerworks.jasm.jasm8.Monitor;
import com.nullpointerworks.jasm.jasm8.Processor;
import com.nullpointerworks.jasm.jasm8.compiler.CompilerJASM8;
import com.nullpointerworks.jasm.jasm8.parts.InstructionsJASM8;
import com.nullpointerworks.jasm.jasm8.parts.Memory8bit;
import com.nullpointerworks.jasm.jasm8.parts.ProcessorJASM8;
import com.nullpointerworks.jasm.loop.Process;
import com.nullpointerworks.util.Log;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class PlaygroundMain
extends LoopAdapter
implements InstructionsJASM8, Monitor
{
	Process loop;
	Memory rom; // read-only memory
	Memory ram; // random access memory
	Processor cpu; // assembly executor
	
	byte[] program = null;
	
	int fps		= 10;
	int cycles 	= fps * 100;  // ~ 1 kHz
	
	public static void main(String[] args) {new PlaygroundMain(args);}

	/* 
	 * 32 kiB = 1024 * 32 = 32768
	 * 64 kiB = 1024 * 64 = 65536
	 * 
	 * address range
	 * 16 bit = 2^16 = 65536 = 0xFFFF = 0b1111 1111 1111 1111
	 * 64 kiB max
	 * 
	 * 
	 * TODO error and information
	 * 
	 * notify about unused labels
	 * error about unknown label jumps
	 * error when detecting duplicate labels
	 * error when two labels are defined in sequence
	 * 
	 */
	public PlaygroundMain(String[] args)
	{
		/*
		 * load text file
		 */
		TextFile tf = null;
		try
		{
			tf = TextFileParser.file("D:\\Development\\Assembly\\workspace\\jasm\\playground.jasm");
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		if (tf == null) return;
		
		/*
		 * compile into jasm8
		 */
		Compiler jasm8 = new CompilerJASM8();
		jasm8.setParserVerbose(false);
		jasm8.setPreprocessorVerbose(false);
		jasm8.setCompilerVerbose(false);
		jasm8.setIncludesPath("D:\\Development\\Assembly\\workspace\\jasm\\");
		program = jasm8.parse(tf.getLines());
		
		/*
		 * run program
		 */
		loop = new Process(this, fps);
		loop.start();
	}
	
	// ==============================================================
	
	@Override
	public void onInit()
	{
		/*
		 * setup virtual machine
		 */
		rom = new Memory8bit(   kiloByte ).load(program);
		ram = new Memory8bit( 4*kiloByte );
		cpu = new ProcessorJASM8(this,rom,ram);
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
	
	// ==============================================================
	
	public void onOUT(int x)
	{
		Log.out("out: "+x);
	}
	
	public void onEND(int x)
	{
		loop.stop();
	}
}
