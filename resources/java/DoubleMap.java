package de.elbosso.scratch.misc;

/*
Copyright (c) 2015.

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
public class DoubleMap
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(DoubleMap.class);
	public static void main(java.lang.String[] args)
	{
		de.elbosso.util.Utilities.sopln("huhu");
		de.elbosso.util.Utilities.configureBasicStdoutLogging(org.apache.log4j.Level.TRACE);
		double machEps = 1.0;

		do
		{
			machEps /= 2.0;
			de.elbosso.util.Utilities.sopln(machEps);
		}
		while ((double) (1.0 + (machEps / 2.0)) != 1.0);
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace("machine Epsilon (double)= "+machEps);
		machEps /= 2.0;
		Double a = java.lang.Double.valueOf(1);
		Double b = java.lang.Double.valueOf(a.doubleValue() + machEps);
		java.util.Set<Double> doubles = new java.util.HashSet();
		doubles.add(a);
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(doubles.contains(b));
		java.math.BigDecimal machEpsBD = new java.math.BigDecimal(1.0);
		java.math.BigDecimal sum=null;
		int loop=0;
//		de.netsysit.util.StopWatch sw=new de.netsysit.util.StopWatch();
		do
		{
			machEpsBD = machEpsBD.divide(java.math.BigDecimal.ONE.add(java.math.BigDecimal.ONE));
//			if(loop%1000==0)
//			{

//				sw.toNull();
//			}
//			++loop;
			sum=java.math.BigDecimal.ONE.add(machEpsBD.divide(java.math.BigDecimal.ONE.add(java.math.BigDecimal.ONE)));
		}
		while (sum.equals(java.math.BigDecimal.ONE)==false);
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace("machine Epsilon (BigDecimal)= "+machEpsBD);
	}
}
