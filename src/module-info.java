/**
 * Creative Commons - Attribution, Share Alike 4.0<br>
 * Nullpointer Works (2021)<br>
 * Use of this library is subject to license terms.<br>
 * @version 3.0.0 beta
 * @author Michiel Drost - Nullpointer Works
 */
module libnpw.jasm
{
	requires libnpw.util; // file reading dependency
	
	/*
	 * abstract compiler code and implementations
	 */
	exports com.nullpointerworks.jasm.compiler;
	exports com.nullpointerworks.jasm.compiler.errors;
	
	/*
	 * virtual machine code
	 */
	exports com.nullpointerworks.jasm.virtualmachine;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.system;
}