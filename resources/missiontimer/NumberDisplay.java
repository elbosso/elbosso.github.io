package de.elbosso.ui.components;
/*
Copyright (c) 2013-2022.

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
public abstract class NumberDisplay extends javax.swing.JPanel implements
	de.elbosso.ui.DisplayNumericalValues
{
 	protected double factor;
	protected int len;
	protected final long max;
	protected boolean leadingZeroes;
	private double value;
	private java.util.Map<java.awt.Color,de.netsysit.util.lang.Tupel<de.netsysit.util.lang.MiniMax,java.awt.Color> > colorMap;

    public NumberDisplay(int len)
    {
		super();
		if(len<1)
			throw new java.lang.IllegalArgumentException("len must not be smaller than 1!");
		this.len=len;
 		max=(long)(java.lang.Math.pow(10, len))-1;
		colorMap=new java.util.HashMap();
   }
	
	public abstract void switchOff();
	
	public void setValue(double value)
	{
		if(colorMap.isEmpty()==false)
		{
			java.awt.Color latch=getColor();
			java.awt.Color c=latch;
			for(de.netsysit.util.lang.Tupel<de.netsysit.util.lang.MiniMax,java.awt.Color> tupel:colorMap.values())
			{
				if(tupel.getLefty().contains(value))
				{
					c=tupel.getRighty();
					break;
				}
			}
			if(c!=latch)
			{
				setColor(c);
			}
		}
		this.value=value;
	}

	public double getValue()
	{
		return value;
	}

	public boolean isLeadingZeroes()
	{
		return leadingZeroes;
	}

	public void setLeadingZeroes(boolean leadingZeroes)
	{
		this.leadingZeroes = leadingZeroes;
	}

	public int getLen()
	{
		return len;
	}

	public void register(java.awt.Color color,de.netsysit.util.lang.MiniMax miniMax)
	{
		colorMap.put(color,new de.netsysit.util.lang.Tupel(miniMax,color));
	}
	public void unregister(java.awt.Color c)
	{
		colorMap.remove(c);
	}
	public abstract void setColor(java.awt.Color c);
	public abstract java.awt.Color getColor();
}
