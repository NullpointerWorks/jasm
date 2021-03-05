package com.nullpointerworks.jasm.vm;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.vm.instruction.arithmetic.*;
import com.nullpointerworks.jasm.vm.instruction.controlflow.*;
import com.nullpointerworks.jasm.vm.instruction.dataflow.*;
import com.nullpointerworks.jasm.vm.instruction.logic.*;
import com.nullpointerworks.jasm.vm.instruction.system.*;

public class BytecodeVirtualMachine implements VirtualMachine
{
	private Register regIP;
	private Register regSP;
	
	private Register regA;
	private Register regB;
	private Register regC;
	private Register regD;
	
	private Register reg0;
	private Register reg1;
	private Register reg2;
	private Register reg3;
	private Register reg4;
	private Register reg5;
	private Register reg6;
	private Register reg7;
	private Register reg8;
	private Register reg9;
	
	private Flag zero;
	private Flag sign;
	private Flag flow;
	
	private InterruptListener listener;
	private List<Integer> memory;
	private List<Instruction> instructions;
	private List<VMProcessException> exceptions;
	
	public BytecodeVirtualMachine()
	{
		regIP = new Register();
		regSP = new Register();
		regA = new Register();
		regB = new Register();
		regC = new Register();
		regD = new Register();
		reg0 = new Register();
		reg1 = new Register();
		reg2 = new Register();
		reg3 = new Register();
		reg4 = new Register();
		reg5 = new Register();
		reg6 = new Register();
		reg7 = new Register();
		reg8 = new Register();
		reg9 = new Register();
		zero = new Flag();
		sign = new Flag();
		flow = new Flag();
		
		listener = (vm,c)->{}; // default empty listener
		memory = new ArrayList<Integer>();
		exceptions = new ArrayList<VMProcessException>();
		instructions = new ArrayList<Instruction>();
		
		instructions.add( new Noop() );
		instructions.add( new Interrupt() );
		
		instructions.add( new Load_RR() );
		instructions.add( new Load_RV() );
		instructions.add( new Load_RM() );
		instructions.add( new Load_MR() );
		instructions.add( new Push() );
		instructions.add( new Pop() );
		
		instructions.add( new Add_RR() );
		instructions.add( new Add_RV() );
		instructions.add( new Sub_RR() );
		instructions.add( new Sub_RV() );
		instructions.add( new Cmp_RR() );
		instructions.add( new Cmp_RV() );
		instructions.add( new Neg() );
		instructions.add( new Inc() );
		instructions.add( new Dec() );
		instructions.add( new Shl() );
		instructions.add( new Shr() );
		//instructions.add( new Mul_RR() );
		//instructions.add( new Mul_RV() );
		//instructions.add( new Div_RR() );
		//instructions.add( new Div_RV() );
		
		instructions.add( new Jump() );
		instructions.add( new JumpEqual() );
		instructions.add( new JumpNotEqual() );
		instructions.add( new JumpLess() );
		instructions.add( new JumpLessEqual() );
		instructions.add( new JumpGreater() );
		instructions.add( new JumpGreaterEqual() );
		instructions.add( new Call() );
		instructions.add( new Ret() );
		
		instructions.add( new Or_RR() );
		instructions.add( new And_RR() );
		instructions.add( new Xor_RR() );
	}
	
	@Override
	public void nextInstruction()
	{
		if (regIP.getValue() >= memory.size())
		{
			throwException( VMException.VMEX_MEMORY_OVERFLOW );
			return;
		}
		
		int opcode = memory.get( regIP.getValue() );
		for (Instruction instruction : instructions)
		{
			if (instruction.match(opcode))
			{
				instruction.execute(this);
				return;
			}
		}
		
		throwException( VMException.VMEX_NO_EXECUTION );
		regIP.addValue(1);
	}
	
	@Override
	public void throwInterrupt(int code)
	{
		if (listener == null)
		{
			listener = (vm,c)->{};
		}
		listener.onInterrupt(this, code);
	}
	
	public void throwException(VMException excode)
	{
		VMProcessException vmex = new VMProcessException(excode, this, -1);
		exceptions.add( vmex );
	}
	
	public void throwException(VMProcessException vmex)
	{
		exceptions.add( vmex );
	}
	
	@Override
	public boolean hasException() 
	{
		return exceptions.size() > 0;
	}
	
	@Override
	public void setInterruptListener(InterruptListener il)
	{
		listener = il;
	}
	
	@Override
	public void setMemorySize(int size)
	{
		regSP.setValue(size);
		regIP.setValue(0);
		memory.clear();
		for (; size>0; size--)
		{
			memory.add(0);
		}
	}

	public void setMemory(int index, int value) 
	{
		if (index < 0) 
		{
			throwException( new VMProcessException(VMException.VMEX_MEMORY_OUTOFBOUNDS, this, index) );
			return;
		}
		if (index > memory.size())
		{
			throwException( new VMProcessException(VMException.VMEX_MEMORY_OUTOFBOUNDS, this, index) );
			return;
		}
		memory.set(index, value);
	}
	
	@Override
	public void setMemory(int offset, List<Integer> mem)
	{
		int j = 0;
		int i = offset;
		int l = i + mem.size();
		for (; i<l; i++, j++)
		{
			int v = mem.get(j);
			setMemory(i, v);
			if (hasException()) return;
		}
	}
	
	@Override
	public Register getRegister(VMRegister sel)
	{
		switch(sel)
		{
		case REG_IP: return regIP;
		case REG_SP: return regSP;
		case REG_A: return regA;
		case REG_B: return regB;
		case REG_C: return regC;
		case REG_D: return regD;
		case REG_0: return reg0;
		case REG_1: return reg1;
		case REG_2: return reg2;
		case REG_3: return reg3;
		case REG_4: return reg4;
		case REG_5: return reg5;
		case REG_6: return reg6;
		case REG_7: return reg7;
		case REG_8: return reg8;
		case REG_9: return reg9;
		default:break;
		}
		
		throwException( VMException.VMEX_NO_REGISTER_FOUND );
		return null;
	}
	
	@Override
	public Flag getFlag(VMFlag sel)
	{
		switch(sel)
		{
		case ZERO: return zero;
		case SIGN: return sign;
		case FLOW: return flow;
		default:break;
		}
		
		throwException( VMException.VMEX_NO_FLAG_FOUND );
		return null;
	}
	
	@Override
	public int getMemory(int index) 
	{
		if (index < 0) 
		{
			throwException( VMException.VMEX_MEMORY_OUTOFBOUNDS );
			return 0;
		}
		if (index > memory.size())
		{
			throwException( VMException.VMEX_MEMORY_OUTOFBOUNDS );
			return 0;
		}
		return memory.get(index);
	}
	
	@Override
	public int getMemoryAt(Register reg) 
	{
		int index = reg.getValue();
		return getMemory(index);
	}
	
	@Override
	public VMProcessException getException() 
	{
		return exceptions.remove(0);
	}
}
