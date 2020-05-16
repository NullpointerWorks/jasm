package com.nullpointerworks.virtualmachine;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.InterruptListener;
import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.Parser;
import com.nullpointerworks.jasm.parser.ParserJASM;
import com.nullpointerworks.jasm.Compiler;
import com.nullpointerworks.jasm.compiler.Draft;
import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachineJASM;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

public class TestVirtualMachine implements InterruptListener
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
		/*
		 * load primary source file
		 */
		URL url = new URL(args[0]);
		
		/*
		 * add linker directory
		 */
		List<String> paths = new ArrayList<String>();
		paths.add(url.folderPath()); // always set source directory as primary linker
		for (int i=1,l=args.length; i<l; i++)
		{
			paths.add(args[i]);
		}
		
		/*
		 * parser
		 */
		Parser jasmParser = new ParserJASM();
		jasmParser.setVerbose(true);
		jasmParser.setIncludesPath(paths);
		jasmParser.parse(url.filePath());
		if (jasmParser.hasErrors())
		{
			var errors = jasmParser.getErrors();
			for (BuildError err : errors)
			{
				System.out.println( err.getDescription()+"\n" );
			}
			jasmParser = null;
		}
		
		/*
		 * compiler
		 */
		Compiler<Instruction> jasmCompiler = new InstructionCompiler();
		jasmCompiler.setVerbose(true);
		jasmCompiler.preprocess(jasmParser);
		if (jasmCompiler.hasErrors())
		{
			var errors = jasmCompiler.getErrors();
			for (BuildError err : errors)
			{
				System.out.println( err.getDescription()+"\n" );
			}
			jasmCompiler = null;
		}
		
		/*
		 * get compiler instruction draft
		 */
		List<Draft<Instruction>> instList = jasmCompiler.getDraft();
		List<Instruction> instructions = new ArrayList<Instruction>();
		for (Draft<Instruction> d : instList)
		{
			instructions.add(d.getInstruction());
		}
		
		/*
		 * prepare VM memory
		 */
		memory = new ArrayList<Integer>(2048);
		for (int l=2048; l>0;l--) memory.add(0);
		
		/*
		 * run instructions
		 */
		jasmVM = new VirtualMachineJASM(this);
		jasmVM.addInstructions(instructions);
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
