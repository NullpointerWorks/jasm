package com.nullpointerworks.jasm.app;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.nullpointerworks.jasm.jasm8.Compiler;
import com.nullpointerworks.jasm.jasm8.compiler.CompilerJASM8;

import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.concurrency.Threading;
import com.nullpointerworks.util.file.bytefile.ByteFileParser;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class CompilerMain
{
	public static void main(String[] args) {new CompilerMain(args);}
	
	private boolean parserVerbose = false;
	private boolean preprocessorVerbose = false;
	private boolean compilerVerbose = false;
	
	/*
	 * java -jar "jasmc.jar" %1
	 * 
	 * optional markers
	 * 
	 * -v = verbose
	 * r = parser
	 * p = preprocessor
	 * c = compiler
	 * 
	 * 
	 * 
	 * 
	 * java -jar "jasmc.jar" -vrpc %1
	 * 
	 */
	public CompilerMain(String[] args)
	{
		/*
		 * make compiler batch
		 */
		if (args.length == 0)
		{
			TextFile tf = new TextFile();
			tf.setEncoding("UTF-8");
			
			tf.addLine("@java -jar \"jasmc.jar\" %1\r\n");
			tf.addLine("@pause\r\n");
			
			try
			{
				TextFileParser.write("compile.bat", tf);
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			return;
		}
		
		Threading.sleep(100);
		
		/*
		 * start compiling
		 */
		
		for (String file : args)
		{
			// enable verbose
			if (file.startsWith("-v"))
			{
				if (file.contains("r")) parserVerbose = true;
				if (file.contains("p")) preprocessorVerbose = true;
				if (file.contains("c")) compilerVerbose = true;
				continue;
			}
			
			// enable logging
			if (file.startsWith("-l"))
			{
				
			}
			
			compile(file);
		}
	}
	
	private void compile(String file)
	{
		/*
		 * load text file
		 */
		TextFile tf = null;
		try
		{
			tf = TextFileParser.file(file);
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		if (tf == null) return;
		
		byte[] program = null;
		
		/*
		 * compile into jasm8
		 */
		Compiler jasm8 = new CompilerJASM8();
		jasm8.setParserVerbose(parserVerbose);
		jasm8.setPreprocessorVerbose(preprocessorVerbose);
		jasm8.setCompilerVerbose(compilerVerbose);
		program = jasm8.parse(tf.getLines());
		
		if (program == null) return;
		
		try
		{
			ByteFileParser.write( FileUtil.swapExtension(file, "bin") , program);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
