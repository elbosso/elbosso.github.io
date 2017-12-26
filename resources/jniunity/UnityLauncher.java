/*
Copyright (c) 2012-2018.

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
WENN SIE AUF DIE MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND. */
package de.elbosso.ui.unity;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elbosso
 */
public class UnityLauncher
{

	private static boolean available;
	private long handle;
	private static CallbackImpl callbackImpl;

	static
	{

		try
		{
			de.netsysit.util.LibPathHacker.addDir("<path_to_library>");
			System.loadLibrary("ebunity");
//			UnityLauncher ul=new UnityLauncher();
//			if(ul.callTest(2)==4)
			available = true;
		}
		catch (java.lang.Throwable ex)
		{
			ex.printStackTrace();
		}
	}

	public static boolean isAvailable()
	{
		return available;
	}

	private native int callTest(int param);

	public native long getForDesktopId(String id);

	public native void setProgressVisible(long self, boolean visible);

	public native void setProgress(long self, double progress);

	public native void setCountVisible(long self, boolean visible);

	public native void setCount(long self, long count);

	public native void setUrgent(long self, boolean urgent);

	public native long getRootMenu(long self);

	public native void addToMenu(long self, String labelText, Callback callback);

	public static void main(java.lang.String[] args) throws InterruptedException
	{
		UnityLauncher ul = new UnityLauncher();
//		new Worker(ul).start();

	}

	public UnityLauncher() throws InterruptedException
	{

		if (UnityLauncher.isAvailable())
		{
			handle = getForDesktopId("gedit.desktop");
			if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(handle);
			long menu = getRootMenu(handle);
			callbackImpl = new CallbackImpl("event1");
			addToMenu(menu, "item1", callbackImpl);
			addToMenu(menu, "item2", callbackImpl);
			addToMenu(menu, "item3", callbackImpl);
		}
		else
		{
			if(CLASS_LOGGER.isEnabledFor(org.apache.log4j.Level.ERROR))CLASS_LOGGER.error("Unity not supported here!");
		}
		new javax.swing.JFrame().setVisible(true);
	}

	public long getHandle()
	{
		return handle;
	}

	static class Worker extends java.lang.Thread
	{

		UnityLauncher ul;

		public Worker(UnityLauncher ul)
		{
			this.ul = ul;
		}

		@Override
		public void run()
		{
			try
			{
				ul.setProgressVisible(ul.getHandle(), true);
				java.lang.Thread.currentThread().sleep(100);
				ul.setCountVisible(ul.getHandle(), true);
				ul.setProgress(ul.getHandle(), 0.4);
				ul.setCount(ul.getHandle(), (long) (0.4 * 100.0));
				double start = 0.4;
				java.lang.Thread.currentThread().sleep(1000);
				ul.setUrgent(ul.getHandle(), true);
				java.lang.Thread.currentThread().sleep(1000);
				while (start < 1.0)
				{
					java.lang.Thread.currentThread().sleep(100);
					ul.setProgress(ul.getHandle(), start);
					ul.setCount(ul.getHandle(), (long) (start * 100.0));
					start += 0.02;
					if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(start);
				}
				ul.setUrgent(ul.getHandle(), true);
				java.lang.Thread.currentThread().sleep(100);
				ul.setCountVisible(ul.getHandle(), false);
				java.lang.Thread.currentThread().sleep(100);
				ul.setProgressVisible(ul.getHandle(), false);
			}
			catch (java.lang.Throwable t)
			{
				t.printStackTrace();
			}
		}

	}

	interface Callback
	{

		void callback(java.lang.String actionCommand);
	}

	class CallbackImpl extends java.lang.Object implements Callback
	{

		private java.lang.String message;

		public CallbackImpl(String message)
		{
			this.message = message;
		}

		public void callback(java.lang.String actionCommand)
		{
			if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(actionCommand);
		}

	}

}
