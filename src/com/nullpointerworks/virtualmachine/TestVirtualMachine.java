package com.nullpointerworks.virtualmachine;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.compiler.Compiler;
import com.nullpointerworks.jasm.virtualmachine.InterruptListener;
import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachine;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachineJASM;

public class TestVirtualMachine extends TestCompiler implements InterruptListener
{
	public static void main(String[] args) 
	{
		//*
		args = new String[] 
		{
			"src\\playground.asm"
		};
		//*/
		
		new TestVirtualMachine(args);
	}
	
	private VirtualMachine jasmVM;
	private List<Integer> memory;
	
	public TestVirtualMachine(String[] args)
	{
		setParserVerbose(false);
		setPreProcessorVerbose(true);
		setCompilerVerbose(false);
		
		//runParser(args);
		//runPreprocessor(args);
		runCompiler(args);
		
		/*
		 * get compiler results
		 */
		Compiler compiler = getCompiler();
		if (compiler==null) return;
		
		/*
		 * prepare
		 */
		memory = new ArrayList<Integer>(2048);
		for (int l=2048; l>0;l--) memory.add(0);
		
		/*
		 * run instructions
		 */
		jasmVM = new VirtualMachineJASM(this);
		jasmVM.addInstructions(compiler.getInstructions());
		jasmVM.setMemory(memory);
		while(jasmVM.hasInstruction())
		{
			jasmVM.nextInstruction();
		}
	}
	
	@Override
	public void onInterrupt(VirtualMachine vm, int intcode)
	{
		switch(intcode)
		{
		case 0: // EXIT
			System.exit(0);
			
		case 10: // PRNT_A
			Register regA = vm.getRegister(Select.REG_A);
			System.out.println(""+regA.getValue());
			break;
			
		default: 
			break;
		}
	}
}
