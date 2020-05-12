package com.nullpointerworks.virtualmachine;

import com.nullpointerworks.jasm.preprocessor.Preprocessor;
import com.nullpointerworks.jasm.compiler.CompileError;
import com.nullpointerworks.jasm.compiler.Compiler;
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
		jasmCompiler.setVerbose(isVerbose());
		jasmCompiler.compile(preproc);
		if (jasmCompiler.hasErrors())
		{
			var errors = jasmCompiler.getErrors();
			for (CompileError err : errors)
			{
				System.out.println( err.getDescription() );
			}
		}
	}
	
	protected Compiler getCompiler()
	{
		return jasmCompiler;
	}
}
