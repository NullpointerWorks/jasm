package com.nullpointerworks.jasm.test;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.Assembler;
import com.nullpointerworks.jasm.asm.assembler.SourceCodeAssembler;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.Parser;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.parser.SourceFileParser;

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
		 * the parser formats the source code writing
		 */
		Parser parser = new SourceFileParser();
		parser.setVerboseListener(this);
		parser.parse("main.jasm");
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
		 * the assembler turns source code objects into machine code
		 */
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
		
		
	}
}
