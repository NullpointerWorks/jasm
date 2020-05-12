package com.nullpointerworks.virtualmachine;

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
			"D:/Development/Assembly/workspace/jasm/playground/playground.jasm",
		};
		//*/
		
		new TestVirtualMachine(args);
	}
	
	private VirtualMachine jasmVM;
	
	public TestVirtualMachine(String[] args)
	{
		setVerbose(true);
		runCompiler(args);
		
		/*
		 * get parser results
		 */
		Compiler compiler = getCompiler();
		if (compiler==null) return;
		
		/*
		 * run instructions
		 */
		jasmVM = new VirtualMachineJASM(this);
		jasmVM.addInstructions( compiler.getInstructions() );
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
