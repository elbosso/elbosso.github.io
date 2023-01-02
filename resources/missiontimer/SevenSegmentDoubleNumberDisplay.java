package de.elbosso.ui.components;
/*
 
 Die untenstehende Lizenz berührt nicht die Rechte des Autors der zugrundeliegenden Software
 http://www.jug-muenster.de/swing-apollo-space-program-mission-timer-280/
 
Copyright (c) 2013-2023.

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

import java.awt.*;
import java.awt.image.BufferedImage;

public class SevenSegmentDoubleNumberDisplay extends NumberDisplay
{    
    private java.awt.image.BufferedImage BACKGROUND_IMAGE;
	private double factor;
	private java.awt.Dimension dim;
	private SevenSegment sevenSegment;
	private double value;
	private int fraclen;

@java.beans.ConstructorProperties({"len","fraclen","factor"})
    public SevenSegmentDoubleNumberDisplay(int len,int fraclen,double factor)
    {
		super(len+1);
		if(fraclen<1)
			throw new java.lang.IllegalArgumentException("fraclen must not be smaller than 1!");
		this.fraclen=fraclen;
		if(factor<0.6)
			throw new java.lang.IllegalArgumentException("factor must not be smaller than 0.6!");
		this.factor=factor;
		this.sevenSegment=new SevenSegment(factor);
		dim=new java.awt.Dimension((int)((38*2+(this.len+fraclen)*46)*factor), (int)(100*factor));
        setPreferredSize(dim);
        setMinimumSize(dim);
        setSize(dim);
		BACKGROUND_IMAGE = createBackground(dim.width,dim.height);
		switchOff();
//		value=-1;
   }

	public double getFactor()
	{
		return factor;
	}

	public int getFraclen()
	{
		return fraclen;
	}
	public void setValue(double value)
	{
		super.setValue(value);
		if(java.lang.Math.abs(value)>max)
		{
			switchOff();
			throw new java.lang.IllegalArgumentException("value must not be greater than "+max+"!");
		}
//		if(value<0)
//		{
//			switchOff();
//			throw new java.lang.IllegalArgumentException("value must not be smaller than 0!");
//		}
		this.value=value;
		repaint();
	}

    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        super.paintComponent(g);

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);

		if(isOpaque())
		{
			if((BACKGROUND_IMAGE.getWidth()!=getSize().width)||(BACKGROUND_IMAGE.getHeight()!=getSize().height))
				BACKGROUND_IMAGE=createBackground(getSize().width, getSize().height);
			g2.drawImage(BACKGROUND_IMAGE, 0, 0, this);
		}

		paintFractionalPart(g2);
		int minusstelle=paintIntPart(g2);
		if(isLeadingZeroes())
			minusstelle=0;
		else if(minusstelle<0)
			minusstelle=0;
		int off=(getSize().width-dim.width)/2+(int)(38*factor);
		int yoff=(getSize().height-dim.height)/2+(int)(dim.height-80*factor);
		for(int i=0;i<=minusstelle;++i)
		{
			if(i==minusstelle)
			{
				if(value<0)
					g2.drawImage(sevenSegment.get(11),off , yoff, this);
				else
					g2.drawImage(sevenSegment.get(10),off , yoff, this);
			}
			else
				g2.drawImage(sevenSegment.get(10),off , yoff, this);
			off+=(int)(46*factor);
		}

		g2.dispose();
	}
    protected int paintIntPart(java.awt.Graphics2D g2)
    {

		int stellen=(int)(java.lang.Math.log10(java.lang.Math.abs(value)))+1;
		if(stellen<0)
			stellen=1;
		java.lang.String number=Long.toString((long)java.lang.Math.abs(value));
		int off=(getSize().width-dim.width)/2+(int)(38*factor)+(int)(46*factor);
		int yoff=(getSize().height-dim.height)/2+(int)(dim.height-80*factor);
		int minusstelle=-1;
		for(int i=1;i<len;++i)
		{
			int stelle=i-(len-stellen);

			if(stelle<0)
			{
//				if(minusstelle<0)
					minusstelle=i;
				g2.drawImage(i==len-1?sevenSegment.getDotted(isLeadingZeroes()?0:10):sevenSegment.get(isLeadingZeroes()?0:10),off , yoff, this);
			}
			else
				g2.drawImage(i==len-1?sevenSegment.getDotted(Integer.parseInt(number.substring(stelle,stelle+1))):sevenSegment.get(Integer.parseInt(number.substring(stelle,stelle+1))),off ,yoff, this);
//			g2.drawImage(sevenSegment.get(0),off , (int)(90*factor), this);
			off+=(int)(46*factor);
		}
		return minusstelle;
    }
	private void paintFractionalPart(java.awt.Graphics2D g2)
	{
		double v=java.lang.Math.abs(value);
		double frac=v-(long)value;
		long fracl=(long)(frac*java.lang.Math.pow(10, fraclen));

		int stellen=(int)(java.lang.Math.log10(fracl))+1;
		if(stellen<0)
			stellen=1;
		java.lang.String number=Long.toString((long)fracl);
		int off=(getSize().width-dim.width)/2+(int)(38*factor)+len*(int)(46*factor);
		int yoff=(getSize().height-dim.height)/2+(int)(dim.height-80*factor);

		for(int i=len;i<len+fraclen;++i)
		{
			int stelle=i-len-(fraclen-stellen);

			if(stelle<0)
				g2.drawImage(sevenSegment.get(0),off , yoff, this);
			else
				g2.drawImage(sevenSegment.get(Integer.parseInt(number.substring(stelle,stelle+1))),off , yoff, this);
			off+=(int)(46*factor);
		}
		
	}

    private java.awt.image.BufferedImage createBackground(int width, int height)
    {
        java.awt.image.BufferedImage IMAGE = new java.awt.image.BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) IMAGE.getGraphics();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);

        final java.awt.geom.Point2D START_BACKGROUND = new java.awt.geom.Point2D.Float(0,0);
        final java.awt.geom.Point2D STOP_BACKGROUND = new java.awt.geom.Point2D.Float(0,height);

        final float[] FRACTIONS =
        {
            0.0f,
            1.0f
        };

        final java.awt.Color[] COLORS_BACKGROUND =
        {
            new java.awt.Color(0x253048),
            new java.awt.Color(0x182635)
        };

        final java.awt.geom.Point2D START_HIGHLIGHT = new java.awt.geom.Point2D.Float(0,(getSize().height-dim.height)/2+(int)(79*factor)-(int)(200*factor-dim.height));
        final java.awt.geom.Point2D STOP_HIGHLIGHT = new java.awt.geom.Point2D.Float(0,(getSize().height-dim.height)/2+(int)(166*factor)-(int)(200*factor-dim.height));

        final float[] FRACTIONS_HIGHLIGHT =
        {
            0.0f,
            0.9f,
            1.0f
        };

        final java.awt.Color[] COLORS_HIGHLIGHT =
        {
            new java.awt.Color(0x000000),
            new java.awt.Color(0x000000),
            new java.awt.Color(0x6C8095)
        };

        final java.awt.geom.Point2D START = new java.awt.geom.Point2D.Float(0,(getSize().height-dim.height)/2+(int)(dim.height-80*factor)+(int)(10*factor));
        final java.awt.geom.Point2D STOP = new java.awt.geom.Point2D.Float(0,(getSize().height-dim.height)/2+(int)(dim.height-80*factor)+(int)(95*factor));
        
        final java.awt.Color[] COLORS_RIGHT =
        {
            new java.awt.Color(0x384A69),
            new java.awt.Color(0x253144)
        };

        final java.awt.Color[] COLORS_LEFT =
        {
            new java.awt.Color(0x2F4566),
            new java.awt.Color(0x243D54)
        };


        final java.awt.LinearGradientPaint GRADIENT_BACKGROUND = new java.awt.LinearGradientPaint(START_BACKGROUND, STOP_BACKGROUND, FRACTIONS, COLORS_BACKGROUND);
        g2.setPaint(GRADIENT_BACKGROUND);
        g2.fillRect(0, 0, width, height);

		final java.awt.LinearGradientPaint GRADIENT_HIGHLIGHT = new java.awt.LinearGradientPaint(START_HIGHLIGHT, STOP_HIGHLIGHT, FRACTIONS_HIGHLIGHT, COLORS_HIGHLIGHT);
        g2.setPaint(GRADIENT_HIGHLIGHT);
        g2.fillRect((getSize().width-dim.width)/2+(int)(17*factor), (getSize().height-dim.height)/2+(int)(dim.height-100*factor)+(int)(9*factor), (int)(176*factor), (int)(87*factor));
        g2.fillRect((getSize().width-dim.width)/2+(int)(193*factor), (getSize().height-dim.height)/2+(int)(dim.height-100*factor)+(int)(9*factor), dim.width-(int)(211*factor), (int)(87*factor));

//        final java.awt.LinearGradientPaint GRADIENT_LEFT = new java.awt.LinearGradientPaint(START, STOP, FRACTIONS, COLORS_LEFT);
//        g2.setPaint(GRADIENT_LEFT);        
//        g2.fillRect((int)(18*factor), (int)(80*factor), (int)(175*factor), (int)(85*factor));

        final java.awt.LinearGradientPaint GRADIENT_RIGHT = new java.awt.LinearGradientPaint(START, STOP, FRACTIONS, COLORS_RIGHT);
        g2.setPaint(GRADIENT_RIGHT);
        g2.fillRect((getSize().width-dim.width)/2+(int)(18*factor), (getSize().height-dim.height)/2+(int)(dim.height-100*factor)+(int)(8*factor), dim.width-(int)(37*factor), (int)(85*factor));

        g2.dispose();

        return IMAGE;
    }

//    @Override
//    public java.awt.Dimension getSize()
//    {
//        return dim;
//    }

	public void switchOff()
	{
		value=-1;
		repaint();
	}

	public void setColor(java.awt.Color c)
	{
		sevenSegment.setColor(c);
		BACKGROUND_IMAGE = createBackground(dim.width,dim.height);
	}
	public java.awt.Color getColor()
	{
		return sevenSegment.getColor();
	}

	public static void main(java.lang.String[] args) throws InterruptedException
	{
		java.util.Random rand=new java.util.Random(java.time.Clock.systemDefaultZone().millis());
		javax.swing.JFrame f=new javax.swing.JFrame();
		SevenSegmentDoubleNumberDisplay nixieNumberDisplay=new SevenSegmentDoubleNumberDisplay(5,3, .7f);
		nixieNumberDisplay.setBackground(java.awt.Color.DARK_GRAY);
		nixieNumberDisplay.setOpaque(true);
		nixieNumberDisplay.setLeadingZeroes(false);
		f.getContentPane().add(nixieNumberDisplay);
		nixieNumberDisplay.setValue(-1230);
		nixieNumberDisplay.setColor(Color.PINK);
		de.netsysit.util.lang.MiniMax miniMax=new de.netsysit.util.lang.MiniMax(-Double.MAX_VALUE,0);
		nixieNumberDisplay.register(Color.WHITE,miniMax);
		miniMax=new de.netsysit.util.lang.MiniMax(0,6000);
		nixieNumberDisplay.register(Color.GREEN,miniMax);
		miniMax=new de.netsysit.util.lang.MiniMax(6000,10000);
		nixieNumberDisplay.register(Color.YELLOW,miniMax);
		miniMax=new de.netsysit.util.lang.MiniMax(10000, Double.MAX_VALUE);
		nixieNumberDisplay.register(Color.RED,miniMax);

		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.pack();
		f.setLocation(0,0);
		f.setVisible(true);
		int i=0;
		while(true)
		{
			try{
			nixieNumberDisplay.setValue(rand.nextDouble()*24000-12000);
			}catch(java.lang.IllegalArgumentException exp){}
			java.lang.Thread.currentThread().sleep(400l);
		}
	}
}
