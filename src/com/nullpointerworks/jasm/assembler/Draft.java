package com.nullpointerworks.jasm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.assembler.errors.BuildError;

/**
 * 
 * @author Michiel Drost - Nullpointer Works
 */
public class Draft
{
	/*
	 * parse data
	 */
	private SourceCode source;
	private String label;
	private int address;
	private BuildError error;
	
	/*
	 * instruction data
	 */
	private Operation operation;
	private List<Operand> operands;
	
	public Draft(SourceCode sc, Operation op)
	{
		source = sc;
		operation = op;
		operands = new ArrayList<Operand>();
		label = "";
		error = null;
		address = 0;
	}
	
	public SourceCode getSourceCode() {return source;}
	public void setJumpAddress(int addr) {address = addr;}
	public int getJumpAddress() {return address;}
	
	/*
	 * 
	 */
	public Operation getOperation() {return operation;}
	public void addOperand(String operand) {operands.add(new Operand(operand));}
	public void addOperand(Operand oper) {operands.add(oper);}
	public List<Operand> getOperands() {return operands;}
	
	
	/*
	 * labels
	 */
	public void setLabel(String l) {label = l;}
	public String getLabel() {return label;}
	public boolean hasLabel() {return label.length() > 0;}
	
	/*
	 * errors
	 */
	public boolean hasError() 
	{
		return error != null;
	}
	public void setError(BuildError e) {error = e;}
	public BuildError getError() {return error;}
}
