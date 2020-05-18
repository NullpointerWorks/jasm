module libnpw.jasm
{
	requires libnpw.util; // file reading dependency
	exports com.nullpointerworks.jasm;
	exports com.nullpointerworks.jasm.compiler;
	exports com.nullpointerworks.jasm.virtualmachine;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.controlflow;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.dataflow;
	exports com.nullpointerworks.jasm.virtualmachine.instruction.system;
}