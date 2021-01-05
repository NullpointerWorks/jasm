package com.nullpointerworks.jasm.virtualmachine;

import java.util.ArrayList;
import java.util.List;

public class VirtualMachineJASM implements VirtualMachine
{
	private InterruptListener interrupt;
	private List<Instruction> instructions;
	private List<Integer> stack;
	private List<Integer> memref;
	
	private Register rCounter; // instruction pointer
	private Register rStack; // stack pointer
	private Register rA;
	private Register rB;
	private Register rC;
	private Register rD;
		private Register rI;
		private Register rJ;
		private Register rK;
	private Register rU;
	private Register rV;
	private Register rW;
		private Register rX;
		private Register rY;
		private Register rZ;
	
	private Flag zero;
	private Flag sign;
	
	public VirtualMachineJASM(InterruptListener il)
	{
		interrupt = il;
		
		rA = new Register();
		rB = new Register();
		rC = new Register();
		rD = new Register();
		
		rI = new Register();
		rJ = new Register();
		rK = new Register();
		
		rU = new Register();
		rV = new Register();
		rW = new Register();
		
		rX = new Register();
		rY = new Register();
		rZ = new Register();
		
		rCounter = new Register();
		rStack = new Register(-1);

		zero = new Flag();
		sign = new Flag();
		
		instructions = new ArrayList<Instruction>();
		stack = new ArrayList<Integer>();
	}
	
	@Override
	public void throwInterrupt(int intcode)
	{
		interrupt.onInterrupt(this, intcode);
	}
	
	// =======================================================
	
	@Override
	public void setMemorySize(int size)
	{
		memref = new ArrayList<Integer>(size);
		for (int l=size; l>0;l--) memref.add(0);
	}
	
	@Override
	public List<Integer> getMemory()
	{
		return memref;
	}
	
	@Override
	public void storeMemory(int index, int value)
	{
		memref.add(index, value);
	}
	
	@Override
	public int readMemory(int index)
	{
		return memref.get(index);
	}
	
	@Override
	public void pushStack(int x)
	{
		rStack.setValue( rStack.getValue()+1 );
		stack.add(rStack.getValue(), x);
	}
	
	@Override
	public int popStack()
	{
		int sp = rStack.getValue();
		int x = stack.get(sp);
		stack.remove(sp);
		rStack.setValue(sp-1);
		return x;
	}
	
	// =======================================================
	
	@Override
	public void addInstructions(List<Instruction> instructions)
	{
		for (Instruction i : instructions)
		{
			addInstruction(i);
		}
	}
	
	@Override
	public void addInstruction(Instruction inst)
	{
		instructions.add(inst);
	}
	
	@Override
	public boolean hasInstruction()
	{
		return !(rCounter.getValue() >= instructions.size());
	}
	
	@Override
	public void nextInstruction()
	{
		var instruct = instructions.get(rCounter.getValue());
		rCounter.setValue( rCounter.getValue() + 1 ); // set to next instruction
		instruct.execute(this);
	}
	
	// =======================================================
	
	@Override
	public void resetFlags()
	{
		zero.setValue(false);
		sign.setValue(false);
	}
	
	@Override
	public void setFlag(Select s, boolean flag)
	{
		Flag f = getFlag(s);
		f.setValue(flag);
	}
	
	@Override
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
	
	// =======================================================
	
	@Override
	public void resetCounter(int c)
	{
		rCounter.setValue(c);
	}
	
	@Override
	public void setRegister(Select s, int v)
	{
		Register r = getRegister(s);
		if (r==null) return; // throw error
		r.setValue(v);
	}
	
	@Override
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
		
		case REG_I: return rI;
		case REG_J: return rJ;
		case REG_K: return rK;
		
		case REG_U: return rU;
		case REG_V: return rV;
		case REG_W: return rW;
		
		case REG_X: return rX;
		case REG_Y: return rY;
		case REG_Z: return rZ;
		
		default: break; // throw error
		}
		return null;
	}
}
