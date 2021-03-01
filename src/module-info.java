/**
 * Creative Commons - Attribution, Share Alike 4.0<br>
 * Nullpointer Works (2021)<br>
 * Use of this library is subject to license terms.<br>
 * @version 6.1.3 beta
 * @author Michiel Drost - Nullpointer Works
 */
module libnpw.jasm
{
	requires libnpw.util; // file reading dependency
	
	/*
	 * compiler and builders
	 */
	exports com.nullpointerworks.jasm.assembler;
	exports com.nullpointerworks.jasm.assembler.errors;
	
	/*
	 * virtual machine
	 */
	exports com.nullpointerworks.jasm.virtualmachine;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.logic;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.system;
}
