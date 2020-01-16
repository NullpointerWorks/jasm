package com.nullpointerworks.jasm8.parts;

public interface InstructionsJASM8
{
	/*
	 * operation codes
	 */
	public static final byte NOP		= (byte)0b0000_0000;
	public static final byte END		= (byte)0b1111_1111; // calls onEND(int)
	public static final byte OUT		= (byte)0b0000_0001; // calls onOUT(int)
	public static final byte LOAD		= (byte)0b0000_0011;
	public static final byte ADD		= (byte)0b0000_0100;
	public static final byte SUB		= (byte)0b0000_0101;
	public static final byte CMP		= (byte)0b0000_0110;
	public static final byte JMP		= (byte)0b0000_0111;
	public static final byte NEG		= (byte)0b0000_1000;
	public static final byte STO		= (byte)0b0000_1001;
	public static final byte PUSH		= (byte)0b0000_1010;
	public static final byte POP		= (byte)0b0000_1011;
	public static final byte CALL		= (byte)0b0000_1100;
	public static final byte RET		= (byte)0b0000_1101;
	public static final byte JE			= (byte)0b0000_1110;
	public static final byte JNE		= (byte)0b0000_1111;
	public static final byte JL			= (byte)0b0001_0000;
	public static final byte JLE		= (byte)0b0001_0001;
	public static final byte JG			= (byte)0b0001_0010;
	public static final byte JGE		= (byte)0b0001_0011;
	public static final byte INC		= (byte)0b0001_0100;
	public static final byte DEC		= (byte)0b0001_0101;
	
	
	/*
	 * directives
	 */
	public static final byte I 		= (byte)0b0000_0000; // select immediate
	public static final byte RA 	= (byte)0b0000_0001; // select register A
	public static final byte RB 	= (byte)0b0000_0010; // select register B
	public static final byte RC 	= (byte)0b0000_0011; // select register C
	public static final byte RD 	= (byte)0b0000_0100; // select register D
	public static final byte R0 	= (byte)0b0000_0101; // select register 0
	public static final byte R1 	= (byte)0b0000_0110; // select register 1
	public static final byte R2 	= (byte)0b0000_0111; // select register 2
	public static final byte R3 	= (byte)0b0000_1000; // select register 3
	public static final byte IP 	= (byte)0b0000_1001; // select instruction pointer 16-bit
	public static final byte SP 	= (byte)0b0000_1010; // select stack pointer 16-bit
	public static final byte M 		= (byte)0b0000_1011; // select RAM address 16-bit
	public static final byte RX 	= (byte)0b0000_1100; // select r0-r1 address 16-bit
	public static final byte RY 	= (byte)0b0000_1101; // select r2-r3 address 16-bit
	
	/*
	 * compile directives
	 */
	public static byte _cd(byte a, byte b)
	{
		a = (byte)(a<<4);
		a = (byte)(a&0b1111_0000);
		return (byte)(a|b);
	}
	
	public static final byte RAI	= (byte)0b0001_0000; // immediate -> register A
	public static final byte RBI 	= (byte)0b0010_0000; // immediate -> register B
	public static final byte RCI 	= (byte)0b0011_0000; // immediate -> register C
	public static final byte RDI 	= (byte)0b0100_0000; // immediate -> register D
	public static final byte R0I 	= (byte)0b0101_0000; // immediate -> register 0
	public static final byte R1I 	= (byte)0b0110_0000; // immediate -> register 1
	public static final byte R2I 	= (byte)0b0111_0000; // immediate -> register 2
	public static final byte R3I 	= (byte)0b1000_0000; // immediate -> register 3
	
	public static final byte RBA 	= (byte)0b0010_0001; // register A -> register B
	public static final byte RCA 	= (byte)0b0011_0001; // register A -> register C
	public static final byte RDA 	= (byte)0b0100_0001; // register A -> register D
	public static final byte R0A 	= (byte)0b0101_0001; // register A -> register 0
	public static final byte R1A 	= (byte)0b0110_0001; // register A -> register 1
	public static final byte R2A 	= (byte)0b0111_0001; // register A -> register 2
	public static final byte R3A 	= (byte)0b1000_0001; // register A -> register 3
	
	public static final byte RAB 	= (byte)0b0001_0010; // register B -> register A
	public static final byte RCB 	= (byte)0b0011_0010; // register B -> register C
	public static final byte RDB 	= (byte)0b0100_0010; // register B -> register D
	public static final byte R0B 	= (byte)0b0101_0010; // register B -> register 0
	public static final byte R1B 	= (byte)0b0110_0010; // register B -> register 1
	public static final byte R2B 	= (byte)0b0111_0010; // register B -> register 2
	public static final byte R3B 	= (byte)0b1000_0010; // register B -> register 3
	
	public static final byte RAC 	= (byte)0b0001_0011; // register C -> register A
	public static final byte RBC 	= (byte)0b0010_0011; // register C -> register B
	public static final byte RDC 	= (byte)0b0100_0011; // register C -> register D
	public static final byte R0C 	= (byte)0b0101_0011; // register C -> register 0
	public static final byte R1C 	= (byte)0b0110_0011; // register C -> register 1
	public static final byte R2C 	= (byte)0b0111_0011; // register C -> register 2
	public static final byte R3C 	= (byte)0b1000_0011; // register C -> register 3
	
	public static final byte RAD 	= (byte)0b0001_0100; // register D -> register A
	public static final byte RBD 	= (byte)0b0010_0100; // register D -> register B
	public static final byte RCD 	= (byte)0b0011_0100; // register D -> register C
	public static final byte R0D 	= (byte)0b0101_0100; // register D -> register 0
	public static final byte R1D 	= (byte)0b0110_0100; // register D -> register 1
	public static final byte R2D 	= (byte)0b0111_0100; // register D -> register 2
	public static final byte R3D 	= (byte)0b1000_0100; // register D -> register 3

	public static final byte RA0 	= (byte)0b0001_0101; // register 0 -> register A
	public static final byte RB0 	= (byte)0b0010_0101; // register 0 -> register B
	public static final byte RC0 	= (byte)0b0011_0101; // register 0 -> register C
	public static final byte RD0 	= (byte)0b0100_0101; // register 0 -> register D
	public static final byte R10 	= (byte)0b0110_0101; // register 0 -> register 1
	public static final byte R20 	= (byte)0b0111_0101; // register 0 -> register 2
	public static final byte R30 	= (byte)0b1000_0101; // register 0 -> register 3
	
	public static final byte RA1 	= (byte)0b0001_0110; // register 1 -> register A
	public static final byte RB1 	= (byte)0b0010_0110; // register 1 -> register B
	public static final byte RC1 	= (byte)0b0011_0110; // register 1 -> register C
	public static final byte RD1 	= (byte)0b0100_0110; // register 1 -> register D
	public static final byte R01 	= (byte)0b0101_0110; // register 1 -> register 0
	public static final byte R21 	= (byte)0b0111_0110; // register 1 -> register 2
	public static final byte R31 	= (byte)0b1000_0110; // register 1 -> register 3
	
	public static final byte RA2 	= (byte)0b0001_0111; // register 2 -> register A
	public static final byte RB2 	= (byte)0b0010_0111; // register 2 -> register B
	public static final byte RC2 	= (byte)0b0011_0111; // register 2 -> register C
	public static final byte RD2 	= (byte)0b0100_0111; // register 2 -> register D
	public static final byte R02 	= (byte)0b0101_0111; // register 2 -> register 0
	public static final byte R12 	= (byte)0b0110_0111; // register 2 -> register 1
	public static final byte R32 	= (byte)0b1000_0111; // register 2 -> register 3
	
	public static final byte RA3 	= (byte)0b0001_1000; // register 3 -> register A
	public static final byte RB3 	= (byte)0b0010_1000; // register 3 -> register B
	public static final byte RC3 	= (byte)0b0011_1000; // register 3 -> register C
	public static final byte RD3 	= (byte)0b0100_1000; // register 3 -> register D
	public static final byte R03 	= (byte)0b0101_1000; // register 3 -> register 0
	public static final byte R13 	= (byte)0b0110_1000; // register 3 -> register 1
	public static final byte R23 	= (byte)0b0111_1000; // register 3 -> register 2
	
	public static final byte RAM	= (byte)0b0001_1011; // memory -> register A
	public static final byte RBM 	= (byte)0b0010_1011; // memory -> register B
	public static final byte RCM 	= (byte)0b0011_1011; // memory -> register C
	public static final byte RDM 	= (byte)0b0100_1011; // memory -> register D
	public static final byte R0M 	= (byte)0b0101_1011; // memory -> register 0
	public static final byte R1M 	= (byte)0b0110_1011; // memory -> register 1
	public static final byte R2M 	= (byte)0b0111_1011; // memory -> register 2
	public static final byte R3M 	= (byte)0b1000_1011; // memory -> register 3
	
}
