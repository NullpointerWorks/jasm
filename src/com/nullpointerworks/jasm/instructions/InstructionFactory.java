package com.nullpointerworks.jasm.instructions;

import com.nullpointerworks.jasm.instructions.arithmetic.*;
import com.nullpointerworks.jasm.processor.Select;


public final class InstructionFactory
{
	public static Instruction NOP()
	{
		return new NOP();
	}
	
	public static Instruction INT(int v)
	{
		return new INT(v);
	}
	
	public static Instruction GEN_SS(InstructionSet inst, Select s1, Select s2)
	{
		switch(inst)
		{
		case ADD: return new ADD_SS(s1,s2);
		case SUB: return new SUB_SS(s1,s2);
		case CMP: return new CMP_SS(s1,s2);
		case LOAD: return new LOAD_SS(s1,s2);
		default: break;
		}
		return null; // error
	}

	public static Instruction GEN_SI(InstructionSet inst, Select s1, int imm)
	{
		switch(inst)
		{
		case ADD: return new ADD_SI(s1,imm);
		case SUB: return new SUB_SI(s1,imm);
		case CMP: return new CMP_SI(s1,imm);
		case LOAD: return new LOAD_SI(s1,imm);
		default: break;
		}
		return null; // error
	}
	
	public static Instruction GEN_JMP(InstructionSet inst)
	{
		switch(inst)
		{
		case JMP: return new JMP(0);
		case JE: return new JE(0);
		case JNE: return new JNE(0);
		case JL: return new JL(0);
		case JLE: return new JLE(0);
		case JG: return new JG(0);
		case JGE: return new JGE(0);
		default: break;
		}
		return null; // error
	}
	
	
	
	
	
	/*
	 * arithmetic
	 */
	public static Instruction Add(Select s1, Select s2)
	{
		return new ADD_SS(s1,s2);
	}
	
	public static Instruction Add(Select s, int imm)
	{
		return new ADD_SI(s,imm);
	}
	
	public static Instruction Sub(Select s1, Select s2)
	{
		return new SUB_SS(s1,s2);
	}
	
	public static Instruction Sub(Select s, int imm)
	{
		return new SUB_SI(s,imm);
	}
	
	public static Instruction Cmp(Select s1, Select s2)
	{
		return new CMP_SS(s1,s2);
	}
	
	public static Instruction Cmp(Select s, int imm)
	{
		return new CMP_SI(s,imm);
	}
	
	/*
	 * memory
	 */
	public static Instruction Load(Select s1, Select s2)
	{
		return new LOAD_SS(s1,s2);
	}
	
	public static Instruction Load(Select s, int imm)
	{
		return new LOAD_SI(s,imm);
	}
	
	public static Instruction Push(Select s)
	{
		return new PUSH_S(s);
	}
	
	public static Instruction Push(int v)
	{
		return new PUSH_I(v);
	}
	
	public static Instruction Pop(Select s)
	{
		return new POP_S(s);
	}
	
	/*
	 * flow control
	 */
	public static Instruction Jmp(int p)
	{
		return new JMP(p);
	}
	
	public static Instruction Je(int p)
	{
		return new JE(p);
	}
	
	public static Instruction Jne(int p)
	{
		return new JNE(p);
	}
	
	public static Instruction Jl(int p)
	{
		return new JL(p);
	}
	
	public static Instruction Jle(int p)
	{
		return new JLE(p);
	}
	
	public static Instruction Jg(int p)
	{
		return new JG(p);
	}
	
	public static Instruction Jge(int p)
	{
		return new JGE(p);
	}
	
	
	
	
	
}
