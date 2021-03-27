package com.nullpointerworks.jasm.asm.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.nullpointerworks.jasm.asm.ParserUtility.*;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.ParseError;

/**
 * Turns source code text files into SourceCode objects which present lines of code.
 * @author Michiel Drost - Nullpointer Works
 */
public class SourceFileParser implements Parser
{
	private List<String> includes = null; // keeps a list of all uniquely included files while parsing
	private List<String> includesAux = null; // contains file yet to be included. this list gets modified
	private List<String> includesPath = null; // all traceable paths to look for jasm source code 
	
	private List<Definition> defs = null; // contains all definition code
	private List<Definition> defDups = null; // contains duplicate definitions
	
	private List<SourceCode> code = null; // contains parsed code
	private List<BuildError> errors; // contains errors
	
	private VerboseListener verbose = (s)->{};
	
	/*
	 * lexicographic sort
	 */
	private Comparator<Definition> comp = new Comparator<Definition>()
	{
		@Override
		public int compare(Definition p1, Definition p2) 
		{
			return p1.NAME.compareTo(p2.NAME);
		}
	};
	
	public SourceFileParser() 
	{
		code = new ArrayList<SourceCode>();
		errors = new ArrayList<BuildError>();
		
		includes = new ArrayList<String>();
		includesAux = new ArrayList<String>();
		includesPath = new ArrayList<String>();
		
		defs = new ArrayList<Definition>();
		defDups = new ArrayList<Definition>();
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
	public void setVerboseListener(VerboseListener verbose)
	{
		this.verbose = verbose;
	}
	
	@Override
	public void addIncludesPath(String path)
	{
		if (!includesPath.contains(path))
			includesPath.add(path);
	}
	
	@Override
	public void parse(String filename)
	{
		verbose.onPrint("-------------------------------");
		verbose.onPrint("Parsing Start\n");
		
		if (!isValidFile(filename))
		{
			addError("Primary source file \""+filename+"\" is not recognized as JASM source code.");
			return; // fatal error
		}
		
		/*
		 * parse the given text as primary source code
		 */
		verbose.onPrint("Source Files");
		verbose.onPrint("  Main:    "+filename);
		int c_start = code.size();
		String[] text = loadCode(filename);
		parseCode(filename, text);
		verbose.onPrint("  Lines:   "+(code.size() - c_start));
		verbose.onPrint("");
		
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
				/*
				 * get next include file
				 */
				String inc = includesAux.remove(l);
				if (!isValidFile(inc)) continue;
				
				/*
				 * scan include directories for a matching path
				 */
				String includeFilePath = null;
				for (String path : includesPath)
				{
					File f = new File(path+inc);
					if (f.exists())
					{
						includeFilePath = path+inc;
						break;
					}
				}
				
				/*
				 * when found, parse code
				 */
				if (includeFilePath != null)
				{
					String[] lines = loadCode(includeFilePath);
					if (lines!=null) 
					{
						verbose.onPrint("  Include: "+inc);

						c_start = code.size();
						parseCode(inc, lines);
						verbose.onPrint("  Lines:   "+(code.size() - c_start));
						verbose.onPrint("");
					}
				}
				else
				{
					addError( getIncludeErrorMessage(inc) );
				}
			}
		}
		while(includesAux.size() > 0);
		
		/*
		 * print included directories
		 */
		if (includesPath.size() > 0)
		{
			verbose.onPrint("Search Paths");
			for (String inc : includesPath)
			{
				verbose.onPrint("  "+inc);
			}
			//verbose.onPrint("");
		}
		
		verbose.onPrint("");
		verbose.onPrint("  Total lines of code:   "+code.size());
		
		/*
		 * check for duplicate definitions
		 */
		defDups.sort(comp); 
		if (defDups.size() > 0)
		{
			String msg = "Duplicate definition declaration\n";
			for (Definition entry : defDups)
			{
				msg += " "+entry.NAME + " in file: "+entry.SOURCE.getFilename()+" on line "+entry.SOURCE.getLinenumber() +"\n";
			}
			addError(msg);
		}
		
		verbose.onPrint("\nParsing End");
		verbose.onPrint("-------------------------------");
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
		//int totalLines = text.length;
		//strLeng = (""+totalLines).length() + 1;
		
		for (String line : text)
		{
			/*
			 * format instruction text to make it more predictable for parsing
			 */
			line = line.replace("\t", " ");
			line = line.replaceAll("\\s+", " ");
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
			if (line.contains(LABEL_MARK))
			{
				String t[] = line.split(LABEL_MARK);
				
				/*
				 * test for allowed label characters
				 */
				if (isValidNumber(t[0]))
				{
					addError(sc, "  Bad label name. It will be mistaken for a number.");
				}
				if (!isValidLabel(t[0]))
				{
					addError(sc, "  Invalid label characters used. Allowed characters are: _ (underscore), a-z, A-Z and 0-9.");
				}
				
				/*
				 * insert the marked label into the code list
				 */
				processLine( new SourceCode(filename, linenumber, t[0]+LABEL_MARK) );
				if (t.length == 1) continue;
				
				/*
				 * parse code possibly on the same line
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
		String line = sc.getLine();
		if (line.equalsIgnoreCase("")) return;
		
		/*
		 * if an include directive, external code
		 */
		if (line.startsWith(".inc "))
		{
			parseInclude(sc);
		}
		
		/*
		 * if a definition directive, store definition
		 */
		if (line.startsWith(".def "))
		{
			parseDefinition(sc, defs, defDups);
		}
		
		code.add(sc);
	}
	
	private void parseInclude(SourceCode sc) 
	{
		String line = sc.getLine();
		String include = line.substring(5); // remove ".inc " 5 characters
		
		/*
		 * the include name must be between quotation marks
		 */
		if (include.startsWith("\"") && include.endsWith("\"")) 
		{
			include = include.replace("\"", "");
			include = include.replace("\\", "/");
			
			String[] tokens = include.split("/");
			String filename = tokens[tokens.length-1];
			
			/*
			 * checks are done based on file names
			 * the auxiliary list hold path information
			 * these paths are later used to create new include directories
			 */
			if (!includes.contains(filename)) 
			{
				includes.add(filename);
				includesAux.add(include);
			}
		}
		else
		{
			addError(sc, "  Bad include syntax");
		}
	}
	
	private void parseDefinition(SourceCode sc, List<Definition> defs, List<Definition> defDups) 
	{
		String line = sc.getLine();
		String equates = line.substring(5);
		String[] tokens = equates.split(" ");
		
		/*
		 * if there are more than 2 tokens, invalid equ definition
		 */
		if (tokens.length != 2)
		{
			addError(sc, "  Bad definition syntax.");
			return;
		}
		
		/*
		 * if the first token is a bad name, error
		 */
		String name = tokens[0];
		if ( isValidNumber(name) )
		{
			addError(sc, "  Bad label name. It might be mistaken for a number.");
			return;
		}
		if (!isValidLabel(name))
		{
			addError(sc, "  Invalid label characters used. Allowed characters are: _(underscore), a-z, A-Z and 0-9.");
			return;
		}
		
		/*
		 * if the second name is a bad number, error
		 * allow for numbers and addresses
		 */
		String value = tokens[1];
		if (isAddress(value)) 
		{
			if (!isValidAddress(value))
			{
				addError(sc, "  Invalid address syntax.");
				return;
			}
		}
		else
		{
			if (!isValidNumber(value))
			{
				addError(sc, "  Invalid number syntax.");
				return;
			}
		}
		
		/*
		 * find def duplicates
		 */
		var pack3 = findDefine(name, defs);
		if (pack3 == null) 
		{
			newDefine(name, value, sc, defs);
		}
		else
		{
			// also add the first instance of that definition
			if (findDefine(name, defDups) == null) defDups.add(pack3);
			defDups.add( new Definition(name, "", sc));
		}
	}
	
	// ==================================================================

	private String[] loadCode(String inc)
	{
		File file = new File(inc);
		if (!file.exists()) 
		{
			return null;
		}
		
		// find path. set as new include directory
		String filepath = file.getAbsolutePath();
		String filename = file.getName();
		String include = filepath.substring(0, filepath.length() - filename.length());
		addIncludesPath(include);
		
		FileReader fr;
		try 
		{
			fr = new FileReader(file);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		BufferedReader br = new BufferedReader(fr); 
		List<String> lines = new ArrayList<String>();
		String string;
		
		try 
		{
			while ((string = br.readLine()) != null) 
			{
				lines.add(string); 
		  	} 
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		try 
		{
			br.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return lines.toArray(new String[0]);
	}
	
	// =================================================================
	
	private String getIncludeErrorMessage(String inc) 
	{
		return "  The included source file could not be found\n  \""+inc+"\"";
	}
	
	private void addError(String message)
	{
		addError(null,message);
	}
	
	private void addError(SourceCode sc, String message)
	{
		errors.add( new ParseError(sc,message) );
	}
	
	// =================================================================
	
	private void newDefine(String n, String v, SourceCode sc, List<Definition> defs) 
	{
		defs.add( new Definition(n, v, sc) );
	}
	
	private Definition findDefine(String name, List<Definition> equs) 
	{
		for (Definition t : equs)
			if (t.NAME.equals(name)) return t;
		return null;
	}
}
