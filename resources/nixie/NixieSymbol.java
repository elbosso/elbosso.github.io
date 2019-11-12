package de.elbosso.ui.components;

import java.awt.image.BufferedImage;

/*
 
 Die untenstehende Lizenz berührt nicht die Rechte des Autors der zugrundeliegenden Software
 http://www.jug-muenster.de/swing-nixieclock-321/
  
Copyright (c) 2013-2019.

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
public class NixieSymbol extends NixieTube
{
    private int symbol;        
    private java.awt.image.BufferedImage[] symbolStack = new java.awt.image.BufferedImage[11];

    private final java.awt.geom.Rectangle2D GLOW_BOX ;


    private final java.awt.image.BufferedImage[] INACTIVE_SYMBOL_ARRAY;

    private java.awt.image.BufferedImage[] ACTIVE_SYMBOL_ARRAY;
	
	private double factor;

	public NixieSymbol()
    {
        this(1.0f);
    }
	public NixieSymbol(java.awt.Color c)
    {
        this(c,1.0f);
    }
	public NixieSymbol(float factor)
    {
        this(NIXIE_ORANGE,factor);
    }
    public NixieSymbol(java.awt.Color c,float factor)
    {
        super(c);
		this.factor=factor;
        this.symbol = -1;
        this.setPreferredSize(new java.awt.Dimension((int)(86*factor), (int)(146*factor)));
        this.setSize(new java.awt.Dimension((int)(86*factor), (int)(146*factor)));
		GLOW_BOX = new java.awt.geom.Rectangle2D.Float((int)(13*factor), (int)(47*factor), (int)(60*factor), (int)(86*factor));
		ACTIVE_SYMBOL_ARRAY = new java.awt.image.BufferedImage[11];
		recreateActiveSymbols();
		INACTIVE_SYMBOL_ARRAY =new java.awt.image.BufferedImage[11];
 		for(int i=0;i<2;++i)
			INACTIVE_SYMBOL_ARRAY[i]=createSymbol(i, false);
        init();
    }
    protected void recreateActiveSymbols()
    {
        if(ACTIVE_SYMBOL_ARRAY!=null)
 		{
			java.awt.image.BufferedImage[] cached=getCached(getColor());
			if(cached==null)
			{
			    ACTIVE_SYMBOL_ARRAY = new java.awt.image.BufferedImage[11];
                for(int i=0;i<2;++i)
                    ACTIVE_SYMBOL_ARRAY[i]=createSymbol(i, true);
				cache(ACTIVE_SYMBOL_ARRAY);
			}
			else
			{
				ACTIVE_SYMBOL_ARRAY=cached;
			}
		}
    }
    private void init()
    {
        setOpaque(false);
        createSymbolStack();
        repaint();        
    }


    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        super.paintComponent(g);

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();

        // Backside glow effect
        if (symbol > -1)
        {
            g2.setPaint(GLOW_GRADIENT);
            g2.fill(GLOW_BOX);
        }

        // Draw nixie number
        for (int i = 0 ; i < 11 ; i++)
        {
            g2.drawImage(this.symbolStack[i], (int)(12*factor), (int)(42*factor), null);
        }

        // Draw hatch
        g2.drawImage(createMiddleImage(), 0, 0, null);

        // Draw tube
        g2.drawImage(createBackgroundImage(), 0, 0, null);

        // Draw light effects of tube
        g2.drawImage(createForegroundImage(), 0, 0, null);

        g2.dispose();
    }

    private java.awt.image.BufferedImage createBackgroundImage()
    {
        final java.awt.image.BufferedImage IMAGE = new java.awt.image.BufferedImage((int)(86*factor), (int)(146*factor), BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics2D g2 = IMAGE.createGraphics();

        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        // Create glass tube form
        final java.awt.geom.Area TUBE = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float(1, (int)(8*factor), (int)(86*factor), (int)(136*factor), (int)(86*factor), (int)(86*factor)));
        final java.awt.geom.Area TUBE_BOTTOM = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float(1, (int)(75*factor), (int)(86*factor), (int)(70*factor), (int)(30*factor), (int)(30*factor)));
        final java.awt.geom.Area TUBE_TOP = new java.awt.geom.Area(new java.awt.geom.Ellipse2D.Float((int)(38*factor), 0, (int)(12*factor), (int)(18*factor)));
        TUBE.add(TUBE_TOP);
        TUBE.add(TUBE_BOTTOM);

        final java.awt.geom.Point2D START = new java.awt.geom.Point2D.Float(0,0);
        final java.awt.geom.Point2D STOP = new java.awt.geom.Point2D.Float((int)(86*factor),0);

        final float[] FRACTIONS =
        {
            0.0f,
            0.15f,
            0.4f,
            0.6f,
            0.85f,
            1.0f
        };

        final java.awt.Color[] COLORS =
        {
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.5f),
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.3f),
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.1f),
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.1f),
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.3f),
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.5f)
        };

        final java.awt.LinearGradientPaint GRADIENT = new java.awt.LinearGradientPaint(START, STOP, FRACTIONS, COLORS);
        
        g2.setPaint(GRADIENT);
        g2.fill(TUBE);

        g2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage createMiddleImage()
    {
        final java.awt.image.BufferedImage IMAGE = new java.awt.image.BufferedImage((int)(86*factor), (int)(146*factor), BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics2D g2 = IMAGE.createGraphics();

        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

		float alpha=0.2f*(float)factor;
		if(alpha<0.1)
			alpha=0.1f;
		else if(alpha>0.2)
		{
			alpha=0.2f;
			 g2.setStroke(new java.awt.BasicStroke((float)factor, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_MITER));
		}
        // Create front hatch
        g2.setColor(new java.awt.Color(0.0f, 0.0f, 0.0f, alpha));
		double x=14*factor;
		double y=47*factor;
        for (; x <= 75*factor ; x += 5*factor)
        {
            g2.draw(new java.awt.geom.Line2D.Float((int)(x), (int)(47*factor), (int)(x), (int)(133*factor)));
        }
        for (; y <= 134*factor ; y += 5*factor)
        {
            g2.draw(new java.awt.geom.Line2D.Float((int)(14*factor), (int)(y), (int)(74*factor), (int)(y)));
        }

        // Create number contacts
        java.awt.LinearGradientPaint contactGradient;
        java.awt.geom.Point2D contactStart;
        java.awt.geom.Point2D contactStop;
        final float[] CONTACT_FRACTIONS =
        {
            0.0f,
            0.5f,
            1.0f
        };

        final java.awt.Color[] CONTACT_COLORS =
        {
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.3f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.3f),
            new java.awt.Color(0.0f, 0.0f, 0.0f, 0.3f)
        };
		x=18*factor;
        for (; x < 67*factor ; x += 6*factor)
        {
            contactStart = new java.awt.geom.Point2D.Float((int)x, 0);
			if((int)x==(int)(x + 3*factor))
	            contactStop = new java.awt.geom.Point2D.Float((int)(x)+1, 0);
			else
	            contactStop = new java.awt.geom.Point2D.Float((int)(x + 3*factor), 0);
            contactGradient = new java.awt.LinearGradientPaint(contactStart, contactStop, CONTACT_FRACTIONS, CONTACT_COLORS);
            g2.setPaint(contactGradient);
            g2.fill(new java.awt.geom.Rectangle2D.Float((int)x, (int)(132*factor), (int)(4*factor)>0?(int)(4*factor):1, (int)(10*factor)));
        }
        
        g2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage createForegroundImage()
    {
        final java.awt.image.BufferedImage IMAGE = new java.awt.image.BufferedImage((int)(86*factor), (int)(146*factor), BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics2D g2 = IMAGE.createGraphics();

        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);

        // Create left side reflection effect
        final java.awt.geom.Area SIDE_LIGHT_EFFECT = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float((int)(3*factor), (int)(43*factor), (int)(36*factor), (int)(99*factor), (int)(18*factor), (int)(18*factor)));
        final java.awt.geom.Area EFFECT_SUB = new java.awt.geom.Area(new java.awt.geom.RoundRectangle2D.Float((int)(7*factor), (int)(37*factor), (int)(36*factor), (int)(105*factor), (int)(18*factor), (int)(18*factor)));
        SIDE_LIGHT_EFFECT.subtract(EFFECT_SUB);

        final java.awt.geom.Point2D SIDE_LIGHT_EFFECT_START = new java.awt.geom.Point2D.Float((int)(3*factor), 0);
        final java.awt.geom.Point2D SIDE_LIGHT_EFFECT_STOP = new java.awt.geom.Point2D.Float((int)(13*factor), 0);

        final float[] SIDE_LIGHT_EFFECT_FRACTIONS =
        {
            0.0f,
            1.0f
        };

        final java.awt.Color[] SIDE_LIGHT_EFFECT_COLORS =
        {
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.5f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f)
        };

        final java.awt.LinearGradientPaint SIDE_LIGHT_EFFECT_GRADIENT = new java.awt.LinearGradientPaint(SIDE_LIGHT_EFFECT_START, SIDE_LIGHT_EFFECT_STOP, SIDE_LIGHT_EFFECT_FRACTIONS, SIDE_LIGHT_EFFECT_COLORS);

        g2.setPaint(SIDE_LIGHT_EFFECT_GRADIENT);
        g2.fill(SIDE_LIGHT_EFFECT);

        // Create stripe reflection effect
        final java.awt.geom.Rectangle2D STRIPE_LIGHT_EFFECT = new java.awt.geom.Rectangle2D.Float((int)(13*factor), (int)(46*factor), (int)(62*factor), 1);

        final java.awt.geom.Point2D STRIPE_LIGHT_EFFECT_START = new java.awt.geom.Point2D.Float((int)(13*factor), 0);
        final java.awt.geom.Point2D STRIPE_LIGHT_EFFECT_STOP = new java.awt.geom.Point2D.Float((int)(75*factor), 0);

        final float[] STRIPE_LIGHT_EFFECT_FRACTIONS =
        {
            0.0f,
            0.5f,
            1.0f
        };

        final java.awt.Color[] STRIPE_LIGHT_EFFECT_COLORS =
        {
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.5f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f)
        };

        final java.awt.LinearGradientPaint STRIPE_LIGHT_EFFECT_GRADIENT = new java.awt.LinearGradientPaint(STRIPE_LIGHT_EFFECT_START, STRIPE_LIGHT_EFFECT_STOP, STRIPE_LIGHT_EFFECT_FRACTIONS, STRIPE_LIGHT_EFFECT_COLORS);

        g2.setPaint(STRIPE_LIGHT_EFFECT_GRADIENT);
        g2.fill(STRIPE_LIGHT_EFFECT);

        // Create top reflection effect
        final java.awt.geom.Ellipse2D TOP_LIGHT_EFFECT = new java.awt.geom.Ellipse2D.Float((int)(17*factor), (int)(11*factor), (int)(52*factor), (int)(21*factor));

        final java.awt.geom.Point2D TOP_LIGHT_EFFECT_START = new java.awt.geom.Point2D.Float(0, (int)(11*factor));
        final java.awt.geom.Point2D TOP_LIGHT_EFFECT_STOP = new java.awt.geom.Point2D.Float(0, (int)(32*factor));

        final float[] TOP_LIGHT_EFFECT_FRACTIONS =
        {
            0.0f,
            1.0f
        };

        final java.awt.Color[] TOP_LIGHT_EFFECT_COLORS =
        {
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.5f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f)
        };

        final java.awt.LinearGradientPaint TOP_LIGHT_EFFECT_GRADIENT = new java.awt.LinearGradientPaint(TOP_LIGHT_EFFECT_START, TOP_LIGHT_EFFECT_STOP, TOP_LIGHT_EFFECT_FRACTIONS, TOP_LIGHT_EFFECT_COLORS);

        g2.setPaint(TOP_LIGHT_EFFECT_GRADIENT);
        g2.fill(TOP_LIGHT_EFFECT);

        // Create small top reflection effect
        final java.awt.geom.Ellipse2D SMALL_TOP_LIGHT_EFFECT = new java.awt.geom.Ellipse2D.Float((int)(39*factor), (int)(3*factor), (int)(4*factor), (int)(6*factor));

        final java.awt.geom.Point2D SMALL_TOP_LIGHT_EFFECT_START = new java.awt.geom.Point2D.Float(0, (int)(3*factor));
        final java.awt.geom.Point2D SMALL_TOP_LIGHT_EFFECT_STOP = new java.awt.geom.Point2D.Float(0, (int)(9*factor));

        final float[] SMALL_TOP_LIGHT_EFFECT_FRACTIONS =
        {
            0.0f,
            1.0f
        };

        final java.awt.Color[] SMALL_TOP_LIGHT_EFFECT_COLORS =
        {
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.3f),
            new java.awt.Color(1.0f, 1.0f, 1.0f, 0.0f)
        };

        final java.awt.LinearGradientPaint SMALL_TOP_LIGHT_EFFECT_GRADIENT = new java.awt.LinearGradientPaint(SMALL_TOP_LIGHT_EFFECT_START, SMALL_TOP_LIGHT_EFFECT_STOP, SMALL_TOP_LIGHT_EFFECT_FRACTIONS, SMALL_TOP_LIGHT_EFFECT_COLORS);

        g2.setPaint(SMALL_TOP_LIGHT_EFFECT_GRADIENT);
        g2.fill(SMALL_TOP_LIGHT_EFFECT);


        g2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage createSymbol(int number, boolean active)
    {

        final java.awt.image.BufferedImage IMAGE = new java.awt.image.BufferedImage((int)(62*factor), (int)(97*factor), BufferedImage.TYPE_INT_ARGB);

        java.awt.Graphics2D g2 = IMAGE.createGraphics();

        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
                
        java.awt.geom.GeneralPath numberPath = new java.awt.geom.GeneralPath();

        switch(number)
        {
            case 0:
                numberPath = new java.awt.geom.GeneralPath(new java.awt.geom.Ellipse2D.Double(20*factor, 69*factor, 15*factor, 15*factor));
                break;
            case 1:
                numberPath = new java.awt.geom.GeneralPath();
                numberPath.moveTo(2*factor, 49*factor);
                numberPath.lineTo(51*factor, 49*factor);
                break;
        }

        // Translate 5,5 because of the linewidth of 12px for the glowing effect
        g2.translate(5*factor,5*factor);

        if (active)
        {
            // Draw active number with glow effect
            for (double width = 12*factor ; width > 0 ; width -= 2*factor)
            {                
                g2.setStroke(new java.awt.BasicStroke((float)width, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_MITER));
                g2.setColor(COLOR_ARRAY[(int)(width/2/factor - 1)]);
                g2.draw(numberPath);
            }
			float alpha=0.2f*(float)factor;
			if(alpha<0.1)
				alpha=0.1f;
			else if(alpha>0.2)
			{
				alpha=0.2f;
                 g2.setStroke(new java.awt.BasicStroke((float)factor, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_MITER));
			}

            g2.setColor(new java.awt.Color(0.2f, 0.2f, 0.2f, alpha));
            g2.draw(numberPath);
        }
        else
        {
            // Draw inactive number
			float alpha=0.6f*(float)factor;
			if(alpha<0.3)
				alpha=0.3f;
			else if(alpha>0.6)
			{
				alpha=0.6f;
                 g2.setStroke(new java.awt.BasicStroke((float)factor, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_MITER));
			}

            g2.setColor(new java.awt.Color(0.2f, 0.2f, 0.2f, alpha));
            g2.draw(numberPath);
        }

        g2.dispose();

        return IMAGE;
    }

    private void createSymbolStack()
    {
        // Order of numbers in stack
        // decimal point

        if (this.symbol > -1)
        {
            // decimal pont
            if(this.symbol == 0)
            {
                symbolStack[0] = ACTIVE_SYMBOL_ARRAY[0];
            }
            else
            {
                symbolStack[0] = INACTIVE_SYMBOL_ARRAY[0];
            }
            // minus
            if(this.symbol == 1)
            {
                symbolStack[1] = ACTIVE_SYMBOL_ARRAY[1];
            }
            else
            {
                symbolStack[1] = INACTIVE_SYMBOL_ARRAY[1];
            }
        }
        else
        {
            for (int i = 0 ; i < 2 ; i++)
            {
                symbolStack[i] = INACTIVE_SYMBOL_ARRAY[i];
            }
        }
    }

    public int getSymbol()
    {
        return this.symbol;
    }

    public void setSymbol(int symbol)
    {
        if (symbol < 0)
        {
            symbol = -1;
        }
        if (symbol > 2)
        {
            symbol = -1;
        }
        this.symbol = symbol;
        createSymbolStack();
        repaint();
    }

    @Override
    public java.awt.Dimension getSize()
    {
        return new java.awt.Dimension(86, 146);
    }

    @Override
    public java.awt.Dimension getSize(java.awt.Dimension dim)
    {
        return super.getSize(new java.awt.Dimension(86, 146));
    }
  
    @Override
    public String toString()
    {
        return "NixieSymbol";
    }
}
