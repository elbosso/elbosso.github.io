package de.netsysit.util;
//$Id$

import javax.accessibility.AccessibleContext;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.beans.Transient;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/*#LICENCE#*/
public class ResourceLoader extends java.lang.Object
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(ResourceLoader.class);
	private final static java.util.regex.Pattern pat1=java.util.regex.Pattern.compile("(.*?)_(\\d*?)([^\\d]*?)\\.png");
	private final static java.util.regex.Pattern pat2=java.util.regex.Pattern.compile("(.*?)_(\\d*?)([^\\d]*?)\\.png");
	private static java.util.Properties props=new java.util.Properties();
	private static java.io.File f;
	private static IconSize size;
	private static java.net.URL fallBack;
	
	public enum IconSize
	{
		small(new int[]{32,36}),medium(new int[]{48}),large(new int[]{64});
		
		private final int[] pixelSize;
		private final int[] smallersize;

		private IconSize(int[] pixelSize)
		{
			this.pixelSize=pixelSize;
			smallersize=new int[pixelSize.length];
			for(int i=0;i<pixelSize.length;++i)
			{
				smallersize[i]=pixelSize[i]==48?32:pixelSize[i]*3/4;
			}
		}
		public int[] getPixelSize()
		{
			return pixelSize;
		}
		public int[] getSmallerPixelSize()
		{
			return smallersize;
		}
	}

	public static javax.swing.ImageIcon getIcon(java.lang.String resourceName)
	{
		javax.swing.ImageIcon rv=null;
		if(props.getProperty(resourceName,resourceName).indexOf('&')>-1)
		{
			try
			{
				java.lang.String[] parts = props.getProperty(resourceName,resourceName).split("&");
				if ((parts != null) && (parts.length == 2))
				{
					rv=new ImageIcon(de.netsysit.ui.image.DecoratedImageProducer.produceImage(de.netsysit.util.ResourceLoader.getImgResource(parts[0]), de.netsysit.util.ResourceLoader.getImgResource(parts[1])));
				}
				else
				{
					rv=new ImageIcon(getImgResource(resourceName));
				}
			}
			catch(java.lang.Throwable t)
			{
				rv=new ImageIcon(getImgResource(resourceName));
			}
		}
		else
		{
			rv=new ImageIcon(getImgResource(resourceName));
		}
		return rv;
	}
	private static class ImageIcon extends javax.swing.ImageIcon
	{
		private javax.swing.ImageIcon proxy;

		public ImageIcon(URL location)
		{
			super();
			proxy=new javax.swing.ImageIcon();
			java.awt.Image img = null;
			try
			{
				img = javax.imageio.ImageIO.read(location);
			}
			catch(java.io.IOException ioexp)
			{
				img = java.awt.Toolkit.getDefaultToolkit().createImage(location);
			}
			int w = img.getWidth(proxy.getImageObserver());
			int h = img.getHeight(proxy.getImageObserver());
			if(h<36)
			{
				java.awt.image.BufferedImage timg = new java.awt.image.BufferedImage(36, 36, BufferedImage.TYPE_INT_ARGB);
				java.awt.Graphics gfx = timg.getGraphics();
				gfx.drawImage(img, (36 - w) / 2, (36 - w) / 2, proxy.getImageObserver());
				gfx.dispose();
				proxy.setImage(timg);
			}
			else
			{
				proxy.setImage(img);
			}
		}

		public ImageIcon(BufferedImage img)
		{
			proxy=new javax.swing.ImageIcon();
			int w = img.getWidth(proxy.getImageObserver());
			int h = img.getHeight(proxy.getImageObserver());
			if(h<36)
			{
				java.awt.image.BufferedImage timg = new java.awt.image.BufferedImage(36, 36, BufferedImage.TYPE_INT_ARGB);
				java.awt.Graphics gfx = timg.getGraphics();
				gfx.drawImage(img, (36 - w) / 2, (36 - w) / 2, proxy.getImageObserver());
				gfx.dispose();
				proxy.setImage(timg);
			}
			else
			{
				proxy.setImage(img);
			}
		}

		@Override
		public int getImageLoadStatus()
		{
			return proxy.getImageLoadStatus();
		}

		@Override
		@Transient
		public Image getImage()
		{
			return proxy.getImage();
		}

		@Override
		public void setImage(Image image)
		{
			proxy.setImage(image);
		}

		@Override
		public String getDescription()
		{
			return proxy.getDescription();
		}

		@Override
		public void setDescription(String description)
		{
			proxy.setDescription(description);
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			proxy.paintIcon(c, g, x, y);
		}

		@Override
		public int getIconWidth()
		{
			return proxy.getIconWidth();
		}

		@Override
		public int getIconHeight()
		{
			return proxy.getIconHeight();
		}

		@Override
		public void setImageObserver(ImageObserver observer)
		{
			proxy.setImageObserver(observer);
		}

		@Override
		@Transient
		public ImageObserver getImageObserver()
		{
			return proxy.getImageObserver();
		}

		@Override
		public String toString()
		{
			return proxy.toString();
		}

		@Override
		public AccessibleContext getAccessibleContext()
		{
			return proxy.getAccessibleContext();
		}
	}
	static
	{
		setSize(IconSize.medium);
		try{
			java.io.InputStream is=de.netsysit.util.ResourceLoader.getResource("de/netsysit/ressources/data/icon_trans.properties").openStream();
			props.load(is);
			is.close();
		}catch(java.lang.Throwable exp){exp.printStackTrace();}
		try{
			fallBack=getResource("de/netsysit/ressources/gfx/symbols/ex_red.gif");
		}catch(java.lang.Throwable exp){exp.printStackTrace();}
		if(fallBack==null)
		{
			try{
				javax.swing.Icon ic=javax.swing.UIManager.getIcon("OptionPane.errorIcon");
				java.awt.image.BufferedImage bi=null;
				if(ic!=null)
					bi=de.elbosso.ui.image.Utilities.iconToImage(ic);
				else
					bi=new java.awt.image.BufferedImage(getSize().getPixelSize()[0],getSize().getPixelSize()[0], BufferedImage.TYPE_INT_ARGB);
				java.io.File f=java.io.File.createTempFile("fallbackicon", "png");
				f.deleteOnExit();
				javax.imageio.ImageIO.write(bi, "png", f);
				fallBack=f.toURI().toURL();
			}catch(java.lang.Throwable exp){exp.printStackTrace();}
		}
	}

	public static IconSize getSize()
	{
		return size;
	}

	public static void setSize(IconSize size)
	{
		ResourceLoader.size = size;
		if(java.awt.GraphicsEnvironment.isHeadless()==false)
		{
			java.awt.Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

			if ((screensize.width < 1281) || (screensize.height < 1025))
			{
				switch (ResourceLoader.size)
				{
					case large:
					{
						ResourceLoader.size = IconSize.medium;
						break;
					}
					case medium:
					{
						ResourceLoader.size = IconSize.small;
						break;
					}
					default:
					{

					}
				}
			}

		}
	}

	public static synchronized void configure(java.util.Properties presets) throws java.io.IOException
	{
		configure(null,presets);
	}
	public static synchronized void configure(java.io.File conf) throws java.io.IOException
	{
		configure(conf,null);
	}
	public static synchronized void configure(java.io.File conf,java.util.Properties presets) throws java.io.IOException
	{
		if(presets!=null)
		{
			props.putAll(presets);
		}
		if((conf!=null)&&((conf.exists())&&(conf.isDirectory()==false)))
		{
			java.io.InputStream is=new java.io.FileInputStream(conf);
			java.util.Properties p=new java.util.Properties();
			p.load(is);
			props.putAll(p);
			is.close();
			f=conf;
		}
	}
	public static synchronized void writeConfiguration(java.io.File conf) throws java.io.IOException
	{
		if((conf!=null)&&(conf.isDirectory()==false))
		{
			java.io.OutputStream os=new java.io.FileOutputStream(conf);
			props.store(os,"icon_trans");
			os.close();
		}
	}
	public static synchronized java.net.URL getImgResource(java.lang.String arg)
	{
		java.net.URL rv=getResource(arg);
		if(rv==null)
		{
			if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace("getImgResource "+arg);
			rv=fallBack;
		}
		return rv;
	}
	public static synchronized java.net.URL getImgResourceUnaltered(java.lang.String arg)
	{
		java.net.URL rv=getResourceUnaltered(arg);
		if(rv==null)
		{
			if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace("getImgResourceUnaltered "+arg);
			rv=fallBack;
		}
		return rv;
	}
	public static synchronized java.net.URL getResource(java.lang.String arg)
	{
		java.net.URL rv=null;
		try
		{
			if(props.containsKey(arg))
			{
				int[] ps=null;
				java.lang.String replacement=props.getProperty(arg,arg);
				java.lang.String prefix=null;
				java.lang.String suffix=null;
				java.util.regex.Matcher m=pat1.matcher(replacement);
				if(m.matches())
				{
					prefix=m.group(1);
					suffix=m.group(3);
					int osize=java.lang.Integer.parseInt(m.group(2));
					if(osize==48)
						ps=size.getPixelSize();
					else
						ps=size.getSmallerPixelSize();
				}
				if(ps!=null)
				{
					for (int size : ps)
					{
						replacement = prefix + "_" + size + suffix+".png";
						rv = ResourceLoader.class.getClassLoader().getResource(replacement);
						if(rv!=null)
							break;
					}
				}
				else
				{
					rv = ResourceLoader.class.getClassLoader().getResource(replacement);
				}
				if (rv == null)
					rv = ResourceLoader.class.getClassLoader().getResource(arg);
			}
			else
			{
				int[] ps=null;
				java.lang.String prefix=null;
				java.lang.String suffix=null;
				java.lang.String replacement=arg;
				java.util.regex.Matcher m=pat2.matcher(arg);
				if(m.matches())
				{
					prefix=m.group(1);
					suffix=m.group(3);
					int osize=java.lang.Integer.parseInt(m.group(2));
					if(osize==48)
						ps=size.getPixelSize();
					else
						ps=size.getSmallerPixelSize();
				}
				if(ps!=null)
				{
					for (int size : ps)
					{
						replacement = prefix + "_" + size + suffix+".png";
						if (replacement.startsWith("!"))
							replacement = replacement.substring(1);
						rv = ResourceLoader.class.getClassLoader().getResource(replacement);
						if(rv!=null)
							break;

					}
				}
				else
				{
					if (replacement.startsWith("!"))
						replacement = replacement.substring(1);
					rv = ResourceLoader.class.getClassLoader().getResource(replacement);
				}
				if(rv!=null)
					props.setProperty(arg, replacement);
			}
		}
		catch(java.lang.Throwable t)
		{
//				t.printStackTrace();
			rv=ResourceLoader.class.getClassLoader().getResource(arg);
		}
		return rv;
	}
	public static synchronized java.net.URL getResourceUnaltered(java.lang.String arg)
	{
		java.net.URL rv=null;

		try
		{
			if(props.containsKey(arg))
			{
				java.lang.String replacement=props.getProperty(arg,arg);
				rv=ResourceLoader.class.getClassLoader().getResource(replacement);
//				rv.openConnection();
				if(rv==null)
					rv=ResourceLoader.class.getClassLoader().getResource(arg);
			}
			else
			{
				rv=ResourceLoader.class.getClassLoader().getResource(arg);
				if(rv!=null)
					props.setProperty(arg, arg);
			}
		}
		catch(java.lang.Throwable t)
		{
//			t.printStackTrace();
			rv=ResourceLoader.class.getClassLoader().getResource(arg);
		}

		return rv;
	}

}

