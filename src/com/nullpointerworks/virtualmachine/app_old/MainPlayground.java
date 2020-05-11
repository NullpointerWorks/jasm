package com.nullpointerworks.virtualmachine.app_old;

import java.io.IOException;

import com.nullpointerworks.jasm.compiler_old.InstructionsJASM;
import com.nullpointerworks.jasm.compiler_old.Memory;
import com.nullpointerworks.jasm.compiler_old.Monitor;
import com.nullpointerworks.jasm.compiler_old.Processor;
import com.nullpointerworks.jasm.compiler_old.Register;
import com.nullpointerworks.game.LoopAdapter;
import com.nullpointerworks.util.Log;
import com.nullpointerworks.util.StringUtil;
import com.nullpointerworks.util.concurrency.Threading;
import com.nullpointerworks.util.file.bytefile.ByteFile;
import com.nullpointerworks.util.file.bytefile.ByteFileParser;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

/*
 * setting cpu cycles-per-second:
 * -c[int]
 * example: 
 * -c10000
 * 
 * setting rom and ran size in bytes
 * -ram[int]
 * -rom[int]
 * example: 
 * -rom1024 -ram4096
 * 
 */
class MainPlayground
extends LoopAdapter
implements InstructionsJASM, Monitor
{
	private Process loop;
	private Memory rom; // read-only memory
	private Memory ram; // random access memory
	private Processor cpu; // assembly executor
	
	private byte[] program = null;
	
	private int fps 		= 30;
	private float spare 	= 0f;
	private float cycles 	= 1f / 10000f; // 10 kHz
	
	private int rom_size 	= Memory.kiloByte; 	// 1 kilobyte default
	private int ram_size 	= Memory.kiloByte*4; // 4 kilobytes default
	
	public static void main(String[] args) {new MainPlayground(args);}
	
	public MainPlayground(String[] args)
	{
		args = new String[] {"D:\\Development\\Assembly\\workspace\\jasm\\playground\\playground.bin"};
		playground(args);
	}
	
	private void playground(String[] args)
	{
		if (args.length == 0) 
		{
			TextFile tf = new TextFile();
			tf.setEncoding("UTF-8");
			tf.addLine("@echo off\r\n");
			tf.addLine("java -jar \"jasm.jar\" %1\r\n");
			tf.addLine("pause\r\n");
			
			try
			{
				TextFileParser.write("run.bat", tf);
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			return;
		}
		
		Threading.sleep(100);
		
		/*
		 * get arguments
		 */
		String file = null;
		for (String arg : args)
		{
			if (arg.startsWith("-"))
			{
				if (arg.startsWith("-c"))
				{
					arg = arg.substring(2);
					if (StringUtil.isInteger(arg))
					{
						cycles = 1f / (float)Integer.parseInt(arg);
					}
					else
					{
						// error
					}
				}
				
				if (arg.startsWith("-rom"))
				{
					arg = arg.substring(4);
					if (StringUtil.isInteger(arg))
					{
						rom_size = Integer.parseInt(arg);
					}
					else
					{
						// error
					}
				}
				
				if (arg.startsWith("-ram"))
				{
					arg = arg.substring(4);
					if (StringUtil.isInteger(arg))
					{
						ram_size = Integer.parseInt(arg);
					}
					else
					{
						// error
					}
				}
				continue;
			}
			else
			{
				file = arg;
				break;
			}
		}
		if (file == null) return;
		
		/*
		 * load file
		 */
		ByteFile bf = null;
		try 
		{
			bf = ByteFileParser.file(file);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		if (bf == null) return;
		
		/*
		 * start program
		 */
		program = bf.getBytes();
		loop = new Process(this, fps);
		loop.start();
	}
	
	// ==============================================================
	
	@Override
	public void onInit()
	{
		rom = new Memory8bit( rom_size ).load(program);
		ram = new Memory8bit( ram_size );
		cpu = new ProcessorBinaryJASM(this, rom, ram);
		
		Log.out(
		"          _    _    ________  __ \r\n" + 
		"         | |  / \\  / ____   \\/  |\r\n" + 
		"      _  | | / _ \\ \\___ \\| |\\/| |\r\n" + 
		"     | |_| // ___ \\____) | |  | |\r\n" + 
		"      \\___//_/   \\______/|_|  |_|\r\n\r\n" + 
		"    Java 8-bit binary virtual machine\r\n" + 
		"    JASM Version "+cpu.getVersion()+"\n");
		
	}
	
	@Override
	public void onUpdate(double time)
	{
		spare += time;
		while (cycles < spare)
		{
			cpu.cycle();
			spare -= cycles;
		}
	}
	
	@Override
	public void onRender(double interpolation) { }
	
	@Override
	public void onDispose() { }
	
	// ==============================================================
	
	@Override
	public void onInterrupt(Processor prog, int code) 
	{
		System.out.println("interrupt : "+code);
		switch(code)
		{
		case 0:
			int regV = prog.getRegister(Register.RA);
			System.out.println(""+regV);
			regV = prog.getRegister(Register.RB);
			System.out.println(""+regV);
			regV = prog.getRegister(Register.RC);
			System.out.println(""+regV);
			regV = prog.getRegister(Register.RD);
			System.out.println(""+regV);
			break;
			
		default: 
			System.exit(0);
		}
	}
}
