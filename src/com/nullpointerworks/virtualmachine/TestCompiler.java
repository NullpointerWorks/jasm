package com.nullpointerworks.virtualmachine;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.Compiler;
import com.nullpointerworks.jasm.Preprocessor;
import com.nullpointerworks.jasm.compiler.CompilerJASM;

public class TestCompiler extends TestPreprocessor
{
	public static void main(String[] args) 
	{
		//*
		args = new String[] 
		{
			"D:/Development/Assembly/workspace/jasm/playground/playground.jasm",
		};
		//*/
		
		new TestCompiler().runCompiler(args);
	}

	private boolean verbose = false;
	private Compiler jasmCompiler;
	
	public void runCompiler(String[] args)
	{
		super.runPreprocessor(args);
		
		/*
		 * get parser results
		 */
		Preprocessor preproc = super.getPreprocessor();
		if (preproc==null) return;
		
		/*
		 * do pre-processing
		 */
		jasmCompiler = new CompilerJASM();
		jasmCompiler.setVerbose(verbose);
		jasmCompiler.compile(preproc);
		if (jasmCompiler.hasErrors())
		{
			var errors = jasmCompiler.getErrors();
			for (BuildError err : errors)
			{
				System.out.println( err.getDescription() );
			}
			jasmCompiler = null;
		}
	}
	
	protected Compiler getCompiler()
	{
		return jasmCompiler;
	}
	
	protected void setCompilerVerbose(boolean v) 
	{
		verbose = v;
	}
}
