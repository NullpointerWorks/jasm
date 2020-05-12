package com.nullpointerworks.virtualmachine;

import com.nullpointerworks.jasm.preprocessor.Preprocessor;
import com.nullpointerworks.jasm.preprocessor.PreprocessorJASM;
import com.nullpointerworks.jasm.parser.Parser;
import com.nullpointerworks.jasm.preprocessor.PreProcessingError;

public class TestPreprocessor extends TestParser
{
	public static void main(String[] args) 
	{
		//*
		args = new String[] 
		{
			"D:/Development/Assembly/workspace/jasm/playground/playground.jasm",
		};
		//*/
		
		new TestPreprocessor(args);
	}
	
	private Preprocessor jasmPreProc;
	
	public TestPreprocessor(String[] args)
	{
		super(args);
		
		/*
		 * get parser results
		 */
		Parser parser = super.getParser();
		if (parser==null) return;
		
		/*
		 * do pre-processing
		 */
		jasmPreProc = new PreprocessorJASM();
		jasmPreProc.setVerbose(true);
		jasmPreProc.preprocess(parser);
		if (jasmPreProc.hasErrors())
		{
			var errors = jasmPreProc.getErrors();
			for (PreProcessingError err : errors)
			{
				System.out.println( err.getDescription() );
			}
		}
	}
	
	public Preprocessor getPreprocessor()
	{
		return jasmPreProc;
	}
}
