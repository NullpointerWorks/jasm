package com.nullpointerworks.jasm.preprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.parser.Parser;
import com.nullpointerworks.jasm.parser.SourceCode;
import com.nullpointerworks.util.Log;

public class PreprocessorJASM implements Preprocessor 
{
	private boolean verbose_preproc = false;
	private int instIndex = 0; // label instruction index
	
	private List<BuildError> errors = null;
	private Map<String, Integer> labels = null;
	private List<DraftJASM> draft = null;
	private List<DraftJASM> labelled = null;
	
	public PreprocessorJASM() 
	{
		reset();
	}
	
	@Override
	public Preprocessor reset() 
	{
		if (errors!=null) errors.clear();
		errors = new ArrayList<BuildError>();
		
		if (labels!=null) labels.clear();
		labels = new HashMap<String, Integer>();

		if (draft!=null) draft.clear();
		draft = new ArrayList<DraftJASM>();
		
		if (labelled!=null) labelled.clear();
		labelled = new ArrayList<DraftJASM>();
		
		return this;
	}

	@Override
	public Preprocessor setVerbose(boolean verbose) 
	{
		verbose_preproc = verbose;
		return this;
	}
	
	@Override
	public Preprocessor preprocess(Parser parser) 
	{
		List<SourceCode> code = parser.getSourceCode();
		
		if (verbose_preproc)
		{
			Log.out("-------------------------------");
			Log.out("\n Pre-processor\n");
		}
		
		process(code);
		
		if (verbose_preproc)
		{
			Log.out("\n Done\n");
			Log.out("-------------------------------");
		}
		return this;
	}
	
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
	public List<DraftJASM> getDraft() 
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
	private Preprocessor process(List<SourceCode> code) 
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
		for (DraftJASM d : labelled)
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
		var draft_inst = buildDraft(instIndex, loc);
		
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

	private DraftJASM buildDraft(int index, SourceCode loc)
	{
		return new DraftJASM(index, loc);
	}
	
	private void addError(SourceCode code, String message)
	{
		errors.add( new PreProcessorError(code, message) );
	}
}
