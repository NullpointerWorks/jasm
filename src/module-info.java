module libnpw.jasm
{
	requires transitive libnpw.game;
	requires libnpw.util;
	
	exports com.nullpointerworks.jasm;
	exports com.nullpointerworks.jasm.compiler;
	exports com.nullpointerworks.jasm.virtualmachine;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.system;
}