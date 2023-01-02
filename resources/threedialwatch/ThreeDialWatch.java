package de.elbosso.ui.components;
/*
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
import java.awt.event.MouseEvent;
import java.util.Calendar;

public class ThreeDialWatch extends javax.swing.JComponent
{

	private final static java.awt.Color DARKALPHA = new java.awt.Color(0, 0, 0, 32);
	private final static java.awt.Color WHITEALPHA = new java.awt.Color(255, 255, 255, 192);
	private static final java.awt.Stroke WIDESTROKE = new java.awt.BasicStroke(3.0f);
	private java.beans.PropertyChangeSupport pcs;
	public final static int GAP = 3;
	private double hourvalue;
	private double minutevalue;
	private double secondvalue;
	private de.netsysit.util.lang.MiniMax hourminiMax;
	private de.netsysit.util.lang.MiniMax minuteminiMax;
	private de.netsysit.util.lang.MiniMax secondminiMax;
	private double houroneround;
	private double minuteoneround;
	private double secondoneround;
	private double oldfracture;
	private double houractualdialvalue;
	private double minuteactualdialvalue;
	private double secondactualdialvalue;
	private boolean dragstartet;
	private boolean hourdragging;
	private boolean minutedragging;
	private boolean seconddragging;
	private double dragoffset;
	private boolean invertRotation;
	private double offsetAngle;

	public ThreeDialWatch()
	{
		super();
		this.hourminiMax = new de.netsysit.util.lang.MiniMax(0, 23.999);
//Würde als alternative auch gehen um die 12 auszuschliessen
//		this.hourminiMax = new de.netsysit.util.lang.MiniMax(0, 11.999);
		this.minuteminiMax = new de.netsysit.util.lang.MiniMax(0, 59.999);
		this.secondminiMax = new de.netsysit.util.lang.MiniMax(0, 59.999);
		this.houroneround = hourminiMax.getMax()/2;
		this.minuteoneround = minuteminiMax.getMax();
		this.secondoneround = secondminiMax.getMax();
		this.hourvalue = 1;
		this.minutevalue = 10;
		this.secondvalue = 15;
		oldfracture = -1;
		computeValues();
		addMouseMotionListener(new java.awt.event.MouseMotionListener()
		{
			public void mouseDragged(MouseEvent e)
			{
				double sangle = computeSinAngle(e);
				if (hourdragging)
				{
					if (setFracture(sangle / (java.lang.Math.PI * 2.0)))
					{
						houractualdialvalue = sangle + java.lang.Math.PI * 0.5 + dragoffset;
					}
				}
				else
				{
					if (minutedragging)
					{
						if (setFracture(sangle / (java.lang.Math.PI * 2.0)))
						{
							minuteactualdialvalue = sangle + java.lang.Math.PI * 0.5 + dragoffset;
						}
					}
					else
					{
						if (seconddragging)
						{
							if (setFracture(sangle / (java.lang.Math.PI * 2.0)))
							{
								secondactualdialvalue = sangle + java.lang.Math.PI * 0.5+ dragoffset;
							}
						}
					}
				}
			}

			public void mouseMoved(MouseEvent e)
			{
			}
		});
		addMouseListener(new java.awt.event.MouseListener()
		{
			public void mouseClicked(MouseEvent e)
			{
			}

			public void mousePressed(MouseEvent e)
			{
				dragstartet = true;
				computeSinAngle(e);
				repaint();
			}

			public void mouseReleased(MouseEvent e)
			{
				dragstartet = false;
				hourdragging = false;
				minutedragging = false;
				seconddragging = false;

				repaint();
			}

			public void mouseEntered(MouseEvent e)
			{
			}

			public void mouseExited(MouseEvent e)
			{
			}
		});
		pcs = new java.beans.PropertyChangeSupport(this);
	}

	public double getOffsetAngle()
	{
		return offsetAngle;
	}

	public void setOffsetAngle(double offsetAngle)
	{
		this.offsetAngle = offsetAngle;
	}

	public double getOffset()
	{
		return offsetAngle*(isInvertRotation()?-1.0:1.0);
	}

	private double computeSinAngle(MouseEvent e)
	{
		int dim = (getSize().width < getSize().height ? getSize().width : getSize().height) - 2 * GAP;
		int smalldim = dim / 4;
		int off = (dim - smalldim) / 2;
		int xstart = (getSize().width - dim) / 2;
		int ystart = (getSize().height - dim) / 2;
		int centerx = xstart + dim / 2;
		int centery = ystart + dim / 2;
		int offcenterx = (int) (java.lang.Math.sin(secondactualdialvalue+getOffset()) * (double) (dim / 2 - smalldim / 2));
		int offcentery = (int) (java.lang.Math.cos(secondactualdialvalue+getOffset()) * (double) (dim / 2 - smalldim / 2));
		int x = e.getX();
		int y = e.getY();
		int leftupperx = e.getComponent().getLocation().x;
		int leftuppery = e.getComponent().getLocation().y;
		int width = e.getComponent().getWidth();
		int height = e.getComponent().getHeight();
		int computex = x - width / 2;
		int computey = y - height / 2;

		double sinangle = 0.0;
		if (computey != 0)
		{
			sinangle = java.lang.Math.asin(((double) java.lang.Math.abs(computey)) / (java.lang.Math.sqrt(computex * computex + computey * computey)));
		}
		if ((computex > 0) && (computey < 1))
		{
			sinangle = java.lang.Math.PI * 2 - sinangle;
		}
		else
		{
			if ((computex < 1) && (computey < 1))
			{
				sinangle += java.lang.Math.PI;
			}
			else
			{
				if ((computex < 1) && (computey > 0))
				{
					sinangle = java.lang.Math.PI - sinangle;
				}
			}
		}
		if (sinangle < 0)
		{
			sinangle += java.lang.Math.PI * 2;
		}
		sinangle = (java.lang.Math.PI * 2) - sinangle;

		if (dragstartet)
		{
			oldfracture = sinangle / (java.lang.Math.PI * 2.0);
			dragstartet = false;
//			dragoffset = houractualdialvalue - java.lang.Math.PI * 0.5 - sinangle;
//			while (dragoffset < 0.0)
//			{
//				dragoffset += 2 * java.lang.Math.PI;
//			}

			java.awt.geom.Ellipse2D elli = new java.awt.geom.Ellipse2D.Double(centerx + offcenterx - smalldim / 4, centery + offcentery - smalldim / 4, smalldim / 2, smalldim / 2);

//					if(java.lang.Math.abs(dragoffset)<java.lang.Math.PI*0.05)
			if (elli.contains(e.getPoint()))
			{
				seconddragging = true;
			dragoffset = secondactualdialvalue - java.lang.Math.PI * 0.5 - sinangle;

			}
			else
			{
				offcenterx = (int) (java.lang.Math.sin(minuteactualdialvalue+getOffset()) * 0.8 * (double) (dim / 2 - smalldim / 2));
				offcentery = (int) (java.lang.Math.cos(minuteactualdialvalue+getOffset()) * 0.8 * (double) (dim / 2 - smalldim / 2));
				elli = new java.awt.geom.Ellipse2D.Double(centerx + offcenterx - smalldim / 4, centery + offcentery - smalldim / 4, smalldim / 2, smalldim / 2);

//					if(java.lang.Math.abs(dragoffset)<java.lang.Math.PI*0.05)
				if (elli.contains(e.getPoint()))
				{
					minutedragging = true;
			dragoffset = minuteactualdialvalue - java.lang.Math.PI * 0.5 - sinangle;
				}
				else
				{
					offcenterx = (int) (java.lang.Math.sin(houractualdialvalue+getOffset()) * 0.5 * (double) (dim / 2 - smalldim / 2));
					offcentery = (int) (java.lang.Math.cos(houractualdialvalue+getOffset()) * 0.5 * (double) (dim / 2 - smalldim / 2));
					elli = new java.awt.geom.Ellipse2D.Double(centerx + offcenterx - smalldim / 4, centery + offcentery - smalldim / 4, smalldim / 2, smalldim / 2);

//					if(java.lang.Math.abs(dragoffset)<java.lang.Math.PI*0.05)
					if (elli.contains(e.getPoint()))
					{
						hourdragging = true;
			dragoffset = houractualdialvalue - java.lang.Math.PI * 0.5 - sinangle;
					}
				}
			}
			while (dragoffset < 0.0)
			{
				dragoffset += 2 * java.lang.Math.PI;
			}
		}
		return sinangle;
	}

	public void paint(java.awt.Graphics gfx)
	{
		java.awt.Rectangle clipRect = gfx.getClipBounds();
		java.awt.Color latch = gfx.getColor();
		gfx.setColor(getBackground());
//		gfx.fillRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
		int dim = (getSize().width < getSize().height ? getSize().width : getSize().height) - 2 * GAP;
		int smalldim = dim / 4;
		int off = (dim - smalldim) / 2;
		int xstart = (getSize().width - dim) / 2;
		int ystart = (getSize().height - dim) / 2;
		int centerx = xstart + dim / 2;
		int centery = ystart + dim / 2;
		java.awt.Graphics2D gfx2D = (java.awt.Graphics2D) gfx;
		java.awt.Font markerFont=getFont().deriveFont((float)((smalldim/2-2)/2));
		java.awt.font.FontRenderContext frc = gfx2D.getFontRenderContext();
		java.lang.Object aavalue = gfx2D.getRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING);
		gfx2D.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
		gfx2D.setPaint(new java.awt.GradientPaint(0, getSize().height, getForeground(), 0, 0, getForeground().brighter()));
//		gfx2D.setColor(DARKALPHA);
//		gfx2D.fillRect(0, (int)(getSize().getHeight()*actualfillvalue), getSize().width,  getSize().height);
		int offcenterx = (int) (java.lang.Math.sin(houractualdialvalue+getOffset()) * 0.5 * (double) (dim / 2 - smalldim / 2));
		int offcentery = (int) (java.lang.Math.cos(houractualdialvalue+getOffset()) * 0.5 * (double) (dim / 2 - smalldim / 2));

		java.awt.Stroke str = gfx2D.getStroke();
		gfx2D.setStroke(WIDESTROKE);
		gfx2D.setColor(DARKALPHA);
		gfx2D.drawOval(centerx - (int)(0.5 *(dim / 2 - smalldim / 2)), centery - (int)(0.5 *(dim / 2 - smalldim / 2)), (int)(0.5 *(dim - smalldim)), (int)(0.5 *(dim - smalldim)));
		gfx2D.drawLine(centerx, centery, centerx + offcenterx, centery + offcentery);
		gfx2D.setStroke(str);
		gfx2D.setColor(WHITEALPHA);
		gfx2D.fillOval(centerx + offcenterx - smalldim / 4 - 1, centery + offcentery - smalldim / 4 - 1, smalldim / 2 + 2, smalldim / 2 + 2);
//		gfx2D.setColor(DARKALPHA);
//		gfx2D.drawOval(centerx+offcenterx-smalldim/2,centery+offcentery-smalldim/2,smalldim,smalldim);
		gfx2D.setColor(getForeground());
		gfx2D.fillOval(centerx + offcenterx - smalldim / 4, centery + offcentery - smalldim / 4, smalldim / 2, smalldim / 2);
		gfx2D.setColor(WHITEALPHA);
		if (hourdragging)
		{
			gfx2D.setStroke(WIDESTROKE);
			gfx2D.drawOval(centerx + offcenterx - smalldim / 4 + 1, centery + offcentery - smalldim / 4 + 1, smalldim / 2 - 2, smalldim / 2 - 2);
			gfx2D.setStroke(str);
		}
		if((smalldim/2-2)/2>6)
		{
			java.lang.String s=""+getHourValue();
			java.text.AttributedString as=new java.text.AttributedString(s);
			java.awt.font.TextLayout layout = new java.awt.font.TextLayout(s, markerFont, frc);
			int stringwidth = (int)layout.getAdvance();
			int stringheight = (int)(layout.getAscent());
			as.addAttribute(java.awt.font.TextAttribute.FONT,markerFont);
			gfx2D.drawString(as.getIterator(), centerx + offcenterx-stringwidth/2, centery + offcentery+stringheight/2);
		}
		gfx2D.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, aavalue);
		gfx.setColor(latch);
		offcenterx = (int) (java.lang.Math.sin(minuteactualdialvalue+getOffset()) * 0.8 * (double) (dim / 2 - smalldim / 2));
		offcentery = (int) (java.lang.Math.cos(minuteactualdialvalue+getOffset()) * 0.8 * (double) (dim / 2 - smalldim / 2));

		str = gfx2D.getStroke();
		gfx2D.setStroke(WIDESTROKE);
		gfx2D.setColor(DARKALPHA);
		gfx2D.drawOval(centerx - (int)(0.8 *(dim / 2 - smalldim / 2)), centery - (int)(0.8 *(dim / 2 - smalldim / 2)), (int)(0.8 *(dim - smalldim)), (int)(0.8 *(dim - smalldim)));
		gfx2D.drawLine(centerx, centery, centerx + offcenterx, centery + offcentery);
		gfx2D.setStroke(str);
		gfx2D.setColor(WHITEALPHA);
		gfx2D.fillOval(centerx + offcenterx - smalldim / 4 - 1, centery + offcentery - smalldim / 4 - 1, smalldim / 2 + 2, smalldim / 2 + 2);
//		gfx2D.setColor(DARKALPHA);
//		gfx2D.drawOval(centerx+offcenterx-smalldim/2,centery+offcentery-smalldim/2,smalldim,smalldim);
		gfx2D.setColor(getForeground());
		gfx2D.fillOval(centerx + offcenterx - smalldim / 4, centery + offcentery - smalldim / 4, smalldim / 2, smalldim / 2);
		gfx2D.setColor(WHITEALPHA);
		if (minutedragging)
		{
			gfx2D.setStroke(WIDESTROKE);
			gfx2D.drawOval(centerx + offcenterx - smalldim / 4 + 1, centery + offcentery - smalldim / 4 + 1, smalldim / 2 - 2, smalldim / 2 - 2);
			gfx2D.setStroke(str);
		}
		if((smalldim/2-2)/2>6)
		{
			java.lang.String s=""+getMinuteValue();
			java.text.AttributedString as=new java.text.AttributedString(s);
			java.awt.font.TextLayout layout = new java.awt.font.TextLayout(s, markerFont, frc);
			int stringwidth = (int)layout.getAdvance();
			int stringheight = (int)(layout.getAscent());
			as.addAttribute(java.awt.font.TextAttribute.FONT,markerFont);
			gfx2D.drawString(as.getIterator(), centerx + offcenterx-stringwidth/2, centery + offcentery+stringheight/2);
		}
		gfx2D.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, aavalue);
		gfx.setColor(latch);
		offcenterx = (int) (java.lang.Math.sin(secondactualdialvalue+getOffset()) * (double) (dim / 2 - smalldim / 2));
		offcentery = (int) (java.lang.Math.cos(secondactualdialvalue+getOffset()) * (double) (dim / 2 - smalldim / 2));

		str = gfx2D.getStroke();
		gfx2D.setStroke(WIDESTROKE);
		gfx2D.setColor(DARKALPHA);
		gfx2D.drawOval(centerx - (dim / 2 - smalldim / 2), centery - (dim / 2 - smalldim / 2), (dim - smalldim), (dim - smalldim));
		gfx2D.drawLine(centerx, centery, centerx + offcenterx, centery + offcentery);
		gfx2D.setStroke(str);
		gfx2D.setColor(WHITEALPHA);
		gfx2D.fillOval(centerx + offcenterx - smalldim / 4 - 1, centery + offcentery - smalldim / 4 - 1, smalldim / 2 + 2, smalldim / 2 + 2);
//		gfx2D.setColor(DARKALPHA);
//		gfx2D.drawOval(centerx+offcenterx-smalldim/2,centery+offcentery-smalldim/2,smalldim,smalldim);
		gfx2D.setColor(getForeground());
		gfx2D.fillOval(centerx + offcenterx - smalldim / 4, centery + offcentery - smalldim / 4, smalldim / 2, smalldim / 2);
		gfx2D.setColor(WHITEALPHA);
		if (seconddragging)
		{
			gfx2D.setStroke(WIDESTROKE);
			gfx2D.drawOval(centerx + offcenterx - smalldim / 4 + 1, centery + offcentery - smalldim / 4 + 1, smalldim / 2 - 2, smalldim / 2 - 2);
			gfx2D.setStroke(str);
		}
		if((smalldim/2-2)/2>6)
		{
			java.lang.String s=""+getSecondValue();
			java.text.AttributedString as=new java.text.AttributedString(s);
			java.awt.font.TextLayout layout = new java.awt.font.TextLayout(s, markerFont, frc);
			int stringwidth = (int)layout.getAdvance();
			int stringheight = (int)(layout.getAscent());
			as.addAttribute(java.awt.font.TextAttribute.FONT,markerFont);
			gfx2D.drawString(as.getIterator(), centerx + offcenterx-stringwidth/2, centery + offcentery+stringheight/2);
		}
		gfx2D.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, aavalue);
		gfx.setColor(latch);
	}

	private void computeValues()
	{
		houractualdialvalue = (invertRotation ? -2 : 2) * java.lang.Math.PI * ((hourvalue - hourminiMax.getMin()) % houroneround) / houroneround;
		minuteactualdialvalue = (invertRotation ? -2 : 2) * java.lang.Math.PI * ((minutevalue - minuteminiMax.getMin()) % minuteoneround) / minuteoneround;
		secondactualdialvalue = (invertRotation ? -2 : 2) * java.lang.Math.PI * ((secondvalue - secondminiMax.getMin()) % secondoneround) / secondoneround;

	}

	public boolean isInvertRotation()
	{
		return invertRotation;
	}

	public void setInvertRotation(boolean invertRotation)
	{
		this.invertRotation = invertRotation;
		computeValues();
	}

	public int getHourValue()
	{
		int h=(int)(hourvalue);
		if(h>(int)hourminiMax.getMax())
			h=(int)hourminiMax.getMax();
		if(h<0)
			h=0;
		return h;
	}

	public int getMinuteValue()
	{
		int m=(int)minutevalue;
		if(m>(int)minuteminiMax.getMax())
			m=(int)minuteminiMax.getMax();
		if(m<0)
			m=0;
		return m;
	}

	public int getSecondValue()
	{
		int s=(int)secondvalue;
		if(s>(int)secondminiMax.getMax())
			s=(int)secondminiMax.getMax();
		if(s<0)
			s=0;
		return s;
	}

	public void setHourValue(double value)
	{
		int oldvalue = getHourValue();
		this.hourvalue = hourminiMax.constrain(value);
		computeValues();
		pcs.firePropertyChange("hourValue", oldvalue, value);
		repaint();
	}

	public void setMinuteValue(double value)
	{
		int oldvalue = getMinuteValue();
		this.minutevalue = minuteminiMax.constrain(value);
		computeValues();
		pcs.firePropertyChange("minuteValue", oldvalue, value);
		repaint();
	}

	public void setSecondValue(double value)
	{
		int oldvalue = getSecondValue();
		this.secondvalue = secondminiMax.constrain(value);
		computeValues();
		pcs.firePropertyChange("secondValue", oldvalue, value);
		repaint();
	}

	public boolean setFracture(double frac)
	{
		boolean rv = false;
		double vv = getHourValue();

		double oneround=houroneround;
		if(minutedragging)
			oneround=minuteoneround;
		else if(seconddragging)
			oneround=secondoneround;
		double newv = vv - (vv % oneround) + oneround * frac;

		double df = invertRotation ? oldfracture - frac : frac - oldfracture;
//		if((newv<=max)&&(newv>=min))
		{
			if (oldfracture > -1)
			{

//				if((frac!=0.0)&&(oldfracture!=0.0))
				if (invertRotation)
				{
					if (frac - oldfracture < -0.7)
					{
						newv -= oneround;
						df = 1.0 - df;

					}
					else
					{
						if (frac - oldfracture > 0.7)
						{
							newv += oneround;
							df += 1.0;

						}
					}
				}
				else
				{
					if (frac - oldfracture > 0.7)
					{
						newv -= oneround;
						df = 1.0 - df;

					}
					else
					{
						if (frac - oldfracture < -0.7)
						{
							newv += oneround;
							df += 1.0;

						}
					}
				}
			}


			oldfracture = frac;
//			if((((getValue()==max)&&(df<0.0))||((getValue()==min)&&(df>0.0)))||((getValue()<max)&&(getValue()>min)))
//				setValue(newv);
//			double v=getValue();

//			double aactualdialvalue+=df*2*java.lang.Math.PI;

			if(hourdragging)
			{
			double v = hourvalue + df * houroneround;
//			if ((((getHourValue() + java.lang.Math.log10(houroneround) / 100.0 > hourminiMax.getMax()) && (df < 0.0)) || ((getHourValue() - java.lang.Math.log10(houroneround) / 100.0 < hourminiMax.getMin()) && (df > 0.0))) || ((getHourValue() + java.lang.Math.log10(houroneround) / 100.0 < hourminiMax.getMax()) && (getHourValue() - java.lang.Math.log10(houroneround) / 100.0 > hourminiMax.getMin())))
			{
				if ((v <= hourminiMax.getMax()) && (v >= hourminiMax.getMin()))
				{
					setHourValue(v);
					rv = true;
				}
				else
				{
					if(v > hourminiMax.getMax())
						setHourValue(hourminiMax.getMax());
					else if(v < hourminiMax.getMin())
						setHourValue(hourminiMax.getMin());

					hourdragging = false;
				}
			}
//			else
//			{

//				hourdragging = false;
//			}
////			actualfillvalue=latvh;
			}
			else if(minutedragging)
			{
			double v = minutevalue + df * minuteoneround;
//			if ((((getMinuteValue() + java.lang.Math.log10(minuteoneround) / 100.0 > minuteminiMax.getMax()) && (df < 0.0)) || ((getMinuteValue() - java.lang.Math.log10(minuteoneround) / 100.0 < minuteminiMax.getMin()) && (df > 0.0))) || ((getMinuteValue() + java.lang.Math.log10(minuteoneround) / 100.0 < minuteminiMax.getMax()) && (getMinuteValue() - java.lang.Math.log10(minuteoneround) / 100.0 > minuteminiMax.getMin())))
			{
				if ((v <= minuteminiMax.getMax()) && (v >= minuteminiMax.getMin()))
				{
					setMinuteValue(v);
					rv = true;
				}
				else
				{
					if(v > minuteminiMax.getMax())
						setMinuteValue(minuteminiMax.getMax());
					else if(v < minuteminiMax.getMin())
						setMinuteValue(minuteminiMax.getMin());

					minutedragging = false;
				}

			}
//			else
//			{
//					if(v > minuteminiMax.getMax())
//						setMinuteValue(minuteminiMax.getMax());
//					else if(v < minuteminiMax.getMin())
//						setMinuteValue(minuteminiMax.getMin());

//				minutedragging = false;
//			}
////			actualfillvalue=latvh;
			}
			else if(seconddragging)
			{
			double v = secondvalue + df * secondoneround;
//			if ((((getSecondValue() + java.lang.Math.log10(secondoneround) / 100.0 > secondminiMax.getMax()) && (df < 0.0)) || ((getSecondValue() - java.lang.Math.log10(secondoneround) / 100.0 < secondminiMax.getMin()) && (df > 0.0))) || ((getSecondValue() + java.lang.Math.log10(secondoneround) / 100.0 < secondminiMax.getMax()) && (getSecondValue() - java.lang.Math.log10(secondoneround) / 100.0 > secondminiMax.getMin())))
			{
				if ((v <= secondminiMax.getMax()) && (v >= secondminiMax.getMin()))
				{
					setSecondValue(v);
					rv = true;
				}
				else
				{
					if(v > secondminiMax.getMax())
						setSecondValue(secondminiMax.getMax());
					else if(v < secondminiMax.getMin())
						setSecondValue(secondminiMax.getMin());
					seconddragging = false;
				}
			}
//			else
//			{
//				seconddragging = false;
//			}
//			actualfillvalue=latvh;
			}
		}
		repaint();
		return rv;
	}

	@Override
	public java.awt.Dimension getMinimumSize()
	{
		return super.getMinimumSize();
	}

	@Override
	public java.awt.Dimension getMaximumSize()
	{
		return super.getMaximumSize();
	}

	@Override
	public java.awt.Dimension getPreferredSize()
	{
		java.awt.Dimension shadow = super.getPreferredSize();
		int dim = 2 * GAP + 200;
		int w = shadow.width > dim ? shadow.width : dim;
		int h = shadow.height > dim ? shadow.height : dim;
		return new java.awt.Dimension(w, h);
	}

//	public boolean isDragging()
//	{
//		return dragging;
//	}
//
	@Override
	public void addPropertyChangeListener(String name, java.beans.PropertyChangeListener l)
	{
		if (pcs != null)
		{
			pcs.addPropertyChangeListener(name, l);
		}
	}

	@Override
	public void addPropertyChangeListener(java.beans.PropertyChangeListener l)
	{
		if (pcs != null)
		{
			pcs.addPropertyChangeListener(l);
		}
	}

	@Override
	public void removePropertyChangeListener(String name, java.beans.PropertyChangeListener l)
	{
		if (pcs != null)
		{
			pcs.removePropertyChangeListener(name, l);
		}
	}

	@Override
	public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
	{
		if (pcs != null)
		{
			pcs.removePropertyChangeListener(l);
		}
	}

	public void setTime(Calendar instance)
	{
		if(instance!=null)
		{
			setMinuteValue(instance.get(instance.MINUTE));
			setHourValue(instance.get(instance.HOUR_OF_DAY));
			setSecondValue(instance.get(instance.SECOND));
		}
	}
	public static void main(java.lang.String[] args)
	{
		javax.swing.JFrame f=new javax.swing.JFrame(ThreeDialWatch.class.getSimpleName());
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		javax.swing.JPanel p=new javax.swing.JPanel(new java.awt.BorderLayout());
		final ThreeDialWatch jd=new ThreeDialWatch();
		p.add(jd);
		javax.swing.JPanel pp=new javax.swing.JPanel(new java.awt.GridLayout(0,1));
		p.add(pp,java.awt.BorderLayout.SOUTH);
		final javax.swing.JLabel l=new javax.swing.JLabel();
		pp.add(l);
		javax.swing.JToolBar tb=new javax.swing.JToolBar();
		tb.setFloatable(false);
		p.add(tb,java.awt.BorderLayout.NORTH);
		java.util.Calendar cal=java.util.Calendar.getInstance();
		jd.setMinuteValue(cal.get(cal.MINUTE));
		jd.setHourValue(cal.get(cal.HOUR_OF_DAY));
		jd.setSecondValue(cal.get(cal.SECOND));
		jd.setInvertRotation(true);
		jd.setOffsetAngle(java.lang.Math.PI);
		jd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

			public void propertyChange(java.beans.PropertyChangeEvent evt)
			{
				if(evt.getPropertyName().equals("value"))
				{
				}
			}
		});
		p.setSize(new java.awt.Dimension(240,300));
		f.setContentPane(p);
		f.pack();
		f.setVisible(true);
	}
}
