package de.elbosso.ui.components;

import java.awt.*;

/**
 * Created by elbosso on 6/28/17.
 */
abstract class NixieTube extends javax.swing.JComponent
{
	public static final java.awt.Color NIXIE_ORANGE=new java.awt.Color(0.9882f, 0.5921f, 0.0f);
	protected final java.awt.geom.Point2D GLOW_START = new java.awt.geom.Point2D.Float(0, 47);
	protected final java.awt.geom.Point2D GLOW_STOP = new java.awt.geom.Point2D.Float(0, 133);
	protected final float[] GLOW_FRACTIONS =
			{
					0.0f,
					0.5f,
					1.0f
			};
	//orange
/*    protected final java.awt.Color[] GLOW_COLORS =
            {
                    new java.awt.Color(0.647f, 0.3137f, 0.0588f, 0.2f),
                    new java.awt.Color(0.9882f, 0.5921f, 0.0f, 0.3f),
                    new java.awt.Color(0.647f, 0.3137f, 0.0588f, 0.2f)
            };
*/
//green
/*    protected final java.awt.Color[] GLOW_COLORS =
            {
                    new java.awt.Color( 0.3137f,0.647f, 0.0588f, 0.2f),
                    new java.awt.Color( 0.5921f,0.9882f, 0.0f, 0.3f),
                    new java.awt.Color( 0.3137f,0.647f, 0.0588f, 0.2f)
            };
*/
//red
/*    protected final java.awt.Color[] GLOW_COLORS =
            {
                    new java.awt.Color(0.647f, 0.1f, 0.1f, 0.2f),
                    new java.awt.Color(0.9882f, 0.2f, 0.2f, 0.3f),
                    new java.awt.Color(0.647f, 0.1f, 0.1f, 0.2f)
            };
*/
	protected java.awt.Color[] GLOW_COLORS;


	//    protected final java.awt.LinearGradientPaint GLOW_GRADIENT = new java.awt.LinearGradientPaint(GLOW_START, GLOW_STOP, GLOW_FRACTIONS, GLOW_COLORS);
	protected  java.awt.LinearGradientPaint GLOW_GRADIENT ;


	//orange
/*    protected static final java.awt.Color[] COLOR_ARRAY =
            {
                    new java.awt.Color(1.0f, 0.6f, 0, 0.90f),
                    new java.awt.Color(1.0f, 0.4f, 0, 0.80f),
                    new java.awt.Color(1.0f, 0.4f, 0, 0.4f),
                    new java.awt.Color(1.0f, 0.4f, 0, 0.15f),
                    new java.awt.Color(1.0f, 0.4f, 0, 0.10f),
                    new java.awt.Color(1.0f, 0.4f, 0, 0.05f)
            };
            */
//green
/*    protected static final java.awt.Color[] COLOR_ARRAY =
            {
                    new java.awt.Color(0.6f, 1.0f, 0, 0.90f),
                    new java.awt.Color(0.4f, 1.0f, 0, 0.80f),
                    new java.awt.Color(0.4f, 1.0f, 0, 0.4f),
                    new java.awt.Color(0.4f, 1.0f, 0, 0.15f),
                    new java.awt.Color(0.4f, 1.0f, 0, 0.10f),
                    new java.awt.Color(0.4f, 1.0f, 0, 0.05f)
            };
*/
//red
/*    protected static final java.awt.Color[] COLOR_ARRAY =
            {
                    new java.awt.Color(1.0f, 0.2f, 0.2f, 0.90f),
                    new java.awt.Color(1.0f, 0.1f, 0.1f, 0.80f),
                    new java.awt.Color(1.0f, 0.1f, 0.1f, 0.4f),
                    new java.awt.Color(1.0f, 0.1f, 0.1f, 0.15f),
                    new java.awt.Color(1.0f, 0.1f, 0.1f, 0.10f),
                    new java.awt.Color(1.0f, 0.1f, 0.1f, 0.05f)
            };
*/
	protected java.awt.Color[] COLOR_ARRAY;
	private java.awt.Color color;

	private java.util.Map<java.awt.Color,java.awt.image.BufferedImage[]> cachedImages;

	protected NixieTube()
	{
		this(NIXIE_ORANGE);
	}
	protected NixieTube(java.awt.Color c)
	{
		super();
		cachedImages=new java.util.HashMap();
		setColor(c);
	}
	public void setColor(java.awt.Color c)
	{
		if(c==null)
			c=NIXIE_ORANGE;
		if(this.color!=c)
		{
			this.color = c;
			if (this.COLOR_ARRAY == null)
				this.COLOR_ARRAY = new java.awt.Color[6];
			float[] comps = c.getRGBComponents(null);
			COLOR_ARRAY[0] = new java.awt.Color(comps[0], comps[1], comps[2], 0.9f);
			COLOR_ARRAY[1] = new java.awt.Color(comps[0], comps[1], comps[2], 0.8f);
			COLOR_ARRAY[2] = new java.awt.Color(comps[0], comps[1], comps[2], 0.4f);
			COLOR_ARRAY[3] = new java.awt.Color(comps[0], comps[1], comps[2], 0.15f);
			COLOR_ARRAY[4] = new java.awt.Color(comps[0], comps[1], comps[2], 0.1f);
			COLOR_ARRAY[5] = new java.awt.Color(comps[0], comps[1], comps[2], 0.05f);
			if (this.GLOW_COLORS == null)
				GLOW_COLORS = new java.awt.Color[3];
			GLOW_COLORS[0] = new java.awt.Color(comps[0] * 0.6f, comps[1] * 0.6f, comps[2] * 0.6f, 0.2f);
			GLOW_COLORS[1] = new java.awt.Color(comps[0], comps[1], comps[2], 0.3f);
			GLOW_COLORS[2] = new java.awt.Color(comps[0] * 0.6f, comps[1] * 0.6f, comps[2] * 0.6f, 0.2f);
			GLOW_GRADIENT = new java.awt.LinearGradientPaint(GLOW_START, GLOW_STOP, GLOW_FRACTIONS, GLOW_COLORS);
			recreateActiveSymbols();
		}
	}

	public Color getColor()
	{
		return color;
	}
    protected abstract void recreateActiveSymbols();
	public java.awt.image.BufferedImage[] getCached(java.awt.Color c)
	{
		return cachedImages.get(c);
	}
	public void cache(java.awt.image.BufferedImage[] images)
	{
		cachedImages.put(getColor(),images);
	}
}
