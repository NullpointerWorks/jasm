package com.nullpointerworks.jasm.test;

import static com.nullpointerworks.jasm.vm.VMRegister.REG_A;
import static com.nullpointerworks.jasm.vm.VMRegister.REG_B;
import static com.nullpointerworks.jasm.vm.VMRegister.REG_C;
import static com.nullpointerworks.jasm.vm.VMRegister.REG_D;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;

import com.nullpointerworks.jasm.asm.parser.Parser;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.parser.SourceFileParser;

import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.SourceCodeTranslator;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.Translator;
import com.nullpointerworks.jasm.vm.BytecodeVirtualMachine;
import com.nullpointerworks.jasm.vm.InterruptListener;
import com.nullpointerworks.jasm.vm.Register;
import com.nullpointerworks.jasm.vm.VMProcessException;
import com.nullpointerworks.jasm.vm.VirtualMachine;
import com.nullpointerworks.jasm.asm.assembler.Assembler;
import com.nullpointerworks.jasm.asm.assembler.TranslationAssembler;

public class MainAssemblerTest implements VerboseListener, InterruptListener
{
	public static void main(String[] args) 
	{
		new MainAssemblerTest("src/com/nullpointerworks/jasm/test/main.jasm");
	}
	
	public MainAssemblerTest(String file)
	{
		/*
		 * the parser formats the source code to make it consistent
		 */
		Parser parser = new SourceFileParser();
		parser.setVerboseListener(this);
		parser.parse(file);
		if(parser.hasErrors())
		{
			List<BuildError> errors = parser.getErrors();
			for (BuildError be : errors) onPrint( be.getDescription() );
			return;
		}
		List<SourceCode> sourcecode = parser.getSourceCode();
		
		/*
		 * turns the source code into an object which is easier to assemble
		 */
		Translator translator = new SourceCodeTranslator();
		translator.setVerboseListener(this);
		translator.translate(sourcecode);
		if(translator.hasErrors())
		{
			List<BuildError> errors = translator.getErrors();
			for (BuildError be : errors) onPrint( be.getDescription() );
			return;
		}
		List<Translation> translation 	= translator.getTranslation();
		List<Definition> definitions 	= translator.getDefinitions();
		List<Allocation> allocations 	= translator.getAllocations();
		List<Label> labels 				= translator.getLabels();
		
		/*
		 * the assembler turns the translation objects into bytecode
		 */
		Assembler assembler = new TranslationAssembler();
		assembler.setVerboseListener(this);
		assembler.assemble(translation, definitions, allocations, labels);
		if(assembler.hasErrors())
		{
			List<BuildError> errors = assembler.getErrors();
			for (BuildError be : errors) onPrint( be.getDescription() );
			return;
		}
		List<Integer> code = assembler.getMachineCode();
		
		printDefinitions(definitions, this);
		printAllocations(allocations, this);
		printLabels(labels, this);
		printTranslation(translation, this);
		printMachineCode(0, code, this);
		
		/*
		 * test the assembled bytecode
		 */
		VirtualMachine vm = new BytecodeVirtualMachine();
		vm.setInterruptListener(this);
		vm.setMemorySize(2048);
		vm.setMemory(0,code);
		
		onPrint("-------------------------------");
		onPrint("Virtual Machine Start\n");
		
		while (!vm.hasException())
		{
			vm.nextInstruction();
		}
		while (vm.hasException())
		{
			VMProcessException ex = vm.getException();
			System.err.println( ex.getMemoryTrace() );
		}
		
		onPrint("\nVirtual Machine End");
		onPrint("-------------------------------");
	}

	@Override
	public void onInterrupt(VirtualMachine vm, int code) 
	{
		if (code == 0)
		{
			onPrint("\nVirtual Machine End");
			onPrint("-------------------------------");
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
	
	@Override
	public void onPrint(String msg) 
	{
		System.out.println(msg);
	}
	
	void printMachineCode(int offset, List<Integer> code, VerboseListener verbose) 
	{
		verbose.onPrint("-------------------------------");
		verbose.onPrint("Byte Code Start\n");
		
		int it = 0;
		int leng = ( code.size()+"" ).length();
		String padding = "";
		for (int k=0; k<leng; k++) padding += " ";
		String paddingFormat = "%0"+leng+"x";
		
		for (int j = offset, l = code.size(); j<l; j++)
		{
			Integer i = code.get(j);
			
			int b1 = (i>>24)&0xff;
			int b2 = (i>>16)&0xff;
			int b3 = (i>>8)&0xff;
			int b4 = (i)&0xff;
			
			String s1 = String.format("%02x ", b1);
			String s2 = String.format("%02x ", b2);
			String s3 = String.format("%02x ", b3);
			String s4 = String.format("%02x", b4);
			
			String addr = padding + String.format(paddingFormat, it);
			addr = "[ "+addr.substring(leng)+" ] ";
			verbose.onPrint(addr+s1+s2+s3+s4);
			it++;
		}
		
		verbose.onPrint("\nByte Code End");
		verbose.onPrint("-------------------------------");
	}
	
	void printDefinitions(List<Definition> definitions, VerboseListener verbose) 
	{
		verbose.onPrint("Definitions:");
		for (Definition def : definitions)
		{
			verbose.onPrint( "  "+def.getDirective()+" "+
									def.getName()+" "+
									def.getNumber().getValue() );
		}
		verbose.onPrint("");
	}
	
	void printAllocations(List<Allocation> allocations, VerboseListener verbose) 
	{
		verbose.onPrint("Allocations:");
		for (Allocation alloc : allocations)
		{
			int v = alloc.getAddress().getValue();
			String s1 = String.format("0x%x", v);
			
			verbose.onPrint("  "+
							alloc.getDirective()+
							" "+
							alloc.getName()+
							" : "+
							s1);
		}
		verbose.onPrint("");
	}
	
	void printLabels(List<Label> labels, VerboseListener verbose) 
	{
		verbose.onPrint("Labels:");
		for (Label label : labels)
		{
			System.out.print( "  "+label.getName());
			System.out.println( " : "+label.getNumber().getValue());
		}
		verbose.onPrint("");
	}

	void printTranslation(List<Translation> translation, VerboseListener verbose) 
	{
		verbose.onPrint("Translation:");
		for (Translation tr : translation)
		{
			String text = "";
			
			text += "  "+tr.getInstruction();
			for (Operand op : tr.getOperands())
			{
				text += " "+(op.isAddress()?"&":"")+op.getOperand();
			}
			
			if (tr.hasLabel())
			{
				text += " : "+tr.getLabel();
			}
			
			verbose.onPrint(text);
		}
		verbose.onPrint("");
	}
}
