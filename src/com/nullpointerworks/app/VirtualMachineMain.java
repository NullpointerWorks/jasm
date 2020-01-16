package com.nullpointerworks.app;

import java.io.IOException;

import com.nullpointerworks.game.LoopAdapter;
import com.nullpointerworks.util.Log;
import com.nullpointerworks.util.file.bytefile.ByteFile;
import com.nullpointerworks.util.file.bytefile.ByteFileParser;

import com.nullpointerworks.jasm8.Memory;
import com.nullpointerworks.jasm8.Monitor;
import com.nullpointerworks.jasm8.Processor;
import com.nullpointerworks.jasm8.parts.ProcessorJASM8;
import com.nullpointerworks.jasm8.parts.InstructionsJASM8;
import com.nullpointerworks.jasm8.parts.Memory8bit;
import static com.nullpointerworks.jasm8.Memory.*;

public class VirtualMachineMain
extends LoopAdapter
implements InstructionsJASM8, Monitor
{
	Process asap;
	Memory rom; // read-only memory
	Memory ram; // random access memory
	Processor cpu; // assembly executor
	byte[] program = null;
	
	public static void main(String[] args) {new VirtualMachineMain(args);}
	
	public VirtualMachineMain(String[] args)
	{
		/*
		 * load binary file
		 */
		ByteFile bf = null;
		try
		{
			bf = ByteFileParser.file("D:\\Development\\Assembly\\workspace\\jasm\\multiply.bin");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (bf == null) return;
		
		program = bf.getBytes();
		
		/*
		 * run program
		 */
		asap = new Process(this, 200);
		asap.start();
	}
	
	// ==============================================================
	
	@Override
	public void onInit()
	{
		rom = new Memory8bit(   kiloByte ).load(program);
		ram = new Memory8bit( 4*kiloByte );
		cpu = new ProcessorJASM8(this,rom,ram);
	}
	
	@Override
	public void onUpdate(double time)
	{
		for (int i=0; i<1000; i++) cpu.cycle();
	}
	
	@Override
	public void onRender(double interpolation) 
	{ }
	
	@Override
	public void onDispose() 
	{ }
	
	// ==============================================================
	
	public void onOUT(int x)
	{
		Log.out("out: "+x);
	}
	
	public void onEND(int x)
	{
		asap.stop();
	}
}
