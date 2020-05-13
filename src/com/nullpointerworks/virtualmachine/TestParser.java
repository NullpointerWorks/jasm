package com.nullpointerworks.virtualmachine;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.parser.ParseError;
import com.nullpointerworks.jasm.parser.Parser;
import com.nullpointerworks.jasm.parser.ParserJASM;

public class TestParser 
{
	public static void main(String[] args) 
	{
		//*
		args = new String[] 
		{
			"D:/Development/Assembly/workspace/jasm/playground/playground.jasm"
		};
		//*/
		
		new TestParser().runParser(args);
	}
	
	private boolean verbose = false;
	private Parser jasmParser;
	
	public void runParser(String[] args)
	{
		/*
		 * load primary source file
		 */
		URL url = new URL(args[0]);
		
		/*
		 * add linker directory
		 */
		List<String> paths = new ArrayList<String>();
		paths.add(url.folderPath()); // always set source directory as primary linker
		for (int i=1,l=args.length; i<l; i++)
		{
			paths.add(args[i]);
		}
		
		/*
		 * parse text into code
		 */
		jasmParser = new ParserJASM();
		jasmParser.setVerbose(verbose);
		jasmParser.setIncludesPath(paths);
		jasmParser.parse(url.filePath());
		if (jasmParser.hasErrors())
		{
			var errors = jasmParser.getErrors();
			for (ParseError err : errors)
			{
				System.out.println( err.getDescription()+"\n" );
			}
			jasmParser = null;
		}
	}
	
	protected Parser getParser()
	{
		return jasmParser;
	}
	
	protected void setParserVerbose(boolean v) 
	{
		verbose = v;
	}
}
