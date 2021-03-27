package com.nullpointerworks.jasm.test;

import static com.nullpointerworks.jasm.vm.VMRegister.REG_A;
import static com.nullpointerworks.jasm.vm.VMRegister.REG_B;
import static com.nullpointerworks.jasm.vm.VMRegister.REG_C;
import static com.nullpointerworks.jasm.vm.VMRegister.REG_D;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.vm.BytecodeVirtualMachine;
import com.nullpointerworks.jasm.vm.InterruptListener;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMProcessException;
import com.nullpointerworks.jasm.vm.VirtualMachine;

public class MainExecutionTest implements InterruptListener
{
	public static void main(String[] args) 
	{
		new MainExecutionTest("src/com/nullpointerworks/jasm/test/program.bin");
	}
	
	public MainExecutionTest(String file)
	{
		List<Integer> code = new ArrayList<Integer>();
		loadFile(file,code);
		
		/*
		 * test the assembled bytecode
		 */
		VirtualMachine vm = new BytecodeVirtualMachine();
		vm.setInterruptListener(this);
		vm.setMemorySize(2048);
		vm.setMemory(0,code);
		
		while (!vm.hasException())
		{
			vm.nextInstruction();
		}
		while (vm.hasException())
		{
			VMProcessException ex = vm.getException();
			System.err.println( ex.getMemoryTrace() );
		}
	}

	@Override
	public void onInterrupt(VirtualMachine vm, int code) 
	{
		if (code == 0)
		{
			System.exit(0);
			return;
		}
		
		if (code == 1)
		{
			Register reg = vm.getRegister( REG_A );
			System.out.println( "A: "+reg.getValue() );
			return;
		}
		
		if (code == 2)
		{
			Register reg = vm.getRegister( REG_B );
			System.out.println( "B: "+reg.getValue() );
			return;
		}
		
		if (code == 3)
		{
			Register reg = vm.getRegister( REG_C );
			System.out.println( "C: "+reg.getValue() );
			return;
		}
		
		if (code == 4)
		{
			Register reg = vm.getRegister( REG_D );
			System.out.println( "D: "+reg.getValue() );
			return;
		}
	}
	
	private void loadFile(String filename, List<Integer> code)
	{
		File f = new File(filename);
		byte[] bytes = null;
		
		try 
		{
			Path path = Paths.get( f.getAbsolutePath() );
			bytes = Files.readAllBytes(path);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if (bytes==null)
		{
			return;
		}
		
		int i = 0;
		int l = bytes.length;
		for (; i<l; i+=4)
		{
			byte b1 = bytes[i];
			byte b2 = bytes[i+1];
			byte b3 = bytes[i+2];
			byte b4 = bytes[i+3];
			
			int i1 = (b1 << 24);
			int i2 = (b2 << 16);
			int i3 = (b3 << 8);
			int i4 = (b4);
			
			int bytecode = i1|i2|i3|i4;
			
			code.add(bytecode);
		}
	}
}
