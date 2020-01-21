package com.nullpointerworks.jasm;

import static com.nullpointerworks.jasm.jasm8.Memory.*;

import java.io.IOException;

import com.nullpointerworks.jasm.jasm8.Memory;
import com.nullpointerworks.jasm.jasm8.Monitor;
import com.nullpointerworks.jasm.jasm8.Processor;
import com.nullpointerworks.jasm.jasm8.processor.InstructionsJASM8;
import com.nullpointerworks.jasm.jasm8.processor.Memory8bit;
import com.nullpointerworks.jasm.jasm8.processor.ProcessorJASM8;
import com.nullpointerworks.game.LoopAdapter;
import com.nullpointerworks.util.StringUtil;
import com.nullpointerworks.util.concurrency.Threading;
import com.nullpointerworks.util.file.bytefile.ByteFile;
import com.nullpointerworks.util.file.bytefile.ByteFileParser;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class MainPlayground
extends LoopAdapter
implements InstructionsJASM8, Monitor
{
	private Process loop;
	private Memory rom; // read-only memory
	private Memory ram; // random access memory
	private Processor cpu; // assembly executor
	
	private byte[] program = null;
	
	private int fps			= 16;
	private int cycles 		= fps * 625;  	// 10 kHz default
	private int rom_size 	= kiloByte; 	// 1 kilobyte default
	private int ram_size 	= kiloByte*4; 	// 4 kilobytes default
	
	public static void main(String[] args) {new MainPlayground(args);}
	
	public MainPlayground(String[] args)
	{
		//args = new String[] {"D:\\Development\\Java\\export\\appJASM8\\playground.bin"};
		playground(args);
	}
	
	private void playground(String[] args)
	{
		if (args.length == 0) 
		{
			TextFile tf = new TextFile();
			tf.setEncoding("UTF-8");
			tf.addLine("@java -jar \"jasm.jar\" %1\r\n");
			tf.addLine("@pause\r\n");
			
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
			if (arg.startsWith("-"))
			{
				if (arg.startsWith("-c"))
				{
					arg = arg.substring(2);
					if (StringUtil.isInteger(arg))
					{
						int desCycles = Integer.parseInt(arg);
						//to ensure 16 Hz program updates, divide desired cycles by 16 ( >> 4 )
						desCycles = desCycles >> 4;
						cycles = fps * desCycles;
						if (cycles<1)cycles=1;
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
	
	public void onOUT(int x) {System.out.println(""+x);}
	
	public void onEND(int x) {loop.stop();}
}
