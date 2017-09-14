/*
Copyright (c) 2012-2017.

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
/*
 * https://blogs.oracle.com/jmxetc/entry/dynamicmbeans,_modelmbeans,_and_pojos...
 */
package de.elbosso.util.lang.jmx;

import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

/**
 *
 * @author elbosso
 */
public class ModelPojoMBeanCreator extends java.lang.Object
{
		static java.util.List<java.lang.reflect.Method> getters = new java.util.ArrayList<java.lang.reflect.Method>();
		static java.util.Map<String, java.lang.reflect.Method> setters = new java.util.LinkedHashMap<String, java.lang.reflect.Method>();

	private ModelPojoMBeanCreator()
	{
		super();
	}

	public static javax.management.modelmbean.ModelMBean makeModelMBean(Object resource)
			throws javax.management.JMException, javax.management.modelmbean.InvalidTargetObjectTypeException, IntrospectionException
	{

		java.lang.reflect.Method[] methods = resource.getClass().getMethods();

		java.util.List<java.beans.PropertyDescriptor> properties = new java.util.ArrayList();
		java.util.List<java.beans.MethodDescriptor> operations = new java.util.ArrayList();
//		java.util.List<java.lang.reflect.Method> getters = new java.util.ArrayList<java.lang.reflect.Method>();
//		java.util.Map<String, java.lang.reflect.Method> setters = new java.util.LinkedHashMap<String, java.lang.reflect.Method>();

//		for (java.lang.reflect.Method method : methods)
//		{
//			// don't want to expose getClass(), hashCode(), equals(), etc...
//			if (method.getDeclaringClass().equals(Object.class))
//			{
//				continue;
//			}
//
//			if (method.getName().startsWith("get")
//					&& !method.getName().equals("get")
//					&& !method.getName().equals("getClass")
//					&& method.getParameterTypes().length == 0
//					&& method.getReturnType() != void.class)
//			{

//				getters.add(method);
//			}
//			if (method.getName().startsWith("set")
//					&& !method.getName().equals("set")
//					&& method.getParameterTypes().length == 1
//					&& method.getReturnType().equals(void.class))
//			{
//				setters.put(method.getName(), method);
//			}
//
////			operations.add(method);
//		}
		java.beans.BeanInfo bi=java.beans.Introspector.getBeanInfo(resource.getClass());
		java.beans.PropertyDescriptor[] pds=bi.getPropertyDescriptors();
		java.util.List<javax.management.modelmbean.ModelMBeanAttributeInfo> attrinfo
				= new java.util.ArrayList<javax.management.modelmbean.ModelMBeanAttributeInfo>();
		for (PropertyDescriptor pd : pds)
		{
			if(pd.isHidden()==false)
			{
				if(pd.getReadMethod()!=null)
					getters.add(pd.getReadMethod());
				if(pd.getWriteMethod()!=null)
					setters.put(pd.getWriteMethod().getName(),pd.getWriteMethod());
				attrinfo.add(makeAttribute(pd));
			}
		}
		java.beans.MethodDescriptor[] mds=bi.getMethodDescriptors();
		for (MethodDescriptor md : mds)
		{
			operations.add(md);
		}
//		for()

		javax.management.modelmbean.ModelMBeanAttributeInfo[] attrs
				= attrinfo.toArray(new javax.management.modelmbean.ModelMBeanAttributeInfo[attrinfo.size()]);
		operations.removeAll(getters);
		operations.removeAll(setters.values());
		int opcount = operations.size()+getters.size()+setters.size();
		javax.management.modelmbean.ModelMBeanOperationInfo[] ops
				= new javax.management.modelmbean.ModelMBeanOperationInfo[opcount];
		int i=0;
		for (MethodDescriptor md:operations)
		{
			ops[i] = new javax.management.modelmbean.ModelMBeanOperationInfo(md.getShortDescription(), md.getMethod());
			++i;
		}
		int j=0;
		for(java.lang.reflect.Method m:getters)
		{
			ops[operations.size()+j]=new javax.management.modelmbean.ModelMBeanOperationInfo(m.getName(), m);
			++j;
		}
		j=0;
		for(java.lang.reflect.Method m:setters.values())
		{
			ops[operations.size()+getters.size()+j]=new javax.management.modelmbean.ModelMBeanOperationInfo(m.getName(), m);
			++j;
		}
		javax.management.modelmbean.ModelMBeanInfo mmbi
				= new javax.management.modelmbean.ModelMBeanInfoSupport(resource.getClass().getName(),
						resource.getClass().getName(),
						attrs,
						null, // constructors
						ops,
						null); // notifications
		javax.management.modelmbean.ModelMBean mmb = new javax.management.modelmbean.RequiredModelMBean(mmbi);
		mmb.setManagedResource(resource, "ObjectReference");
		return mmb;
	}

	private static javax.management.modelmbean.ModelMBeanAttributeInfo makeAttribute(java.beans.PropertyDescriptor pd)
			throws javax.management.IntrospectionException
	{
		java.lang.reflect.Method getter=getters.get(0);//pd.getReadMethod();
			java.lang.reflect.Method setter=pd.getWriteMethod();

//		String attrName;
//		if (getter != null)
//		{
//			attrName = getter.getName().substring(3);
//		}
//		else
//		{
//			attrName = setter.getName().substring(3);
//		}

		java.util.List<String> descriptors = new java.util.ArrayList<String>();
		descriptors.add("name=" + pd.getName());
		descriptors.add("descriptorType=attribute");
		if (getter != null)
		{
			descriptors.add("getMethod=" + getter.getName());
		}
		if (setter != null)
		{
			descriptors.add("setMethod=" + setter.getName());
		}

		javax.management.Descriptor attrD = new javax.management.modelmbean.DescriptorSupport(
				descriptors.toArray(new String[descriptors.size()]));

		return new javax.management.modelmbean.ModelMBeanAttributeInfo(pd.getName(), pd.getShortDescription(), getter, setter,
				attrD);
	}
}
