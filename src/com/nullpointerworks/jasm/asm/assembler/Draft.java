package com.nullpointerworks.jasm.asm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.segment.Number;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class Draft
{
	private SourceCode source;
	private BuildError error;
	
	public Draft(SourceCode sc) 
	{
		source = sc;
	}
	
	public void setError(BuildError err) {error = err;}
	public boolean hasError() {return error != null;}
	public BuildError getError() {return error;}
	public SourceCode getSourceCode() {return source;}
	
	
	
	
	
	
}

/*
public class Draft
{
	private SourceCode source;
	private String label;
	private BuildError error;
	private List<Number> values;
	
	public Draft(SourceCode sc) 
	{
		source = sc;
		label = null;
		error = null;
		values = new ArrayList<Number>();
	}
	
	public boolean hasError() {return error != null;}
	public void setError(BuildError err) {error = err;}
	public BuildError getError() {return error;}
	
	public void addMachineCode(int r) { addMachineCode( new Number(r) );}
	public void addMachineCode(Number r) {values.add(r);}
	public List<Number> getMachineCode() {return values;}
	public SourceCode getSourceCode() {return source;}
	
	public boolean hasLabel() {return label != null;}
	public void setLabel(String l) {label = l;}
	public String getLabel() {return label;}
}
//*/
