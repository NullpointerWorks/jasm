package com.nullpointerworks.jasm.test;

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

import com.nullpointerworks.jasm.asm.assembler.Assembler;
import com.nullpointerworks.jasm.asm.assembler.TranslationAssembler;

public class MainAssemblerTest implements VerboseListener
{
	public static void main(String[] args) 
	{
		new MainAssemblerTest("src/com/nullpointerworks/jasm/test/main.jasm");
	}
	
	@Override
	public void onPrint(String msg) 
	{
		System.out.println(msg);
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
			for (BuildError be : errors)
			{
				System.out.println( be.getDescription() );
			}
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
			for (BuildError be : errors)
			{
				System.out.println( be.getDescription() );
			}
			return;
		}
		List<Definition> definitions 	= translator.getDefinitions();
		List<Allocation> allocations 	= translator.getAllocations();
		List<Label> labels 				= translator.getLabels();
		List<Translation> translation 	= translator.getTranslation();
		
		/*
		 * the assembler turns the translation objects into bytecode
		 */
		Assembler assembler = new TranslationAssembler();
		assembler.setVerboseListener(this);
		assembler.assemble(translation, definitions, allocations, labels);
		if(assembler.hasErrors())
		{
			List<BuildError> errors = assembler.getErrors();
			for (BuildError be : errors)
			{
				System.out.println( be.getDescription() );
			}
			return;
		}
		List<Integer> code = assembler.getMachineCode();
		
		printDefinitions(definitions);
		printAllocations(allocations);
		printLabels(labels);
		printTranslation(translation);
		printMachineCode(0, code, this);
	}
	
	private void printMachineCode(int offset, List<Integer> code, VerboseListener verbose) 
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
	
	private void printDefinitions(List<Definition> definitions) 
	{
		System.out.println("Definitions:");
		for (Definition def : definitions)
		{
			System.out.println( "  "+def.getDirective()+" "+
									def.getName()+" "+
									def.getNumber().getValue() );
		}
		System.out.println("");
	}
	
	private void printAllocations(List<Allocation> allocations) 
	{
		System.out.println("Allocations:");
		for (Allocation alloc : allocations)
		{
			System.out.println( "  "+alloc.getDirective()+" "+
									alloc.getName());
		}
		System.out.println("");
	}
	
	private void printLabels(List<Label> labels) 
	{
		System.out.println("Labels:");
		for (Label label : labels)
		{
			System.out.println( "  "+label.getName()+" : "+label.getNumber().getValue());
		}
		System.out.println("");
	}

	private void printTranslation(List<Translation> translation) 
	{
		System.out.println("Translation:");
		for (Translation tr : translation)
		{
			System.out.print( "  "+tr.getInstruction() );
			for (Operand op : tr.getOperands())
			{
				System.out.print( " "+op.getOperand() );
			}

			if (tr.hasLabel())
			{
				System.out.print( " : "+tr.getLabel() );
			}
			
			System.out.println();
		}
		System.out.println("");
	}
}
