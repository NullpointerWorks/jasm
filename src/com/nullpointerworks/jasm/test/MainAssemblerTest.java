package com.nullpointerworks.jasm.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
		new MainAssemblerTest("src/com/nullpointerworks/jasm/test/main.jasm",
							  "src/com/nullpointerworks/jasm/test/program.bin");
	}
	
	public MainAssemblerTest(String inFile, String outFile)
	{
		/*
		 * the parser formats the source code to make it consistent
		 */
		Parser parser = new SourceFileParser();
		parser.setVerboseListener(this);
		parser.parse(inFile);
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
		
		//printDefinitions(definitions, this);
		//printAllocations(allocations, this);
		//printLabels(labels, this);
		//printTranslation(translation, this);
		//printMachineCode(0, code, this);
		
		/*
		 * convert integers to bytes and write data to file
		 */
		int i = 0;
		int j = 0;
		int l = code.size();
		byte[] binary = new byte[l*4];
		for (; i<l; i++, j+=4)
		{
			Integer bytecode = code.get(i);
			int b1 = (bytecode>>24)&0xff;
			int b2 = (bytecode>>16)&0xff;
			int b3 = (bytecode>>8)&0xff;
			int b4 = (bytecode)&0xff;
			binary[j] 	= (byte)b1;
			binary[j+1] = (byte)b2;
			binary[j+2] = (byte)b3;
			binary[j+3] = (byte)b4;
		}
		
		File outputFile = new File(outFile);
		try (FileOutputStream outputStream = new FileOutputStream(outputFile)) 
		{
		    outputStream.write(binary);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
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
