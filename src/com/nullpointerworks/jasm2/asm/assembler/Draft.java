package com.nullpointerworks.jasm2.asm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm2.asm.error.BuildError;
import com.nullpointerworks.jasm2.asm.parser.SourceCode;

public class Draft
{
	private SourceCode source;
	private String label;
	private BuildError error;
	private List<Integer> values;
	
	public Draft(SourceCode sc) 
	{
		source = sc;
		label = null;
		error = null;
		values = new ArrayList<Integer>();
	}
	
	public boolean hasError() {return error != null;}
	public void setError(BuildError err) {error = err;}
	public BuildError getError() {return error;}
	
	public void addMachineCode(Integer r) {values.add(r);}
	public List<Integer> getMachineCode() {return values;}
	public SourceCode getSourceCode() {return source;}
	
	public boolean hasLabel() {return label != null;}
	public void setLabel(String l) {label = l;}
	public String getLabel() {return label;}
}
