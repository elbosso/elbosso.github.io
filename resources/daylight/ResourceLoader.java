package de.netsysit.util;
//$Id$

import de.elbosso.util.Utilities;
import org.slf4j.event.Level;

import javax.accessibility.AccessibleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.beans.Transient;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/*#LICENCE#*/
public class ResourceLoader extends java.lang.Object
{
	private final static org.slf4j.Logger EXCEPTION_LOGGER =org.slf4j.LoggerFactory.getLogger("ExceptionCatcher");
	private final static org.slf4j.Logger CLASS_LOGGER =org.slf4j.LoggerFactory.getLogger(ResourceLoader.class);
	private final static java.util.regex.Pattern pat1=java.util.regex.Pattern.compile("([^\\!].*?)_(\\d*?)([^\\d]*?)\\.png");
	private final static java.util.regex.Pattern pat2=java.util.regex.Pattern.compile("(.*?)_(\\d*?)([^\\d]*?)\\.png");
	private static java.util.Properties props=new java.util.Properties();
	private static java.io.File f;
	private static IconSize size;
	private static java.net.URL fallBack;
	private static java.util.Map<java.lang.String, java.net.URL> resourceCache=new java.util.HashMap();
	private static java.util.Map<java.lang.String, java.net.URL> resourceCacheUnaltered=new java.util.HashMap();
	private static java.io.File dockerSecretRoot=new java.io.File("/run/secrets/");
	private static java.io.File destinationDir;
	private static boolean storeLoadedImages;

	public static InputStream getResourceAsStream(String s)
	{
		java.io.InputStream rv=null;
		java.net.URL url=getResource(s);
		if(url!=null)
		{
			try
			{
				java.net.URLConnection conn = url.openConnection();
				rv = conn.getInputStream();
			}
			catch(java.io.IOException exp)
			{
				CLASS_LOGGER.warn(exp.getMessage(),exp);
			}
		}
		return rv;
	}

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
			java.io.InputStream is=ResourceLoader.class.getClassLoader().getResource("de/netsysit/ressources/data/icon_trans.properties").openStream();
			props.load(is);
			is.close();
		}catch(java.lang.Throwable exp){EXCEPTION_LOGGER.error(exp.getMessage(),exp);}
		manageFallback();
		storeLoadedImages=false;
		java.lang.String destinationDirName=System.getProperty(ResourceLoader.class.getName()+".destinationDirName");
		if(destinationDirName!=null)
		{
			destinationDir=new java.io.File(destinationDirName);
			if (destinationDir.exists() == false)
				destinationDir.mkdirs();
			if ((destinationDir.exists()) && (destinationDir.isDirectory()))
				storeLoadedImages = true;
		}
	}
	static void manageFallback()
	{
		try{
			fallBack=ResourceLoader.class.getClassLoader().getResource("de/netsysit/ressources/gfx/symbols/ex_red.gif");
		}catch(java.lang.Throwable exp){EXCEPTION_LOGGER.error(exp.getMessage(),exp);}
		if(fallBack==null)
		{
			try{
				java.awt.image.BufferedImage bi=null;
				javax.swing.Icon ic=javax.swing.UIManager.getIcon("OptionPane.errorIcon");
				if((java.awt.GraphicsEnvironment.isHeadless()==false)&&(ic!=null))
					bi=de.elbosso.ui.image.Utilities.iconToImage(ic);
				else
					bi=new java.awt.image.BufferedImage(getSize().getPixelSize()[0],getSize().getPixelSize()[0], BufferedImage.TYPE_INT_ARGB);
				java.io.File f=java.io.File.createTempFile("fallbackicon", "png");
				f.deleteOnExit();
				javax.imageio.ImageIO.write(bi, "png", f);
				fallBack=f.toPath().toUri().toURL();
			}catch(java.lang.Throwable exp)
			{
				EXCEPTION_LOGGER.warn(exp.getMessage(),exp);
				try{
					java.awt.image.BufferedImage bi=new java.awt.image.BufferedImage(getSize().getPixelSize()[0],getSize().getPixelSize()[0], BufferedImage.TYPE_INT_ARGB);
					java.io.File f=java.io.File.createTempFile("fallbackicon", "png");
					f.deleteOnExit();
					javax.imageio.ImageIO.write(bi, "png", f);
					fallBack=f.toPath().toUri().toURL();
				}catch(java.lang.Throwable ex)
				{
					EXCEPTION_LOGGER.warn(ex.getMessage(), ex);
				}
			}
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
		resourceCache.clear();
		manageFallback();
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
	private static void saveAsPNG(java.net.URL url)
	{
		if(url!=null)
		{
			if(storeLoadedImages)
			{
				try
				{
					java.awt.image.BufferedImage bimg = javax.imageio.ImageIO.read(url);
					java.io.File destinationFile = new java.io.File(destinationDir, url.getPath().substring(url.getPath().lastIndexOf('/') + 1));
					javax.imageio.ImageIO.write(bimg, "png", destinationFile);
				}
				catch(java.io.IOException exp)
				{
					EXCEPTION_LOGGER.warn(exp.getMessage(),exp);
				}
			}
		}
	}
	public static synchronized java.net.URL getImgResource(java.lang.String arg)
	{
		java.net.URL rv=getResource(arg);
		if(rv==null)
		{
			CLASS_LOGGER.trace("getImgResource "+arg);
			rv=fallBack;
		}
		else
			saveAsPNG(rv);
		return rv;
	}
	public static synchronized java.net.URL getImgResourceUnaltered(java.lang.String arg)
	{
		java.net.URL rv=getResourceUnaltered(arg);
		if(rv==null)
		{
			CLASS_LOGGER.trace("getImgResourceUnaltered "+arg);
			rv=fallBack;
		}
		else
			saveAsPNG(rv);
		return rv;
	}
	public static synchronized java.net.URL getDockerSecretResource(java.lang.String arg)
	{
		return getDockerSecretResource(arg,false);
	}
	public static synchronized java.net.URL getDockerSecretResource(java.lang.String arg,boolean classpathFallback)
	{
		java.net.URL rv=null;
		if(dockerSecretRoot.exists())
		{
			java.io.File f=new java.io.File(dockerSecretRoot,arg);
			if(f.exists())
			{
				try
				{
					rv = f.toPath().toUri().toURL();
				}
				catch (java.net.MalformedURLException exp)
				{
					rv = null;
					
						CLASS_LOGGER.warn(exp.getMessage(),exp);
				}
			}
		}
		if((rv==null)&&(classpathFallback==true))
		{
			CLASS_LOGGER.warn("found no docker secret named "+arg+" - searching instead on CLASSPATH!");
			rv = getResource(arg);
		}
		return rv;
	}
	public static synchronized java.net.URL getResource(java.lang.String arg)
	{
		java.net.URL rv=null;
		if(resourceCache.containsKey(arg))
		{
			rv = resourceCache.get(arg);
			CLASS_LOGGER.trace("found in cache: "+arg+" - "+rv);
		}
		else
		{
			try
			{
				 CLASS_LOGGER.trace("requested resource " + arg);
				if (props.containsKey(arg))
				{
					int[] ps = null;
					java.lang.String replacement = props.getProperty(arg, arg);
					while(true)//(replacement.equals(arg)==false)
					{
						if (replacement.startsWith("#"))
						{
							 CLASS_LOGGER.trace("found url in props " + replacement);
							rv = new java.net.URL(replacement.substring(1));
						}
						else
						{
							
								CLASS_LOGGER.trace("replacement in props " + replacement);
							java.lang.String prefix = null;
							java.lang.String suffix = null;
							java.util.regex.Matcher m = pat1.matcher(replacement);
							java.lang.String osizeString=null;
							if (m.matches())
							{
								prefix = m.group(1);
								suffix = m.group(3);
								osizeString=m.group(2);
								int osize = java.lang.Integer.parseInt(m.group(2));
								if (osize == 48)
									ps = size.getPixelSize();
								else
									ps = size.getSmallerPixelSize();
								
									CLASS_LOGGER.trace("prefix#suffix#osize#ps " + prefix + "#" + suffix + "#" + osize + "#" + ps.length);
							}
							else
							{
								if(replacement.startsWith("!"))
									replacement=replacement.substring(1);
							}
							if (ps != null)
							{
								for (int size : ps)
								{
									java.lang.String constructedReplacement = prefix + "_" + size + suffix + ".png";
									if(prefix.contains(osizeString))
									{
										constructedReplacement=(prefix.replace(osizeString,java.lang.Integer.toString(size))) + "_" + size + suffix + ".png";
									}
									
										CLASS_LOGGER.trace("trying " + constructedReplacement);
									rv = ResourceLoader.class.getClassLoader().getResource(constructedReplacement);
									if (rv != null)
										break;
								}
							}
							if (rv == null)
							{
								 CLASS_LOGGER.trace("trying " + replacement);
								rv = ResourceLoader.class.getClassLoader().getResource(replacement);
							}
							if (rv == null)
							{
								 CLASS_LOGGER.trace("trying " + arg);
								rv = ResourceLoader.class.getClassLoader().getResource(arg);
							}
						}
						arg = replacement;
						replacement = props.getProperty(arg, arg);
						if(replacement.equals(arg))
							break;
					}
				}
				else
				{
					int[] ps = null;
					java.lang.String prefix = null;
					java.lang.String suffix = null;
					java.lang.String replacement = arg;
					if (replacement.startsWith("#"))
					{
						 CLASS_LOGGER.trace("found url in props " + replacement);
						rv = new java.net.URL(replacement.substring(1));
					}
					else
					{
						java.util.regex.Matcher m = pat2.matcher(arg);
						if (m.matches())
						{
							prefix = m.group(1);
							suffix = m.group(3);
							int osize = java.lang.Integer.parseInt(m.group(2));
							if (osize == 48)
								ps = size.getPixelSize();
							else
								ps = size.getSmallerPixelSize();
							if (ps != null)
							{
								for (int sizepx : ps)
								{
									replacement = prefix + "_" + java.lang.Integer.toString(sizepx) + suffix + ".png";
									if (replacement.startsWith("!"))
										replacement = replacement.substring(1);
									rv = ResourceLoader.class.getClassLoader().getResource(replacement);
									if (rv != null)
										break;

								}
								if(rv==null)
								{
									replacement = prefix + "_" + java.lang.Integer.toString(osize) + suffix + ".png";
									if (replacement.startsWith("!"))
										replacement = replacement.substring(1);
									rv = ResourceLoader.class.getClassLoader().getResource(replacement);
								}
							}
							else
							{
								if (replacement.startsWith("!"))
									replacement = replacement.substring(1);
								rv = ResourceLoader.class.getClassLoader().getResource(replacement);
							}
						}
						else
						{
							if (replacement.startsWith("!"))
								replacement = replacement.substring(1);
							rv = ResourceLoader.class.getClassLoader().getResource(replacement);
						}
						//if (rv != null)
						//	props.setProperty(arg, replacement);
					}
				}
			} catch (java.lang.Throwable t)
			{
				//				EXCEPTION_LOGGER.error(t.getMessage(),t);
				rv = ResourceLoader.class.getClassLoader().getResource(arg);
			}
			if(rv!=null)
				resourceCache.put(arg,rv);
		}
		return rv;
	}
	public static synchronized java.net.URL getResourceUnaltered(java.lang.String arg)
	{
		java.net.URL rv=null;
		if(resourceCacheUnaltered.containsKey(arg))
		{
			rv = resourceCacheUnaltered.get(arg);
			CLASS_LOGGER.trace("found in cache: "+arg+" - "+rv);
		}
		else
		{
			try
			{
				if (props.containsKey(arg))
				{
					java.lang.String replacement = props.getProperty(arg, arg);
					rv = ResourceLoader.class.getClassLoader().getResource(replacement);
					//				rv.openConnection();
					if (rv == null)
						rv = ResourceLoader.class.getClassLoader().getResource(arg);
				}
				else
				{
					rv = ResourceLoader.class.getClassLoader().getResource(arg);
					if (rv != null)
						props.setProperty(arg, arg);
				}
			} catch (java.lang.Throwable t)
			{
				//			EXCEPTION_LOGGER.error(t.getMessage(),t);
				rv = ResourceLoader.class.getClassLoader().getResource(arg);
			}
			if(rv!=null)
				resourceCacheUnaltered.put(arg,rv);
		}
		return rv;
	}
	public static void main(java.lang.String[] args) throws IOException
	{
		Utilities.configureBasicStdoutLogging(org.slf4j.event.Level.TRACE);
		de.netsysit.util.ResourceLoader.setSize(IconSize.small);
		javax.swing.ImageIcon i1=de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/ca/Makro expandieren_48.png");
		CLASS_LOGGER.trace(i1.getIconWidth()+" "+i1.getIconHeight());
		javax.swing.ImageIcon i2=de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/ca/Makro expandieren_48.png");
		CLASS_LOGGER.trace(i2.getIconWidth()+" "+i2.getIconHeight());
		i1=de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/New24.gif");
		CLASS_LOGGER.trace(i1.getIconWidth()+" "+i1.getIconHeight());
		i1=de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Open24.gif");
		CLASS_LOGGER.trace(i1.getIconWidth()+" "+i1.getIconHeight());
		java.util.Properties iconFallbacks = new java.util.Properties();
		java.io.InputStream is=de.netsysit.util.ResourceLoader.getResource("de/elbosso/ressources/data/icon_trans_material.properties").openStream();
		iconFallbacks.load(is);
		is.close();
		de.netsysit.util.ResourceLoader.configure(iconFallbacks);
		javax.swing.JToolBar tb=new javax.swing.JToolBar();
		i1=de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/ca/Makro expandieren_48.png");
		CLASS_LOGGER.trace(i1.getIconWidth()+" "+i1.getIconHeight());
		i1=de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/New24.gif");
		CLASS_LOGGER.trace(i1.getIconWidth()+" "+i1.getIconHeight());
		javax.swing.Action act=new javax.swing.AbstractAction(null,i1)
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

			}
		};
		tb.add(act);
		i1=de.netsysit.util.ResourceLoader.getIcon("toolbarButtonGraphics/general/Open24.gif");
		CLASS_LOGGER.trace(i1.getIconWidth()+" "+i1.getIconHeight());
		act=new javax.swing.AbstractAction(null,i1)
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

			}
		};
		tb.add(act);
		i1=de.netsysit.util.ResourceLoader.getIcon("de/netsysit/ressources/gfx/symbols/ex_red.gif");
		CLASS_LOGGER.trace(i1.getIconWidth()+" "+i1.getIconHeight());
		act=new javax.swing.AbstractAction(null,i1)
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{

			}
		};
		tb.add(act);
		javax.swing.JFrame f=new javax.swing.JFrame();
		f.setContentPane(tb);
		f.pack();
		f.setVisible(true);
	}
}

