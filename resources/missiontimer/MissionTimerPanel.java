package de.elbosso.ui.components;

import java.awt.image.BufferedImage;

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
public class MissionTimerPanel extends javax.swing.JPanel implements java.awt.event.ActionListener
{    
    private final java.awt.image.BufferedImage DOTS_ON;
    private final java.awt.image.BufferedImage DOTS_OFF;
    private boolean dotsOn = true;
    private final java.awt.image.BufferedImage BACKGROUND_IMAGE;
    private final javax.swing.Timer TIMER = new javax.swing.Timer(100, this);    
    private int sec_frac = 0;
    private int sec_right = 0;
    private int sec_left = 0;
    private int min_right = 0;
    private int min_left = 0;
    private int hour_right = 0;
    private int hour_mid = 0;
    private int hour_left = 0;
    private int split_sec_frac = 0;
    private int split_sec_right = 0;
    private int split_sec_left = 0;
    private int split_min_right = 0;
    private int split_min_left = 0;
    private int split_hour_right = 0;
    private int split_hour_mid = 0;
    private int split_hour_left = 0;
	private double factor;
	private java.awt.Dimension dim;
	private boolean split;
	private boolean showTenths;
	private SevenSegment sevenSegment;

    public MissionTimerPanel(double factor)
    {
		super();
		this.factor=factor;
		this.sevenSegment=new SevenSegment(factor);
		dim=new java.awt.Dimension((int)(480*factor), (int)(180*factor));
        setPreferredSize(dim);
        setSize(dim);
		DOTS_ON = createDots(true);
		DOTS_OFF = createDots(false);
		BACKGROUND_IMAGE = createBackground(dim.width,dim.height);
    }

	public boolean isShowTenths()
	{
		return showTenths;
	}

	public void setShowTenths(boolean showTenths)
	{
		this.showTenths = showTenths;
	}
	public boolean isRunning()
	{
		return TIMER.isRunning();
	}
    
    public void startTimer()
    {
        TIMER.start();        
    }

    public void stopTimer()
    {
        TIMER.stop(); 
		if(split==true)
			toggleSplit();
        dotsOn = true;
        repaint();
   }
	
	public void toggleSplit()
	{
		if(split==true)
		{
			sec_frac =split_sec_frac ;
			sec_right =split_sec_right ;
			sec_left = split_sec_left ;
			min_right =split_min_right ;
			min_left = split_min_left ;
			hour_right =split_hour_right;
			hour_mid = split_hour_mid ;
			hour_left =split_hour_left ;
		}
		else
		{
			split_sec_frac =sec_frac ;
			split_sec_right =sec_right ;
			split_sec_left = sec_left ;
			split_min_right =min_right ;
			split_min_left = min_left ;
			split_hour_right =hour_right;
			split_hour_mid = hour_mid ;
			split_hour_left =hour_left ;
		}
		split=!split;
	}

    public void resetTimer()
    {
        TIMER.stop();        
        sec_frac = 0;
        sec_right = 0;
        sec_left = 0;
        min_right = 0;
        min_left = 0;
        hour_right = 0;
        hour_mid = 0;
        hour_left = 0;
		split_sec_frac = 0;
		split_sec_right = 0;
        split_sec_left = 0;
        split_min_right = 0;
        split_min_left = 0;
        split_hour_right = 0;
        split_hour_mid = 0;
        split_hour_left = 0;
		split=false;
       repaint();
        dotsOn = true;
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

        g2.drawImage(BACKGROUND_IMAGE, 0, 0, this);

        g2.drawImage(sevenSegment.get(hour_left), (int)(38*factor), (int)(90*factor), this);
        g2.drawImage(sevenSegment.get(hour_mid), (int)(84*factor), (int)(90*factor), this);
        g2.drawImage(sevenSegment.get(hour_right), (int)(130*factor), (int)(90*factor), this);

        if (dotsOn)
        {
            g2.drawImage(DOTS_ON, (int)(172*factor), (int)(90*factor), this);
            g2.drawImage(DOTS_ON, (int)(301*factor), (int)(90*factor), this);
        }
        else
        {
            g2.drawImage(DOTS_OFF, (int)(172*factor), (int)(90*factor), this);
            g2.drawImage(DOTS_OFF, (int)(301*factor), (int)(90*factor), this);
        }

        g2.drawImage(sevenSegment.get(min_left), (int)(213*factor), (int)(90*factor), this);
        g2.drawImage(sevenSegment.get(min_right), (int)(259*factor), (int)(90*factor), this);

        g2.drawImage(sevenSegment.get(sec_left), (int)(319*factor), (int)(90*factor), this);
        g2.drawImage(sevenSegment.get(sec_right), (int)(365*factor), (int)(90*factor), this);

		if(((split==true)||(showTenths==true))||(isRunning()==false))
	        g2.drawImage(sevenSegment.getSmall(sec_frac), (int)(411*factor), (int)(90*factor)+(int)(65*factor-65*factor/1.4), this);
		else
	        g2.drawImage(sevenSegment.getSmall(10), (int)(411*factor), (int)(90*factor)+(int)(65*factor-65*factor/1.4), this);
        g2.dispose();
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

        final java.awt.geom.Point2D START_HIGHLIGHT = new java.awt.geom.Point2D.Float(0,(int)(79*factor));
        final java.awt.geom.Point2D STOP_HIGHLIGHT = new java.awt.geom.Point2D.Float(0,(int)(166*factor));

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

        final java.awt.geom.Point2D START = new java.awt.geom.Point2D.Float(0,(int)(80*factor));
        final java.awt.geom.Point2D STOP = new java.awt.geom.Point2D.Float(0,(int)(165*factor));
        
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
        g2.fillRect((int)(17*factor), (int)(79*factor), (int)(176*factor), (int)(87*factor));
        g2.fillRect((int)(193*factor), (int)(79*factor), width-(int)(211*factor), (int)(87*factor));

        final java.awt.LinearGradientPaint GRADIENT_LEFT = new java.awt.LinearGradientPaint(START, STOP, FRACTIONS, COLORS_LEFT);
        g2.setPaint(GRADIENT_LEFT);        
        g2.fillRect((int)(18*factor), (int)(80*factor), (int)(175*factor), (int)(85*factor));

        final java.awt.LinearGradientPaint GRADIENT_RIGHT = new java.awt.LinearGradientPaint(START, STOP, FRACTIONS, COLORS_RIGHT);
        g2.setPaint(GRADIENT_RIGHT);
        g2.fillRect((int)(193*factor), (int)(80*factor), width-(int)(212*factor), (int)(85*factor));

        g2.setColor(new java.awt.Color(0x6C8095));
        g2.drawLine((int)(193*factor), (int)(81*factor), (int)(193*factor), (int)(164*factor));
        g2.setColor(new java.awt.Color(0x000000));
        g2.drawLine((int)(194*factor), (int)(81*factor), (int)(194*factor), (int)(164*factor));

        final java.awt.BasicStroke STROKE = new java.awt.BasicStroke(4.0f, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL);
        g2.setStroke(STROKE);
        g2.setColor(new java.awt.Color(0xDDF3F4));
        g2.drawLine((int)(19*factor), (int)(30*factor), (int)(19*factor), (int)(36*factor));
        g2.drawLine((int)(19*factor), (int)(30*factor),(int)( 130*factor), (int)(30*factor));
        g2.drawLine((int)(318*factor), (int)(30*factor), width-(int)(21*factor), (int)(30*factor));
        g2.drawLine(width-(int)(21*factor), (int)(30*factor), width-(int)(21*factor),(int)( 36*factor));

        g2.setFont(new java.awt.Font("SansSerif", 1, (int)(22*factor)));
        g2.drawString("MISSION TIMER", (int)(140*factor), (int)(37*factor));

        g2.setFont(new java.awt.Font("SansSerif", 1, (int)(16*factor)));
        g2.drawString("HOURS", (int)(75*factor), (int)(65*factor));
        g2.drawString("MIN", (int)(243*factor), (int)(65*factor));
        g2.drawString("SEC", (int)(355*factor), (int)(65*factor));

        g2.dispose();

        return IMAGE;
    }

    private java.awt.image.BufferedImage createDots(boolean on)
    {
        java.awt.image.BufferedImage IMAGE = new java.awt.image.BufferedImage((int)(23*factor), (int)(65*factor), BufferedImage.TYPE_INT_ARGB);

        final java.awt.Color COLOR_ON = new java.awt.Color(0xEEFFEE);
        final java.awt.Color FRAME_COLOR_ON = new java.awt.Color(50, 200, 100, 128);
        final java.awt.Color COLOR_OFF = new java.awt.Color(0x4E5571);
        final java.awt.Color FRAME_COLOR_OFF = new java.awt.Color(61, 57, 142, 128);
        final java.awt.BasicStroke FRAME_STROKE = new java.awt.BasicStroke(1.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND);

        java.awt.Graphics2D g2 = (java.awt.Graphics2D) IMAGE.getGraphics();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION, java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);

        g2.setStroke(FRAME_STROKE);

        if (on)
        {
            g2.setColor(COLOR_ON);
            g2.fillOval((int)(8*factor), (int)(20*factor), (int)(7*factor), (int)(7*factor));
            g2.fillOval((int)(5*factor), (int)(39*factor), (int)(7*factor), (int)(7*factor));

            g2.setColor(FRAME_COLOR_ON);
            g2.drawOval((int)(8*factor), (int)(20*factor), (int)(7*factor),(int)( 7*factor));
            g2.drawOval((int)(5*factor), (int)(39*factor), (int)(7*factor), (int)(7*factor));
        }
        else
        {
            g2.setColor(COLOR_OFF);
            g2.fillOval((int)(8*factor), (int)(20*factor), (int)(7*factor),(int)( 7*factor));
            g2.fillOval((int)(5*factor), (int)(39*factor), (int)(7*factor), (int)(7*factor));

            g2.setColor(FRAME_COLOR_OFF);
            g2.drawOval((int)(8*factor), (int)(20*factor), (int)(7*factor), (int)(7*factor));
            g2.drawOval((int)(5*factor), (int)(39*factor), (int)(7*factor), (int)(7*factor));
        }

        g2.dispose();

        return IMAGE;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent event)
    {
        if (event.getSource().equals(TIMER))
        {
			if(split)
			{
				++split_sec_frac;
				if (split_sec_frac==10)
				{
					split_sec_frac=0;
					split_sec_right ++;
				}
				if((split_sec_frac==0)||(split_sec_frac==5))
					dotsOn = !dotsOn;

				if (split_sec_right == 10)
				{
					split_sec_right = 0;
					split_sec_left++;
				}

				if (split_sec_left == 6)
				{
					split_sec_left = 0;
					split_min_right++;
				}

				if (split_min_right == 10)
				{
					split_min_right = 0;
					split_min_left++;
				}

				if (split_min_left == 6)
				{
					split_min_left = 0;
					split_hour_right++;
				}

				if (split_hour_right == 10)
				{
					split_hour_right = 0;
					split_hour_mid++;
				}

				if (split_hour_mid == 10)
				{
					split_hour_mid = 0;
					split_hour_left++;
				}

				if (split_hour_left == 10)
				{
					split_sec_right = 0;
					split_sec_left = 0;
					split_min_right = 0;
					split_min_left = 0;
					split_hour_right = 0;
					split_hour_mid = 0;
					split_hour_left = 0;
				}
			}
			else
			{
				++sec_frac;
				if (sec_frac==10)
				{
					sec_frac=0;
					sec_right ++;
				}
				if((sec_frac==0)||(sec_frac==5))
					dotsOn = !dotsOn;

				if (sec_right == 10)
				{
					sec_right = 0;
					sec_left++;
				}

				if (sec_left == 6)
				{
					sec_left = 0;
					min_right++;
				}

				if (min_right == 10)
				{
					min_right = 0;
					min_left++;
				}

				if (min_left == 6)
				{
					min_left = 0;
					hour_right++;
				}

				if (hour_right == 10)
				{
					hour_right = 0;
					hour_mid++;
				}

				if (hour_mid == 10)
				{
					hour_mid = 0;
					hour_left++;
				}

				if (hour_left == 10)
				{
					sec_right = 0;
					sec_left = 0;
					min_right = 0;
					min_left = 0;
					hour_right = 0;
					hour_mid = 0;
					hour_left = 0;
				}
			}
            repaint();
        }              
    }

    @Override
    public java.awt.Dimension getSize()
    {
        return dim;
    }

}
