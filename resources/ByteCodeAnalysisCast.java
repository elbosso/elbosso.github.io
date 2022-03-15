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

import de.elbosso.util.Utilities;
import de.elbosso.util.lang.collections.DoublyLinkedListHead;
import de.elbosso.util.lang.collections.DoublyLinkedListPart;
import de.netsysit.util.StopWatch;
import ch.qos.logback.classic.Level;

public class ByteCodeAnalysisCast extends java.lang.Object
{
	private final static org.slf4j.Logger CLASS_LOGGER =org.slf4j.LoggerFactory.getLogger(ByteCodeAnalysisCast.class);
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
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
		CLASS_LOGGER.trace(""+sum);
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
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
		CLASS_LOGGER.trace(""+sum);
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
	private DoublyLinkedListHead<String> createDoublyLinkedStringList()
	{
		de.netsysit.util.generator.RandomNumberSequence<Integer> counter=new de.netsysit.util.generator.generalpurpose.RandomIntSequence();
		counter.setMin(49999);
		counter.setMax(50000);
		int howmany=counter.next();
		de.netsysit.util.generator.semantics.PlacesSequence seq=new de.netsysit.util.generator.semantics.PlacesSequence();
		seq.setAllowsNull(false);
		DoublyLinkedListHead<java.lang.String> l=new DoublyLinkedListHead();
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
		for (java.util.Iterator<java.lang.String> iter=l.listIterator();iter.hasNext();)
		{
			sb.append(iter.next());
		}
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
	}
	private void iterListLazy(DoublyLinkedListHead<String> l)
	{
		java.lang.StringBuffer sb=new java.lang.StringBuffer();
		StopWatch sw=new StopWatch(true);
		for (java.util.Iterator<DoublyLinkedListPart<java.lang.String>> iter=l.internalIterator();iter.hasNext();)
		{
			sb.append(iter.next().getContent());
		}
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
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
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
	}
	private void iterListVarSideEffect(java.util.List<java.lang.String> l)
	{
		java.lang.StringBuffer sb=new java.lang.StringBuffer();
		StopWatch sw=new StopWatch(true);
		for(int i=0;i<l.size();++i)
		{
			sb.append(l.get(i));
		}
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
	}
	private void iterArray(java.lang.String[] l)
	{
		java.lang.StringBuffer sb=new java.lang.StringBuffer();
		StopWatch sw=new StopWatch(true);
		for(String s:l)
		{
			sb.append(s);
		}
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
	}
	private void iterArrayVar(java.lang.String[] l)
	{
		java.lang.StringBuffer sb=new java.lang.StringBuffer();
		int size=l.length;
		StopWatch sw=new StopWatch(true);
		for(int i=0;i<size;++i)
		{
			sb.append(l[i]);
		}
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
	}
	private void iterArrayVarSideEffect(java.lang.String[] l)
	{
		java.lang.StringBuffer sb=new java.lang.StringBuffer();
		StopWatch sw=new StopWatch(true);
		for(int i=0;i<l.length;++i)
		{
			sb.append(l[i]);
		}
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
	}
	private void iterList(java.util.List<java.lang.String> l)
	{
		java.lang.StringBuffer sb=new java.lang.StringBuffer();
		StopWatch sw=new StopWatch(true);
		for (String string : l)
		{
			sb.append(string);
		}
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
	}
	private void iterList(DoublyLinkedListHead<java.lang.String> l)
	{
		java.lang.StringBuffer sb=new java.lang.StringBuffer();
		StopWatch sw=new StopWatch(true);
		for (DoublyLinkedListPart<java.lang.String> part: l.getInternalCollection())
		{
			sb.append(part.getContent());
		}
		CLASS_LOGGER.trace(java.util.Objects.toString(sw));
	}

	public static void main(java.lang.String[] args)
	{
		Utilities.configureBasicStdoutLogging(Level.TRACE);
		ByteCodeAnalysisCast bcac=new ByteCodeAnalysisCast();
//		for(int i=0;i<10;++i)
//		{
//			bcac.calcCast();
//			bcac.calcMult();
//		}
/*		java.util.List<java.lang.String> l=bcac.createStringList();
		for(int i=0;i<10;++i)
		{
			bcac.iterListLazy(l);
			bcac.iterListVar(l);
			bcac.iterList(l);
		}
*/
		DoublyLinkedListHead<java.lang.String> l=bcac.createDoublyLinkedStringList();
/*		for(int i=0;i<100;++i)
		{
//			bcac.iterListLazy(l);
			bcac.iterListVar(l);
//			bcac.iterArrayVar(l.toArray(new String[0]));
		}
*/		System.out.println("--");
		for(int i=0;i<100;++i)
		{
//			bcac.iterListLazy(l);
//			bcac.iterListVar(l);
//			bcac.iterArrayVar(l.toArray(new String[0]));
			bcac.iterArray(l.toArray(new String[0]));
		}
		System.out.println("--");
		//cat /tmp/data.dat|grep -F 'iterListLazy.'|rev|cut -d '.' -f 1|rev >/tmp/iterListLazy.dat
		for(int i=0;i<100;++i)
		{
//			bcac.iterListLazy(l);
			bcac.iterList(l);
//			bcac.iterListVarSideEffect(l);
//			bcac.iterArrayVarSideEffect(l.toArray(new String[0]));
		}

	}
}
