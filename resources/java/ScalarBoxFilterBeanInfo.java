package de.netsysit.dataflowframework.modules.filter;

import java.beans.BeanDescriptor;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;
import java.lang.reflect.Method;

public class ScalarBoxFilterBeanInfo
    extends java.beans.SimpleBeanInfo {

    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( de.netsysit.dataflowframework.modules.filter.ScalarBoxFilter.class , null );
//        beanDescriptor.setHidden(true);
//        beanDescriptor.setExpert(true);
//        beanDescriptor.setShortDescription(null);
//        beanDescriptor.setPreferred(true);
//        beanDescriptor.setShortDescription(null);
//        beanDescriptor.setDisplayName(null);
//          beanDescriptor.setValue(ModuleWidget.DEFAULTLAYERTITLE,"Commodities");
    // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;
     }

    // Property array
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        java.util.Map<String,PropertyDescriptor> properties = new java.util.HashMap();

        try {
            properties.put("width",new PropertyDescriptor ( "width", de.netsysit.dataflowframework.modules.filter.ScalarBoxFilter.class,  "getWidth" , "setWidth"  ));
//            properties.get("width").setBound(true);
//            properties.get("width").setConstrained(true);
//            properties.get("width").setPropertyEditorClass(null);
//            properties.get("width").setExpert(true);
//            properties.get("width").setHidden(true);
//            properties.get("width").setDisplayName(null);
//            properties.get("width").setPreferred(true);
//            properties.get("width").setShortDescription(null);
//              properties.get("width").setValue(de.netsysit.dataflowframework.ui.Slot.AUTOCONNECTALLOWEDATTRIBUTE, java.lang.Boolean.TRUE);
//              properties.get("width").setValue("Generic", java.lang.Boolean.TRUE);
//              properties.get("width").setValue(Slot.MAXCONNECTIONSALLOWED, java.lang.Integer.valueOf(2));
            properties.put("filterresult",new PropertyDescriptor ( "filterresult", de.netsysit.dataflowframework.modules.filter.ScalarBoxFilter.class,  "getFilterresult" ,  null  ));
//            properties.get("filterresult").setBound(true);
//            properties.get("filterresult").setConstrained(true);
//            properties.get("filterresult").setPropertyEditorClass(null);
//            properties.get("filterresult").setExpert(true);
//            properties.get("filterresult").setHidden(true);
//            properties.get("filterresult").setDisplayName(null);
//            properties.get("filterresult").setPreferred(true);
//            properties.get("filterresult").setShortDescription(null);
              properties.get("filterresult").setValue(de.netsysit.dataflowframework.ui.Slot.AUTOCONNECTALLOWEDATTRIBUTE, java.lang.Boolean.TRUE);
//              properties.get("filterresult").setValue("Generic", java.lang.Boolean.TRUE);
//              properties.get("filterresult").setValue(Slot.MAXCONNECTIONSALLOWED, java.lang.Integer.valueOf(2));

        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }

    // Here you can add code for customizing the properties array.
//        properties.get("").setValue(de.netsysit.ui.beans.customizerwidgets.java.lang.StringCustomizer.JAVA_EDITOR_KEY, java.lang.Boolean.TRUE);
//        properties.get("").setValue(InterfaceFactory.HIDDEN,"true");
//        properties.get("").setValue("transient", Boolean.TRUE);
//        properties.get("").setValue(de.netsysit.ui.beans.customizerwidgets.java.lang.StringCustomizer.OPTIONS_KEY, de.elbosso.dataflowframework.modules.communication.ControllerTypeModel.getModel(net.java.games.input.Controller.Type.GAMEPAD));
//        properties.get("").setValue(de.netsysit.ui.beans.customizerwidgets.java.io.FileCustomizer.FILESELECTIONMODE, de.netsysit.ui.beans.customizerwidgets.java.io.FileCustomizer.DIRECTORIES_ONLY);
//        properties.get("").setValue(de.netsysit.ui.beans.customizerwidgets.booleanCustomizer.NORMAL_ICON_KEY,normal);
//        properties.get("").setValue(de.netsysit.ui.beans.customizerwidgets.booleanCustomizer.SELECTED_ICON_KEY,selected);
//        properties.get("").setValue(de.netsysit.ui.beans.customizerwidgets.booleanCustomizer.DISABLED_NORMAL_ICON_KEY,normal);
//        properties.get("").setValue(de.netsysit.ui.beans.customizerwidgets.booleanCustomizer.DISABLED_SELECTED_ICON_KEY,selected);
/*        properties.get("").setValue(de.netsysit.ui.beans.customizerwidgets.arrayCustomizer.INDEXEDOBJECTCREATOR, new de.netsysit.util.ObjectCreator() {

			public Object create()
			{
				return new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB);
			}
		});*/
//        properties.get("").setValue(de.netsysit.util.validator.Rule.KEY,new de.netsysit.util.validator.rules.IntegerMinMaxRule(0, 100));
//        properties.get("").setValue(de.netsysit.dataflowframework.ui.beans.StateUpdaterManager.STATEUPDATER, "de.elbosso.dataflowframework.ui.beans.NixieIntegerStateUpdater");

        return new java.util.LinkedList<PropertyDescriptor>(properties.values()).toArray(new PropertyDescriptor[0]);
        }

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        java.util.Map<String,EventSetDescriptor> eventSets = new java.util.HashMap();

        try {
            eventSets.put("propertyChangeListener",new EventSetDescriptor (  de.elbosso.util.beans.EventHandlingSupport.class , "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] {"propertyChange"}, "addPropertyChangeListener" , "removePropertyChangeListener"  ));
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    // Here you can add code for customizing the event sets array.

        return new java.util.LinkedList<EventSetDescriptor>(eventSets.values()).toArray(new EventSetDescriptor[0]);
         }

    // Method array
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        java.util.Map<String,MethodDescriptor> methods = new java.util.HashMap();

        try {
            methods.put("putInputBoxed",new MethodDescriptor(de.netsysit.dataflowframework.modules.filter.ScalarBoxFilter.class.getMethod("putInputBoxed", new Class[] {
                        		                java.lang.Number.class
                        })));
//            methods.get("putInputBoxed").setDisplayName (null);
//            methods.get("putInputBoxed").setExpert(true);
//            methods.get("putInputBoxed").setPreferred(true);
//            methods.get("putInputBoxed").setShortDescription(null);
//            methods.get("putInputBoxed").setHidden(true);
//          methods.get("putInputBoxed").setValue(de.netsysit.dataflowframework.ui.Slot.AUTOCONNECTALLOWEDATTRIBUTE, java.lang.Boolean.TRUE);
//          methods.get("putInputBoxed").setValue("VariablePortCount", Boolean.TRUE);
//          methods.get("putInputBoxed").setValue("VariablePortCountMin", java.lang.Integer.valueOf(2));
//          methods.get("putInputBoxed").setValue("GenericPortDescription", new de.netsysit.dataflowframework.logic.GenericPortDescription(new java.lang.String[0]));
//          methods.get("putInputBoxed").setValue(Slot.MAXCONNECTIONSALLOWED, java.lang.Integer.valueOf(4));
        }
        catch( Exception e) {}//GEN-HEADEREND:Methods

    // Here you can add code for customizing the methods array.

        return new java.util.LinkedList<MethodDescriptor>(methods.values()).toArray(new MethodDescriptor[0]);
        }

    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons

    private static final int defaultPropertyIndex = -1;
    private static final int defaultEventIndex = -1;

// Here you can add code for customizing the Superclass BeanInfo.


    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     *
     * @return BeanDescriptor describing the editable
     * properties of this bean.  May return null if the
     * information should be obtained by automatic analysis.
     */
    public BeanDescriptor getBeanDescriptor() {
	return getBdescriptor();
    }

    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     *
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean.  May return null if the
     * information should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will
     * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
     * A client of getPropertyDescriptors can use "instanceof" to check
     * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
	return getPdescriptor();
    }

    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     *
     * @return  An array of EventSetDescriptors describing the kinds of
     * events fired by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public EventSetDescriptor[] getEventSetDescriptors() {
	return getEdescriptor();
    }

    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     *
     * @return  An array of MethodDescriptors describing the methods
     * implemented by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    public MethodDescriptor[] getMethodDescriptors() {
	return getMdescriptor();
    }

    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are
     * customizing the bean.
     * @return  Index of default property in the PropertyDescriptor array
     * 		returned by getPropertyDescriptors.
     * <P>	Returns -1 if there is no default property.
     */
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    /**
     * A bean may have a "default" event that is the event that will
     * mostly commonly be used by human's when using the bean.
     * @return Index of default event in the EventSetDescriptor array
     *		returned by getEventSetDescriptors.
     * <P>	Returns -1 if there is no default event.
     */
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    /**
     * This method returns an image object that can be used to
     * represent the bean in toolboxes, toolbars, etc.   Icon images
     * will typically be GIFs, but may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from
     * this method.
     * <p>
     * There are four possible flavors of icons (16x16 color,
     * 32x32 color, 16x16 mono, 32x32 mono).  If a bean choses to only
     * support a single icon we recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background
     * so they can be rendered onto an existing background.
     *
     * @param  iconKind  The kind of icon requested.  This should be
     *    one of the constant values ICON_COLOR_16x16, ICON_COLOR_32x32,
     *    ICON_MONO_16x16, or ICON_MONO_32x32.
     * @return  An image object representing the requested icon.  May
     *    return null if no suitable icon is available.
     */
    public java.awt.Image getIcon(int iconKind) {
        switch ( iconKind ) {
        case ICON_COLOR_16x16:
            if ( iconNameC16 == null )
                return null;
            else {
                if( iconColor16 == null )
                    iconColor16 = loadImage( iconNameC16 );
                return iconColor16;
            }
        case ICON_COLOR_32x32:
            if ( iconNameC32 == null )
                return null;
            else {
                if( iconColor32 == null )
                    iconColor32 = loadImage( iconNameC32 );
                return iconColor32;
            }
        case ICON_MONO_16x16:
            if ( iconNameM16 == null )
                return null;
            else {
                if( iconMono16 == null )
                    iconMono16 = loadImage( iconNameM16 );
                return iconMono16;
            }
        case ICON_MONO_32x32:
            if ( iconNameM32 == null )
                return null;
            else {
                if( iconMono32 == null )
                    iconMono32 = loadImage( iconNameM32 );
                return iconMono32;
            }
	default: return null;
        }
    }
}
