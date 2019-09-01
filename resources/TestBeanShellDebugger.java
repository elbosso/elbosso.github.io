/*
 * Copyright (c) 2019.
 *
 * Juergen Key. Alle Rechte vorbehalten.
 *
 * Weiterverbreitung und Verwendung in nichtkompilierter oder kompilierter Form,
 * mit oder ohne Veraenderung, sind unter den folgenden Bedingungen zulaessig:
 *
 *    1. Weiterverbreitete nichtkompilierte Exemplare muessen das obige Copyright,
 * die Liste der Bedingungen und den folgenden Haftungsausschluss im Quelltext
 * enthalten.
 *    2. Weiterverbreitete kompilierte Exemplare muessen das obige Copyright,
 * die Liste der Bedingungen und den folgenden Haftungsausschluss in der
 * Dokumentation und/oder anderen Materialien, die mit dem Exemplar verbreitet
 * werden, enthalten.
 *    3. Weder der Name des Autors noch die Namen der Beitragsleistenden
 * duerfen zum Kennzeichnen oder Bewerben von Produkten, die von dieser Software
 * abgeleitet wurden, ohne spezielle vorherige schriftliche Genehmigung verwendet
 * werden.
 *
 * DIESE SOFTWARE WIRD VOM AUTOR UND DEN BEITRAGSLEISTENDEN OHNE
 * JEGLICHE SPEZIELLE ODER IMPLIZIERTE GARANTIEN ZUR VERFUEGUNG GESTELLT, DIE
 * UNTER ANDEREM EINSCHLIESSEN: DIE IMPLIZIERTE GARANTIE DER VERWENDBARKEIT DER
 * SOFTWARE FUER EINEN BESTIMMTEN ZWECK. AUF KEINEN FALL IST DER AUTOR
 * ODER DIE BEITRAGSLEISTENDEN FUER IRGENDWELCHE DIREKTEN, INDIREKTEN,
 * ZUFAELLIGEN, SPEZIELLEN, BEISPIELHAFTEN ODER FOLGENDEN SCHAEDEN (UNTER ANDEREM
 * VERSCHAFFEN VON ERSATZGUETERN ODER -DIENSTLEISTUNGEN; EINSCHRAENKUNG DER
 * NUTZUNGSFAEHIGKEIT; VERLUST VON NUTZUNGSFAEHIGKEIT; DATEN; PROFIT ODER
 * GESCHAEFTSUNTERBRECHUNG), WIE AUCH IMMER VERURSACHT UND UNTER WELCHER
 * VERPFLICHTUNG AUCH IMMER, OB IN VERTRAG, STRIKTER VERPFLICHTUNG ODER
 * UNERLAUBTE HANDLUNG (INKLUSIVE FAHRLAESSIGKEIT) VERANTWORTLICH, AUF WELCHEM
 * WEG SIE AUCH IMMER DURCH DIE BENUTZUNG DIESER SOFTWARE ENTSTANDEN SIND, SOGAR,
 * WENN SIE AUF DIE MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND.
 *
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.elbosso.util.test;

import bsh.CallStack;
import bsh.EvalError;

public class TestBeanShellDebugger
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(TestBeanShellDebugger.class);
	private static final String DEBUG_ON = "global.___eb_debug_on___=true;";

	@org.junit.Rule
	public org.junit.rules.TestName name = new org.junit.rules.TestName();
	private Visitor visitor;

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodExceptionThrown()
	{
		java.lang.String script="java.lang.Object huhu=null;huhu.toString();";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Throwable expected=null;
		java.lang.Throwable actual=null;
		try
		{
			inter.eval(script);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			expected=t.getCause();
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,script,visitor);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			actual=t.getCause();
		}
		org.junit.Assert.assertEquals(expected.toString(),actual.toString());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodExceptionThrownDebugActive()
	{
		java.lang.String script=DEBUG_ON+"java.lang.Object huhu=null;huhu.toString();";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Throwable expected=null;
		java.lang.Throwable actual=null;
		try
		{
			inter.eval(script);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			expected=t.getCause();
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,script,visitor);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			actual=t.getCause();
		}
		org.junit.Assert.assertEquals(expected.toString(),actual.toString());
		org.junit.Assert.assertEquals("_uncaughtExceptionCaught",visitor.getKey());
	}

//	@org.junit.Ignore
	@org.junit.Test
	public void methodExceptionThrownReader()
	{
		java.lang.String script="java.lang.Object huhu=null;huhu.toString();";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Throwable expected=null;
		java.lang.Throwable actual=null;
		try
		{
			inter.eval(script);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			expected=t.getCause();
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,new java.io.StringReader(script),visitor);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			actual=t.getCause();
		}
		org.junit.Assert.assertEquals(expected.toString(),actual.toString());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodExceptionThrownDebugActiveReader()
	{
		java.lang.String script=DEBUG_ON+"java.lang.Object huhu=null;huhu.toString();";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Throwable expected=null;
		java.lang.Throwable actual=null;
		try
		{
			inter.eval(new java.io.StringReader(script));
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			expected=t.getCause();
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,new java.io.StringReader(script),visitor);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			actual=t.getCause();
		}
		org.junit.Assert.assertEquals(expected.toString(),actual.toString());
		org.junit.Assert.assertEquals("_uncaughtExceptionCaught",visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodundefinedMethod()
	{
		java.lang.String script="java.lang.Object huhu=\"hallo\";toStrinng();";
		bsh.Interpreter inter=new bsh.Interpreter();
		try
		{
			inter.eval(script);
			org.junit.Assert.fail();
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,script,visitor);
		}
		catch(java.lang.Throwable t)
		{
			org.junit.Assert.fail();
		}
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodundefinedMethodDebugActive()
	{
		java.lang.String script=DEBUG_ON+"java.lang.Object huhu=\"hallo\";toStrinng();";
		bsh.Interpreter inter=new bsh.Interpreter();
		try
		{
			inter.eval(script);
			org.junit.Assert.fail();
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,script,visitor);
		}
		catch(java.lang.Throwable t)
		{
			org.junit.Assert.fail();
		}
		org.junit.Assert.assertEquals("toStrinng",visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodundefinedMethodReader()
	{
		java.lang.String script="java.lang.Object huhu=\"hallo\";toStrinng();";
		bsh.Interpreter inter=new bsh.Interpreter();
		try
		{
			inter.eval(new java.io.StringReader(script));
			org.junit.Assert.fail();
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,new java.io.StringReader(script),visitor);
		}
		catch(java.lang.Throwable t)
		{
			org.junit.Assert.fail();
		}
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodundefinedMethodDebugActiveReader()
	{
		java.lang.String script=DEBUG_ON+"java.lang.Object huhu=\"hallo\";toStrinng();";
		bsh.Interpreter inter=new bsh.Interpreter();
		try
		{
			inter.eval(new java.io.StringReader(script));
			org.junit.Assert.fail();
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,new java.io.StringReader(script),visitor);
		}
		catch(java.lang.Throwable t)
		{
			org.junit.Assert.fail();
		}
		org.junit.Assert.assertEquals("toStrinng",visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodSyntaxError()
	{
		java.lang.String script="java.lang.Object huhu=\"hallo\";-;huhu.toString();";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Throwable expected=null;
		java.lang.Throwable actual=null;
		try
		{
			inter.eval(script);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			expected=t;
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,script,visitor);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			actual=t;
		}
		org.junit.Assert.assertEquals(expected.getClass(),actual.getClass());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodSyntaxErrorDebugActive()
	{
		java.lang.String script=DEBUG_ON+"java.lang.Object huhu=\"hallo\";-;huhu.toString();";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Throwable expected=null;
		java.lang.Throwable actual=null;
		try
		{
			inter.eval(script);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			expected=t;
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,script,visitor);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			actual=t;
		}
		org.junit.Assert.assertEquals(expected.getClass(),actual.getClass());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodSyntaxErrorReader()
	{
		java.lang.String script="java.lang.Object huhu=\"hallo\";-;huhu.toString();";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Throwable expected=null;
		java.lang.Throwable actual=null;
		try
		{
			inter.eval(new java.io.StringReader(script));
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			expected=t;
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,new java.io.StringReader(script),visitor);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			actual=t;
		}
		org.junit.Assert.assertEquals(expected.getClass(),actual.getClass());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodSyntaxErrorDebugActiveReader()
	{
		java.lang.String script=DEBUG_ON+"java.lang.Object huhu=\"hallo\";-;huhu.toString();";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Throwable expected=null;
		java.lang.Throwable actual=null;
		try
		{
			inter.eval(new java.io.StringReader(script));
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			expected=t;
		}
		inter=new bsh.Interpreter();
		try
		{
			de.elbosso.util.BeanShellDebugger.eval(inter,new java.io.StringReader(script),visitor);
		}
		catch(java.lang.Throwable t)
		{
//			CLASS_LOGGER.error(t.getMessage(),t);
			actual=t;
		}
		org.junit.Assert.assertEquals(expected.getClass(),actual.getClass());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodimplicitReturnValue() throws EvalError
	{
		java.lang.String script="a=2*2;";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Object expected=inter.eval(script);
		inter=new bsh.Interpreter();
		java.lang.Object actual=de.elbosso.util.BeanShellDebugger.eval(inter,script,visitor);
		org.junit.Assert.assertNotNull(expected);
		org.junit.Assert.assertNotNull(actual);
		org.junit.Assert.assertEquals(expected,actual);
		org.junit.Assert.assertTrue(java.lang.Number.class.isAssignableFrom(expected.getClass()));
		org.junit.Assert.assertEquals(4,((java.lang.Number)actual).intValue());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodimplicitReturnValueDebugActive() throws EvalError
	{
		java.lang.String script=DEBUG_ON+"a=2*2;";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Object expected=inter.eval(script);
		inter=new bsh.Interpreter();
		java.lang.Object actual=de.elbosso.util.BeanShellDebugger.eval(inter,script,visitor);
		org.junit.Assert.assertNotNull(expected);
		org.junit.Assert.assertNotNull(actual);
		org.junit.Assert.assertEquals(expected,actual);
		org.junit.Assert.assertTrue(java.lang.Number.class.isAssignableFrom(expected.getClass()));
		org.junit.Assert.assertEquals(4,((java.lang.Number)actual).intValue());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodimplicitReturnValueReader() throws EvalError
	{
		java.lang.String script="a=2*2;";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Object expected=inter.eval(new java.io.StringReader(script));
		java.lang.Object actual=de.elbosso.util.BeanShellDebugger.eval(inter,new java.io.StringReader(script),visitor);
		org.junit.Assert.assertNotNull(expected);
		org.junit.Assert.assertNotNull(actual);
		org.junit.Assert.assertEquals(expected,actual);
		org.junit.Assert.assertTrue(java.lang.Number.class.isAssignableFrom(expected.getClass()));
		org.junit.Assert.assertEquals(4,((java.lang.Number)actual).intValue());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodimplicitReturnValueDebugActiveReader() throws EvalError
	{
		java.lang.String script=DEBUG_ON+"a=2*2;";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Object expected=inter.eval(new java.io.StringReader(script));
		java.lang.Object actual=de.elbosso.util.BeanShellDebugger.eval(inter,new java.io.StringReader(script),visitor);
		org.junit.Assert.assertNotNull(expected);
		org.junit.Assert.assertNotNull(actual);
		org.junit.Assert.assertEquals(expected,actual);
		org.junit.Assert.assertTrue(java.lang.Number.class.isAssignableFrom(expected.getClass()));
		org.junit.Assert.assertEquals(4,((java.lang.Number)actual).intValue());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodexplicitReturnValue() throws EvalError
	{
		java.lang.String script="a=2*2;b=3*3;return a;";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Object expected=inter.eval(script);
		inter=new bsh.Interpreter();
		java.lang.Object actual=de.elbosso.util.BeanShellDebugger.eval(inter,script,visitor);
		org.junit.Assert.assertNotNull(expected);
		org.junit.Assert.assertNotNull(actual);
		org.junit.Assert.assertEquals(expected,actual);
		org.junit.Assert.assertTrue(java.lang.Number.class.isAssignableFrom(expected.getClass()));
		org.junit.Assert.assertEquals(4,((java.lang.Number)actual).intValue());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodexplicitReturnValueDebugActive() throws EvalError
	{
		java.lang.String script=DEBUG_ON+"a=2*2;b=3*3;return a;";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Object expected=inter.eval(script);
		inter=new bsh.Interpreter();
		java.lang.Object actual=de.elbosso.util.BeanShellDebugger.eval(inter,script,visitor);
		org.junit.Assert.assertNotNull(expected);
		org.junit.Assert.assertNotNull(actual);
		org.junit.Assert.assertEquals(expected,actual);
		org.junit.Assert.assertTrue(java.lang.Number.class.isAssignableFrom(expected.getClass()));
		org.junit.Assert.assertEquals(4,((java.lang.Number)actual).intValue());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodexplicitReturnValueReader() throws EvalError
	{
		java.lang.String script="a=2*2;b=3*3;return a;";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Object expected=inter.eval(new java.io.StringReader(script));
		java.lang.Object actual=de.elbosso.util.BeanShellDebugger.eval(inter,new java.io.StringReader(script),visitor);
		org.junit.Assert.assertNotNull(expected);
		org.junit.Assert.assertNotNull(actual);
		org.junit.Assert.assertEquals(expected,actual);
		org.junit.Assert.assertTrue(java.lang.Number.class.isAssignableFrom(expected.getClass()));
		org.junit.Assert.assertEquals(4,((java.lang.Number)actual).intValue());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Test annotation indicates that the public void method to which it is
	 * attached can be run as a test case.
	 */
//	@org.junit.Ignore
	@org.junit.Test
	public void methodexplicitReturnValueDebugActiveReader() throws EvalError
	{
		java.lang.String script=DEBUG_ON+"a=2*2;b=3*3;return a;";
		bsh.Interpreter inter=new bsh.Interpreter();
		java.lang.Object expected=inter.eval(new java.io.StringReader(script));
		java.lang.Object actual=de.elbosso.util.BeanShellDebugger.eval(inter,new java.io.StringReader(script),visitor);
		org.junit.Assert.assertNotNull(expected);
		org.junit.Assert.assertNotNull(actual);
		org.junit.Assert.assertEquals(expected,actual);
		org.junit.Assert.assertTrue(java.lang.Number.class.isAssignableFrom(expected.getClass()));
		org.junit.Assert.assertEquals(4,((java.lang.Number)actual).intValue());
		org.junit.Assert.assertNull(visitor.getKey());
	}

	/**
	 * The Before annotation indicates that this method must be executed before
	 * each test in the class, so as to execute some preconditions necessary for
	 * the test.
	 */

	@org.junit.Before
	public void methodBefore()
	{
		visitor=new Visitor();
	}

	/**
	 * The BeforeClass annotation indicates that the static method to which is
	 * attached must be executed once and before all tests in the class. That
	 * happens when the test methods share computationally expensive setup (e.g.
	 * connect to database).
	 */

	@org.junit.BeforeClass
	public static void methodBeforeClass()
	{
	}

	/**
	 * The After annotation indicates that this method gets executed after
	 * execution of each test (e.g. reset some variables after execution of
	 * every test, delete temporary variables etc)
	 */

	@org.junit.After
	public void methodAfter()
	{
	}

	/**
	 * The AfterClass annotation can be used when a method needs to be executed
	 * after executing all the tests in a JUnit Test Case class so as to
	 * clean-up the expensive set-up (e.g disconnect from a database).
	 * Attention: The method attached with this annotation (similar to
	 * BeforeClass) must be defined as static.
	 */

	@org.junit.AfterClass
	public static void methodAfterClass()
	{
	}
	class Visitor extends java.lang.Object implements de.elbosso.util.BeanShellDebugger.Visitor
	{
		private java.lang.String key;

		@Override
		public void debug(String key, CallStack callStack, Object[] arguments)
		{
			this.key=key;
		}

		public String getKey()
		{
			return key;
		}
	}
}
