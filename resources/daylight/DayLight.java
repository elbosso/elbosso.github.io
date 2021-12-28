package de.elbosso.ui.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.IOException;

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
public class DayLight extends javax.swing.JComponent
{
	private java.awt.image.BufferedImage bimg;
	private double angle;

	public DayLight() throws IOException
	{
		super();
		setBackground(java.awt.Color.red);
		bimg=javax.imageio.ImageIO.read(de.netsysit.util.ResourceLoader.getImgResource("de/elbosso/ressources/gfx/eb/daynight.png"));
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new java.awt.Dimension(bimg.getWidth(),bimg.getHeight()/2);
	}

	@Override
	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}

	@Override
	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}

	public double getAngle()
	{
		return angle;
	}

	public void setAngle(double angle)
	{
		this.angle = angle;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		java.awt.Graphics2D g2=(java.awt.Graphics2D)g;
		java.awt.geom.Rectangle2D rect=new java.awt.geom.Rectangle2D.Double(0, 0, bimg.getWidth(), bimg.getHeight()/2);
		java.awt.geom.Path2D p = new java.awt.geom.Path2D.Double(java.awt.geom.Path2D.WIND_EVEN_ODD);
//		p.append(rect, true);
		p.append(new java.awt.geom.Ellipse2D.Double(0, bimg.getHeight()/3, bimg.getHeight()*2/5, bimg.getHeight()/3),false);
		p.append(new java.awt.geom.Ellipse2D.Double(bimg.getHeight()*3/5, bimg.getHeight()/3, bimg.getHeight()*2/5, bimg.getHeight()/3),false);
		p.append(rect, true);
		p.append(new java.awt.geom.Rectangle2D.Double(0, bimg.getHeight()/2, bimg.getWidth(), bimg.getHeight()/2), true);
		g2.clip(p);
		g2.clip(rect);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//		g2.setTransform(AffineTransform.getRotateInstance(java.lang.Math.toRadians(0), 0,0));//bimg.getWidth()/2, bimg.getHeight()/2));
		g2.drawImage(bimg,AffineTransform.getRotateInstance(java.lang.Math.toRadians(angle), bimg.getWidth()/2, bimg.getHeight()/2) , this);
	}
}
