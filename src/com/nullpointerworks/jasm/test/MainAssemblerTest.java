package com.nullpointerworks.jasm.test;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;

import com.nullpointerworks.jasm.asm.parser.Parser;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.parser.SourceFileParser;

import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
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
		List<Definition> definitions = translator.getDefinitions();
		List<Allocation> allocations = translator.getAllocations();
		List<Translation> translation = translator.getTranslation();
		
		//*
		for (Definition def : definitions)
		{
			System.out.println( def.getDirective()+" "+
								def.getName()+" "+
								def.getNumber().getValue() );
		}
		//*/
		
		//*
		for (Allocation alloc : allocations)
		{
			System.out.println( alloc.getDirective()+" "+
								alloc.getName());
		}
		//*/
		
		//*
		for (Translation tr : translation)
		{
			if (tr.hasLabel())
			{
				System.out.print( tr.getLabel()+": " );
			}
			
			System.out.print( tr.getInstruction() );
			for (Operand op : tr.getOperands())
			{
				System.out.print( " "+op.getOperand() );
			}
			
			System.out.println();
		}
		//*/
		
		/*
		 * the assembler turns the translation objects into bytecode
		 */
		Assembler assembler = new TranslationAssembler();
		assembler.setVerboseListener(this);
		assembler.assemble(translation, definitions, allocations);
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
		
	}
}
