package com.nullpointerworks.jasm.preprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.parser.Parser;
import com.nullpointerworks.jasm.parser.SourceCode;
import com.nullpointerworks.util.Log;

public class PreprocessorJASM implements Preprocessor 
{
	private boolean verbose_preproc = false;
	private int instIndex = 0; // label instruction index
	
	private List<PreProcessingError> errors = null;
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
		errors = new ArrayList<PreProcessingError>();
		
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
	
	/* ==================================================================
	 * 
	 * pre-processor
	 * 
	 * responsibility:
	 * handles directives
	 * turns labels into addresses
	 * drafts machine instructions
	 * 
	 * ==================================================================
	 */
	@Override
	public Preprocessor preprocess(Parser parser) 
	{
		List<SourceCode> code = parser.getSourceCode();
		
		if (verbose_preproc)
		{
			Log.out("-------------------------------");
			Log.out("\n Pre-processor\n");
		}
		
		/*
		 * track directives and labels
		 * process instructions
		 */
		for (int i=0,l=code.size(); i<l; i++)
		{
			SourceCode loc = code.get(i);
			String line = loc.getLine();
			if (line.startsWith("."))
			{
				continue;
			}
			
			// is a label
			if (line.contains(":")) 
			{
				String label = line.substring(0,line.length()-1);
				labels.put(label, instIndex);
				continue;
			}
			
			instIndex += 1;
			var draft_inst = buildDraft(instIndex, loc);
			
			if (draft_inst.hasError())
			{
				
			}
			
			if (draft_inst.hasLabel())
			{
				labelled.add(draft_inst);
			}
			
			draft.add(draft_inst);
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
			d.setLabelAddress(addr);
		}
		
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
		return false;
	}

	@Override
	public List<PreProcessingError> getErrors() 
	{
		return null;
	}

	@Override
	public List<DraftJASM> getDraft() 
	{
		return null;
	}
	
	/* ==================================================================
	 * 
	 * draft builder
	 * 
	 * responsibility:
	 * takes a line of code and turns it into an instruction
	 * 
	 * 
	 * ==================================================================
	 */
	private DraftJASM buildDraft(int index, SourceCode loc)
	{
		return new DraftJASM(index, loc);
	}
	
	// ==================================================================
	
	private void addError(SourceCode code, String message)
	{
		errors.add( new PreProcessingError(code, message) );
	}
}
