/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.elbosso.util.beans;

import de.elbosso.util.lang.annotations.BeanInfo;
import de.elbosso.util.lang.annotations.Event;
import de.elbosso.util.lang.annotations.Method;
import de.elbosso.util.lang.annotations.Property;

/**
 *
 * @author elbosso
 */
@BeanInfo(
//		i18nBundle = "\"de.netsysit.util.6i18n\""
)
public class EventHandlingSupport extends java.lang.Object implements de.netsysit.util.beans.PropertyChangeSender
{
	private final java.beans.PropertyChangeSupport pcs;


	/**
	 * The constructor doesnt really do anything
	 */
	public EventHandlingSupport() 
	{
		super();
        pcs = new java.beans.PropertyChangeSupport(this);
	}


	/**
	 *  This method registers listeners for PropertyChangeEvents.
	 *
	 * @param  name  The name of the property for which a particular listener wishes to be informed about changes.
	 * @param  l     The listener interested in changes in the named property.
	 */
	public void addPropertyChangeListener(String name, java.beans.PropertyChangeListener l)
    {
//		if(name.equals("inAndOuts"))

		pcs.addPropertyChangeListener(name, l);
	}


	/**
	 *  This method deregisters listeners from PropertyChangeEvents.
	 *
	 * @param  name  The name of the property for which a particular listener is no longer interested
	 * @param  l     The listener to be deregistered.
	 */
	public void removePropertyChangeListener(String name, java.beans.PropertyChangeListener l)
    {
		pcs.removePropertyChangeListener(name, l);
	}

/*	@Property(
			description = "\"huhu\"",
			displayName = "\"hallo\"",
			bound = true
	)
	public int getProperty()
	{
		return -1;
	}
	@Method
			(
					displayName="i18n.getString(\"ScalarBoxFilter.putInputBoxed.displayName\")"
			)
	public void meth()
	{

	}
*/	/**
	 *  This method registers listeners for PropertyChangeEvents.
	 *
	 * @param  l     The listener interested in changes in the named property.
	 */

	@Event(
		inDefaultEventSet = true
		)
	public void addPropertyChangeListener( java.beans.PropertyChangeListener l)
    {
		pcs.addPropertyChangeListener(l);
	}


	/**
	 *  This method deregisters listeners from PropertyChangeEvents.
	 *
	 * @param  l     The listener to be deregistered.
	 */
	@Event(
		inDefaultEventSet = true
		)
	public void removePropertyChangeListener(java.beans.PropertyChangeListener l)
    {
		pcs.removePropertyChangeListener( l);
	}


	/**
	 *  This method can be used whenever listeners shall be informed about a change of a property
	 *  of type boolean.
	 *
	 * @param  name      The name of the property.
	 * @param  oldvalue  The old value.
	 * @param  newvalue  The new value.
	 */
	public void send(java.lang.String name, boolean oldvalue, boolean newvalue)
    {
		if(pcs.hasListeners(name))
			send(new java.beans.PropertyChangeEvent(this, name, java.lang.Boolean.valueOf(oldvalue), java.lang.Boolean.valueOf(newvalue)));
	}


	/**
	 *  This method can be used whenever listeners shall be informed about a change of a property
	 *  of type byte.
	 *
	 * @param  name      The name of the property.
	 * @param  oldvalue  The old value.
	 * @param  newvalue  The new value.
	 */
	public void send(java.lang.String name, byte oldvalue, byte newvalue)
    {
		if(pcs.hasListeners(name))
			send(new java.beans.PropertyChangeEvent(this, name, java.lang.Byte.valueOf(oldvalue), java.lang.Byte.valueOf(newvalue)));
	}


	/**
	 *  This method can be used whenever listeners shall be informed about a change of a property
	 *  of type short.
	 *
	 * @param  name      The name of the property.
	 * @param  oldvalue  The old value.
	 * @param  newvalue  The new value.
	 */
	public void send(java.lang.String name, short oldvalue, short newvalue)
    {
		if(pcs.hasListeners(name))
			send(new java.beans.PropertyChangeEvent(this, name, java.lang.Short.valueOf(oldvalue), java.lang.Short.valueOf(newvalue)));
	}


	/**
	 *  This method can be used whenever listeners shall be informed about a change of a property
	 *  of type int.
	 *
	 * @param  name      The name of the property.
	 * @param  oldvalue  The old value.
	 * @param  newvalue  The new value.
	 */
	public void send(java.lang.String name, int oldvalue, int newvalue)
    {
		if(pcs.hasListeners(name))
			send(new java.beans.PropertyChangeEvent(this, name, java.lang.Integer.valueOf(oldvalue), java.lang.Integer.valueOf(newvalue)));
	}


	/**
	 *  This method can be used whenever listeners shall be informed about a change of a property
	 *  of type double.
	 *
	 * @param  name      The name of the property.
	 * @param  oldvalue  The old value.
	 * @param  newvalue  The new value.
	 */
	public void send(java.lang.String name, double oldvalue, double newvalue)
    {
		if(pcs.hasListeners(name))
			send(new java.beans.PropertyChangeEvent(this, name, java.lang.Double.valueOf(oldvalue), java.lang.Double.valueOf(newvalue)));
	}


	/**
	 *  This method can be used whenever listeners shall be informed about a change of a property
	 *  of type float.
	 *
	 * @param  name      The name of the property.
	 * @param  oldvalue  The old value.
	 * @param  newvalue  The new value.
	 */
	public void send(java.lang.String name, float oldvalue, float newvalue)
    {
		if(pcs.hasListeners(name))
			send(new java.beans.PropertyChangeEvent(this, name, java.lang.Float.valueOf(oldvalue), java.lang.Float.valueOf(newvalue)));
	}


	/**
	 *  This method can be used whenever listeners shall be informed about a change of a property
	 *  of type long.
	 *
	 * @param  name      The name of the property.
	 * @param  oldvalue  The old value.
	 * @param  newvalue  The new value.
	 */
	public void send(java.lang.String name, long oldvalue, long newvalue)
    {
		if(pcs.hasListeners(name))
			send(new java.beans.PropertyChangeEvent(this, name, java.lang.Long.valueOf(oldvalue), java.lang.Long.valueOf(newvalue)));
	}


	/**
	 *  This method can be used whenever listeners shall be informed about a change of a property
	 *  of any class type.
	 *
	 * @param  name      The name of the property.
	 * @param  oldvalue  The old value.
	 * @param  newvalue  The new value.
	 */
	public void send(java.lang.String name, java.lang.Object oldvalue, java.lang.Object newvalue)
    {
//		if(name.equals("inAndOuts"))
//			new java.lang.Throwable().printStackTrace();
		if(pcs.hasListeners(name))
			send(new java.beans.PropertyChangeEvent(this, name, oldvalue, newvalue));
	}

	public void send(java.beans.PropertyChangeEvent evt)
	{
		pcs.firePropertyChange(evt);
	}
}