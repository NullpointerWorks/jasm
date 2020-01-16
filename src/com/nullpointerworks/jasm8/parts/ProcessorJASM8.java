package com.nullpointerworks.jasm8.parts;

import com.nullpointerworks.jasm8.Memory;
import com.nullpointerworks.jasm8.Monitor;
import com.nullpointerworks.jasm8.Processor;

public class ProcessorJASM8 
implements Processor, InstructionsJASM8
{
	/*
	 * 16-bit registers
	 */
	private short ip = 0; // instruction pointer
	private short sp = 512; // stack pointer
	private short x = 0; // X register
	private short y = 0; // Y register
	
	/*
	 * 8-bit registers
	 */
	private byte regA = 0; // accumulator
	private byte regB = 0; // base
	private byte regC = 0; // counter
	private byte regD = 0; // data
	private byte reg0 = 0; // GP 0 | X
	private byte reg1 = 0; // GP 1 |
	private byte reg2 = 0; // GP 2  | Y
	private byte reg3 = 0; // GP 3  |
	
	/*
	 * "1-bit" status registers
	 */
    private boolean OF = false; // overflow flag
    private boolean CF = false; // carry flag
    private boolean SF = false; // sign flag, negative
    private boolean ZF = false; // zero flag
    private boolean HF = false; // halt flag
    
    /*
     * internal
     */
    private Monitor monitor = null;
    private Memory rom = null;
    private Memory ram = null;
	
	public ProcessorJASM8(Monitor m, Memory rom, Memory ram) 
	{
		this.monitor=m;
		this.rom=rom;
		this.ram=ram;
		this.sp=(short)ram.length();
	}
	
	/*
	 * fetching
	 * decoding
	 * acquire
	 * execute
	 */
	@Override
    public final void cycle()
    {
    	if (HF) return;
    	byte inst = _fetch();
    	_decode(inst);
    }
    
	// ====================================================================
	// 		INTERNAL
	// ====================================================================
    
    private final void _decode(byte inst)
    {
    	switch(inst)
    	{
    	case NOP: return;
    	case END: _end(); return;
    	case LOAD: _load(); return;
    	case OUT: _out(); return;
    	case ADD: _add(false); return;
    	case SUB: _add(true); return;
    	case CMP: _cmp(); return;
    	case JMP: _jmp(); return;
    	case NEG: _neg(); return;
    	case STO: _sto(); return;
    	case PUSH: _push(); return;
    	case POP: _pop(); return;
    	case CALL: _call(); return;
    	case RET: _ret(); return;
    	case JE: _je(); return; // JZ
    	case JNE: _jne(); return;
    	case JL: _jl(); return;
    	case JLE: _jle(); return;
    	case JG: _jg(); return;
    	case JGE: _jge(); return;
    	case INC: _inc(); return;
    	case DEC: _dec(); return;
    	}
    	HF = true;
    }
    
    // ====================================================================
    // 
    // ====================================================================
    
    private final void _inc()
	{
    	byte directive = _fetch();
    	switch(directive)
    	{
    	case RA: regA = _alu(regA,(byte)1,false); return;
    	case RB: regB = _alu(regB,(byte)1,false); return;
    	case RC: regC = _alu(regC,(byte)1,false); return;
    	case RD: regD = _alu(regD,(byte)1,false); return;
    	case R0: reg0 = _alu(reg0,(byte)1,false); return;
    	case R1: reg1 = _alu(reg1,(byte)1,false); return;
    	case R2: reg2 = _alu(reg2,(byte)1,false); return;
    	case R3: reg3 = _alu(reg3,(byte)1,false); return;
    	}
	}
    
    private final void _dec()
	{
    	byte directive = _fetch();
    	switch(directive)
    	{
    	case RA: regA = _alu(regA,(byte)1,true); return;
    	case RB: regB = _alu(regB,(byte)1,true); return;
    	case RC: regC = _alu(regC,(byte)1,true); return;
    	case RD: regD = _alu(regD,(byte)1,true); return;
    	case R0: reg0 = _alu(reg0,(byte)1,true); return;
    	case R1: reg1 = _alu(reg1,(byte)1,true); return;
    	case R2: reg2 = _alu(reg2,(byte)1,true); return;
    	case R3: reg3 = _alu(reg3,(byte)1,true); return;
    	}
	}
    
    private final void _je()
	{
    	if ( ZF )
    	{
    		_jmp();
    	}
    	else
    	{
    		ip+=2;
    	}
	}
    
    private final void _jne()
	{
    	if ( !ZF )
    	{
    		_jmp();
    	}
    	else
    	{
    		ip+=2;
    	}
	}
    
    private final void _jl()
	{
    	if ( SF ^ OF )
    	{
    		_jmp();
    	}
    	else
    	{
    		ip+=2;
    	}
	}
    
    private final void _jle()
	{
    	if ( ZF || (SF ^ OF) )
    	{
    		_jmp();
    	}
    	else
    	{
    		ip+=2;
    	}
	}
    
    private final void _jg()
	{
    	if ( !ZF && (SF == OF) )
    	{
    		_jmp();
    	}
    	else
    	{
    		ip+=2;
    	}
	}
    
    private final void _jge()
	{
    	if ( SF == OF )
    	{
    		_jmp();
    	}
    	else
    	{
    		ip+=2;
    	}
	}
    
    private final void _push()
	{
    	byte directive = _fetch();
    	sp--;
    	
    	switch(directive)
    	{
    	case RA: ram.write(sp, regA); return;
    	case RB: ram.write(sp, regB); return;
    	case RC: ram.write(sp, regC); return;
    	case RD: ram.write(sp, regD); return;
    	case R0: ram.write(sp, reg0); return;
    	case R1: ram.write(sp, reg1); return;
    	case R2: ram.write(sp, reg2); return;
    	case R3: ram.write(sp, reg3); return;
    	}
    	
    	switch(directive)
    	{
    	case IP:
    		{
    			byte ipH = (byte)((ip>>8)&0xff);
    			byte ipL = (byte)((ip)&0xff);
    			ram.write(sp, ipH);
    			sp--;
    			ram.write(sp, ipL);
    		}
    		return;
    	case SP:
			{
				byte ipH = (byte)((sp>>8)&0xff);
				byte ipL = (byte)((sp)&0xff);
				ram.write(sp, ipH);
				sp--;
				ram.write(sp, ipL);
			}
			return;
    	}
    	
    	// write 8 bits
    	byte data8 = _fetch();
    	ram.write(sp, data8);
    	
    	// write 16 bits
    	if (directive == M)
    	{
        	sp--;
    		data8 = _fetch();
        	ram.write(sp, data8);
    	}
	}
    
    private final void _pop()
	{
    	byte directive = _fetch();
    	byte data = ram.read(sp);
    	sp++;
    	
    	switch(directive)
    	{
    	case RA: regA = data; return;
    	case RB: regB = data; return;
    	case RC: regC = data; return;
    	case RD: regD = data; return;
    	case R0: reg0 = data; return;
    	case R1: reg1 = data; return;
    	case R2: reg2 = data; return;
    	case R3: reg3 = data; return;
    	}
    	
    	short pointer = (short)((ram.read(sp)<<8) + data);
    	sp++;
    	
    	switch(directive)
    	{
    	case IP: ip = pointer; return;
    	case SP: sp = pointer; return;
    	}
	}
    
    private final void _call()
	{
		short address = _fetch16(); // moves the instruction pointer to the next instruction
		
    	// put instruction pointer on the stack
    	byte ipH = (byte)((ip>>8)&0xff);
		byte ipL = (byte)((ip)&0xff);
		sp--;
		ram.write(sp, ipH);
		sp--;
		ram.write(sp, ipL);
		
		// jump to address
		ip = address;
	}
    
    private final void _ret()
	{
		byte arrdsL = ram.read(sp);
    	sp++;
    	byte arrdsH = ram.read(sp);
    	sp++;
    	
    	short address = (short)((arrdsH<<8) + arrdsL);
    	ip = address;
	}
    
    private final void _sto()
	{
    	byte directive 	= _fetch();
    	short addrs16 	= _fetch16();
    	
    	switch(directive)
    	{
    	case RA: ram.write(addrs16, regA); return;
    	case RB: ram.write(addrs16, regB); return;
    	case RC: ram.write(addrs16, regC); return;
    	case RD: ram.write(addrs16, regD); return;
    	case R0: ram.write(addrs16, reg0); return;
    	case R1: ram.write(addrs16, reg1); return;
    	case R2: ram.write(addrs16, reg2); return;
    	case R3: ram.write(addrs16, reg3); return;
    	default: break;
    	}
    	
    	byte immediate = _fetch();
    	ram.write(addrs16, immediate);
	}
    
    private final void _neg()
	{
    	byte directive = _fetch();
    	switch(directive)
    	{
    	case RA: regA = _neg(regA); break;
		case RB: regB = _neg(regB); break;
		case RC: regC = _neg(regC); break;
		case RD: regD = _neg(regD); break;
		case R0: reg0 = _neg(reg0); break;
		case R1: reg1 = _neg(reg1); break;
		case R2: reg2 = _neg(reg2); break;
		case R3: reg3 = _neg(reg3); break;
    	}
	}
    
    private final void _end()
	{
    	HF = true;
    	monitor.onEND(regD);
	}
    
    private final void _add(boolean sub)
	{
    	byte directive = _fetch();
    	byte rA = (byte)((directive>>4)&0x0f);
    	byte rB = (byte)(directive&0x0f);
    	byte select = rA;
    	
    	if (rB != I) 
    	{
    		switch(rB)
    		{
        	case RA: rB = regA; break;
        	case RB: rB = regB; break;
        	case RC: rB = regC; break;
        	case RD: rB = regD; break;
        	case R0: rB = reg0; break;
        	case R1: rB = reg1; break;
        	case R2: rB = reg2; break;
        	case R3: rB = reg3; break;
    		}
        	
        	switch(rA)
    		{
        	case RA: rA = regA; break;
        	case RB: rA = regB; break;
        	case RC: rA = regC; break;
        	case RD: rA = regD; break;
        	case R0: rA = reg0; break;
        	case R1: rA = reg1; break;
        	case R2: rA = reg2; break;
        	case R3: rA = reg3; break;
    		}
        	
        	rA = _alu(rA, rB, sub);
        	
        	switch(select)
    		{
        	case RA: regA = rA; return;
        	case RB: regB = rA; return;
        	case RC: regC = rA; return;
        	case RD: regD = rA; return;
        	case R0: reg0 = rA; return;
        	case R1: reg1 = rA; return;
        	case R2: reg2 = rA; return;
        	case R3: reg3 = rA; return;
    		}
    	}
    	
    	byte operant = _fetch();
    	
    	// 8 bit immediate to register
    	switch(directive)
		{
    	case RAI: regA = _alu(regA, operant, sub); return;
    	case RBI: regB = _alu(regB, operant, sub); return;
    	case RCI: regC = _alu(regC, operant, sub); return;
    	case RDI: regD = _alu(regD, operant, sub); return;
    	case R0I: reg0 = _alu(reg0, operant, sub); return;
    	case R1I: reg1 = _alu(reg1, operant, sub); return;
    	case R2I: reg2 = _alu(reg2, operant, sub); return;
    	case R3I: reg3 = _alu(reg3, operant, sub); return;
		}
	}
    
    private final void _cmp()
	{
    	byte directive = _fetch();
    	
    	// 8 bit register to register
    	switch(directive)
		{
    	case RBA: _alu(regB, regA, true); return;
    	case RCA: _alu(regC, regA, true); return;
    	case RDA: _alu(regD, regA, true); return;
    	
    	case RAB: _alu(regA, regB, true); return;
    	case RCB: _alu(regC, regB, true); return;
    	case RDB: _alu(regD, regB, true); return;
    	
    	case RAC: _alu(regA, regC, true); return;
    	case RBC: _alu(regB, regC, true); return;
    	case RDC: _alu(regD, regC, true); return;
    	
    	case RAD: _alu(regA, regD, true); return;
    	case RBD: _alu(regB, regD, true); return;
    	case RCD: _alu(regC, regD, true); return;
		}
    	
    	byte operant = _fetch();
    	
    	// 8 bit immediate to register
    	switch(directive)
		{
    	case RAI: _alu(regA, operant, true); return;
    	case RBI: _alu(regB, operant, true); return;
    	case RCI: _alu(regC, operant, true); return;
    	case RDI: _alu(regD, operant, true); return;
		}
	}
    
    private final void _jmp()
	{
    	short addrs16 = _fetch16(); // label as address to instruction
    	ip = addrs16;
	}
    
    private final void _load()
	{
    	byte directive = _fetch();
    	
    	byte H = (byte)((directive>>4)&0x0f);
    	byte L = (byte)(directive&0x0f);
    	
    	if ( (L != I) && (L != M) )
    	{
        	L = _register_value8(L);
        	switch(H)
    		{
        	case RA: regA = L; return;
        	case RB: regB = L; return;
        	case RC: regC = L; return;
        	case RD: regD = L; return;
        	case R0: reg0 = L; return;
        	case R1: reg1 = L; return;
        	case R2: reg2 = L; return;
        	case R3: reg3 = L; return;
    		}
    	}
    	
    	byte operant = _fetch();
    	
    	// 8 bit immediate to register
    	switch(directive)
		{
		case RAI: regA = operant; return;
		case RBI: regB = operant; return;
		case RCI: regC = operant; return;
		case RDI: regD = operant; return;
		case R0I: reg0 = operant; return;
		case R1I: reg1 = operant; return;
		case R2I: reg2 = operant; return;
		case R3I: reg3 = operant; return;
		default: break;
		}
    	
    	short operant16 = (short)((operant << 8) + _fetch());
    	
    	// 16 bit address from RAM to register
    	switch(directive)
		{
		case RAM: regA = ram.read(operant16); return;
		case RBM: regB = ram.read(operant16); return;
		case RCM: regC = ram.read(operant16); return;
		case RDM: regD = ram.read(operant16); return;
		case R0M: reg0 = ram.read(operant16); return;
		case R1M: reg1 = ram.read(operant16); return;
		case R2M: reg2 = ram.read(operant16); return;
		case R3M: reg3 = ram.read(operant16); return;
		default: break;
		}
    	
	}
 
 	private final void _out()
	{
		byte directive = _fetch();
		int output = 0;
		switch(directive)
		{
		case RA: output = regA&0xff; break;
		case RB: output = regB&0xff; break;
		case RC: output = regC&0xff; break;
		case RD: output = regD&0xff; break;
		case R0: output = reg0&0xff; break;
		case R1: output = reg1&0xff; break;
		case R2: output = reg2&0xff; break;
		case R3: output = reg3&0xff; break;
		case IP: output = ip&0xffff; break;
		case SP: output = sp&0xffff; break;
		case RX: output = x&0xffff; break;
		case RY: output = y&0xffff; break;
		case I:
			{
				byte operant = _fetch();
				output = operant&0xff;
				break;
			}
		default: return;
		}
		
		monitor.onOUT( output );
	}
 	
    // ====================================================================
    // 
    // ====================================================================
 	
 	private final byte _register_value8(byte reg)
    {
    	switch(reg)
		{
    	case RA: return regA;
    	case RB: return regB;
    	case RC: return regC;
    	case RD: return regD;
    	case R0: return reg0;
    	case R1: return reg1;
    	case R2: return reg2;
    	case R3: return reg3;
		}
		return -1;
    }
    
 	private final byte _alu(byte a, byte b, boolean sub)
	{
        b = sub ? _neg(b) : b;
        short s = (short)(a + b);
        CF = s >> 8 != 0;
        byte sum = (byte)s;
        ZF = sum == 0;
        SF = sum >> 7 != 0;
        OF = CF ^ SF;
        return sum;
    }
	
    private final byte _neg(byte num)
    {
        return (byte)(~num+1);
    }
 	
 	private final short _alu16(short a, short b, boolean sub)
	{
        b = sub ? _neg16(b) : b;
        int s = (int)(a + b);
        CF = s >> 16 != 0;
        short sum = (short)s;
        ZF = sum == 0;
        SF = sum >> 15 != 0;
        OF = CF ^ SF;
        return sum;
    }
	
    private final short _neg16(short num)
    {
        return (short)(~num+1);
    }
    
    private final byte _fetch()
    {
    	byte inst = rom.read(ip);
    	ip++;
    	return inst;
    }
    
    private final short _fetch16()
    {
    	byte addrs8 = _fetch();
    	short addrs16 = (short)((addrs8 << 8) + _fetch());
    	return addrs16;
    }
}
