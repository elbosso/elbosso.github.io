package de.elbosso.ui.components;
/*
 
 Die untenstehende Lizenz berührt nicht die Rechte des Autors der zugrundeliegenden Software
 http://www.jug-muenster.de/swing-nixieclock-321/
  
Copyright (c) 2013-2017.

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
public class NixieNumber extends javax.swing.JComponent
{
    private int number;        
    private java.awt.image.BufferedImage[] numberStack = new java.awt.image.BufferedImage[11];
    private final java.awt.geom.Point2D GLOW_START = new java.awt.geom.Point2D.Float(0, 47);
    private final java.awt.geom.Point2D GLOW_STOP = new java.awt.geom.Point2D.Float(0, 133);
    private final float[] GLOW_FRACTIONS =
    {
        0.0f,
        0.5f,
        1.0f
    };
    private final java.awt.Color[] GLOW_COLORS =
    {
        new java.awt.Color(0.647f, 0.3137f, 0.0588f, 0.2f),
        new java.awt.Color(0.9882f, 0.5921f, 0.0f, 0.3f),
        new java.awt.Color(0.647f, 0.3137f, 0.0588f, 0.2f)
    };
    
    private final java.awt.LinearGradientPaint GLOW_GRADIENT = new java.awt.LinearGradientPaint(GLOW_START, GLOW_STOP, GLOW_FRACTIONS, GLOW_COLORS);
    private final java.awt.geom.Rectangle2D GLOW_BOX ;

    private static final java.awt.Color[] COLOR_ARRAY =
    {        
        new java.awt.Color(1.0f, 0.6f, 0, 0.90f),
        new java.awt.Color(1.0f, 0.4f, 0, 0.80f),
        new java.awt.Color(1.0f, 0.4f, 0, 0.4f),
        new java.awt.Color(1.0f, 0.4f, 0, 0.15f),
        new java.awt.Color(1.0f, 0.4f, 0, 0.10f),
        new java.awt.Color(1.0f, 0.4f, 0, 0.05f)
    };

    private final java.awt.image.BufferedImage[] INACTIVE_NUMBER_ARRAY;

    private final java.awt.image.BufferedImage[] ACTIVE_NUMBER_ARRAY;
	
	private double factor;

    public NixieNumber()
    {
		this(1.0f);
	}
    public NixieNumber(float factor)
    {
        super();
		this.factor=factor;
        this.number = -1;
        this.setPreferredSize(new java.awt.Dimension((int)(86*factor), (int)(146*factor)));
        this.setSize(new java.awt.Dimension((int)(86*factor), (int)(146*factor)));
		GLOW_BOX = new java.awt.geom.Rectangle2D.Float((int)(13*factor), (int)(47*factor), (int)(60*factor), (int)(86*factor));
		ACTIVE_NUMBER_ARRAY = new java.awt.image.BufferedImage[11];
		for(int i=0;i<10;++i)
	        ACTIVE_NUMBER_ARRAY[i]=createNumber(i, true);
		INACTIVE_NUMBER_ARRAY =new java.awt.image.BufferedImage[11];
 		for(int i=0;i<10;++i)
			INACTIVE_NUMBER_ARRAY[i]=createNumber(i, false);
        init();
    }
    
    private void init()
    {
        setOpaque(false);
        createNumberStack();
        repaint();        
    }


    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        super.paintComponent(g);

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();

        // Backside glow effect
        if (number > -1)
        {
            g2.setPaint(GLOW_GRADIENT);
            g2.fill(GLOW_BOX);
        }

        // Draw nixie number
        for (int i = 0 ; i < 11 ; i++)
        {
            g2.drawImage(this.numberStack[i], (int)(12*factor), (int)(42*factor), null);
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
        java.awt.GraphicsConfiguration gfxConf = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = gfxConf.createCompatibleImage((int)(86*factor), (int)(146*factor), java.awt.Transparency.TRANSLUCENT);

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
        java.awt.GraphicsConfiguration gfxConf = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = gfxConf.createCompatibleImage((int)(86*factor), (int)(146*factor), java.awt.Transparency.TRANSLUCENT);

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
        java.awt.GraphicsConfiguration gfxConf = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = gfxConf.createCompatibleImage((int)(86*factor), (int)(146*factor), java.awt.Transparency.TRANSLUCENT);

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

    private java.awt.image.BufferedImage createNumber(int number, boolean active)
    {
//		System.out.println(factor);
        java.awt.GraphicsConfiguration gfxConf = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        final java.awt.image.BufferedImage IMAGE = gfxConf.createCompatibleImage((int)(62*factor), (int)(97*factor), java.awt.Transparency.TRANSLUCENT);

        java.awt.Graphics2D g2 = IMAGE.createGraphics();

        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
                
        java.awt.geom.GeneralPath numberPath = new java.awt.geom.GeneralPath();

        switch(number)
        {
            case 0:
                numberPath = new java.awt.geom.GeneralPath(new java.awt.geom.Ellipse2D.Double(4*factor, 6*factor, 46*factor, 77*factor));
                break;
            case 1:
                numberPath = new java.awt.geom.GeneralPath(new java.awt.geom.Ellipse2D.Double(25*factor, 1*factor, 3*factor, 85*factor));
                break;
            case 2:
                numberPath = new java.awt.geom.GeneralPath();
                numberPath.moveTo(2*factor, 23*factor);
                numberPath.lineTo(3*factor, 22*factor);
                numberPath.lineTo(6*factor, 16*factor);
                numberPath.lineTo(9*factor, 13*factor);
                numberPath.lineTo(13*factor, 10*factor);
                numberPath.lineTo(18*factor, 7*factor);
                numberPath.lineTo(25*factor, 6*factor);
                numberPath.lineTo(31*factor, 6*factor);
                numberPath.lineTo(37*factor, 8*factor);
                numberPath.lineTo(44*factor, 12*factor);
                numberPath.lineTo(47*factor, 18*factor);
                numberPath.lineTo(49*factor, 25*factor);
                numberPath.lineTo(48*factor, 31*factor);
                numberPath.lineTo(46*factor, 37*factor);
                numberPath.lineTo(42*factor, 39*factor);
                numberPath.lineTo(36*factor, 42*factor);
                numberPath.lineTo(29*factor, 44*factor);
                numberPath.lineTo(22*factor, 47*factor);
                numberPath.lineTo(14*factor, 54*factor);
                numberPath.lineTo(9*factor, 60*factor);
                numberPath.lineTo(5*factor, 68*factor);
                numberPath.lineTo(4*factor, 76*factor);
                numberPath.lineTo(3*factor, 82*factor);
                numberPath.lineTo(51*factor, 82*factor);
                break;
            case 3:
                numberPath = new java.awt.geom.GeneralPath();
                numberPath.moveTo(3*factor, 6*factor);
                numberPath.lineTo(48*factor, 6*factor);
                numberPath.lineTo(48*factor, 8*factor);
                numberPath.lineTo(28*factor, 34*factor);
                numberPath.lineTo(31*factor, 36*factor);
                numberPath.lineTo(35*factor, 37*factor);
                numberPath.lineTo(41*factor, 40*factor);
                numberPath.lineTo(45*factor, 46*factor);
                numberPath.lineTo(49*factor, 53*factor);
                numberPath.lineTo(49*factor, 60*factor);
                numberPath.lineTo(48*factor, 67*factor);
                numberPath.lineTo(46*factor, 71*factor);
                numberPath.lineTo(43*factor, 75*factor);
                numberPath.lineTo(38*factor, 80*factor);
                numberPath.lineTo(27*factor, 83*factor);
                numberPath.lineTo(22*factor, 83*factor);
                numberPath.lineTo(17*factor, 82*factor);
                numberPath.lineTo(12*factor, 80*factor);
                numberPath.lineTo(8*factor, 77*factor);
                numberPath.lineTo(3*factor, 71*factor);
                break;
            case 4:
                numberPath = new java.awt.geom.GeneralPath();
                numberPath.moveTo(50*factor, 59*factor);
                numberPath.lineTo(4*factor, 59*factor);
                numberPath.lineTo(2*factor, 58*factor);
                numberPath.lineTo(1*factor, 56*factor);
                numberPath.lineTo(35*factor, 4*factor);
                numberPath.lineTo(35*factor, 83*factor);
                break;
            case 5:
                numberPath = new java.awt.geom.GeneralPath();
                numberPath.moveTo(44*factor, 6*factor);
                numberPath.lineTo(10*factor, 6*factor);
                numberPath.lineTo(6*factor, 41*factor);
                numberPath.lineTo(8*factor, 41*factor);
                numberPath.lineTo(14*factor, 37*factor);
                numberPath.lineTo(21*factor, 36*factor);
                numberPath.lineTo(30*factor, 36*factor);
                numberPath.lineTo(38*factor, 39*factor);
                numberPath.lineTo(49*factor, 53*factor);
                numberPath.lineTo(49*factor, 61*factor);
                numberPath.lineTo(47*factor, 69*factor);
                numberPath.lineTo(44*factor, 75*factor);
                numberPath.lineTo(38*factor, 80*factor);
                numberPath.lineTo(30*factor, 82*factor);
                numberPath.lineTo(24*factor, 83*factor);
                numberPath.lineTo(18*factor, 82*factor);
                numberPath.lineTo(12*factor, 79*factor);
                numberPath.lineTo(6*factor, 74*factor);
                numberPath.lineTo(3*factor, 69*factor);
                break;
            case 6:
                numberPath = new java.awt.geom.GeneralPath();
                numberPath.moveTo(28*factor, 1*factor);
                numberPath.lineTo(4*factor, 48*factor);
                numberPath.append(new java.awt.geom.Ellipse2D.Double(2*factor, 34*factor, 48*factor, 48*factor), false);
                break;
            case 7:
                numberPath = new java.awt.geom.GeneralPath();
                numberPath.moveTo(2*factor, 3*factor);
                numberPath.lineTo(51*factor, 3*factor);
                numberPath.lineTo(25*factor, 85*factor);
                break;
            case 8:
                numberPath = new java.awt.geom.GeneralPath(new java.awt.geom.Ellipse2D.Double(1*factor, 39*factor, 50*factor, 45*factor));
                numberPath.append(new java.awt.geom.Ellipse2D.Double(6*factor, 2*factor, 42*factor, 37*factor), false);
                break;
            case 9:
                numberPath = new java.awt.geom.GeneralPath();
                numberPath.moveTo(30*factor, 85*factor);
                numberPath.lineTo(51*factor, 32*factor);
                numberPath.append(new java.awt.geom.Ellipse2D.Double(3*factor, 3*factor, 48*factor, 48*factor), false);
                break;
//             case 10:
//                numberPath = new java.awt.geom.GeneralPath(new java.awt.geom.Ellipse2D.Double(20*factor, 69*factor, 15*factor, 15*factor));
//                break;
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
//			System.out.println(alpha);
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
//			System.out.println(alpha);
            g2.setColor(new java.awt.Color(0.2f, 0.2f, 0.2f, alpha));
            g2.draw(numberPath);
        }

        g2.dispose();

        return IMAGE;
    }

    private void createNumberStack()
    {
        // Order of numbers in stack
        // 1 0 2 3 9 4 8 5 7 6 decimal point

        if (this.number > -1)
        {
            // 1
            if(this.number == 1)
            {
                numberStack[0] = ACTIVE_NUMBER_ARRAY[1];
            }
            else
            {
                numberStack[0] = INACTIVE_NUMBER_ARRAY[1];
            }

            // 0
            if(this.number == 0)
            {
                numberStack[1] = ACTIVE_NUMBER_ARRAY[0];
            }
            else
            {
                numberStack[1] = INACTIVE_NUMBER_ARRAY[0];
            }

            // 2
            if(this.number == 2)
            {
                numberStack[2] = ACTIVE_NUMBER_ARRAY[2];
            }
            else
            {
                numberStack[2] = INACTIVE_NUMBER_ARRAY[2];
            }

            // 3
            if(this.number == 3)
            {
                numberStack[3] = ACTIVE_NUMBER_ARRAY[3];
            }
            else
            {
                numberStack[3] = INACTIVE_NUMBER_ARRAY[3];
            }

            // 9
            if(this.number == 9)
            {
                numberStack[4] = ACTIVE_NUMBER_ARRAY[9];
            }
            else
            {
                numberStack[4] = INACTIVE_NUMBER_ARRAY[9];
            }

            // 4
            if(this.number == 4)
            {
                numberStack[5] = ACTIVE_NUMBER_ARRAY[4];
            }
            else
            {
                numberStack[5] = INACTIVE_NUMBER_ARRAY[4];
            }

            // 8
            if(this.number == 8)
            {
                numberStack[6] = ACTIVE_NUMBER_ARRAY[8];
            }
            else
            {
                numberStack[6] = INACTIVE_NUMBER_ARRAY[8];
            }

            // 5
            if(this.number == 5)
            {
                numberStack[7] = ACTIVE_NUMBER_ARRAY[5];
            }
            else
            {
                numberStack[7] = INACTIVE_NUMBER_ARRAY[5];
            }

            // 7
            if(this.number == 7)
            {
                numberStack[8] = ACTIVE_NUMBER_ARRAY[7];
            }
            else
            {
                numberStack[8] = INACTIVE_NUMBER_ARRAY[7];
            }

            // 6
            if(this.number == 6)
            {
                numberStack[9] = ACTIVE_NUMBER_ARRAY[6];
            }
            else
            {
                numberStack[9] = INACTIVE_NUMBER_ARRAY[6];
            }
//            // decimal pont
//            if(this.number == 10)
//            {
//                numberStack[10] = ACTIVE_NUMBER_ARRAY[10];
//            }
//            else
//            {
//                numberStack[10] = INACTIVE_NUMBER_ARRAY[10];
//            }
        }
        else
        {
            for (int i = 0 ; i < 10 ; i++)
            {
                numberStack[i] = INACTIVE_NUMBER_ARRAY[i];
            }
        }
    }

    public int getNumber()
    {
        return this.number;
    }

    public void setNumber(int number)
    {
        if (number < 0)
        {
            number = -1;
        }
        if (number > 9)
        {
            number = -1;
        }
        this.number = number;
        createNumberStack();
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
        return "NixieNumber";
    }
}
