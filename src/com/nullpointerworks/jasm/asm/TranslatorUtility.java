package com.nullpointerworks.jasm.asm;

import java.util.List;

import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Label;

public class TranslatorUtility 
{
	
	
	public static Definition findDefinition(List<Definition> definitions, String name)
	{
		for (Definition d : definitions)
		{
			if (d.getName().equalsIgnoreCase(name))
			{
				return d;
			}
		}
		return null;
	}
	
	public static Allocation findAllocation(List<Allocation> allocations, String name)
	{
		for (Allocation d : allocations)
		{
			if (d.getName().equalsIgnoreCase(name))
			{
				return d;
			}
		}
		return null;
	}
	
	public static Label findLabel(List<Label> labels, String name)
	{
		for (Label d : labels)
		{
			if (d.getName().equalsIgnoreCase(name))
			{
				return d;
			}
		}
		return null;
	}
}
