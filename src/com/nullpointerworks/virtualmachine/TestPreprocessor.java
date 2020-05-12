package com.nullpointerworks.virtualmachine;

import com.nullpointerworks.jasm.preprocessor.Preprocessor;
import com.nullpointerworks.jasm.preprocessor.PreprocessorJASM;
import com.nullpointerworks.jasm.parser.Parser;
import com.nullpointerworks.jasm.preprocessor.PreProcessorError;

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
		
		new TestPreprocessor().runPreprocessor(args);
	}
	
	private boolean verbose = false;
	private Preprocessor jasmPreProc;
	
	public void runPreprocessor(String[] args)
	{
		super.runParser(args);
		
		/*
		 * get parser results
		 */
		Parser parser = super.getParser();
		if (parser==null) return;
		
		/*
		 * do pre-processing
		 */
		jasmPreProc = new PreprocessorJASM();
		jasmPreProc.setVerbose(verbose);
		jasmPreProc.preprocess(parser);
		if (jasmPreProc.hasErrors())
		{
			var errors = jasmPreProc.getErrors();
			for (PreProcessorError err : errors)
			{
				System.out.println( err.getDescription() );
			}
			jasmPreProc = null;
		}
	}
	
	protected Preprocessor getPreprocessor()
	{
		return jasmPreProc;
	}
	
	protected void setPreProcessorVerbose(boolean v) 
	{
		verbose = v;
	}
}
