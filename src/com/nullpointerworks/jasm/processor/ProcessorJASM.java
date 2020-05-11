package com.nullpointerworks.jasm.processor;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.instructions.Instruction;

public class ProcessorJASM implements Processor
{
	private InterruptListener interrupt;
	private List<Instruction> instructions;
	private List<Integer> stack;
	
	private Register rCounter; // instruction pointer
	private Register rStack; // stack pointer
	private Register rA;
	private Register rB;
	private Register rC;
	private Register rD;
	
	private Flag zero;
	private Flag sign;
	
	public ProcessorJASM(InterruptListener il)
	{
		interrupt = il;
		
		rA = new Register();
		rB = new Register();
		rC = new Register();
		rD = new Register();
		rCounter = new Register();
		rStack = new Register(-1);

		zero = new Flag();
		sign = new Flag();
		
		instructions = new ArrayList<Instruction>();
		stack = new ArrayList<Integer>();
	}

	public void addInstructions(List<Instruction> instructions)
	{
		for (Instruction i : instructions)
		{
			addInstruction(i);
		}
	}
	
	public void addInstruction(Instruction inst)
	{
		instructions.add(inst);
	}
	
	public void pushStack(int x)
	{
		rStack.setValue( rStack.getValue()+1 );
		stack.add(rStack.getValue(), x);
	}
	
	public int popStack()
	{
		int x = stack.get(rStack.getValue());
		stack.remove(rStack.getValue());
		rStack.setValue(rStack.getValue()-1);
		return x;
	}
	
	public boolean hasInstruction()
	{
		return !(rCounter.getValue() >= instructions.size());
	}

	public void throwInterrupt(int intcode)
	{
		interrupt.onInterrupt(this, intcode);
	}
	
	public void nextInstruction()
	{
		var instruct = instructions.get(rCounter.getValue());
		rCounter.setValue( rCounter.getValue() + 1 );
		instruct.execute(this);
	}
	
	public void resetCounter(int c)
	{
		rCounter.setValue(c);
	}
	
	public void setSelection(Select s, int v)
	{
		Register r = getRegister(s);
		if (r==null) return; // throw error
		r.setValue(v);
	}
	
	public void resetFlags()
	{
		zero.setValue(false);
		sign.setValue(false);
	}

	public void setFlag(Select s, boolean flag)
	{
		Flag f = getFlag(s);
		f.setValue(flag);
	}
	
	public Register getRegister(Select s)
	{
		switch(s)
		{
		case IP: return rCounter;
		case SP: return rStack;
		case REG_A: return rA;
		case REG_B: return rB;
		case REG_C: return rC;
		case REG_D: return rD;
		
		
		default: break; // throw error
		}
		return null;
	}
	
	public Flag getFlag(Select s)
	{
		switch(s)
		{
		case ZERO: return zero;
		case SIGN: return sign;
		
		default: break; // throw error
		}
		return null;
	}
}
