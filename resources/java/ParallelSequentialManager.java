/*
Copyright (c) 2012-2017.

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
package de.elbosso.util.threads;

import de.netsysit.util.threads.Synchronizer;
import de.netsysit.util.threads.ThreadManager;

/**
 *
 * @author elbosso
 */
public class ParallelSequentialManager extends java.lang.Object
{
	private de.netsysit.util.beans.context.service.BackgroundExecutor threadManager;
	private de.netsysit.util.threads.ThreadManager sequentializer;

	public ParallelSequentialManager(de.netsysit.util.beans.context.service.BackgroundExecutor threadManager)
	{
		super();
		if(threadManager==null)
			throw new java.lang.IllegalArgumentException("threadManager must not be null");
		sequentializer=new de.netsysit.util.threads.ThreadManager("Sequentializer"+this,1);
		this.threadManager = threadManager;			
	}
	public void enqueue(Workload workload)
	{
		de.netsysit.util.threads.Synchronizer sync=threadManager.execute(workload);
		if(workload.getSequential()!=null)
		{
			sequentializer.execute(new Sequential(workload.getSequential(),sync));
		}
	}
	private class Sequential extends de.elbosso.util.threads.StoppableImpl
	{
		private java.lang.Runnable runnable;
		private de.netsysit.util.threads.Synchronizer sync;

		public Sequential(Runnable runnable, Synchronizer sync)
		{
			super();
			this.runnable = runnable;
			this.sync = sync;
		}
		
		
		public void run()
		{
			try
			{
				sync.join();
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
			runnable.run();
		}
		
	}
}
