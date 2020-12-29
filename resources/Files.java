/*
Copyright (c) 2012-2021.

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
package de.elbosso.scratch.misc;

import java.io.IOException;

public class Files
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(Files.class);
	public static void main(java.lang.String[] args) throws IOException, InterruptedException
	{
		byte[] bo = new byte[100];
		String[] cmd =
		{
			"bash", "-c", "echo $PPID"
		};
		Process p = Runtime.getRuntime().exec(cmd);
		p.getInputStream().read(bo);
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(new String(bo));
		String[] cmda =
		{
			System.getProperty("user.home")+java.io.File.separator+"script.sh", new String(bo).replace('\n', ' ').trim()
		};
		p = Runtime.getRuntime().exec(cmda);
		bo = new byte[100];
		p.getInputStream().read(bo);
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(new String(bo));// + " " + new String(bo).length());
		p.waitFor();
		if (System.currentTimeMillis() > 0)
		{
			java.io.FileOutputStream fos = new java.io.FileOutputStream(de.elbosso.util.Utilities.getTempDir()+"/console");
			java.io.PrintWriter pw = new java.io.PrintWriter(fos);
			pw.println("console");
			p = Runtime.getRuntime().exec(cmda);
			bo = new byte[100];
			p.getInputStream().read(bo);
			if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(new String(bo));// + " " + new String(bo).length());
			p.waitFor();
			pw.close();
			fos.close();
			p = Runtime.getRuntime().exec(cmda);
			bo = new byte[100];
			p.getInputStream().read(bo);
			if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(new String(bo));// + " " + new String(bo).length());
			p.waitFor();
		}
		if (System.currentTimeMillis() > 0)
		{
			java.io.FileOutputStream fos = new java.io.FileOutputStream(de.elbosso.util.Utilities.getTempDir()+"/console");
			java.io.PrintWriter pw = new java.io.PrintWriter(fos);
			pw.println("console");
			p = Runtime.getRuntime().exec(cmda);
			bo = new byte[100];
			p.getInputStream().read(bo);
			if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(new String(bo));// + " " + new String(bo).length());
			p.waitFor();
		}
		p = Runtime.getRuntime().exec(cmda);
		bo = new byte[100];
		p.getInputStream().read(bo);
		if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(new String(bo));// + " " + new String(bo).length());
		p.waitFor();

		while (true)
		{
			java.lang.Thread.currentThread().sleep(1000l);
		}
	}
}
