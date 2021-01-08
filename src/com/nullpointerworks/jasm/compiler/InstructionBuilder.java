package com.nullpointerworks.jasm.compiler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.controlflow.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.dataflow.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.system.*;

public final class InstructionBuilder
{
	private List<Draft> draft;
	private List<Instruction> instructions;
	
	public InstructionBuilder()
	{
		instructions = new ArrayList<Instruction>();
	}
	
	public void setDraft(List<Draft> draft)
	{
		this.draft = draft;
	}
	
	public List<Instruction> getInstructions()
	{
		return instructions;
	}
	
	public void convert()
	{
		instructions.clear();
		for (Draft d : draft)
		{
			Operation opcode = d.getOperation();
			
			switch(opcode)
			{
			case LOAD:
				_load(d);
				break;
			case READ:
				_read(d);
				break;
			case STO:
				_store(d);
				break;
			case PUSH:
				_push(d);
				break;
			case POP:
				_pop(d);
				break;
				
				
			case JMP:
				instructions.add( new Jump( d.getJumpAddress() ) );
				break;
			case CALL:
				instructions.add( new Call( d.getJumpAddress() ) );
				break;
			case RET:
				instructions.add( new Return() );
				break;
			case JE:
				instructions.add( new JumpEqual( d.getJumpAddress() ) );
				break;
			case JNE:
				instructions.add( new JumpNotEqual( d.getJumpAddress() ) );
				break;
			case JL:
				instructions.add( new JumpLess( d.getJumpAddress() ) );
				break;
			case JLE:
				instructions.add( new JumpLessEqual( d.getJumpAddress() ) );
				break;
			case JG:
				instructions.add( new JumpGreater( d.getJumpAddress() ) );
				break;
			case JGE:
				instructions.add( new JumpGreaterEqual( d.getJumpAddress() ) );
				break;
				
				
			case ADD: 
				_addition(d);
				break;
				
			case SUB: 
				_subtract(d);
				break;
				
			case CMP: 
				_compare(d);
				break;
				
			case INC: 
				_increment(d);
				break;
				
			case DEC: 
				_decrement(d);
				break;
				
			case NEG: 
				_negate(d);
				break;
				
			case SHL: 
				_shiftleft(d);
				break;
				
			case SHR: 
				_shiftright(d);
				break;
				
				
			case INT:
				_interrupt(d);
				break;
			
			case NOP: 
			default:
				_default();
				break;
			}
		}
	}
	
	/*
	 * ======================================
	 */
	
	private void _pop(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		instructions.add( new Pop_S( op.getRegister() ) );
	}
	
	private void _push(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		if (op.isRegister())
		{
			instructions.add( new Push_S( op.getRegister() ) );
		}
		else
		if (op.isImmediate())
		{
			instructions.add( new Push_I( op.getImmediate() ) );
		}
	}

	private void _store(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			instructions.add( new Store_SS( op1.getRegister(), op2.getRegister() ) );
		}
		else
		if (op2.isImmediate())
		{
			instructions.add( new Store_IS( op1.getImmediate(), op2.getRegister() ) );
		}
	}

	private void _read(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			instructions.add( new Read_SS( op1.getRegister(), op2.getRegister() ) );
		}
		else
		if (op2.isImmediate())
		{
			instructions.add( new Read_SI( op1.getRegister(), op2.getImmediate() ) );
		}
	}

	private void _load(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			instructions.add( new Load_SS( op1.getRegister(), op2.getRegister() ) );
		}
		else
		if (op2.isImmediate())
		{
			instructions.add( new Load_SI( op1.getRegister(), op2.getImmediate() ) );
		}
	}

	private void _addition(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			instructions.add( new Addition_SS( op1.getRegister(), op2.getRegister() ) );
		}
		else
		if (op2.isImmediate())
		{
			instructions.add( new Addition_SI( op1.getRegister(), op2.getImmediate() ) );
		}
	}
	
	private void _subtract(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			instructions.add( new Subtract_SS( op1.getRegister(), op2.getRegister() ) );
		}
		else
		if (op2.isImmediate())
		{
			instructions.add( new Subtract_SI( op1.getRegister(), op2.getImmediate() ) );
		}
	}
	
	private void _compare(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op1 = ops.get(0);
		Operand op2 = ops.get(1);
		
		if (op2.isRegister())
		{
			instructions.add( new Compare_SS( op1.getRegister(), op2.getRegister() ) );
		}
		else
		if (op2.isImmediate())
		{
			instructions.add( new Compare_SI( op1.getRegister(), op2.getImmediate() ) );
		}
	}
	
	private void _increment(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		instructions.add( new Increment_S( op.getRegister() ) );
	}
	
	private void _decrement(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		instructions.add( new Decrement_S( op.getRegister() ) );
	}
	
	private void _negate(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		instructions.add( new Negate_S( op.getRegister() ) );
	}
	
	private void _shiftleft(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		instructions.add( new ShiftLeft_S( op.getRegister() ) );
	}
	
	private void _shiftright(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		instructions.add( new ShiftRight_S( op.getRegister() ) );
	}
	
	private void _interrupt(Draft d)
	{
		List<Operand> ops = d.getOperands();
		Operand op = ops.get(0);
		instructions.add( new Interrupt( op.getImmediate() ) );
	}

	private void _default()
	{
		instructions.add( new NoOperation() );
	}
}
