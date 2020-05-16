package com.nullpointerworks.jasm.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.Parser;
import com.nullpointerworks.jasm.parser.DefineRecord;
import com.nullpointerworks.jasm.parser.SourceCode;
import com.nullpointerworks.jasm.Compiler;

import com.nullpointerworks.util.Log;

public abstract class CompilerJASM<T> implements Compiler<T>
{
	private boolean verbose = false;
	private int instIndex = 0; // label instruction index
	
	private List<BuildError> errors = null;
	private Map<String, Integer> labels = null;
	private List<Draft<T>> draft = null;
	private List<Draft<T>> labelled = null;
	
	public CompilerJASM() 
	{
		reset();
	}
	
	@Override
	public Compiler<T> reset() 
	{
		if (errors!=null) errors.clear();
		errors = new ArrayList<BuildError>();
		
		if (labels!=null) labels.clear();
		labels = new HashMap<String, Integer>();

		if (draft!=null) draft.clear();
		draft = new ArrayList<Draft<T>>();
		
		if (labelled!=null) labelled.clear();
		labelled = new ArrayList<Draft<T>>();
		
		return this;
	}

	@Override
	public Compiler<T> setVerbose(boolean verbose) 
	{
		this.verbose = verbose;
		return this;
	}
	
	@Override
	public Compiler<T> preprocess(Parser parser) 
	{
		List<SourceCode> code = parser.getSourceCode();
		List<DefineRecord> defs = parser.getDefinitions();
		
		if (verbose)
		{
			Log.out("-------------------------------");
			Log.out("\n Compiling\n");
		}
		
		insert(code, defs);
		
		process(code);
		
		if (verbose)
		{
			Log.out("\n Done\n");
			Log.out("-------------------------------");
		}
		return this;
	}
	
	public abstract Draft<T> compile(int index, SourceCode loc);
	
	@Override
	public boolean hasErrors() 
	{
		return errors.size() > 0;
	}
	
	@Override
	public List<BuildError> getErrors() 
	{
		return errors;
	}
	
	@Override
	public List<Draft<T>> getDraft() 
	{
		return draft;
	}
	
	/* ==================================================================
	 * 
	 * pre-processor
	 * 
	 * responsibility:
	 * insert "def" directives
	 * turns labels into addresses
	 * drafts machine instructions
	 * inserts addresses into draft
	 * 
	 * ==================================================================
	 */
	private Compiler<T> process(List<SourceCode> code) 
	{
		/*
		 * track directives and labels
		 * process instructions
		 */
		for (int i=0,l=code.size(); i<l; i++)
		{
			SourceCode loc = code.get(i);
			processLine(loc);
			if (hasErrors()) return this;
		}
		
		/*
		 * insert label addresses
		 */
		for (Draft<T> d : labelled)
		{
			String label = d.getLabel();
			if (!labels.containsKey(label))
			{
				addError(d.getLineOfCode(), "Unknown label reference");
				return this;
			}
			int addr = labels.get(label);
			d.setJumpAddress(addr);
		}
		
		return this;
	}

	private void processLine(SourceCode loc) 
	{
		String line = loc.getLine();
		
		/*
		 * is a definition
		 */
		if (line.startsWith("."))
		{
			
			return;
		}
		
		/*
		 * is a label
		 */
		if (line.contains(":"))
		{
			String label = line.substring(0,line.length()-1);
			labels.put(label, instIndex);
			return;
		}
		
		instIndex += 1;
		var draft_inst = compile(instIndex, loc);
		
		if (draft_inst.hasError())
		{
			return;
		}
		
		if (draft_inst.hasLabel())
		{
			labelled.add(draft_inst);
		}
		
		draft.add(draft_inst);
	}
	
	private void insert(List<SourceCode> code, List<DefineRecord> defs) 
	{
		/*
		 * print definitions
		 */
		if (verbose)
		{
			out("Definitions");
			for (DefineRecord d : defs)
			{
				out( "  "+d.NAME +" = "+d.VALUE );
			}
		}
		
		for (int i=0,l=code.size(); i<l; i++)
		{
			SourceCode loc = code.get(i);
			String line = loc.getLine();
			
			for (DefineRecord d : defs)
			{
				String name = d.NAME;
				if (line.contains(name))
				{
					line = line.replace(name, d.VALUE);
					loc.setLine(line);
					break;
				}
			}
		}
	}
	
	private void addError(SourceCode code, String message)
	{
		errors.add( new PreProcessorError(code, message) );
	}
	
	private void out(String string)
	{
		if (verbose) System.out.println(string);
	}
}
