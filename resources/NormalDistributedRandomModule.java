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
WENN SIE AUF DIE MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND.
*/
package de.elbosso.dataflowframework.modules.generator.util.generator.generalpurpose;

@javax.annotation.Generated(value="de.elbosso.dataflowframework.processors.GeneratorProcessor", date="2018-01-21T08:48:00.213Z")
public class NormalDistributedRandomModule extends de.netsysit.dataflowframework.modules.BeanContextChildModuleBase
{
	static
	{
		de.netsysit.util.beans.InterfaceFactory.setSuperclassAssociationForEventDispatchThread(NormalDistributedRandomModule.class, de.netsysit.dataflowframework.modules.BeanContextChildModuleBase.class);
	}
	private java.lang.Number next;
	private de.elbosso.util.generator.generalpurpose.NormalDistributedRandom generator;

	public NormalDistributedRandomModule()
	{
		super();
		this.generator=new de.elbosso.util.generator.generalpurpose.NormalDistributedRandom();
	}
	@de.elbosso.dataflowframework.ui.annotations.AutoConnectAllowed
	public java.lang.Number getNext()
	{
		return next;
	}

	@de.elbosso.dataflowframework.ui.annotations.AutoConnectAllowed
	public void input(java.lang.Object trigger)
	{
		java.lang.Number old=getNext();
		try
		{
    		next=generator.next();
	    	send("next", old, getNext());
	    }
	    catch(java.lang.Throwable t)
	    {
	        error(null,t.getMessage());
	    }
	}
	public de.elbosso.util.generator.generalpurpose.NormalDistributedRandom getGenerator()
	{
		return generator;
	}

	public void setGenerator(de.elbosso.util.generator.generalpurpose.NormalDistributedRandom generator)
	{
		this.generator = generator;
	}
}

