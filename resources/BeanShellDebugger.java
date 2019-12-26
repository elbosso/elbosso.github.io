/*
Copyright (c) 2012-2020.

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
global.___eb_debug_on___=true;
//Switch, to enable (true) or disable (false) interactive console for breakpoints
global.___eb_debug_on___="console";
doFunc()
{
	a=3+4;
//Next line causes Breakpoint - Callstack allows accessing Variables a and c
	hallo();
	b=a*2;
//Next line causes Breakpoint - Callstack allows accessing Variables a, b, and c
	huhu(b);
	de.elbosso.util.Utilities.sopln(this.variables);
}
c=5;
doFunc();
de.elbosso.util.Utilities.sopln("huhu");
c=null;
//Next line causes NullPointerException - because the Script does not handle it, it causes
//a Breakpoint - Callstack allows accessing Variables exp and c - exp being the Exception being raised
print(c.toString());
 */
package de.elbosso.util;

/**
 *
 * @author elbosso
 */
public class BeanShellDebugger extends java.lang.Object
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(BeanShellDebugger.class);

	private static final java.util.Set<java.lang.String> hidden=new java.util.HashSet();
	static
	{
		hidden.add("___eb_debug_on___");
		hidden.add("bsh");
	}

	private static Visitor visitor;
	public synchronized static void debug(java.lang.String key,bsh.CallStack callStack,Object[] arguments)
	{
		try
		{
			if(callStack.get(0).getVariable("___eb_debug_on___").toString().equals("console".toString()))
			{
				final bsh.util.JConsole jConsole=new bsh.util.JConsole();
				jConsole.setPreferredSize(new java.awt.Dimension(430, 240));
				javax.swing.JFrame f=new javax.swing.JFrame(key);
				f.setContentPane(jConsole);
				bsh.Interpreter inter=new bsh.Interpreter(jConsole);
				inter.setExitOnEOF(false);
				f.pack();
				f.setLocation(40,40);
				f.setVisible(true);
				final Thread t=new Thread(inter);
				f.addWindowListener(new java.awt.event.WindowAdapter()
				{
					@Override
					public void windowClosing(java.awt.event.WindowEvent e)
					{
						super.windowClosing(e); //To change body of generated methods, choose Tools | Templates.
						t.interrupt();
						try
						{
							jConsole.getIn().close();
						}
						catch(java.io.IOException exp)
						{
							
						}
					}
				});
				try
				{
					inter.set("callStack", callStack);
					inter.set("arguments", arguments);
					inter.set("ns", callStack.get(0));
					inter.eval("print(\"Variables:\");");
					inter.eval("print(\"-------------\");");
					java.util.Set<java.lang.String> alreadyPrinted=new java.util.HashSet();
					for(int i=0;i<callStack.depth();++i)
					{
						printVariableNames(inter,callStack.get(i),alreadyPrinted);
					}
					inter.eval("print(\"-------------\");");
					inter.eval("print(\"Get value of variable bla: latch=ns.getVariable(\\\"bla\\\");\");");
					inter.eval("print(\"Set value of variable bla: ns.setVariable(\\\"bla\\\",<value>,true);\");");
					t.start();
					t.join();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				f.setVisible(false);
				f.dispose();
			}
			else
			{
//				System.out.println(key+" "+visitor);
				if(key.equals("_uncaughtExceptionCaught"))
				{
					CLASS_LOGGER.debug("Exception caught!");
					if(((arguments.length>0)&&(arguments[0]!=null))&&(java.lang.Throwable.class.isAssignableFrom(arguments[0].getClass())))
					{
						java.lang.Throwable t = (java.lang.Throwable) arguments[0];
						CLASS_LOGGER.debug(t.getMessage(), t);
					}
				}
				else
				{
					CLASS_LOGGER.debug("Undefined method called: "+key+"!");
				}
				if(visitor!=null)
					visitor.debug(key, callStack, arguments);
			}
		}
		catch(bsh.UtilEvalError e)
		{
			CLASS_LOGGER.error(e.getMessage(),e);
		}
	}
	private static void printVariableNames(bsh.Interpreter inter, bsh.NameSpace ns,java.util.Set<java.lang.String> alreadyPrinted) throws bsh.EvalError
	{
		if(ns.getParent()!=null)
			printVariableNames(inter, ns.getParent(),alreadyPrinted);
		java.lang.String[] vnames=ns.getVariableNames();
		if((vnames!=null)&&(vnames.length>0))
		{
			for (String vname : vnames)
			{
				if((alreadyPrinted.contains(vname)==false)&&(hidden.contains(vname)==false))
				{
					inter.eval("print(\""+vname+"\");");
					alreadyPrinted.add(vname);
				}
			}
		}
	}
	private static void instrument(bsh.Interpreter inter) throws bsh.EvalError
	{
		java.lang.String statements="invoke(String methodName, Object [] arguments){"
//				+ "System.out.println(\"invoke \"+methodName+\" \"+(___eb_debug_on___!=null?___eb_debug_on___:false));"
//				+ "System.out.println(\"invoke \"+global.___eb_debug_on___);"
				+ "if((___eb_debug_on___!=void)&&((___eb_debug_on___==true)||(___eb_debug_on___.equals(\"console\"))))"
				+ "{"
				+ "cs=this.callstack.copy();"
				+ "cs.pop();"
				+ BeanShellDebugger.class.getName()
				+ ".debug(methodName,cs,arguments);"
				+ "}"
				+ "}";
		inter.eval(statements);
	}
	public static java.lang.Object eval(bsh.Interpreter inter, java.lang.String fragment) throws bsh.EvalError
	{
		return eval(inter,fragment,null);
	}
	public static java.lang.Object eval(bsh.Interpreter inter, java.io.Reader reader) throws bsh.EvalError
	{
		return eval(inter,reader,null);
	}
	public static java.lang.Object eval(bsh.Interpreter inter, java.lang.String fragment,de.elbosso.util.BeanShellDebugger.Visitor visitor) throws bsh.EvalError
	{
		java.lang.Object rv=null;
		if(System.getProperty("de.elbosso.util.BeanShellDebugger.active","true").equals("true"))
		{
			BeanShellDebugger.visitor=visitor;
			instrument(inter);
			rv=inter.eval("try{___eb_function___(){\n"+fragment+"\n}\nreturn ___eb_function___();}catch(exp){_uncaughtExceptionCaught(exp);throw(exp);}");
			BeanShellDebugger.visitor=null;
		}
		else
		{
			rv=inter.eval(fragment);
		}
		return rv;
	}
	public static java.lang.Object eval(bsh.Interpreter inter, java.io.Reader reader,de.elbosso.util.BeanShellDebugger.Visitor visitor) throws bsh.EvalError
	{
		java.lang.Object rv=null;
		if(System.getProperty("de.elbosso.util.BeanShellDebugger.active","true").equals("true"))
		{
			BeanShellDebugger.visitor=visitor;
			instrument(inter);
			rv=inter.eval(new ReaderFacade(reader));
			BeanShellDebugger.visitor=null;
		}
		else
		{
			rv=inter.eval(reader);
		}
		return rv;
	}

	public static interface Visitor
	{
		void debug(java.lang.String key,bsh.CallStack callStack,Object[] arguments);
	}
	
	private static class ReaderFacade extends java.io.BufferedReader
	{
		private final java.io.StringReader prepend=new java.io.StringReader("try{___eb_function___(){\n");
		private final java.io.StringReader append=new java.io.StringReader("\n}\nreturn ___eb_function___();}catch(exp){_uncaughtExceptionCaught(exp);throw(exp);}");
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
