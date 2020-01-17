package com.nullpointerworks.jasm.jasm8.processor;

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
	//public static final byte ADC		= (byte)0b0001_0110;
	
	/*
	 * directives
	 */
	public static final byte I 		= (byte)0b0000_0000; // select immediate
	public static final byte RA 	= (byte)0b0000_0001; // select register A
	public static final byte RB 	= (byte)0b0000_0010; // select register B
	public static final byte RC 	= (byte)0b0000_0011; // select register C
	public static final byte RD 	= (byte)0b0000_0100; // select register D
	public static final byte XH 	= (byte)0b0000_0101; // select register x high
	public static final byte XL 	= (byte)0b0000_0110; // select register x low
	public static final byte YH 	= (byte)0b0000_0111; // select register y high
	public static final byte YL 	= (byte)0b0000_1000; // select register y low
	public static final byte IP 	= (byte)0b0000_1001; // select instruction pointer 16-bit
	public static final byte SP 	= (byte)0b0000_1010; // select stack pointer 16-bit
	public static final byte M 		= (byte)0b0000_1011; // select RAM address 16-bit
	public static final byte RX 	= (byte)0b0000_1100; // select XH-r1 address 16-bit
	public static final byte RY 	= (byte)0b0000_1101; // select YH-r3 address 16-bit
	public static final byte IL		= (byte)0b0000_1110; // select long immediate
	
	/*
	 * pre-compiled
	 */
	
	public static final byte RAI	= (byte)0b0001_0000; // immediate -> register A
	public static final byte RBI 	= (byte)0b0010_0000; // immediate -> register B
	public static final byte RCI 	= (byte)0b0011_0000; // immediate -> register C
	public static final byte RDI 	= (byte)0b0100_0000; // immediate -> register D
	public static final byte XHI 	= (byte)0b0101_0000; // immediate -> register x high
	public static final byte XLI 	= (byte)0b0110_0000; // immediate -> register x low
	public static final byte YHI 	= (byte)0b0111_0000; // immediate -> register y high
	public static final byte YLI 	= (byte)0b1000_0000; // immediate -> register y low
	
	public static final byte RBA 	= (byte)0b0010_0001; // register A -> register B
	public static final byte RCA 	= (byte)0b0011_0001; // register A -> register C
	public static final byte RDA 	= (byte)0b0100_0001; // register A -> register D
	public static final byte XHA 	= (byte)0b0101_0001; // register A -> register 0
	public static final byte XLA 	= (byte)0b0110_0001; // register A -> register 1
	public static final byte YHA 	= (byte)0b0111_0001; // register A -> register 2
	public static final byte YLA 	= (byte)0b1000_0001; // register A -> register 3
	
	public static final byte RAB 	= (byte)0b0001_0010; // register B -> register A
	public static final byte RCB 	= (byte)0b0011_0010; // register B -> register C
	public static final byte RDB 	= (byte)0b0100_0010; // register B -> register D
	public static final byte XHB 	= (byte)0b0101_0010; // register B -> register 0
	public static final byte XLB 	= (byte)0b0110_0010; // register B -> register 1
	public static final byte YHB 	= (byte)0b0111_0010; // register B -> register 2
	public static final byte YLB 	= (byte)0b1000_0010; // register B -> register 3
	
	public static final byte RAC 	= (byte)0b0001_0011; // register C -> register A
	public static final byte RBC 	= (byte)0b0010_0011; // register C -> register B
	public static final byte RDC 	= (byte)0b0100_0011; // register C -> register D
	public static final byte XHC 	= (byte)0b0101_0011; // register C -> register 0
	public static final byte XLC 	= (byte)0b0110_0011; // register C -> register 1
	public static final byte YHC 	= (byte)0b0111_0011; // register C -> register 2
	public static final byte YLC 	= (byte)0b1000_0011; // register C -> register 3
	
	public static final byte RAD 	= (byte)0b0001_0100; // register D -> register A
	public static final byte RBD 	= (byte)0b0010_0100; // register D -> register B
	public static final byte RCD 	= (byte)0b0011_0100; // register D -> register C
	public static final byte XHD 	= (byte)0b0101_0100; // register D -> register 0
	public static final byte XLD 	= (byte)0b0110_0100; // register D -> register 1
	public static final byte YHD 	= (byte)0b0111_0100; // register D -> register 2
	public static final byte YLD 	= (byte)0b1000_0100; // register D -> register 3

	public static final byte RA0 	= (byte)0b0001_0101; // register 0 -> register A
	public static final byte RB0 	= (byte)0b0010_0101; // register 0 -> register B
	public static final byte RC0 	= (byte)0b0011_0101; // register 0 -> register C
	public static final byte RD0 	= (byte)0b0100_0101; // register 0 -> register D
	public static final byte XL0 	= (byte)0b0110_0101; // register 0 -> register 1
	public static final byte YH0 	= (byte)0b0111_0101; // register 0 -> register 2
	public static final byte YL0 	= (byte)0b1000_0101; // register 0 -> register 3
	
	public static final byte RA1 	= (byte)0b0001_0110; // register 1 -> register A
	public static final byte RB1 	= (byte)0b0010_0110; // register 1 -> register B
	public static final byte RC1 	= (byte)0b0011_0110; // register 1 -> register C
	public static final byte RD1 	= (byte)0b0100_0110; // register 1 -> register D
	public static final byte XH1 	= (byte)0b0101_0110; // register 1 -> register 0
	public static final byte YH1 	= (byte)0b0111_0110; // register 1 -> register 2
	public static final byte YL1 	= (byte)0b1000_0110; // register 1 -> register 3
	
	public static final byte RA2 	= (byte)0b0001_0111; // register 2 -> register A
	public static final byte RB2 	= (byte)0b0010_0111; // register 2 -> register B
	public static final byte RC2 	= (byte)0b0011_0111; // register 2 -> register C
	public static final byte RD2 	= (byte)0b0100_0111; // register 2 -> register D
	public static final byte XH2 	= (byte)0b0101_0111; // register 2 -> register 0
	public static final byte XL2 	= (byte)0b0110_0111; // register 2 -> register 1
	public static final byte YL2 	= (byte)0b1000_0111; // register 2 -> register 3
	
	public static final byte RA3 	= (byte)0b0001_1000; // register 3 -> register A
	public static final byte RB3 	= (byte)0b0010_1000; // register 3 -> register B
	public static final byte RC3 	= (byte)0b0011_1000; // register 3 -> register C
	public static final byte RD3 	= (byte)0b0100_1000; // register 3 -> register D
	public static final byte XH3 	= (byte)0b0101_1000; // register 3 -> register 0
	public static final byte XL3 	= (byte)0b0110_1000; // register 3 -> register 1
	public static final byte YH3 	= (byte)0b0111_1000; // register 3 -> register 2
	
	public static final byte RAM	= (byte)0b0001_1011; // memory -> register A
	public static final byte RBM 	= (byte)0b0010_1011; // memory -> register B
	public static final byte RCM 	= (byte)0b0011_1011; // memory -> register C
	public static final byte RDM 	= (byte)0b0100_1011; // memory -> register D
	public static final byte XHM 	= (byte)0b0101_1011; // memory -> register 0
	public static final byte XLM 	= (byte)0b0110_1011; // memory -> register 1
	public static final byte YHM 	= (byte)0b0111_1011; // memory -> register 2
	public static final byte YLM 	= (byte)0b1000_1011; // memory -> register 3
	
}
