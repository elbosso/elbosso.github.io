/*
Copyright (c) 2017.

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
package de.elbosso.scratch.util;

import de.netsysit.util.StopWatch;
import org.apache.log4j.Level;

public class ByteCodeAnalysisCast extends java.lang.Object
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(ByteCodeAnalysisCast.class);
	private java.util.Random rand=new java.util.Random(System.currentTimeMillis());

	public ByteCodeAnalysisCast()
	{
		super();
	}
	private void calcMult()
	{
		int a=rand.nextInt();
		int b=rand.nextInt();
		StopWatch sw=new StopWatch(true);
		double sum=0.0;
		for(int i=0;i<100000000;++i)
		{
			a=rand.nextInt();
			b=rand.nextInt();
			double d=(a*1.0)/(b*1.0);
			sum+=d;
		}
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(sw.toString());
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(sum);
	}
	private void calcCast()
	{
		int a=rand.nextInt();
		int b=rand.nextInt();
		StopWatch sw=new StopWatch(true);
		double sum=0.0;
		for(int i=0;i<100000000;++i)
		{
			a=rand.nextInt();
			b=rand.nextInt();
			double d=(double)a/(double)b;
			sum+=d;
		}
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(sw.toString());
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(sum);
	}
	private java.util.List<java.lang.String> createStringList()
	{
		de.netsysit.util.generator.RandomNumberSequence<Integer> counter=new de.netsysit.util.generator.generalpurpose.RandomIntSequence();
		counter.setMin(49999);
		counter.setMax(50000);
		int howmany=counter.next();
		de.netsysit.util.generator.semantics.PlacesSequence seq=new de.netsysit.util.generator.semantics.PlacesSequence();
		seq.setAllowsNull(false);
		java.util.List<java.lang.String> l=new java.util.LinkedList();
		for(int i=0;i<howmany;++i)
		{
			l.add(seq.next());
		}
		return l;
	}
	private void iterListLazy(java.util.List<java.lang.String> l)
	{
		java.lang.StringBuffer sb=new java.lang.StringBuffer();
		StopWatch sw=new StopWatch(true);
		for (String string : l)
		{
			sb.append(string);
		}
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(sw.toString());
	}
	private void iterListVar(java.util.List<java.lang.String> l)
	{
		java.lang.StringBuffer sb=new java.lang.StringBuffer();
		int size=l.size();
		StopWatch sw=new StopWatch(true);
		for(int i=0;i<size;++i)
		{
			sb.append(l.get(i));
		}
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(sw.toString());
	}
	private void iterList(java.util.List<java.lang.String> l)
	{
		java.lang.StringBuffer sb=new java.lang.StringBuffer();
		StopWatch sw=new StopWatch(true);
		for (String string : l)
		{
			sb.append(string);
		}
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(sw.toString());
	}
	
	public static void main(java.lang.String[] args)
	{
		de.elbosso.util.Utilities.configureBasicStdoutLogging(Level.TRACE);
		ByteCodeAnalysisCast bcac=new ByteCodeAnalysisCast();
//		for(int i=0;i<10;++i)
//		{
//			bcac.calcCast();
//			bcac.calcMult();
//		}
		java.util.List<java.lang.String> l=bcac.createStringList();
		for(int i=0;i<10;++i)
		{
			bcac.iterListLazy(l);
			bcac.iterListVar(l);
			bcac.iterList(l);
		}
	}
}
