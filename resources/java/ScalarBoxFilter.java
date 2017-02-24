package de.netsysit.dataflowframework.modules.filter;

import de.elbosso.util.lang.annotations.BeanInfo;
import de.elbosso.util.lang.annotations.Method;
import de.elbosso.util.lang.annotations.Property;

//$Id:ScalarBoxFilter.java 12 2005-07-03 06:32:14Z elbosso $
@BeanInfo
public class ScalarBoxFilter extends de.netsysit.dataflowframework.modules.ThreadingModuleBase
{
	private final static org.apache.log4j.Logger CLASS_LOGGER=org.apache.log4j.Logger.getLogger(ScalarBoxFilter.class);
	private final static org.apache.log4j.Logger EXCEPTION_LOGGER=org.apache.log4j.Logger.getLogger("ExceptionCatcher");
	private de.elbosso.algorithms.filter.ScalarBoxFilter scalarBoxFilter;

	public ScalarBoxFilter()
	{
		super(ScalarBoxFilter.class.getName());
		scalarBoxFilter=new de.elbosso.algorithms.filter.ScalarBoxFilter();
		scalarBoxFilter.setWidth(5);
	}
	protected de.netsysit.util.threads.CubbyHole createCubbyHole()
	{
		return new de.netsysit.util.threads.SimpleBufferingCubbyHole();
	}

	@Method
	public  void putInputBoxed(Number nextnumber)
	{
		processData(nextnumber);
	}
/*	@de.elbosso.util.lang.annotations.Method
	public  void putInputBoxed2(Number nextnumber,boolean a)
	{
		processData(nextnumber);
	}
*/	protected synchronized void doWork(java.lang.Object ref) throws InterruptedException
	{
		if(ref!=null)
		{
			double next=((java.lang.Number)(ref)).doubleValue();
			double oldfilterresult=scalarBoxFilter.getFilterresult();
			scalarBoxFilter.doFilter(next);
			send("filterresult",oldfilterresult,scalarBoxFilter.getFilterresult());
		}
	}
	@Property
	public java.lang.Number getFilterresult()
	{
		return scalarBoxFilter.getFilterresult();
	}
	@Property
	public int getWidth()
	{
		return scalarBoxFilter.getWidth();
	}
	@Property
	public synchronized void setWidth(int newwidth)
	{
		int oldwidth=getWidth();
		scalarBoxFilter.setWidth(newwidth);
		send("width",oldwidth,getWidth());
	}

/*	@de.elbosso.util.lang.annotations.Event
	@Override
	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		super.addPropertyChangeListener(l);
	}

	@de.elbosso.util.lang.annotations.Event
	@Override
	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		super.removePropertyChangeListener(l);
	}
*/}
