package com.nullpointerworks.jasm;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.nullpointerworks.jasm.jasm8.Compiler;
import com.nullpointerworks.jasm.jasm8.LogListener;
import com.nullpointerworks.jasm.jasm8.compiler.CompilerJASM8;
import com.nullpointerworks.jasm.jasm8.compiler.GenericLog;

import com.nullpointerworks.util.FileUtil;
import com.nullpointerworks.util.concurrency.Threading;
import com.nullpointerworks.util.file.bytefile.ByteFileParser;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

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
 * -l = enable log
 * 
 * 
 * java -jar "jasmc.jar" -vrpc %1
 * 
 */
public class MainCompiler
{
	public static void main(String[] args) {new MainCompiler(args);}
	
	private boolean parserVerbose = false;
	private boolean preprocessorVerbose = false;
	private boolean compilerVerbose = false;
	private boolean enableLogging = false;
	private LogListener log;

	/* 
	 * 32 kiB = 1024 * 32 = 32768
	 * 64 kiB = 1024 * 64 = 65536
	 * 
	 * address range
	 * 16 bit = 2^16 = 65536 = 0xFFFF = 0b1111 1111 1111 1111
	 * 64 kiB max
	 * 
	 * 
	 * TODO error and information
	 * 
	 * notify about unused labels
	 * error about unknown label jumps
	 * error when detecting duplicate labels
	 * error when two labels are defined in sequence
	 * 
	 */
	public MainCompiler(String[] args)
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
				enableLogging = true;
				continue;
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
		
		/*
		 * make log file
		 */
		log = new GenericLog();
		
		/*
		 * compile into jasm8
		 */
		Compiler jasm8 = new CompilerJASM8();
		jasm8.setParserVerbose(parserVerbose);
		jasm8.setPreprocessorVerbose(preprocessorVerbose);
		jasm8.setCompilerVerbose(compilerVerbose);
		jasm8.setLogListener(log);
		byte[] program = jasm8.parse(tf.getLines());
		
		/*
		 * save log
		 */
		if (enableLogging)
		{
			log.save( FileUtil.swapExtension(file, "log") );
		}
		
		if (program == null) return;
		
		/*
		 * save output
		 */
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
