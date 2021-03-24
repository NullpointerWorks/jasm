package com.nullpointerworks.jasm.test;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.Assembler;
import com.nullpointerworks.jasm.asm.assembler.SourceCodeAssembler;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.Parser;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.parser.SourceFileParser;
import com.nullpointerworks.jasm.asm.translator.SourceCodeTranslator;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.Translator;

public class MainAssemblerTest implements VerboseListener
{
	public static void main(String[] args) 
	{
		new MainAssemblerTest();
	}
	
	@Override
	public void onPrint(String msg) 
	{
		System.out.println(msg);
	}
	
	public MainAssemblerTest()
	{
		/*
		 * the parser formats the source code to make it consistent
		 */
		Parser parser = new SourceFileParser();
		parser.setVerboseListener(this);
		parser.parse("src/com/nullpointerworks/jasm/test/main.jasm");
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
		List<Translation> translation = translator.getTranslation();
		
		/*
		 * the assembler turns the translation objects into bytecode
		 *
		Assembler assemble = new SourceCodeAssembler();
		assemble.setVerboseListener(this);
		assemble.draft(sourcecode);
		if(assemble.hasErrors())
		{
			List<BuildError> errors = assemble.getErrors();
			for (BuildError be : errors)
			{
				System.out.println( be.getDescription() );
			}
			return;
		}
		List<Integer> code = assemble.getMachineCode();
		//*/
		
	}
}
