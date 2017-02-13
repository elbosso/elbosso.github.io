/*
Copyright (c) 2012-2016.

Juergen Key. Alle Rechte vorbehalten.

Weiterverbreitung und Verwendung in nichtkompilierter oder kompilierter Form, 
mit oder ohne Veraenderung, sind unter den folgenden Bedingungen zulaessig:

   1. Weiterverbreitete nichtkompilierte Exemplare muessen das obige Copyright, 
die Liste der Bedingungen und den folgenden Haftungsausschluss im Quelltext 
enthalten.
   2. Weiterverbreitete kompilierte Exemplare muessen das obige Copyright, 
die Liste der Bedingungen und den folgenden Haftungsausschluss in der 
Dokumentation und/oder anderen Materialien, die mit dem Exemplar verbreitet 
werden, enthalten.
   3. Weder der Name des Autors noch die Namen der Beitragsleistenden 
duerfen zum Kennzeichnen oder Bewerben von Produkten, die von dieser Software 
abgeleitet wurden, ohne spezielle vorherige schriftliche Genehmigung verwendet 
werden.

DIESE SOFTWARE WIRD VOM AUTOR UND DEN BEITRAGSLEISTENDEN OHNE 
JEGLICHE SPEZIELLE ODER IMPLIZIERTE GARANTIEN ZUR VERFUEGUNG GESTELLT, DIE 
UNTER ANDEREM EINSCHLIESSEN: DIE IMPLIZIERTE GARANTIE DER VERWENDBARKEIT DER 
SOFTWARE FUER EINEN BESTIMMTEN ZWECK. AUF KEINEN FALL IST DER AUTOR 
ODER DIE BEITRAGSLEISTENDEN FUER IRGENDWELCHE DIREKTEN, INDIREKTEN, 
ZUFAELLIGEN, SPEZIELLEN, BEISPIELHAFTEN ODER FOLGENDEN SCHAEDEN (UNTER ANDEREM 
VERSCHAFFEN VON ERSATZGUETERN ODER -DIENSTLEISTUNGEN; EINSCHRAENKUNG DER 
NUTZUNGSFAEHIGKEIT; VERLUST VON NUTZUNGSFAEHIGKEIT; DATEN; PROFIT ODER 
GESCHAEFTSUNTERBRECHUNG), WIE AUCH IMMER VERURSACHT UND UNTER WELCHER 
VERPFLICHTUNG AUCH IMMER, OB IN VERTRAG, STRIKTER VERPFLICHTUNG ODER 
UNERLAUBTE HANDLUNG (INKLUSIVE FAHRLAESSIGKEIT) VERANTWORTLICH, AUF WELCHEM 
WEG SIE AUCH IMMER DURCH DIE BENUTZUNG DIESER SOFTWARE ENTSTANDEN SIND, SOGAR, 
WENN SIE AUF DIE MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND.
*/

/* Example:
//Switch, to enable (true) or disable (false) Debugging
___eb_debug_on___=true;
doFunc()
{
	a=3+4;
//Next line causes Breakpoint - Callstack allows accessing Variables a and c
	hallo();
	b=a*2;
//Next line causes Breakpoint - Callstack allows accessing Variables a, b, and c
	huhu(b);
	System.out.println(this.variables);
}
c=5;
doFunc();
System.out.println("huhu");
c=null;
//Next line causes NullPointerException - because the Script does not handle it, it causes
a Breakpoint - Callstack allows accessing Variables exp and c - exp being the Exception being raised
print(c.toString());
 */
package de.elbosso.util;

/**
 *
 * @author elbosso
 */
public class BeanShellDebugger
{
	public static void debug(java.lang.String key,bsh.CallStack callStack,Object[] arguments)
	{
		//Set Breakpoint in next line!
	}
	private static void instrument(bsh.Interpreter inter) throws bsh.EvalError
	{
		inter.eval("invoke(String methodName, Object [] arguments){"
				+ "if((___eb_debug_on___!=void)&&(___eb_debug_on___==true))"
				+ "{"
				+ "cs=this.callstack.copy();"
				+ "cs.pop();"
				+ BeanShellDebugger.class.getName()
				+ ".debug(methodName,cs,arguments);"
				+ "}"
				+ "}");
	}
	public static void eval(bsh.Interpreter inter, java.lang.String fragment) throws bsh.EvalError
	{
		instrument(inter);
		inter.eval("try{"+fragment+"}catch(exp){_uncaughtExceptionCaught()}");
	}
	public static void eval(bsh.Interpreter inter, java.io.Reader reader) throws bsh.EvalError
	{
		instrument(inter);
		inter.eval(reader);
	}
	
	static class ReaderFacade extends java.io.BufferedReader
	{
		private final java.io.StringReader prepend=new java.io.StringReader("try{\n");
		private final java.io.StringReader append=new java.io.StringReader("}catch(exp){_uncaughtExceptionCaught();}");
		public ReaderFacade(java.io.Reader reader)
		{
			super(reader);
		}

		@Override
		public int read() throws java.io.IOException
		{
			int rv=-1;
			rv=prepend.read();
			if(rv==-1)
				rv=super.read();
			if(rv==-1)
				rv=append.read();
			return rv;
		}

		@Override
		public int read(java.nio.CharBuffer target) throws java.io.IOException
		{
			int c=read();
			if(c>-1)
			{
				target.append((char)c);
				c=1;
			}
			return c;
		}

		@Override
		public int read(char[] cbuf) throws java.io.IOException
		{
			int c=read();
			if(c>-1)
			{
				cbuf[0]=(char)c;
				c=1;
			}
			return c;
		}

		@Override
		public int read(char[] cbuf, int off, int len) throws java.io.IOException
		{
			int c=read();
			if(c>-1)
			{
				cbuf[off]=(char)c;
				c=1;
			}
			return c;
		}
	}
}
