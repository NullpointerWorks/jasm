package com.nullpointerworks.virtualmachine;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.parser.ParseError;
import com.nullpointerworks.jasm.parser.Parser;
import com.nullpointerworks.jasm.parser.ParserJASM;
import com.nullpointerworks.jasm.preprocessor.Preprocessor;
import com.nullpointerworks.jasm.preprocessor.PreprocessorJASM;
import com.nullpointerworks.jasm.preprocessor.PreProcessingError;

public class TestPreprocessor 
{
	public static void main(String[] args) 
	{
		//*
		args = new String[] 
		{
			"D:/Development/Assembly/workspace/jasm2/playground.jasm",
			"D:/Development/Assembly/workspace/jasm2/"
		};
		//*/
		
		new TestPreprocessor(args);
	}
	
	public TestPreprocessor(String[] args)
	{
		/*
		 * load primary source file
		 */
		String filename = args[0];
		
		/*
		 * add linker directory
		 */
		List<String> paths = new ArrayList<String>();
		for (int i=1,l=args.length; i<l; i++)
		{
			paths.add(args[i]);
		}
		
		/*
		 * parse text into code
		 */
		Parser jasmParser = new ParserJASM();
		jasmParser.setVerbose(false);
		jasmParser.setIncludesPath(paths);
		jasmParser.parse(filename);
		if (jasmParser.hasErrors())
		{
			var errors = jasmParser.getErrors();
			for (ParseError err : errors)
			{
				System.out.println( err.getDescription() );
			}
			return;
		}
		
		/*
		 * do pre-processing
		 */
		Preprocessor preproc = new PreprocessorJASM();
		preproc.setVerbose(true);
		preproc.preprocess(jasmParser);
		if (preproc.hasErrors())
		{
			var errors = preproc.getErrors();
			for (PreProcessingError err : errors)
			{
				System.out.println( err.getDescription() );
			}
			return;
		}
		
		
		
		
	}
}
