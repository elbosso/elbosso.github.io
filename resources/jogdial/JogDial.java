package de.netsysit.ui.components;

import de.netsysit.ui.roundinstrument.ConicalGradientPaint;
import de.netsysit.util.lang.MiniMax;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;

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
public class JogDial extends javax.swing.JComponent implements
		java.beans.PropertyChangeListener
{
	final float[] CONE_FRACTIONS =
	{
		0.0f,
		0.2f,
		0.3f,
		0.4f,
		0.5f,
		0.6f,
		0.7f,
		0.8f,
		1.0f
	};
	final java.awt.Color[] CONE_COLORS =
	{
		java.awt.Color.white,
		java.awt.Color.GRAY,
		java.awt.Color.white,
		java.awt.Color.GRAY,
		java.awt.Color.DARK_GRAY,
		java.awt.Color.GRAY,
		java.awt.Color.white,
		java.awt.Color.GRAY,
		java.awt.Color.white,
	};
	private final static java.awt.Color DARKALPHA=new java.awt.Color(0,0,0,32);
	private final static java.awt.Color WHITEALPHA=new java.awt.Color(255,255,255,192);
	private static final java.awt.Stroke WIDESTROKE=new java.awt.BasicStroke(3.0f);
	private final static java.awt.image.BufferedImage upImage;
	private final static java.awt.image.BufferedImage downImage;
	static
	{
		java.awt.image.BufferedImage intermediate=null;
		try
		{
			intermediate=javax.imageio.ImageIO.read(de.netsysit.util.ResourceLoader.getImgResource("de/netsysit/ressources/gfx/ca/join_up_32.png"));
		}
		catch(java.io.IOException exp){}
		upImage=intermediate;
		try
		{
			intermediate=javax.imageio.ImageIO.read(de.netsysit.util.ResourceLoader.getImgResource("de/netsysit/ressources/gfx/ca/join_down_32.png"));
		}
		catch(java.io.IOException exp){}
		downImage=intermediate;
	};
	private java.beans.PropertyChangeSupport pcs;
	public final static int GAP=3;
	private double value;
	private de.netsysit.util.lang.MiniMax miniMax;
	private double oneround;
	private double oldfracture;
	private double actualdialvalue;
	private double actualfillvalue;
	private boolean draginitiated;
	private boolean dragstarted;
	private boolean dragging;
	private double dragoffset;
	private boolean invertRotation;
	private boolean inside;
	private float[] smallFractions;
	private float[] largeFractions;
	private java.awt.Color[] largeColors;
	private java.awt.Color[] smallColors;

	public JogDial(double min, double max, double oneround)
	{
		this(new de.netsysit.util.lang.MiniMax(min,max),oneround);
	}
	public JogDial( double oneround)
	{
		this(null,oneround);
	}
	@java.beans.ConstructorProperties({"miniMax", "oneround"})
	public JogDial(de.netsysit.util.lang.MiniMax minimax, double oneround)
	{
		super();
		this.miniMax=minimax;
		this.oneround=oneround;
		this.value=this.miniMax!=null?this.miniMax.getMin():0.0;
		float lsum=0.0f;
		float ssum=0.0f;
		largeFractions=new float[9];
		smallFractions=new float[9];
		largeColors=new java.awt.Color[9];
		smallColors=new java.awt.Color[9];
		java.util.Random r=new java.util.Random(System.currentTimeMillis()+hashCode());
		for(int i=1;i<largeFractions.length;++i)
		{
			largeFractions[i]=r.nextFloat();
			smallFractions[i]=r.nextFloat();
			lsum+=largeFractions[i];
			ssum+=smallFractions[i];
		}
		
		for(int i=0;i<largeFractions.length;++i)
		{
			largeFractions[i]/=lsum;
			smallFractions[i]/=ssum;
			if(i>1)
			{
				largeFractions[i]+=largeFractions[i-1];
				smallFractions[i]+=smallFractions[i-1];
			}
		}
		largeFractions[largeFractions.length-1]=1.0f;
		smallFractions[smallFractions.length-1]=1.0f;
		int off=r.nextInt(9);
		for(int i=0;i<largeColors.length;++i)
		{
			largeColors[i]=CONE_COLORS[(i+off)%9];
		}
		off=r.nextInt(9);
		for(int i=0;i<smallColors.length;++i)
		{
			smallColors[i]=CONE_COLORS[(i+off)%9];
		}
		if(this.miniMax!=null)
			this.miniMax.addPropertyChangeListener(this);
		oldfracture=-1;
		computeValues();
		addMouseMotionListener(new java.awt.event.MouseMotionListener() {

			public void mouseDragged(MouseEvent e)
			{
				double sangle=computeSinAngle(e);
				dragstarted=true;
				if(dragging)
				{
					if(setFracture(sangle/(java.lang.Math.PI*2.0)))
					{
						actualdialvalue=sangle+java.lang.Math.PI*0.5+dragoffset;
					}
				}
				else
				{
					
//				if((dragging==false)&&(dragstarted==false))
				{
					if((inside==false)&&(miniMax!=null))
					{
						double frac=(double)(getBounds().height-e.getPoint().y)/(double)getBounds().height;
						if(frac<0.0)
							frac=0.0;
						else if(frac>1.0)
							frac=1.0;
						setValue(getMiniMax().getSpan()*frac+getMiniMax().getMin());
					}
				}
				}
			}

			public void mouseMoved(MouseEvent e)
			{
			}

		});
		addMouseListener(new java.awt.event.MouseListener() {

			public void mouseClicked(MouseEvent e)
			{
			}

			public void mousePressed(MouseEvent e)
			{
				draginitiated=true;
				computeSinAngle(e);
				if((miniMax!=null)&&((inside==false)&&(dragging==false)))
				{
					double frac=(double)(getBounds().height-e.getPoint().y)/(double)getBounds().height;
					if(frac<0.0)
						frac=0.0;
					else if(frac>1.0)
						frac=1.0;
					setValue(getMiniMax().getSpan()*frac+getMiniMax().getMin());
				}
				repaint();
			}

			public void mouseReleased(MouseEvent e)
			{
				if((dragging==false)&&(dragstarted==false))
				{
					if(inside)
					{


						if(e.getPoint().y>getBounds().height/2)
						{
							double intermediate=getValue()-JogDial.this.oneround;
							if((miniMax!=null)&&(intermediate<getMiniMax().getMin()))
								intermediate=getMiniMax().getMin();
							setValue(intermediate);
						}
						else
						{
							double intermediate=getValue()+JogDial.this.oneround;
							if((miniMax!=null)&&(intermediate>getMiniMax().getMax()))
								intermediate=getMiniMax().getMax();
							setValue(intermediate);
						}
					}
					else if(miniMax!=null)
					{
						double frac=(double)(getBounds().height-e.getPoint().y)/(double)getBounds().height;
						if(frac<0.0)
							frac=0.0;
						else if(frac>1.0)
							frac=1.0;
						setValue(getMiniMax().getSpan()*frac+getMiniMax().getMin());
					}
				}
				draginitiated=false;
				dragging=false;
				dragstarted=false;
				repaint();
			}

			public void mouseEntered(MouseEvent e)
			{
			}

			public void mouseExited(MouseEvent e)
			{
			}
		});
		pcs=new java.beans.PropertyChangeSupport(this);
	}
	private double computeSinAngle(MouseEvent e)
	{
		int dim=(getSize().width<getSize().height?getSize().width:getSize().height)-2*GAP;
		int smalldim=dim/4;
		int off=(dim-smalldim)/2;
		int xstart=(getSize().width-dim)/2;
		int ystart=(getSize().height-dim)/2;
		int centerx=xstart+dim/2;
		int centery=ystart+dim/2;
		int offcenterx=(int)(java.lang.Math.sin(actualdialvalue)*(double)(dim/2-smalldim/2));
		int offcentery=(int)(java.lang.Math.cos(actualdialvalue)*(double)(dim/2-smalldim/2));
				int x=e.getX();
				int y=e.getY();
				int leftupperx=e.getComponent().getLocation().x;
				int leftuppery=e.getComponent().getLocation().y;
				int width=e.getComponent().getWidth();
				int height=e.getComponent().getHeight();
				int computex=x-width/2;
				int computey=y-height/2;

				double sinangle=0.0;
				if(computey!=0)
					sinangle=java.lang.Math.asin(((double)java.lang.Math.abs(computey))/(java.lang.Math.sqrt(computex*computex+computey*computey)));
				if((computex>0)&&(computey<1))
				{
					sinangle=java.lang.Math.PI*2-sinangle;
				}
				else if((computex<1)&&(computey<1))
				{
					sinangle+=java.lang.Math.PI;
				}
				else if((computex<1)&&(computey>0))
				{
					sinangle=java.lang.Math.PI-sinangle;
				}
				if(sinangle<0)
					sinangle+=java.lang.Math.PI*2;
				sinangle=(java.lang.Math.PI*2)-sinangle;

				if(draginitiated)
				{
					oldfracture=sinangle/(java.lang.Math.PI*2.0);
					draginitiated=false;
					dragoffset=actualdialvalue-java.lang.Math.PI*0.5-sinangle;
					while(dragoffset<0.0)
						dragoffset+=2*java.lang.Math.PI;

					java.awt.geom.Ellipse2D elli=new java.awt.geom.Ellipse2D.Double(centerx+offcenterx-smalldim/2,centery+offcentery-smalldim/2,smalldim,smalldim);

//					if(java.lang.Math.abs(dragoffset)<java.lang.Math.PI*0.05)
					if(elli.contains(e.getPoint()))
						dragging=true;
					elli=new java.awt.geom.Ellipse2D.Double(centerx-(dim/2-smalldim/2),centery-(dim/2-smalldim/2),(dim-smalldim),(dim-smalldim));
						inside=elli.contains(e.getPoint());
				}
				return sinangle;
	}
	public void paint(java.awt.Graphics gfx)
	{
		java.awt.Rectangle clipRect = gfx.getClipBounds();
		java.awt.Color latch=gfx.getColor();
		gfx.setColor(getBackground());
		gfx.fillRect(clipRect.x,clipRect.y,clipRect.width,clipRect.height);
		int dim=(getSize().width<getSize().height?getSize().width:getSize().height)-2*GAP;
		int smalldim=dim/4;
		int off=(dim-smalldim)/2;
		int xstart=(getSize().width-dim)/2;
		int ystart=(getSize().height-dim)/2;
		int centerx=xstart+dim/2;
		int centery=ystart+dim/2;
		java.awt.Graphics2D gfx2D=(java.awt.Graphics2D)gfx;
		java.lang.Object aavalue=gfx2D.getRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING);
		gfx2D.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
		gfx2D.setPaint(new java.awt.GradientPaint(0,getSize().height,getForeground(),0,0,getForeground().brighter()));
//		gfx2D.setColor(DARKALPHA);
		gfx2D.fillRect(0, (int)(getSize().getHeight()*actualfillvalue), getSize().width,  getSize().height);
		int offcenterx=(int)(java.lang.Math.sin(actualdialvalue)*(double)(dim/2-smalldim/2));
		int offcentery=(int)(java.lang.Math.cos(actualdialvalue)*(double)(dim/2-smalldim/2));
		java.awt.Stroke str=gfx2D.getStroke();
		gfx2D.setStroke(WIDESTROKE);
		gfx2D.setStroke(str);
        int w = dim, h = dim;
        java.awt.geom.Point2D center = new java.awt.geom.Point2D.Float(centerx, centery);
        float radius = dim /3*2;
        float[] dist = { 0.0f, 1.0f };
        java.awt.geom.Point2D focus = new java.awt.geom.Point2D.Float(centerx-dim/6, centery-dim/6);
		java.awt.Color[] colors = { java.awt.Color.LIGHT_GRAY, java.awt.Color.DARK_GRAY };
        java.awt.RadialGradientPaint rgp = new java.awt.RadialGradientPaint(center, radius,
                focus, dist, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE);
        gfx2D.setPaint(rgp); 
		java.awt.geom.Ellipse2D CONE = new java.awt.geom.Ellipse2D.Double(centerx-(dim/2-smalldim/2),centery-(dim/2-smalldim/2),(dim-smalldim),(dim-smalldim));
		java.awt.geom.Point2D CONE_CENTER = new java.awt.geom.Point2D.Double(CONE.getCenterX(), CONE.getCenterY());
		ConicalGradientPaint CONE_GRADIENT = new ConicalGradientPaint(CONE_CENTER, largeFractions, largeColors);
		gfx2D.setPaint(CONE_GRADIENT);
		gfx2D.fillOval(centerx-(dim/2),centery-(dim/2),(dim),(dim));
		gfx2D.setColor(DARKALPHA);
		gfx2D.drawOval(centerx-(dim/2-smalldim/2),centery-(dim/2-smalldim/2),(dim-smalldim),(dim-smalldim));
		if(upImage!=null)
		{
			gfx2D.drawImage(upImage, null, centerx-upImage.getWidth()/2, centery-upImage.getHeight());
		}
		if(downImage!=null)
		{
			gfx2D.drawImage(downImage, null, centerx-downImage.getWidth()/2, centery);
		}
		gfx2D.setColor(WHITEALPHA);
		gfx2D.fillOval(centerx+offcenterx-smalldim/2-1,centery+offcentery-smalldim/2-1,smalldim+2,smalldim+2);
//		gfx2D.setColor(DARKALPHA);
//		gfx2D.drawOval(centerx+offcenterx-smalldim/2,centery+offcentery-smalldim/2,smalldim,smalldim);
		gfx2D.setColor(getForeground());
//		w = smalldim+2; h = smalldim+2;
//        center = new java.awt.geom.Point2D.Float(centerx+offcenterx,centery+offcentery);
//        radius = smalldim /3*2;
//        dist = new float[]{ 0.0f,0.08f, 1.0f };
//        focus = new java.awt.geom.Point2D.Float(centerx+offcenterx-smalldim/6,centery+offcentery-smalldim/6);
//		colors = new java.awt.Color[]{ java.awt.Color.DARK_GRAY, java.awt.Color.DARK_GRAY,java.awt.Color.LIGHT_GRAY };
//        rgp = new java.awt.RadialGradientPaint(center, radius,
//                focus, dist, colors, java.awt.MultipleGradientPaint.CycleMethod.NO_CYCLE);
//        gfx2D.setPaint(rgp); 
		CONE = new java.awt.geom.Ellipse2D.Double(centerx+offcenterx-smalldim/2,centery+offcentery-smalldim/2,smalldim,smalldim);
		CONE_CENTER = new java.awt.geom.Point2D.Double(CONE.getCenterX(), CONE.getCenterY());
		CONE_GRADIENT = new ConicalGradientPaint(CONE_CENTER, smallFractions, smallColors);
		gfx2D.setPaint(CONE_GRADIENT);
		gfx2D.fillOval(centerx+offcenterx-smalldim/2,centery+offcentery-smalldim/2,smalldim,smalldim);
		if(dragging)
		{
			gfx2D.setStroke(WIDESTROKE);
			gfx2D.setColor(WHITEALPHA);
			gfx2D.drawOval(centerx+offcenterx-smalldim/2+1,centery+offcentery-smalldim/2+1,smalldim-2,smalldim-2);
			gfx2D.setStroke(str);
		}
		gfx2D.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,aavalue);
		gfx.setColor(latch);
	}
	private void computeValues()
	{
		if(miniMax!=null)
		{
			actualdialvalue = (invertRotation ? -2 : 2) * java.lang.Math.PI * ((value - miniMax.getMin()) % oneround) / oneround;
			actualfillvalue = 1.0 - ((value - miniMax.getMin()) / (miniMax.getSpan()));
		}
		else
		{
			actualdialvalue = (invertRotation ? -2 : 2)* java.lang.Math.PI * (value % oneround) / oneround;
			actualfillvalue=0.0;
		}
	}

	public boolean isInvertRotation()
	{
		return invertRotation;
	}

	public void setInvertRotation(boolean invertRotation)
	{
		this.invertRotation = invertRotation;
	}
	public synchronized double getValue()
	{
		return value;
	}

	public synchronized void setValue(double value)
	{
		double oldvalue=this.value;
		this.value = miniMax!=null?miniMax.constrain(value):value;
		computeValues();
		pcs.firePropertyChange("value", oldvalue, value);
		repaint();
	}

	public MiniMax getMiniMax()
	{
		return miniMax;
	}

	public void setMiniMax(MiniMax miniMax)
	{
		MiniMax old=getMiniMax();
		this.miniMax = miniMax;
		pcs.firePropertyChange("miniMax", old, getMiniMax());
		setValue(getValue());
	}


	public synchronized double getOneround()
	{
		return oneround;
	}

	public synchronized void setOneround(double oneround)
	{
		this.oneround = oneround;
		computeValues();
		repaint();
	}

	public boolean setFracture(double frac)
	{
		boolean rv=false;
		double vv=getValue();

		double newv=vv-(vv%oneround)+oneround*frac;

		double df=invertRotation?oldfracture-frac:frac-oldfracture;
//		if((newv<=max)&&(newv>=min))
		{
			if(oldfracture>-1)
			{

//				if((frac!=0.0)&&(oldfracture!=0.0))
				if(invertRotation)
				{
					if(frac-oldfracture<-0.7)
					{
						newv-=oneround;
						df=1.0-df;

					}
					else if(frac-oldfracture>0.7)
					{
						newv+=oneround;
						df+=1.0;

					}
				}
				else
				{
					if(frac-oldfracture>0.7)
					{
						newv-=oneround;
						df=1.0-df;

					}
					else if(frac-oldfracture<-0.7)
					{
						newv+=oneround;
						df+=1.0;

					}
				}
			}


			oldfracture=frac;
			double v=value+df*oneround;
			if((((getValue()>(miniMax!=null?miniMax.getMax(): Double.MAX_VALUE)-java.lang.Math.log10(oneround)/100.0)&&(df<0.0))||
					((getValue()<(miniMax!=null?miniMax.getMin(): -Double.MAX_VALUE)+java.lang.Math.log10(oneround)/100.0)&&(df>0.0)))
					||((getValue()<(miniMax!=null?miniMax.getMax(): Double.MAX_VALUE)-java.lang.Math.log10(oneround)/100.0)&&
					(getValue()>(miniMax!=null?miniMax.getMin(): -Double.MAX_VALUE)+java.lang.Math.log10(oneround)/100.0)))
			{
				if((miniMax==null)||((v<=miniMax.getMax())&&(v>=miniMax.getMin())))
				{

					double oldvalue=value;
					value=v;
					if(miniMax!=null)
						actualfillvalue=1.0-((value-miniMax.getMin())/(miniMax.getSpan()));


					pcs.firePropertyChange("value", oldvalue, value);

					rv=true;
				}
				else
					dragging=false;
			}
			else
				dragging=false;
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
		java.awt.Dimension shadow=super.getPreferredSize();
		int dim=2*GAP+50;
		int w=shadow.width>dim?shadow.width:dim;
		int h=shadow.height>dim?shadow.height:dim;
		return new java.awt.Dimension(w,h);
	}

	public boolean isDragging()
	{
		return dragging;
	}
	public void addPropertyChangeListener(String name, java.beans.PropertyChangeListener l)
	{
		if(pcs!=null)
			pcs.addPropertyChangeListener(name, l);
	}
	public void addPropertyChangeListener(java.beans.PropertyChangeListener l)
	{
		if(pcs!=null)
			pcs.addPropertyChangeListener(l);
	}
	public void removePropertyChangeListener(String name, java.beans.PropertyChangeListener l)
	{
		if(pcs!=null)
			pcs.removePropertyChangeListener(name, l);
	}
	public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
	{
		if(pcs!=null)
			pcs.removePropertyChangeListener(l);
	}
	public static void main(java.lang.String[] args)
	{
		final java.text.NumberFormat nf=java.text.NumberFormat.getNumberInstance();
		javax.swing.JFrame f=new javax.swing.JFrame(JogDial.class.getSimpleName());
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		javax.swing.JPanel p=new javax.swing.JPanel(new java.awt.BorderLayout());
		final JogDial jd=new JogDial(11.37);
		final javax.swing.JScrollBar scroller=new javax.swing.JScrollBar(javax.swing.SwingConstants.HORIZONTAL);
		p.add(jd);
		scroller.setMinimum(-32);
		scroller.setMaximum(110);
		scroller.setValue(37);
		javax.swing.JPanel pp=new javax.swing.JPanel(new java.awt.GridLayout(0,1));
		pp.add(scroller);
		p.add(pp,java.awt.BorderLayout.SOUTH);
		final javax.swing.JLabel l=new javax.swing.JLabel();
		pp.add(l);
		javax.swing.JToolBar tb=new javax.swing.JToolBar();
		tb.setFloatable(false);
		p.add(tb,java.awt.BorderLayout.NORTH);
//			jd.setInvertRotation(true);
		jd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt)
			{
				if(evt.getPropertyName().equals("value"))
				{
					l.setText(nf.format(evt.getNewValue()));
					scroller.setValue(((java.lang.Number)evt.getNewValue()).intValue());
				}
			}
		});
		jd.setValue(-32);
		tb.add(new javax.swing.AbstractAction("Reset")
		{

			public void actionPerformed(java.awt.event.ActionEvent arg0)
			{
				scroller.setValue(50);
			}

		});
		de.netsysit.util.pattern.command.ColorProcessor colorProcessor=new de.netsysit.util.pattern.command.ColorProcessor() {

			public boolean process(java.awt.Color color)
			{
				if (color != null)
				{
					jd.setForeground(color);
				}
				return true;
			}
		};
		tb.add(new de.netsysit.util.pattern.command.ChooseColorAction(colorProcessor,"Color"));
		scroller.addAdjustmentListener(new java.awt.event.AdjustmentListener() {

			public void adjustmentValueChanged(java.awt.event.AdjustmentEvent arg0)
			{
				if(jd.isDragging()==false)
				{
					jd.setValue((double)scroller.getValue());
				}
			}
		});
		p.setSize(new java.awt.Dimension(240,300));
		f.setContentPane(p);
		f.pack();
		f.setVisible(true);
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getSource()==miniMax)
			setValue(getValue());
	}
}
