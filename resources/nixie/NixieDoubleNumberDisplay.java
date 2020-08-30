package de.elbosso.ui.components;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/*
Copyright (c) 2013-2020.

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
public class NixieDoubleNumberDisplay extends NumberDisplay 
{
	private NixieNumber[] nixieNumbers;
	private NixieSymbol decimalPoint;
	private NixieSymbol sign;
	private int fraclen;
	private java.awt.GridBagConstraints constraints;
	private java.awt.GridBagLayout gridbag;
	private float factor;

@java.beans.ConstructorProperties({"len","fraclen","factor"})
	public NixieDoubleNumberDisplay(int len,int fraclen,float factor)
	{
		this(NixieTube.NIXIE_ORANGE,len,fraclen,factor);
	}
	public NixieDoubleNumberDisplay(java.awt.Color c,int len,int fraclen,float factor)
	{
		super(len);
		this.factor=factor;
		if(fraclen<1)
			throw new java.lang.IllegalArgumentException("fraclen must not be smaller than 1!");
		this.fraclen=fraclen;
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
		int w=0;
		int h=0;
		if(factor<0.1)
			throw new java.lang.IllegalArgumentException("factor must not be smaller than 0.1!");
		nixieNumbers=new NixieNumber[len+fraclen+1];
		sign=new NixieSymbol(c,factor);
		w+=sign.getPreferredSize().width;
		h=h>sign.getPreferredSize().height?h:sign.getPreferredSize().height;
		gridbag.addLayoutComponent(sign, constraints);
		add(sign);
		constraints.gridx=constraints.gridx+1;
		for(int i=0;i<len;++i)
		{
			nixieNumbers[i]=new NixieNumber(c,factor);
			w+=nixieNumbers[i].getPreferredSize().width;
			h=h>nixieNumbers[i].getPreferredSize().height?h:nixieNumbers[i].getPreferredSize().height;
			gridbag.addLayoutComponent(nixieNumbers[i], constraints);
			add(nixieNumbers[i]);
			constraints.gridx=constraints.gridx+1;
		}
		decimalPoint=new NixieSymbol(c,factor);
		w+=decimalPoint.getPreferredSize().width;
		h=h>decimalPoint.getPreferredSize().height?h:decimalPoint.getPreferredSize().height;
		gridbag.addLayoutComponent(decimalPoint, constraints);
		add(decimalPoint);
		constraints.gridx=constraints.gridx+1;
		for(int i=len+1;i<len+fraclen+1;++i)
		{
			nixieNumbers[i]=new NixieNumber(c,factor);
			w+=nixieNumbers[i].getPreferredSize().width;
			h=h>nixieNumbers[i].getPreferredSize().height?h:nixieNumbers[i].getPreferredSize().height;
			gridbag.addLayoutComponent(nixieNumbers[i], constraints);
			add(nixieNumbers[i]);
			constraints.gridx=constraints.gridx+1;
		}
		constraints.fill=constraints.BOTH;
		constraints.weightx=1;
		l=new javax.swing.JLabel();
		gridbag.addLayoutComponent(l, constraints);
		add(l);
//		System.out.println("§ "+w+" "+h);
		java.awt.Dimension dim=new java.awt.Dimension(w,h);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setSize(dim);
	}
	public void setValue(double value)
	{
		super.setValue(value);

		setFractionalPart(value);
		setDecPoint();
		setIntPart(value);
	}
	public void setIntPart(double value)
	{
		if(java.lang.Math.abs(value)>max)
		{
			switchOff();
			throw new java.lang.IllegalArgumentException("value must not be greater than "+max+"!");
		}
		if(value<0)
		{
			sign.setSymbol(1);
		}
		else
		{
			sign.setSymbol(-1);
		}
		int stellen=(int)(java.lang.Math.log10(java.lang.Math.abs(value)))+1;
		if(stellen<0)
			stellen=1;
		java.lang.String number=Long.toString((long)java.lang.Math.abs(value));
		for(int i=0;i<len;++i)
		{
			int stelle=i-(len-stellen);

			if(stelle<0)
				nixieNumbers[i].setNumber(isLeadingZeroes()?0:-1);
			else
				nixieNumbers[i].setNumber(Integer.parseInt(number.substring(stelle,stelle+1)));
		}
	}
	private void setDecPoint()
	{
		decimalPoint.setSymbol(0);
	}

	private void setFractionalPart(double value)
	{
		double v=java.lang.Math.abs(value);
		double frac=v-(long)value;
		long fracl=(long)(frac*java.lang.Math.pow(10, fraclen));

		int stellen=(int)(java.lang.Math.log10(fracl))+1;
		if(stellen<0)
			stellen=1;
		java.lang.String number=Long.toString((long)fracl);
		for(int i=len+1;i<len+fraclen+1;++i)
		{
			int stelle=i-len-1-(fraclen-stellen);

			if(stelle<0)
				nixieNumbers[i].setNumber(0);
			else
				nixieNumbers[i].setNumber(Integer.parseInt(number.substring(stelle,stelle+1)));
		}
		
	}
	public static void main(java.lang.String[] args) throws InterruptedException, IOException
	{
		java.util.Random rand=new java.util.Random(System.currentTimeMillis());
		javax.swing.JFrame f=new javax.swing.JFrame();
		de.netsysit.util.generator.Sequence<java.awt.Color> seq=new de.elbosso.util.generator.generalpurpose.RandomColor();
		javax.swing.JPanel p=new javax.swing.JPanel(new java.awt.GridLayout(0,1));
		for(int i=0;i<7;++i)
		{
			NixieDoubleNumberDisplay nixieNumberDisplay=new NixieDoubleNumberDisplay(seq.next(),5,3, .62f);
			nixieNumberDisplay.setBackground(java.awt.Color.DARK_GRAY);
			nixieNumberDisplay.setOpaque(true);
			nixieNumberDisplay.setLeadingZeroes(false);
			p.add(nixieNumberDisplay);
			double v=rand.nextDouble()*24000-12000;
			nixieNumberDisplay.setValue(v);
//			System.out.println("\" "+nixieNumberDisplay.getPreferredSize());
		}
		NixieDoubleNumberDisplay nixieNumberDisplay=new NixieDoubleNumberDisplay(Color.WHITE,5,3, .62f);
		nixieNumberDisplay.setBackground(java.awt.Color.DARK_GRAY);
		nixieNumberDisplay.setOpaque(true);
		nixieNumberDisplay.setLeadingZeroes(false);
		de.netsysit.util.lang.MiniMax miniMax=new de.netsysit.util.lang.MiniMax(-Double.MAX_VALUE,0);
		nixieNumberDisplay.register(Color.WHITE,miniMax);
		miniMax=new de.netsysit.util.lang.MiniMax(0,6000);
		nixieNumberDisplay.register(Color.GREEN,miniMax);
		miniMax=new de.netsysit.util.lang.MiniMax(6000,10000);
		nixieNumberDisplay.register(Color.YELLOW,miniMax);
		miniMax=new de.netsysit.util.lang.MiniMax(10000, Double.MAX_VALUE);
		nixieNumberDisplay.register(Color.RED,miniMax);
			java.awt.Dimension dim=nixieNumberDisplay.getPreferredSize();
			System.out.println(dim);
			//if we wouldnt do that -width and height of nnd would be 0 and that would mean that JComponent doesnt even start to paint anything
			nixieNumberDisplay.setSize(dim);
		nixieNumberDisplay.invalidate();
		nixieNumberDisplay.validate();
		nixieNumberDisplay.doLayout();
				double v=rand.nextDouble()*24000-12000;
			nixieNumberDisplay.setValue(v);
			java.awt.image.BufferedImage bufferedImage=new java.awt.image.BufferedImage(dim.width,dim.height, BufferedImage.TYPE_INT_ARGB);
			java.awt.Graphics g=bufferedImage.createGraphics();
			nixieNumberDisplay.paint(g);
			g.dispose();
			javax.imageio.ImageIO.write(bufferedImage,"png",new java.io.File(de.elbosso.util.Utilities.getTempDir()+"/nnd.png"));

		p.add(nixieNumberDisplay);
		f.setContentPane(p);
		nixieNumberDisplay.setValue(0);
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.pack();
		f.setLocation(0,0);
		f.setVisible(true);
		int i=0;
		while(true)
		{
			try{
				v=rand.nextDouble()*24000-12000;
			nixieNumberDisplay.setValue(v);
			}catch(java.lang.IllegalArgumentException exp){exp.printStackTrace();}
			java.lang.Thread.currentThread().sleep(300l);
		}			
	}

	public void switchOff()
	{
		for(int i=0;i<len+fraclen+1;++i)
		{
			nixieNumbers[i].setNumber(-1);
		}
		decimalPoint.setSymbol(-1);
		sign.setSymbol(-1);
	}

	public void setColor(java.awt.Color c)
	{
		for(NixieTube nixieTube:nixieNumbers)
		{
			if(nixieTube!=null)
				nixieTube.setColor(c);
		}
		sign.setColor(c);
		decimalPoint.setColor(c);
	}
	public java.awt.Color getColor()
	{
		return ((nixieNumbers!=null)&&(nixieNumbers[0]!=null))?nixieNumbers[0].getColor():null;
	}
}
