package com.nullpointerworks.jasm.jasm8.parts;

import com.nullpointerworks.jasm.jasm8.Memory;
import com.nullpointerworks.jasm.jasm8.Monitor;
import com.nullpointerworks.jasm.jasm8.Processor;

public class ProcessorJASM8 
implements Processor, InstructionsJASM8
{
	/*
	 * 16-bit registers
	 */
	private short ip = 0; // instruction pointer
	private short sp = 512; // stack pointer
	
	/*
	 * 8-bit registers
	 */
	private byte regA = 0; // accumulator
	private byte regB = 0; // base
	private byte regC = 0; // counter
	private byte regD = 0; // data
	private byte regXH = 0; // GP 0 | X
	private byte regXL = 0; // GP 1 |
	private byte regYH = 0; // GP 2  | Y
	private byte regYL = 0; // GP 3  |
	
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
    	
    	case ADD: _add(false); return;
    	case SUB: _add(true); return;
    	case CMP: _cmp(); return;
    	case NEG: _neg(); return;
    	case INC: _inc(); return;
    	case DEC: _dec(); return;
    	
    	case PUSH: _push(); return;
    	case POP: _pop(); return;
    	
    	case STO: _sto(); return;
    	
    	
    	
    	
    	
    	
    	case END: _end(); return;
    	case LOAD: _load(); return;
    	case OUT: _out(); return;
    	case RET: _ret(); return;
    	case CALL: _call(); return;
    	case JMP: _jmp(); return;
    	case JE: _je(); return; // JZ
    	case JNE: _jne(); return;
    	case JL: _jl(); return;
    	case JLE: _jle(); return;
    	case JG: _jg(); return;
    	case JGE: _jge(); return;
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
    	case XH: regXH = _alu(regXH,(byte)1,false); return;
    	case XL: regXL = _alu(regXL,(byte)1,false); return;
    	case YH: regYH = _alu(regYH,(byte)1,false); return;
    	case YL: regYL = _alu(regYL,(byte)1,false); return;
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
    	case XH: regXH = _alu(regXH,(byte)1,true); return;
    	case XL: regXL = _alu(regXL,(byte)1,true); return;
    	case YH: regYH = _alu(regYH,(byte)1,true); return;
    	case YL: regYL = _alu(regYL,(byte)1,true); return;
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
    	case XH: ram.write(sp, regXH); return;
    	case XL: ram.write(sp, regXL); return;
    	case YH: ram.write(sp, regYH); return;
    	case YL: ram.write(sp, regYL); return;
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
    	case XH: regXH = data; return;
    	case XL: regXL = data; return;
    	case YH: regYH = data; return;
    	case YL: regYL = data; return;
    	}
    	
    	short pointer = (short)((ram.read(sp)<<8) + data);
    	sp++;
    	
    	switch(directive)
    	{
    	case IP: ip = pointer; return;
    	case SP: sp = pointer; return;
    	}
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
    	case XH: ram.write(addrs16, regXH); return;
    	case XL: ram.write(addrs16, regXL); return;
    	case YH: ram.write(addrs16, regYH); return;
    	case YL: ram.write(addrs16, regYL); return;
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
		case XH: regXH = _neg(regXH); break;
		case XL: regXL = _neg(regXL); break;
		case YH: regYH = _neg(regYH); break;
		case YL: regYL = _neg(regYL); break;
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
    
    private final void _add(boolean sub)
	{
    	byte directive = _fetch();
    	byte H = (byte)((directive>>4)&0x0f);
    	byte L = (byte)(directive&0x0f);
    	
    	// register to register
    	if ( _is_register8(H) )
    	{
    		byte regH8 = _register_value8(H);
    		
    		if ( _is_register8(L) )
        	{
    			byte regL8 = _register_value8(L);
    			byte res = _alu(regH8, regL8, sub);
    			_set_reg8(H,res);
    			return;
        	}
    		
    		if ( L == I )
    		{
    			byte adds = _fetch();
    			byte res = _alu(regH8, adds, sub);
    			_set_reg8(H,res);
    			return;
    		}
    		
    		return;
    	}
    	
    	if ( _is_register16(H) )
    	{
    		short regV16 = _register_value16(H);
    		
    		if ( _is_register(L) )
        	{
    			short regVxx = _register_value(L);
    			short res = _alu16(regV16, regVxx, sub);
    			_set_reg16(H, res);
    			return;
        	}
    		
    		if ( L == I )
    		{
    			
    			
    			
    			
    			return;
    		}
    		
    		if ( L == IL )
    		{
    			
    			
    			
    			
    			return;
    		}
    		
    		return;
    	}
	}
    
    
    
    
    /*
     * updated
     */
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
    
    /*
     * updated
     */
    private final void _ret()
	{
		byte arrdsL = ram.read(sp);
    	sp++;
    	byte arrdsH = ram.read(sp);
    	sp++;
    	
    	short address = (short)((arrdsH<<8) + arrdsL);
    	ip = address;
	}
    
    /*
     * updated
     */
    private final void _end()
	{
    	HF = true;
    	monitor.onEND(regD);
	}
    
    /*
     * updated
     */
    private final void _jmp()
	{
    	short addrs16 = _fetch16(); // label as address to instruction
    	ip = addrs16;
	}

    /*
     * updated
     */
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

    /*
     * updated
     */
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

    /*
     * updated
     */
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

    /*
     * updated
     */
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

    /*
     * updated
     */
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

    /*
     * updated
     */
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

    /*
     * updated
     */
    private final void _load()
	{
    	byte directive = _fetch();
    	byte H = (byte)((directive>>4)&0x0f);
    	byte L = (byte)(directive&0x0f);
    	
    	// register to register
    	if ( _is_register(H) && _is_register(L) )
    	{
        	short v = _register_value(L);
        	byte bv = (byte)(v&0xff);
        	switch(H)
    		{
        	case RA: regA = bv; return;
        	case RB: regB = bv; return;
        	case RC: regC = bv; return;
        	case RD: regD = bv; return;
        	case XH: regXH = bv; return;
        	case XL: regXL = bv; return;
        	case YH: regYH = bv; return;
        	case YL: regYL = bv; return;
        	
        	case IP: ip = v; return;
        	case SP: sp = v; return;
        	case RX: _set_reg16(RX, v); return;
        	case RY: _set_reg16(RY, v); return;
    		}
        	return;
    	}
    	byte operant = _fetch();
    	
    	// loading immediate 8-bit
    	if ( L == I )
    	{
    		// 8 bit immediate to register
    		switch(H)
    		{
        	case RA: regA = operant; return;
        	case RB: regB = operant; return;
        	case RC: regC = operant; return;
        	case RD: regD = operant; return;
        	case XH: regXH = operant; return;
        	case XL: regXL = operant; return;
        	case YH: regYH = operant; return;
        	case YL: regYL = operant; return;
        	
        	case IP: ip = operant; return;
        	case SP: sp = operant; return;
        	case RX: _set_reg16(RX, operant); return;
        	case RY: _set_reg16(RY, operant); return;
    		}
    		return;
    	}
    	
    	short operant16 = (short)((operant << 8) + _fetch());
    	
    	// loading immediate 16-bit
    	if ( L == IL )
    	{
    		switch(H)
    		{
        	case IP: ip = operant16; return;
        	case SP: sp = operant16; return;
        	case RX: _set_reg16(RX, operant16); return;
        	case RY: _set_reg16(RY, operant16); return;
    		}
    		return;
    	}
    	
    	// else, load from ram
    	
    	// 16 bit address from RAM to register
    	switch(directive)
		{
		case RAM: regA = ram.read(operant16); return;
		case RBM: regB = ram.read(operant16); return;
		case RCM: regC = ram.read(operant16); return;
		case RDM: regD = ram.read(operant16); return;
		case XHM: regXH = ram.read(operant16); return;
		case XLM: regXL = ram.read(operant16); return;
		case YHM: regYH = ram.read(operant16); return;
		case YLM: regYL = ram.read(operant16); return;
		default: break;
		}
	}
    
    /*
     * updated
     */
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
		case XH: output = regXH&0xff; break;
		case XL: output = regXL&0xff; break;
		case YH: output = regYH&0xff; break;
		case YL: output = regYL&0xff; break;
		case IP: output = ip&0xffff; break;
		case SP: output = sp&0xffff; break;
		case RX: output = _to16(regXH, regXL); break;
		case RY: output = _to16(regYH, regYL); break;
		case I:
			{
				byte operant = _fetch();
				output = operant&0xff;
				break;
			}
		case IL:
			{
				short operant = _fetch16();
				output = operant&0xffff;
				break;
			}
		default: return;
		}
		
		monitor.onOUT( output );
	}
 	
    // ====================================================================
    // utility
    // ====================================================================

 	private final boolean _is_register(byte reg)
    {
 		if (reg != I)
 		if (reg != IL)
 		if (reg != M) return true;
		return false;
    }
 	
 	private final boolean _is_register8(byte reg)
    {
 		if (reg != RX)
 		if (reg != RY)
 		if (reg != IP)
 		if (reg != SP)
 		if (reg != I)
 		if (reg != IL)
 		if (reg != M) return true;
 		return false;
    }
 	
 	private final boolean _is_register16(byte reg)
    {
 		if (reg == IP) return true;
 		if (reg == SP) return true;
 		if (reg == RX) return true;
 		if (reg == RY) return true;
 		return false;
    }
 	
 	private final short _register_value(byte reg)
    {
    	switch(reg)
		{
    	case RA: return regA;
    	case RB: return regB;
    	case RC: return regC;
    	case RD: return regD;
    	case XH: return regXH;
    	case XL: return regXL;
    	case YH: return regYH;
    	case YL: return regYL;
    	case IP: return ip;
    	case SP: return sp;
    	case RX: return _to16(regXH, regXL);
    	case RY: return _to16(regYH, regYL);
		}
		return -1;
    }
 	
 	private final byte _register_value8(byte reg)
    {
    	switch(reg)
		{
    	case RA: return regA;
    	case RB: return regB;
    	case RC: return regC;
    	case RD: return regD;
    	case XH: return regXH;
    	case XL: return regXL;
    	case YH: return regYH;
    	case YL: return regYL;
		}
		return -1;
    }
 	
 	private final short _register_value16(byte reg)
    {
    	switch(reg)
		{
    	case IP: return ip;
    	case SP: return sp;
    	case RX: return _to16(regXH, regXL);
    	case RY: return _to16(regYH, regYL);
		}
		return -1;
    }
 	
 	private final void _set_reg8(byte reg, byte v)
 	{
 		switch(reg)
		{
    	case RA: regA = v; return;
    	case RB: regB = v; return;
    	case RC: regC = v; return;
    	case RD: regD = v; return;
    	case XH: regXH = v; return;
    	case XL: regXL = v; return;
    	case YH: regYH = v; return;
    	case YL: regYL = v; return;
		}
 	}
 	
 	private final void _set_reg16(byte reg, short v)
 	{
 		if (reg == RX)
 		{
 			regXH = (byte)(v>>8);
 	 		regXL = (byte)(v);
 		}
 		if (reg == RY)
 		{
 			regYH = (byte)(v>>8);
 	 		regYL = (byte)(v);
 		}
 	}
 	
 	private final byte _alu(byte a, byte b, boolean sub)
	{
        b = sub ? _neg(b) : b;
        short s = (short)(a + b);
        CF = s >> 8 != 0;
        byte sum = (byte)s;
        ZF = sum == 0;
        SF = (sum >> 7) != 0;
        OF = CF ^ SF;
        return sum;
    }
 	
 	private final short _alu16(short a, short b, boolean sub)
	{
        b = sub ? _neg16(b) : b;
        int s = (int)(a + b);
        CF = s >> 16 != 0;
        short sum = (short)s;
        ZF = sum == 0;
        SF = (sum >> 15) != 0;
        OF = CF ^ SF;
        return sum;
    }
	
    private final byte _neg(byte num)
    {
        return (byte)(~num+1);
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
    
    private final short _to16(byte h, byte l)
    {
    	return (short)( (h<<8)+l );
    }
}
