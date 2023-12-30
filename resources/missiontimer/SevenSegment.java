package de.elbosso.ui.components;

import java.awt.Image;
import java.awt.image.BufferedImage;

/*
 
 Die untenstehende Lizenz berührt nicht die Rechte des Autors der zugrundeliegenden Software
 http://www.jug-muenster.de/swing-apollo-space-program-mission-timer-280/

Copyright (c) 2013-2024.

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
public class SevenSegment extends java.lang.Object 
{
	private final static java.awt.Color SEVENSEGMENT_COLOR=new java.awt.Color(0xEEFFEE);
	private java.awt.Color COLOR_ON ;
	private java.awt.Color FRAME_COLOR_ON = new java.awt.Color(50, 200, 100, 128);
	private static java.awt.Color COLOR_OFF = new java.awt.Color(0x4E5571);
	private static java.awt.Color FRAME_COLOR_OFF = new java.awt.Color(61, 57, 142, 128);
	private static java.awt.BasicStroke FRAME_STROKE = new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND);
    protected java.awt.image.BufferedImage[] DIGIT_ARRAY;
    protected java.awt.image.BufferedImage[] SMALL_DIGIT_ARRAY;
    protected java.awt.image.BufferedImage[] DOTTED_DIGIT_ARRAY;
    protected java.awt.image.BufferedImage[] SMALL_DOTTED_DIGIT_ARRAY;
    private double factor;

	public SevenSegment(double factor)
	{
		this(SEVENSEGMENT_COLOR,factor);
	}
	protected SevenSegment(java.awt.Color c,double factor)
	{
		super();
		this.factor=factor;
		setColor(c);
	}
	public void setColor(java.awt.Color c)
	{
		if(c==null)
			c=SEVENSEGMENT_COLOR;
		if(COLOR_ON!=c)
		{
			this.COLOR_ON = c;
			this.FRAME_COLOR_ON=COLOR_ON.darker();
			DIGIT_ARRAY = new java.awt.image.BufferedImage[]
					{
							createDigit(0, factor, false),
							createDigit(1, factor, false),
							createDigit(2, factor, false),
							createDigit(3, factor, false),
							createDigit(4, factor, false),
							createDigit(5, factor, false),
							createDigit(6, factor, false),
							createDigit(7, factor, false),
							createDigit(8, factor, false),
							createDigit(9, factor, false),
							createDigit(10, factor, false),
							createDigit(11, factor, false),
							createDigit(12, factor, false),
							createDigit(13, factor, false),
							createDigit(14, factor, false),
					};
			DOTTED_DIGIT_ARRAY = new java.awt.image.BufferedImage[]
					{
							createDigit(0, factor, true),
							createDigit(1, factor, true),
							createDigit(2, factor, true),
							createDigit(3, factor, true),
							createDigit(4, factor, true),
							createDigit(5, factor, true),
							createDigit(6, factor, true),
							createDigit(7, factor, true),
							createDigit(8, factor, true),
							createDigit(9, factor, true),
							createDigit(10, factor, true),
							createDigit(11, factor, true),
							createDigit(12, factor, true),
							createDigit(13, factor, true),
							createDigit(14, factor, true),
					};
			SMALL_DIGIT_ARRAY = new java.awt.image.BufferedImage[]
					{
							createDigit(0, factor / 1.4, false),
							createDigit(1, factor / 1.4, false),
							createDigit(2, factor / 1.4, false),
							createDigit(3, factor / 1.4, false),
							createDigit(4, factor / 1.4, false),
							createDigit(5, factor / 1.4, false),
							createDigit(6, factor / 1.4, false),
							createDigit(7, factor / 1.4, false),
							createDigit(8, factor / 1.4, false),
							createDigit(9, factor / 1.4, false),
							createDigit(10, factor / 1.4, false),
							createDigit(11, factor / 1.4, false),
							createDigit(12, factor / 1.4, false),
							createDigit(13, factor / 1.4, false),
							createDigit(14, factor / 1.4, false),
					};
			SMALL_DOTTED_DIGIT_ARRAY = new java.awt.image.BufferedImage[]
					{
							createDigit(0, factor / 1.4, true),
							createDigit(1, factor / 1.4, true),
							createDigit(2, factor / 1.4, true),
							createDigit(3, factor / 1.4, true),
							createDigit(4, factor / 1.4, true),
							createDigit(5, factor / 1.4, true),
							createDigit(6, factor / 1.4, true),
							createDigit(7, factor / 1.4, true),
							createDigit(8, factor / 1.4, true),
							createDigit(9, factor / 1.4, true),
							createDigit(10, factor / 1.4, true),
							createDigit(11, factor / 1.4, true),
							createDigit(12, factor / 1.4, true),
							createDigit(13, factor / 1.4, true),
							createDigit(14, factor / 1.4, true),
					};
		}
	}
	public java.awt.Color getColor()
	{
		return COLOR_ON;
	}

	Image get(int index)
	{
		return DIGIT_ARRAY[index];
	}

	Image getDotted(int index)
	{
		return DOTTED_DIGIT_ARRAY[index];
	}

	Image getSmall(int index)
	{
		return SMALL_DIGIT_ARRAY[index];
	}

	Image getSmallDotted(int index)
	{
		return SMALL_DOTTED_DIGIT_ARRAY[index];
	}

    private java.awt.image.BufferedImage createDigit(int digit,double fac,boolean dotted)
    {
        java.awt.image.BufferedImage IMAGE = new java.awt.image.BufferedImage((int)(46*fac), (int)(65*fac), BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) IMAGE.getGraphics();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);


        // A
        java.awt.geom.GeneralPath segment_a = new java.awt.geom.GeneralPath();
        segment_a.moveTo((int)(17*fac), 0);
        segment_a.lineTo((int)(38*fac), 0);
        segment_a.lineTo((int)(37*fac), (int)(8*fac));
        segment_a.lineTo((int)(16*fac), (int)(8*fac));
        segment_a.closePath();

        // B
        java.awt.geom.GeneralPath segment_b = new java.awt.geom.GeneralPath();
        segment_b.moveTo((int)(39*fac), 0);
        segment_b.lineTo((int)(41*fac), 0);
        segment_b.quadTo((int)(45*fac), 0, (int)(45*fac), (int)(5*fac));
        segment_b.lineTo((int)(41*fac), (int)(32*fac));
        segment_b.lineTo((int)(38*fac), (int)(32*fac));
        segment_b.lineTo((int)(35*fac), (int)(28*fac));
        segment_b.closePath();

        // C
        java.awt.geom.GeneralPath segment_c = new java.awt.geom.GeneralPath();
        segment_c.moveTo((int)(37*fac), (int)(33*fac));
        segment_c.lineTo((int)(41*fac), (int)(33*fac));
        segment_c.lineTo((int)(37*fac), (int)(60*fac));
        segment_c.quadTo((int)(36*fac), (int)(65*fac),(int)( 32*fac), (int)(65*fac));
        segment_c.lineTo((int)(30*fac), (int)(65*fac));
        segment_c.lineTo((int)(34*fac), (int)(37*fac));
        segment_c.closePath();

        // D
        java.awt.geom.GeneralPath segment_d = new java.awt.geom.GeneralPath();
        segment_d.moveTo((int)(9*fac), (int)(57*fac));
        segment_d.lineTo((int)(30*fac), (int)(57*fac));
        segment_d.lineTo((int)(29*fac), (int)(65*fac));
        segment_d.lineTo((int)(8*fac), (int)(65*fac));
        segment_d.closePath();

        // E
        java.awt.geom.GeneralPath segment_e = new java.awt.geom.GeneralPath();
        segment_e.moveTo((int)(4*fac), (int)(33*fac));
        segment_e.lineTo((int)(8*fac), (int)(33*fac));
        segment_e.lineTo((int)(11*fac), (int)(37*fac));
        segment_e.lineTo((int)(7*fac), (int)(65*fac));
        segment_e.lineTo((int)(4*fac), (int)(65*fac));
        segment_e.quadTo(0, (int)(65*fac), 0, (int)(60*fac));
        segment_e.closePath();

        // F
        java.awt.geom.GeneralPath segment_f = new java.awt.geom.GeneralPath();
        segment_f.moveTo((int)(8*fac), (int)(5*fac));
        segment_f.quadTo((int)(8*fac), 0, (int)(13*fac), 0);
        segment_f.lineTo((int)(16*fac), 0);
        segment_f.lineTo((int)(12*fac), (int)(28*fac));
        segment_f.lineTo((int)(8*fac), (int)(32*fac));
        segment_f.lineTo((int)(4*fac), (int)(32*fac));
        segment_f.closePath();

        // G
        java.awt.geom.GeneralPath segment_g = new java.awt.geom.GeneralPath();
        segment_g.moveTo((int)(14*fac), (int)(29*fac));
        segment_g.lineTo((int)(34*fac), (int)(29*fac));
        segment_g.lineTo((int)(36*fac), (int)(33*fac));
        segment_g.lineTo((int)(32*fac), (int)(37*fac));
        segment_g.lineTo((int)(13*fac), (int)(37*fac));
        segment_g.lineTo((int)(9*fac), (int)(33*fac));
        segment_g.closePath();

        // H
        java.awt.geom.GeneralPath segment_h = new java.awt.geom.GeneralPath();
		segment_h.append(new java.awt.geom.Ellipse2D.Double(38*fac, 57*fac, 7*fac, 7*fac),false);
        g2.setStroke(FRAME_STROKE);

        switch (digit)
        {
            case 0:
                g2.setColor(COLOR_ON);
                g2.fill(segment_a);
                g2.fill(segment_b);
                g2.fill(segment_c);
                g2.fill(segment_d);
                g2.fill(segment_e);
                g2.fill(segment_f);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
                g2.fill(segment_g);              
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_a);
                g2.draw(segment_b);
                g2.draw(segment_c);
                g2.draw(segment_d);
                g2.draw(segment_e);
                g2.draw(segment_f);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_g);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
            case 1:
                g2.setColor(COLOR_ON);
                g2.fill(segment_b);
                g2.fill(segment_c);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
                g2.fill(segment_a);
                g2.fill(segment_d);
                g2.fill(segment_e);
                g2.fill(segment_f);
                g2.fill(segment_g);
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_b);
                g2.draw(segment_c);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_a);
                g2.draw(segment_d);
                g2.draw(segment_e);
                g2.draw(segment_f);
                g2.draw(segment_g);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
            case 2:
                g2.setColor(COLOR_ON);
                g2.fill(segment_a);
                g2.fill(segment_b);
                g2.fill(segment_d);
                g2.fill(segment_e);
                g2.fill(segment_g);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
                g2.fill(segment_c);
                g2.fill(segment_f);
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_a);
                g2.draw(segment_b);
                g2.draw(segment_d);
                g2.draw(segment_e);
                g2.draw(segment_g);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_c);
                g2.draw(segment_f);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
            case 3:
                g2.setColor(COLOR_ON);
                g2.fill(segment_a);
                g2.fill(segment_b);
                g2.fill(segment_c);
                g2.fill(segment_d);
                g2.fill(segment_g);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
                g2.fill(segment_e);
                g2.fill(segment_f);
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_a);
                g2.draw(segment_b);
                g2.draw(segment_c);
                g2.draw(segment_d);
                g2.draw(segment_g);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_e);
                g2.draw(segment_f);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
            case 4:
                g2.setColor(COLOR_ON);
                g2.fill(segment_b);
                g2.fill(segment_c);
                g2.fill(segment_f);
                g2.fill(segment_g);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
                g2.fill(segment_a);
                g2.fill(segment_d);
                g2.fill(segment_e);
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_b);
                g2.draw(segment_c);
                g2.draw(segment_f);
                g2.draw(segment_g);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_a);
                g2.draw(segment_d);
                g2.draw(segment_e);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
            case 5:
                g2.setColor(COLOR_ON);
                g2.fill(segment_a);
                g2.fill(segment_c);
                g2.fill(segment_d);
                g2.fill(segment_f);
                g2.fill(segment_g);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
                g2.fill(segment_b);
                g2.fill(segment_e);
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_a);
                g2.draw(segment_c);
                g2.draw(segment_d);
                g2.draw(segment_f);
                g2.draw(segment_g);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_b);
                g2.draw(segment_e);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
            case 6:
                g2.setColor(COLOR_ON);
                g2.fill(segment_a);
                g2.fill(segment_c);
                g2.fill(segment_d);
                g2.fill(segment_e);
                g2.fill(segment_f);
                g2.fill(segment_g);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
                g2.fill(segment_b);
    				if(dotted==false)
	            g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_a);
                g2.draw(segment_c);
                g2.draw(segment_d);
                g2.draw(segment_e);
                g2.draw(segment_f);
                g2.draw(segment_g);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_b);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
            case 7:
                g2.setColor(COLOR_ON);
                g2.fill(segment_a);
                g2.fill(segment_b);
                g2.fill(segment_c);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
                g2.fill(segment_d);
                g2.fill(segment_e);
                g2.fill(segment_f);
                g2.fill(segment_g);
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_a);
                g2.draw(segment_b);
                g2.draw(segment_c);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_d);
                g2.draw(segment_e);
                g2.draw(segment_f);
                g2.draw(segment_g);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
            case 8:
                g2.setColor(COLOR_ON);
                g2.fill(segment_a);
                g2.fill(segment_b);
                g2.fill(segment_c);
                g2.fill(segment_d);
                g2.fill(segment_e);
                g2.fill(segment_f);
                g2.fill(segment_g);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_a);
                g2.draw(segment_b);
                g2.draw(segment_c);
                g2.draw(segment_d);
                g2.draw(segment_e);
                g2.draw(segment_f);
                g2.draw(segment_g);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
            case 9:
                g2.setColor(COLOR_ON);
                g2.fill(segment_a);
                g2.fill(segment_b);
                g2.fill(segment_c);
                g2.fill(segment_d);
                g2.fill(segment_f);
                g2.fill(segment_g);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
                g2.fill(segment_e);
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_a);
                g2.draw(segment_b);
                g2.draw(segment_c);
                g2.draw(segment_d);
                g2.draw(segment_f);
                g2.draw(segment_g);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_e);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
			case 10:
                g2.setColor(COLOR_OFF);
                g2.fill(segment_a);
                g2.fill(segment_b);
                g2.fill(segment_c);
                g2.fill(segment_d);
                g2.fill(segment_e);
                g2.fill(segment_f);
                g2.fill(segment_g);
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_a);
                g2.draw(segment_b);
                g2.draw(segment_c);
                g2.draw(segment_d);
                g2.draw(segment_e);
                g2.draw(segment_f);
                g2.draw(segment_g);
				if(dotted==false)
	                g2.draw(segment_h);
               break;
			case 14:
                g2.setColor(COLOR_ON);
                g2.fill(segment_a);
                g2.fill(segment_e);
                g2.fill(segment_f);
                g2.fill(segment_d);
                g2.fill(segment_g);
 				if(dotted==true)
	                g2.fill(segment_h);                
                g2.setColor(COLOR_OFF);
                g2.fill(segment_b);
                g2.fill(segment_c);
				if(dotted==false)
	                g2.fill(segment_h);                
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_a);
                g2.draw(segment_e);
                g2.draw(segment_f);
                g2.draw(segment_d);
                g2.draw(segment_g);
				if(dotted==true)
	                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_b);
                g2.draw(segment_c);
				if(dotted==false)
	                g2.draw(segment_h);
                break;
			default:
                g2.setColor(COLOR_OFF);
                g2.fill(segment_a);
                g2.fill(segment_b);
                g2.fill(segment_c);
                g2.fill(segment_d);
                g2.fill(segment_e);
                g2.fill(segment_f);
                g2.fill(segment_h);
                g2.setColor(COLOR_ON);
                g2.fill(segment_g);                
                g2.setColor(FRAME_COLOR_OFF);
                g2.draw(segment_a);
                g2.draw(segment_b);
                g2.draw(segment_c);
                g2.draw(segment_d);
                g2.draw(segment_e);
                g2.draw(segment_f);
                g2.draw(segment_h);
                g2.setColor(FRAME_COLOR_ON);
                g2.draw(segment_g);
        }

        g2.dispose();

        return IMAGE;
    }
	
}
