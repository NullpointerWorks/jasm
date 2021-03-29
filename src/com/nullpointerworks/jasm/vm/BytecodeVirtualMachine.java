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
	// processor registers
	private Register regIP;
	private Register regSP;
	
	// system registers
	private Register regA;
	private Register regB;
	private Register regC;
	private Register regD;
	
	// general purpose registers
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
		
		addInstruction( new Noop() );
		addInstruction( new Interrupt() );
		
		addInstruction( new Load_RR() );
		addInstruction( new Load_RRM() );
		addInstruction( new Load_RMR() );
		addInstruction( new Load_RV() );
		addInstruction( new Load_RM() );
		addInstruction( new Load_MR() );
		addInstruction( new Push_R() );
		addInstruction( new Push_V() );
		addInstruction( new Pop() );
		addInstruction( new Load_RMV() );
		
		addInstruction( new Add_RR() );
		addInstruction( new Add_RV() );
		addInstruction( new Sub_RR() );
		addInstruction( new Sub_RV() );
		addInstruction( new Cmp_RR() );
		addInstruction( new Cmp_RV() );
		addInstruction( new Shl() );
		addInstruction( new Shr() );
		addInstruction( new Neg() );
		addInstruction( new Inc() );
		addInstruction( new Dec() );
		
		addInstruction( new Jump() );
		addInstruction( new JumpEqual() );
		addInstruction( new JumpNotEqual() );
		addInstruction( new JumpLess() );
		addInstruction( new JumpLessEqual() );
		addInstruction( new JumpGreater() );
		addInstruction( new JumpGreaterEqual() );
		addInstruction( new Call() );
		addInstruction( new Ret() );
		
		addInstruction( new Or_RR() );
		addInstruction( new And_RR() );
		addInstruction( new Xor_RR() );
	}
	
	@Override
	public void addInstruction(Instruction inst)
	{
		if (inst==null) return;
		instructions.add(inst);
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
		//System.out.println(""+regIP.getValue());
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
	
	private void throwException(VMException excode)
	{
		throwException(excode, BytecodeVirtualMachine.class);
	}
	
	public void throwException(VMException excode, Class<?> clazz)
	{
		VMProcessException vmex = new VMProcessException(excode, this, "", clazz);
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
	public void setOrigin(int origin)
	{
		regIP.setValue(origin);
	}
	
	@Override
	public void setMemorySize(int size)
	{
		regSP.setValue(size);
		memory.clear();
		for (; size>0; size--)
		{
			memory.add(0);
		}
	}
	
	@Override
	public void setMemory(int index, int value) 
	{
		if (index < 0 || index >= memory.size()) 
		{
			throwException( new VMProcessException(VMException.VMEX_MEMORY_OUTOFBOUNDS, 
													this, 
													"Trying to set a value in memory at address: "+index, 
													BytecodeVirtualMachine.class) );
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
