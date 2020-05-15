package com.nullpointerworks.virtualmachine;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.InterruptListener;
import com.nullpointerworks.jasm.Parser;
import com.nullpointerworks.jasm.Preprocessor;
import com.nullpointerworks.jasm.Compiler;
import com.nullpointerworks.jasm.VirtualMachine;
import com.nullpointerworks.jasm.compiler.CompilerJASM;
import com.nullpointerworks.jasm.parser.ParserJASM;
import com.nullpointerworks.jasm.preprocessor.PreprocessorJASM;
import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.VirtualMachineJASM;

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
		 * parse text into code
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
		 * do pre-processing
		 */
		Preprocessor jasmPreProc = new PreprocessorJASM();
		jasmPreProc.setVerbose(true);
		jasmPreProc.preprocess(jasmParser);
		if (jasmPreProc.hasErrors())
		{
			var errors = jasmPreProc.getErrors();
			for (BuildError err : errors)
			{
				System.out.println( err.getDescription() );
			}
			jasmPreProc = null;
		}
		
		/*
		 * compile
		 */
		Compiler jasmCompiler = new CompilerJASM();
		jasmCompiler.setVerbose(true);
		jasmCompiler.compile(jasmPreProc);
		if (jasmCompiler.hasErrors())
		{
			var errors = jasmCompiler.getErrors();
			for (BuildError err : errors)
			{
				System.out.println( err.getDescription() );
			}
			jasmCompiler = null;
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
		jasmVM.addInstructions(jasmCompiler.getInstructions());
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
