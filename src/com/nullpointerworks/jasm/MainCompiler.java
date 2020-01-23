package com.nullpointerworks.jasm;

import java.io.FileNotFoundException;
import java.io.IOException;

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
 * options
 * 
 * -verbose = verbose
 * r = parser
 * p = preprocessor
 * c = compiler
 * 
 * -verify = verify only
 * 
 * -log = logging
 * 
 */
class MainCompiler
{
	public static void main(String[] args) {new MainCompiler(args);}
	
	private boolean parserVerbose = false;
	private boolean preprocessorVerbose = false;
	private boolean compilerVerbose = false;
	private boolean enableLogging = false;
	private boolean verifyOnly = false;
	private LogListener log;
	
	/* 
	 * 32 kiB = 1024 * 32 = 32768
	 * 64 kiB = 1024 * 64 = 65536
	 * 
	 * address range
	 * 16 bit = 2^16 = 65536 = 0xFFFF = 0b1111 1111 1111 1111
	 * 64 kiB max
	 * 
	 */
	public MainCompiler(String[] args)
	{
		//args = new String[] {"-verbose-rpc", "-verify", "-log", "D:\\Development\\Assembly\\workspace\\jasm\\playground\\playground.jasm"};
		startCompiler(args);
	}
	
	private void startCompiler(String[] args)
	{
		/*
		 * make compiler batch
		 */
		if (args.length == 0)
		{
			// bare compiler
			TextFile tf = new TextFile();
			tf.setEncoding("UTF-8");
			tf.addLine("@echo off\r\n");
			tf.addLine("java -jar \"jasmc.jar\" %1\r\n");
			tf.addLine("pause\r\n");
			try {TextFileParser.write("compile.bat", tf);} 
			catch (IOException e){e.printStackTrace();}
			
			// compile and run
			tf.clear();
			tf.addLine("@echo off\r\n");
			tf.addLine("java -jar \"jasmc.jar\" %1\r\n");
			tf.addLine("set \"file=%1\"\r\n");
			tf.addLine("set \"file=%file:.=\"^&REM #%\r\n");
			tf.addLine("java -jar \"jasm.jar\" %file%.bin\r\n");
			tf.addLine("pause\r\n");
			try {TextFileParser.write("compile_and_run.bat", tf);} 
			catch (IOException e) {e.printStackTrace();}
			
			return;
		}
		
		Threading.sleep(100);
		
		/*
		 * start compiling
		 */
		for (String file : args)
		{
			// enable verbose
			if (file.startsWith("-verbose-"))
			{
				file=file.substring(8);
				if (file.contains("r")) parserVerbose = true;
				if (file.contains("p")) preprocessorVerbose = true;
				if (file.contains("c")) compilerVerbose = true;
				continue;
			}
			
			// enable toolchain stop
			if (file.startsWith("-verify"))
			{
				verifyOnly = true;
				continue;
			}
			
			// enable logging
			if (file.startsWith("-log"))
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
		tf.setName(file);
		
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
		jasm8.setVerifyOnly(verifyOnly);
		jasm8.setLogListener(log);
		byte[] program = jasm8.parse(file, tf.getLines());
		
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
