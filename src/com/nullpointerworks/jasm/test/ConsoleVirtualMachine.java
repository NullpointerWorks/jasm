package com.nullpointerworks.jasm.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.assembly.*;
import com.nullpointerworks.jasm.assembly.Compiler;
import com.nullpointerworks.jasm.assembly.compiler.InstructionCompiler;
import com.nullpointerworks.jasm.assembly.drafting.Draft;
import com.nullpointerworks.jasm.assembly.drafting.DraftAssembler;
import com.nullpointerworks.jasm.assembly.errors.*;
import com.nullpointerworks.jasm.assembly.parser.Definition;
import com.nullpointerworks.jasm.assembly.parser.SourceCode;
import com.nullpointerworks.jasm.assembly.parser.SourceParser;
import com.nullpointerworks.jasm.virtualmachine.*;
import com.nullpointerworks.util.Convert;
import com.nullpointerworks.util.StringUtil;
import com.nullpointerworks.util.file.Encoding;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class ConsoleVirtualMachine implements InterruptListener
{
	private static final String CRNL = "\r\n";

	public static void main(String[] args) 
	{
		//*
		args = new String[] 
		{
			"F:\\Development\\Assembly\\workspace\\jasm\\math\\playground.jasm",
			"-LF:\\Development\\Assembly\\workspace\\jasm\\math\\math\\",
			"-M4096",
			"-V"
		};
		//*/
		
		if (args.length < 1)
		{
			makeBatch();
			makeCVMDef();
			makePlayGround();
			return;
		}
		
		var vm = new ConsoleVirtualMachine();
		vm.runVirtualMachine(args);
	}
	
	// ================================================================================
	// SEFT-CREATION
	// ================================================================================
	
	private static void makeBatch()
	{
		TextFile tf = new TextFile();
		tf.addLine("@echo off"+CRNL);
		tf.addLine("REM add \"-L<absolute\\path\\to\\include\\folder>\" to set an include folder"+CRNL);
		tf.addLine("REM add \"-M2048\" parameter to set the internal memory size of 2048 integers"+CRNL);
		tf.addLine("REM add \"-VPC\" parameter to enable verbose parser(P) and verbose compiler(C)"+CRNL);
		tf.addLine("java -jar \"cvm.jar\" \"playground.jasm\" -M2048 -VPC"+CRNL);
		tf.addLine("pause"+CRNL);
		tf.setEncoding(Encoding.UTF8);
		try
		{
			TextFileParser.write("run.bat", tf);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		tf.clear();
	}
	
	private static void makeCVMDef()
	{
		File f = new File("defcvm.jasm");
		if (f.exists()) return;
		
		TextFile tf = new TextFile();
		tf.addLine("; ==========================="+CRNL);
		tf.addLine("; exit codes"+CRNL);
		tf.addLine("; ==========================="+CRNL);
		tf.addLine(".def EXIT 0"+CRNL);
		tf.addLine(".def BAD_EXIT 1"+CRNL);
		tf.addLine("; ==========================="+CRNL);
		tf.addLine("; print register"+CRNL);
		tf.addLine("; ==========================="+CRNL);
		tf.addLine(".def PRINT_A 10"+CRNL);
		tf.addLine(".def PRINT_B 11"+CRNL);
		tf.addLine(".def PRINT_C 12"+CRNL);
		tf.addLine(".def PRINT_D 13"+CRNL);
		
		tf.addLine(".def PRINT_I 14"+CRNL);
		tf.addLine(".def PRINT_J 15"+CRNL);
		tf.addLine(".def PRINT_K 16"+CRNL);
		
		tf.addLine(".def PRINT_U 17"+CRNL);
		tf.addLine(".def PRINT_V 18"+CRNL);
		tf.addLine(".def PRINT_W 19"+CRNL);
		
		tf.addLine(".def PRINT_X 20"+CRNL);
		tf.addLine(".def PRINT_Y 21"+CRNL);
		tf.addLine(".def PRINT_Z 22"+CRNL);
		tf.setEncoding(Encoding.UTF8);
		try
		{
			TextFileParser.write("defcvm.jasm", tf);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		tf.clear();
	}
	
	private static void makePlayGround()
	{
		File f = new File("playground.jasm");
		if (f.exists()) return;
		
		TextFile tf = new TextFile();
		tf.addLine(".inc \"defcvm.jasm\""+CRNL);
		tf.addLine("jmp main"+CRNL+CRNL);
		tf.addLine("; ==========================="+CRNL);
		tf.addLine("; program entry"+CRNL);
		tf.addLine("; ==========================="+CRNL);
		tf.addLine("main:"+CRNL);
		tf.addLine("    int EXIT"+CRNL);
		tf.setEncoding(Encoding.UTF8);
		try
		{
			TextFileParser.write("playground.jasm", tf);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		tf.clear();
	}
	
	// ================================================================================
	// VIRTUAL MACHINE
	// ================================================================================
	
	private InstructionVirtualMachine jasmVM;
	private int sizeMemory = 1024 * 4;
	private boolean verboseParser = false;
	private boolean verboseCompiler = false;
	
	public void runVirtualMachine(String[] args)
	{
		/*
		 * load primary source file
		 */
		PathBuilder url = new PathBuilder(args[0]);
		List<String> paths = new ArrayList<String>();
		paths.add(url.folderPath()); // always set source directory as primary linker
		
		/*
		 * parse parameters
		 */
		for (int i=1,l=args.length; i<l; i++)
		{
			String str = args[i];
			
			if (str.startsWith("-L"))
			{
				str = str.substring(2);
				PathBuilder incPath = new PathBuilder(str);
				paths.add(incPath.folderPath());
			}
			
			if (str.startsWith("-M"))
			{
				str = str.substring(2);
				if (StringUtil.isInteger(str))
				{
					int memSize = Convert.toInt(str);
					if (memSize>0) sizeMemory = memSize;
				}
			}
			
			if (str.startsWith("-V"))
			{
				str = str.substring(2);
				if (str.contains("P"))
				{
					verboseParser=true;
				}
				if (str.contains("C"))
				{
					verboseCompiler=true;
				}
			}
		}
		
		/*
		 * parser
		 */
		Parser jasmParser = new SourceParser();
		jasmParser.setVerbose(verboseParser);
		jasmParser.setIncludesPath(paths);
		jasmParser.parse(url.filePath());
		if (jasmParser.hasErrors())
		{
			var errors = jasmParser.getErrors();
			for (BuildError err : errors)
			{
				System.out.println( err.getDescription()+"\n" );
			}
			return;
		}
		List<Definition> definitions = jasmParser.getDefinitions();
		List<SourceCode> sourceCode = jasmParser.getSourceCode();
		
		/*
		 * assemble draft
		 */
		Assembler asmBuilder = new DraftAssembler();
		asmBuilder.setVerbose(verboseCompiler);
		asmBuilder.setSourceCode(sourceCode);
		asmBuilder.setDefinitions(definitions);
		asmBuilder.assemble();
		if (asmBuilder.hasErrors())
		{
			var errors = asmBuilder.getErrors();
			for (BuildError err : errors)
			{
				System.out.println( err.getDescription()+"\n" );
			}
			return;
		}
		List<Draft> draft = asmBuilder.getDraft();
		
		/*
		 * compile draft to instructions
		 */
		Compiler<Instruction> builder = new InstructionCompiler();
		builder.setDraft(draft);
		builder.build();
		List<Instruction> instructions = builder.getInstructions();
		
		/*
		 * run instructions
		 */
		jasmVM = new InstructionVM();
		jasmVM.setInterruptListener(this);
		jasmVM.setInstructions(instructions);
		jasmVM.setMemorySize(sizeMemory);
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
			break;
			
		case 1: // ERROR EXIT
			System.exit(1);
			break;
			
			
		case 10: // PRNT_A
			Register regA = vm.getRegister(Select.REG_A);
			System.out.println(""+regA.getValue());
			break;
			
		case 11: // PRNT_B
			Register regB = vm.getRegister(Select.REG_B);
			System.out.println(""+regB.getValue());
			break;
			
		case 12: // PRNT_C
			Register regC = vm.getRegister(Select.REG_C);
			System.out.println(""+regC.getValue());
			break;
			
		case 13: // PRNT_D
			Register regD = vm.getRegister(Select.REG_D);
			System.out.println(""+regD.getValue());
			break;
			
			
			
		case 14: // PRNT_I
			Register regI = vm.getRegister(Select.REG_I);
			System.out.println(""+regI.getValue());
			break;
			
		case 15: // PRNT_J
			Register regJ = vm.getRegister(Select.REG_J);
			System.out.println(""+regJ.getValue());
			break;
			
		case 16: // PRNT_K
			Register regK = vm.getRegister(Select.REG_K);
			System.out.println(""+regK.getValue());
			break;
			
			
			
		case 17: // PRNT_U
			Register regU = vm.getRegister(Select.REG_U);
			System.out.println(""+regU.getValue());
			break;
			
		case 18: // PRNT_V
			Register regV = vm.getRegister(Select.REG_V);
			System.out.println(""+regV.getValue());
			break;
			
		case 19: // PRNT_W
			Register regW = vm.getRegister(Select.REG_W);
			System.out.println(""+regW.getValue());
			break;
			
			
			
		case 20: // PRNT_X
			Register regX = vm.getRegister(Select.REG_X);
			System.out.println(""+regX.getValue());
			break;
			
		case 21: // PRNT_Y
			Register regY = vm.getRegister(Select.REG_Y);
			System.out.println(""+regY.getValue());
			break;
			
		case 22: // PRNT_Z
			Register regZ = vm.getRegister(Select.REG_Z);
			System.out.println(""+regZ.getValue());
			break;
			
		default: 
			break;
		}
	}
}
