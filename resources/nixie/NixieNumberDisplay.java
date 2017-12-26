package de.elbosso.ui.components;
/*
Copyright (c) 2013-2018.

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
public class NixieNumberDisplay extends NumberDisplay
{
	private NixieNumber[] nixieNumbers;
	private java.awt.GridBagConstraints constraints;
	private java.awt.GridBagLayout gridbag;
	private float factor;

@java.beans.ConstructorProperties({"len","factor"})
	public NixieNumberDisplay(int len,float factor)
	{
		this(NixieTube.NIXIE_ORANGE,len,factor);
	}
	public NixieNumberDisplay(java.awt.Color c,int len,float factor)
	{
		super(len);
		this.factor=factor;
		gridbag=new java.awt.GridBagLayout();
		constraints=new java.awt.GridBagConstraints();
		constraints.anchor=constraints.CENTER;
		constraints.fill=constraints.BOTH;
		constraints.gridheight=1;
		constraints.gridwidth=1;
		constraints.gridx=0;
		constraints.gridy=0;
		constraints.weightx=1;
		constraints.weighty=1;
		setLayout(gridbag);
		javax.swing.JLabel l=new javax.swing.JLabel();
		gridbag.addLayoutComponent(l, constraints);
		add(l);
		constraints.gridx=constraints.gridx+1;
		constraints.fill=constraints.NONE;
		constraints.weightx=0;
		if(factor<0.1)
			throw new java.lang.IllegalArgumentException("factor must not be smaller than 0.1!");
		nixieNumbers=new NixieNumber[len];
		for(int i=0;i<len;++i)
		{
			nixieNumbers[i]=new NixieNumber(c,factor);
			gridbag.addLayoutComponent(nixieNumbers[i], constraints);
			add(nixieNumbers[i]);
			constraints.gridx=constraints.gridx+1;
		}
		constraints.fill=constraints.BOTH;
		constraints.weightx=1;
		l=new javax.swing.JLabel();
		gridbag.addLayoutComponent(l, constraints);
		add(l);
	}

	public float getFactor()
	{
		return factor;
	}
	public void setValue(double value)
	{
		super.setValue(value);
		if(value>max)
		{
			switchOff();
			throw new java.lang.IllegalArgumentException("value must not be greater than "+max+"!");
		}
		if(value<0)
		{
			switchOff();
			throw new java.lang.IllegalArgumentException("value must not be smaller than 0!");
		}
		int stellen=(int)(java.lang.Math.log10(value))+1;
		if(stellen<0)
			stellen=1;
		java.lang.String number=Long.toString((long)value);
		for(int i=0;i<len;++i)
		{
			int stelle=i-(len-stellen);

			if(stelle<0)
				nixieNumbers[i].setNumber(isLeadingZeroes()?0:-1);
			else
				nixieNumbers[i].setNumber(Integer.parseInt(number.substring(stelle,stelle+1)));
		}
	}
	public static void main(java.lang.String[] args) throws InterruptedException
	{
		java.util.Random rand=new java.util.Random(System.currentTimeMillis());
		javax.swing.JFrame f=new javax.swing.JFrame();
		NixieNumberDisplay nixieNumberDisplay=new NixieNumberDisplay(5, .62f);
		nixieNumberDisplay.setBackground(java.awt.Color.DARK_GRAY);
		nixieNumberDisplay.setOpaque(true);
		nixieNumberDisplay.setLeadingZeroes(true);
		f.getContentPane().add(nixieNumberDisplay);
		nixieNumberDisplay.setValue(0);
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
		int i=0;
		while(true)
		{
			try{
			nixieNumberDisplay.setValue(++i);//rand.nextInt(120000));
			}catch(java.lang.IllegalArgumentException exp){}
			java.lang.Thread.currentThread().sleep(3l);
		}			
	}

	public void switchOff()
	{
		for(int i=0;i<len;++i)
		{
			nixieNumbers[i].setNumber(-1);
		}
	}
	public void setColor(java.awt.Color c)
	{
		for(NixieTube nixieTube:nixieNumbers)
		{
			if(nixieTube!=null)
				nixieTube.setColor(c);
		}
	}
	public java.awt.Color getColor()
	{
		return ((nixieNumbers!=null)&&(nixieNumbers[0]!=null))?nixieNumbers[0].getColor():null;
	}
}
