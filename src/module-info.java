module libnpw.jasm
{
	requires transitive libnpw.game;
	requires libnpw.util;
	
	exports com.nullpointerworks.jasm;
	exports com.nullpointerworks.jasm.parser;
	exports com.nullpointerworks.jasm.preprocessor;
	exports com.nullpointerworks.jasm.compiler;
	exports com.nullpointerworks.jasm.virtualmachine.instruction;
	exports com.nullpointerworks.jasm.virtualmachine;
}