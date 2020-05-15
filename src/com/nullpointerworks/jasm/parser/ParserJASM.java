package com.nullpointerworks.jasm.parser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.util.StringUtil;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class ParserJASM implements Parser
{
	private List<String> includes = null; // keeps a list of all included files. this list is leading
	private List<String> includesAux = null; // contains file yet to be included. this list gets modified
	private List<String> includesPath = null; // all traceable paths to look for jasm source code 

	private List<DefineRecord> defs = null; // contains all definition code
	private List<DefineRecord> defDups = null;
	
	private List<SourceCode> code = null; // contains parsed code
	private List<BuildError> errors; // contains errors
	
	private boolean verbose_parser = false;
	private int strLeng = 2;
	
	/*
	 * lexicographic sort
	 */
	private Comparator<DefineRecord> comp = new Comparator<DefineRecord>()
	{
		@Override
		public int compare(DefineRecord p1, DefineRecord p2) 
		{
			return p1.NAME.compareTo(p2.NAME);
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
		errors = new ArrayList<BuildError>();
		
		if (defs!=null) defs.clear();
		defs = new ArrayList<DefineRecord>();
		
		if (defDups!=null) defDups.clear();
		defDups = new ArrayList<DefineRecord>();
		
		return this;
	}

	@Override
	public boolean hasErrors()
	{
		return errors.size() > 0;
	}
	
	@Override
	public List<BuildError> getErrors()
	{
		return errors;
	}
	
	@Override
	public List<SourceCode> getSourceCode()
	{
		return code;
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
		if (!isValidFile(filename))
		{
			return null; // fatal error
		}
		
		String[] text = loadCode(filename);
		
		/*
		 * if verbose, notify about parsing and what includes are used
		 */
		if (verbose_parser)
		{
			out("-------------------------------");
			
			if (includesPath.size() > 0)
			{
				out("\nLinker\n");
				for (String inc : includesPath)
					out(" "+inc);
			}
			
			out("\nParsing\n");
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
				includesAux.remove(l);
				
				/*
				 * if the file is not a recognized source file, skip
				 */
				if (!isValidFile(inc)) continue;
				
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
						out("\nInclude: "+inc+"\n");
						found = true;
						parseCode(inc, lines);
					}
				}
				
				/*
				 * if the included file was not found in any linker path, throw an error
				 */
				if (!found)
				{
					addError( getIncludeErrorMessage(inc) );
				}
			}
		}
		while(includesAux.size() > 0);
		
		/*
		 * check for duplicate definitions
		 */
		defDups.sort(comp); 
		if (defDups.size() > 0)
		{
			String msg = "Duplicate definition declaration\n";
			for (DefineRecord entry : defDups)
			{
				msg += " "+entry.NAME + " "+entry.SOURCE.getFilename()+" on line "+entry.SOURCE.getLinenumber() +"\n";
			}
			addError(msg);
		}
		
		/*
		 * insert definitions if there were no errors
		 */
		if (!hasErrors())
		{
			/*
			 * print definitions
			 */
			if (verbose_parser)
			{
				System.out.println("\nDefinitions\n");
				for (DefineRecord d : defs)
				{
					System.out.println( "  "+d.NAME +" = "+d.VALUE );
				}
			}
			
			for (int i=0,l=code.size(); i<l; i++)
			{
				SourceCode loc = code.get(i);
				String line = loc.getLine();
				
				for (DefineRecord d : defs)
				{
					String name = d.NAME;
					if (line.contains(name))
					{
						line = line.replace(name, d.VALUE);
						loc.setLine(line);
						break;
					}
				}
			}
		}
		
		out("\nParsing Done\n");
		out("-------------------------------");
		
		return this;
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
					addError(sc, "  Invalid label name");
				}
				
				/*
				 * insert the marked label into the code list
				 */
				processLine( new SourceCode(filename, linenumber, t[0]+":") );
				if (t.length == 1) continue;
				
				/*
				 * parse possibly code on the same line
				 */
				String i = t[1].trim();
				processLine( new SourceCode(filename, linenumber, i) );
			}
			else
			{
				/*
				 * if there's no label marker, parse the line of code normally
				 */
				processLine( sc );
			}
		}
	}
	
	/* ==================================================================
	 * 
	 * source code parser
	 * 
	 * responsibility:
	 * check to see if the line of code can be added 
	 * 
	 * ==================================================================
	 */
	private void processLine(SourceCode sc)
	{
		int linenumber = sc.getLinenumber();
		String line = sc.getLine();
		
		if (line.equalsIgnoreCase("")) return;
		
		/*
		 * include external code
		 */
		if (line.startsWith(".inc "))
		{
			parseInclude(sc);
			return;
		}
		
		/*
		 * store EQU definitions
		 */
		if (line.startsWith(".def "))
		{
			parseDefinition(sc);
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
	
	private void parseInclude(SourceCode sc) 
	{
		String line = sc.getLine();
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
	}
	
	private void parseDefinition(SourceCode sc) 
	{
		String line = sc.getLine();
		String equates = line.substring(5);
		String[] tokens = equates.split(" ");
		
		/*
		 * if there are more than 2 tokens, invalid equ definition
		 */
		if (tokens.length != 2)
		{
			addError(sc, "Invalid definition syntax");
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
		 * find def duplicates
		 */
		var pack3 = findDefine(name,defs);
		if (pack3 == null) 
		{
			newDefine(name, value, sc, defs); // new definition
		}
		else
		{
			// also add the first instance of that definition
			if (findDefine(name,defDups) == null) defDups.add(pack3);
			defDups.add( new DefineRecord(name, "", sc));
		}
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
			return null;
		}
		if (tf == null) return null;
		
		return tf.getLines();
	}
	
	// =================================================================
	
	private String getIncludeErrorMessage(String inc) 
	{
		return "  The included source file could not be found\n  \""+inc+"\"";
	}
	
	private void addError(String message)
	{
		errors.add( new ParseError(message) );
	}
	
	private void addError(SourceCode sc, String message)
	{
		errors.add( new ParseError(sc,message) );
	}
	
	// =================================================================
	
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
	
	private void newDefine(String n, String v, SourceCode sc, List<DefineRecord> defs) 
	{
		defs.add( new DefineRecord(n, v, sc) );
	}
	
	private DefineRecord findDefine(String name, List<DefineRecord> equs) 
	{
		for (DefineRecord t : equs)
			if (t.NAME.equals(name)) return t;
		return null;
	}
	
	private boolean isValidFile(String fn) 
	{
		if (fn.endsWith(".jasm") ) return true;
		if (fn.endsWith(".jsm") ) return true;
		if (fn.endsWith(".asm") ) return true;
		return false;
	}
	
	private void out(String string)
	{
		if (verbose_parser) System.out.println(string);
	}
}
