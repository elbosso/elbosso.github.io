package de.netsysit.util;
//$Id$

import java.util.logging.Level;
import java.util.logging.Logger;

/*#LICENCE#*/
public class ResourceLoader extends java.lang.Object
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(ResourceLoader.class);
	private static java.util.Properties props=new java.util.Properties();
	private static java.io.File f;
	private static IconSize size;
	private static java.net.URL fallBack;
	
	public enum IconSize
	{
		small(32),medium(48),large(64);
		
		private final int pixelSize;
		private final int smallersize;

		private IconSize(int pixelSize)
		{
			this.pixelSize=pixelSize;
			smallersize=pixelSize==48?32:pixelSize*3/4;
		}
		public int getPixelSize()
		{
			return pixelSize;
		}
		public int getSmallerPixelSize()
		{
			return smallersize;
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
				java.awt.image.BufferedImage bi=de.elbosso.ui.image.Utilities.iconToImage(ic);
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
			//		System.out.println(screensize+" "+ResourceLoader.size);
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
			//		System.out.println(screensize+" $ "+ResourceLoader.size);
		}
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
		if((conf.exists())&&(conf.isDirectory()==false))
		{
			java.io.InputStream is=new java.io.FileInputStream(conf);
			java.util.Properties p=new java.util.Properties();
			p.load(is);
			props.putAll(p);
			is.close();
			f=conf;
//			p.list(System.err);
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
//		if(arg.contains("Previous"))
//			System.out.println(arg+" "+props.containsKey(arg));
			try
			{
				if(props.containsKey(arg))
				{
//					System.out.println("huhu");
					java.lang.String replacement=props.getProperty(arg,arg);
//		if(arg.contains("Previous"))
//					System.out.println(replacement);
					java.util.regex.Pattern pat=java.util.regex.Pattern.compile("(.*?)_(\\d*?).png");
					java.util.regex.Matcher m=pat.matcher(replacement);
					if(m.matches())
					{
						java.lang.String prefix=m.group(1);
						int osize=java.lang.Integer.parseInt(m.group(2));
						if(osize==48)
							replacement=prefix+"_"+size.getPixelSize()+".png";
						else //if(replacement.contains("32."))
							replacement=prefix+"_"+size.getSmallerPixelSize()+".png";
					}
//					else
//		if(arg.contains("Previous"))
//						System.out.println(replacement);
					rv=ResourceLoader.class.getClassLoader().getResource(replacement);
	//				rv.openConnection();
					if(rv==null)
						rv=ResourceLoader.class.getClassLoader().getResource(arg);
				}
				else
				{
					java.util.regex.Pattern pat=java.util.regex.Pattern.compile("(.*?)_(\\d*?).png");
					java.util.regex.Matcher m=pat.matcher(arg);
					if(m.matches())
					{
						java.lang.String prefix=m.group(1);
						int osize=java.lang.Integer.parseInt(m.group(2));
						if(osize==48)
							arg=prefix+"_"+size.getPixelSize()+".png";
						else //if(replacement.contains("32."))
							arg=prefix+"_"+size.getSmallerPixelSize()+".png";
					}
//					else
//						System.out.println(arg);
					if(arg.startsWith("!"))
						arg=arg.substring(1);
					rv=ResourceLoader.class.getClassLoader().getResource(arg);
					if(rv!=null)
						props.setProperty(arg, arg);
				}
			}
			catch(java.lang.Throwable t)
			{
//				t.printStackTrace();
				rv=ResourceLoader.class.getClassLoader().getResource(arg);
			}
//			if(rv==null)
//		System.out.println(arg+" "+rv);
		return rv;
	}
	public static synchronized java.net.URL getResourceUnaltered(java.lang.String arg)
	{
		java.net.URL rv=null;
//		System.out.println(arg+" "+props.containsKey(arg));
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
//		System.out.println(arg+" "+rv);
		return rv;
	}

}

