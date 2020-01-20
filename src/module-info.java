module libnpw.jasm
{
	requires transitive libnpw.game;
	requires libnpw.util;
	requires libnpw.core;
	requires libnpw.color;

	exports com.nullpointerworks.jasm;
	exports com.nullpointerworks.jasm.jasm8;
	exports com.nullpointerworks.jasm.jasm8.compiler;
	exports com.nullpointerworks.jasm.jasm8.processor;
}