package com.nullpointerworks.jasm.parser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.nullpointerworks.util.StringUtil;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class ParserJASM implements Parser
{
	private List<String> includes = null; // keeps a list of all included files. this list is leading
	private List<String> includesAux = null; // contains file yet to be included. this list gets modified
	private List<String> includesPath = null; // all traceable paths to look for jasm source code 

	private List<EquRecord> equals = null; // contains all equ code
	private List<EquRecord> equDups = null;
	
	private List<SourceCode> code = null; // contains parsed code
	private List<ParseError> errors; // contains errors
	
	private boolean verbose_parser = false;
	private int strLeng = 2;
	
	/*
	 * lexicographic sort
	 */
	private Comparator<EquRecord> comp = new Comparator<EquRecord>()
	{
		@Override
		public int compare(EquRecord p1, EquRecord p2) 
		{
			return p1.first.compareTo(p2.first);
		}
	};
	public ParserJASM() {reset();}
	
	@Override
	public Parser reset()
	{
		strLeng = 2;
		
		if (code!=null) code.clear();
		code = new ArrayList<SourceCode>();
		
		if (includes!=null) includes.clear();
		includes = new ArrayList<String>();
		
		if (includesAux!=null) includesAux.clear();
		includesAux = new ArrayList<String>();
		
		if (errors!=null) errors.clear();
		errors = new ArrayList<ParseError>();
		
		if (equals!=null) equals.clear();
		equals = new ArrayList<EquRecord>();
		
		if (equDups!=null) equDups.clear();
		equDups = new ArrayList<EquRecord>();
		
		return this;
	}
	
	@Override
	public Parser setVerbose(boolean verbose)
	{
		verbose_parser = verbose;
		return this;
	}
	
	@Override
	public Parser setIncludesPath(List<String> paths)
	{
		includesPath = new ArrayList<String>(paths);
		return this;
	}
	
	@Override
	public Parser parse(String filename)
	{
		String[] text = loadCode(filename);
		
		/*
		 * if verbose, notify about parsing and what includes are used
		 */
		if (verbose_parser)
		{
			out("-------------------------------");
			
			if (includesPath.size() > 0)
			{
				out("\n linker\n");
				for (String inc : includesPath)
					out("   "+inc);
			}
			
			out("\n parsing\n");
		}
		
		/*
		 * parse the given text as primary source code
		 */
		parseCode(filename, text);
		
		/*
		 * scan include paths.
		 * each parse cycle adds new non-included source files. 
		 * then they get parsed in the next iteration
		 */
		do 
		{
			int l = includesAux.size()-1;
			for (;l>=0;l--)
			{
				String inc = includesAux.get(l);
				
				/*
				 * if the file is a jasm source file
				 */
				if (inc.endsWith(".jasm"))
				{
					out("\n include: "+inc+"\n");
					
					/*
					 * check each include path if the file can be found
					 */
					boolean found = false;
					for (String path : includesPath)
					{
						/*
						 * try to load a file
						 */
						String[] lines = loadCode(path+inc);
						
						/*
						 * if text was returned, file loading was successful
						 */
						if (lines!=null) 
						{
							found = true;
							parseCode(inc, lines);
							if (hasErrors()) return this; // if errors occurred, return
						}
					}
					
					/*
					 * if the included file was not found in any linker path, throw an error
					 */
					if (!found)
					{
						out(inc+" (The system cannot find the file specified)");
						return this;
					}
				}
				
				includesAux.remove(l);
			}
		}
		while(includesAux.size() > 0);
		
		/*
		 * if verbose, show some warnings and other info
		 */
		if (verbose_parser)
		{
			// notify about duplicate ".equ" declarations
			equDups.sort(comp); 
			if (equDups.size() > 0)
			{
				out("\n Duplicate \"equ\" declared\n");
				for (EquRecord entry : equDups)
				{
					out(" .equ "+entry.first + " "+entry.third.getFilename());
				}
			}
			out("\n parsing done\n");
			out("-------------------------------");
		}
		
		return this;
	}

	@Override
	public boolean hasErrors()
	{
		return errors.size() > 0;
	}
	
	@Override
	public List<ParseError> getErrors()
	{
		return errors;
	}
	
	@Override
	public List<SourceCode> getSourceCode()
	{
		return code;
	}
	
	@Override
	public List<EquRecord> getDefinitions()
	{
		return equals;
	}
	
	/* ==================================================================
	 * 
	 * text file parser
	 * 
	 * responsibility:
	 * format text so it can be easily processed into machine code
	 * 
	 * ==================================================================
	 */
	private void parseCode(String filename, String[] text)
	{
		int linenumber = 0;
		int totalLines = text.length;
		strLeng = (""+totalLines).length() + 1;
		
		for (String line : text)
		{
			line = line.replace("\t", " ");
			line = line.replaceAll(" +", " ");
			line = line.replace(", ", ",");
			line = line.replace(" ,", ",");
			line = line.trim();
			linenumber++;
			
			/*
			 * if the line starts with a comment mark, skip the line
			 */
			if (line.startsWith(";"))
			{
				continue;
			}
			
			/*
			 * if the line has a comment mark after the code, only parse 
			 * the text before the comment mark. 
			 */
			if (line.contains(";"))
			{
				line = line.split(";")[0];
				line = line.trim();
			}

			SourceCode sc = new SourceCode(filename, linenumber, line);
			
			/*
			 * if the line of code contains a label mark, extract the label
			 */
			if (line.contains(":"))
			{
				String t[] = line.split(":");
				
				/*
				 * test for allowed label characters
				 */
				if (!isValidLabel(t[0]))
				{
					addError(sc, "Invalid label name");
				}
				
				if (t.length > 2)
				{
					if (t[1].contains(" "))
					{
						addError(sc, "Invalid label location");
					}
				}
				
				/*
				 * insert the marked label into the code list
				 */
				processLine( new SourceCode(filename, linenumber, t[0]+":") );
				
				/*
				 * parse possibly code on the same line
				 */
				if (t.length == 2)
				{
					String i = t[1].trim();
					processLine( new SourceCode(filename, linenumber, i) );
				}
			}
			else
			{
				/*
				 * if there's no label marker, parse the line of code normally
				 */
				processLine( sc );
			}
			
			/*
			 * if there are errors, return the method
			 */
			if (hasErrors()) return;
		}
	}
	
	/* ==================================================================
	 * 
	 * source code parser
	 * 
	 * responsibility:
	 * 
	 * 
	 * ==================================================================
	 */
	private void processLine(SourceCode sc)
	{
		int linenumber = sc.getLinenumber();
		String line = sc.getLine();
		
		if (line.equalsIgnoreCase("")) return;
		if (hasErrors()) return;
		
		/*
		 * include external code
		 */
		if (line.startsWith(".inc "))
		{
			String include = line.substring(5);
			
			/*
			 * the include name must be between quotation marks
			 */
			if (include.startsWith("\"") && include.endsWith("\"")) 
			{
				include = include.replace("\"", "");
				if (!includes.contains(include)) 
				{
					includes.add(include);
					includesAux.add(include);
				}
			}
			else
			{
				addError(sc, "Bad include syntax");
			}
			return;
		}
		
		/*
		 * store EQU definitions
		 */
		if (line.startsWith(".equ "))
		{
			String equates = line.substring(5);
			String[] tokens = equates.split(" ");
			/*
			 * if there are more than 2 tokens, invalid equ definition
			 */
			if (tokens.length != 2)
			{
				addError(sc, "Invalid equate syntax");
				return;
			}
			
			/*
			 * if the first token is a bad name, error
			 */
			String name = tokens[0];
			if (!isValidLabel(name))
			{
				addError(sc, "Invalid label characters used");
				return;
			}
			
			/*
			 * if the second name is a bad number, error
			 */
			String value = tokens[1];
			if (!isValidNumber(value))
			{
				addError(sc, "Invalid number syntax");
				return;
			}
			
			/*
			 * find equ duplicates
			 */
			var pack3 = findEqu(name,equals);
			if (pack3 == null) 
			{
				putEqu(name, value, sc, equals);
			}
			else
			{
				/*
				 * make sure its only added once in the duplicate list when found
				 */
				if (findEqu(name,equDups) == null) 
					equDups.add(pack3);
				
				/*
				 * add name and source to duplicate list
				 */
				equDups.add( new EquRecord(name, "", sc));
			}
		}
		
		/*
		 * if verbose, print parsed line
		 */
		if (verbose_parser) 
		{
			String linemarker = fillFromBack(""+linenumber," ",strLeng)+"| "+line;
			out(linemarker);
		}
		
		code.add(sc);
	}
	
	// ==================================================================
	
	private String[] loadCode(String inc)
	{
		TextFile tf = null;
		try
		{
			tf = TextFileParser.file(inc);
		} 
		catch (FileNotFoundException e)
		{
			err(inc+" (The system cannot find the file specified)");
			return null;
		}
		if (tf == null) return null;
		
		return tf.getLines();
	}
	
	// =================================================================
	
	private void addError(SourceCode sc, String message)
	{
		errors.add( new ParseError(sc.getFilename(),sc.getLinenumber(),sc.getLine(),message) );
	}
	
	private boolean isValidLabel(String label)
	{
		if ( label.matches("\\D[a-zA-Z0-9\\_]+") ) return true;
		return false;
	}
	
	private boolean isValidNumber(String number)
	{
		if (number.startsWith("&")) number = number.substring(1);
		if (StringUtil.isInteger(number)) return true;
		if (StringUtil.isHexadec(number)) return true;
		return false;
	}
	
	private String fillFromBack(String msg, String chr, int leng) 
	{
		String filler = "";
		for (int s=0; s<leng; s++) filler += chr;
		String concat = filler+msg;
		int strLeng = concat.length();
		return concat.substring(strLeng-leng, strLeng);
	}
	
	private void putEqu(String n, String v, SourceCode sc, List<EquRecord> equs) 
	{
		equs.add( new EquRecord(n, v, sc) );
	}
	
	private EquRecord findEqu(String name, List<EquRecord> equs) 
	{
		for (EquRecord t : equs)
			if (t.first.equals(name)) return t;
		return null;
	}
	
	private void out(String string)
	{
		if (verbose_parser) System.out.println(string);
	}
	
	private void err(String string)
	{
		System.err.println(string);
	}
}
