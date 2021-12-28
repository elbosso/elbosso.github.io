/*
Copyright (c) 2012-2022.

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

/**
 *
 * @author elbosso
 */
public class DynamicPOJOMBean extends java.lang.Object implements javax.management.DynamicMBean {
        
    final java.util.Map<String,java.lang.reflect.Method> getters;
    final java.util.Map<String,java.lang.reflect.Method> setters;
    final java.util.Set<java.lang.reflect.Method> operations;
    final Object resource;
    final javax.management.MBeanInfo info;
        
    public DynamicPOJOMBean(Object obj) throws IntrospectionException {
        getters = new java.util.LinkedHashMap<String,java.lang.reflect.Method>();
        setters = new java.util.LinkedHashMap<String,java.lang.reflect.Method>();
        operations = new java.util.LinkedHashSet<java.lang.reflect.Method>();
        resource = obj;
        try {
            info = initialize();
        } catch (javax.management.IntrospectionException ex) {
            throw new IllegalArgumentException(obj.getClass().getName(),ex);
        }
    }
    private javax.management.MBeanInfo initialize() throws javax.management.IntrospectionException, IntrospectionException {
        final java.util.List<javax.management.MBeanAttributeInfo> attributesInfo = 
                new java.util.ArrayList<javax.management.MBeanAttributeInfo>();
        final java.util.List<javax.management.MBeanOperationInfo> operationsInfo = 
                new java.util.ArrayList<javax.management.MBeanOperationInfo>();
//        final java.util.Set<String> attributesName = new java.util.HashSet<String>();
        final java.util.ArrayList<java.beans.MethodDescriptor> ops = new java.util.ArrayList();
//        for (java.lang.reflect.Method m:resource.getClass().getMethods()) {
//            if (m.getDeclaringClass().equals(Object.class)) continue;
//            if (m.getName().startsWith("get") &&
//                    !m.getName().equals("get") &&
//                    !m.getName().equals("getClass") &&
//                    m.getParameterTypes().length == 0 &&
//                    m.getReturnType() != void.class) {
//                getters.put(m.getName().substring(3),m);
//            } else if (m.getName().startsWith("is") &&
//                    !m.getName().equals("is") &&
//                    m.getParameterTypes().length == 0 &&
//                    m.getReturnType() == boolean.class) {
//                getters.put(m.getName().substring(2),m);
//            } else if (m.getName().startsWith("set") &&
//                    !m.getName().equals("set") &&
//                    m.getParameterTypes().length == 1 &&
//                    m.getReturnType().equals(void.class)) {
//                setters.put(m.getName().substring(3),m);
//            } else {
////                ops.add(m);
//            }
//        }
//        
//        attributesName.addAll(getters.keySet());
//        attributesName.addAll(setters.keySet());
        
//        for (String attrName : attributesName) {
//            final java.lang.reflect.Method get = getters.get(attrName);
//            java.lang.reflect.Method set = setters.get(attrName);
//            if (get != null && set != null && 
//                get.getReturnType() != set.getParameterTypes()[0]) {
//                set = null;
//                ops.add(setters.remove(attrName));
//            }
//            final javax.management.MBeanAttributeInfo mbi = 
//                    getAttributeInfo(attrName,get,set);
//            if (mbi == null && get != null) {
//                ops.add(getters.remove(attrName));
//            }
//            if (mbi == null && set != null) {
//                ops.add(setters.remove(attrName));
//            }
////            if (mbi != null) attributesInfo.add(mbi);
//        }
		java.beans.BeanInfo bi=java.beans.Introspector.getBeanInfo(resource.getClass());
		java.beans.PropertyDescriptor[] pds=bi.getPropertyDescriptors();
		for (java.beans.PropertyDescriptor pd : pds)
		{
			if(pd.isHidden()==false)
			{
//				if(pd.getReadMethod()!=null)
//					getters.add(pd.getReadMethod());
//				if(pd.getWriteMethod()!=null)
//					setters.put(pd.getWriteMethod().getName(),pd.getWriteMethod());
				javax.management.MBeanAttributeInfo mbi=new javax.management.MBeanAttributeInfo(pd.getName(),pd.getShortDescription(),pd.getReadMethod(),pd.getWriteMethod());
				attributesInfo.add(mbi);
				if(pd.getReadMethod()!=null)
					getters.put(pd.getName(),pd.getReadMethod());
				if(pd.getWriteMethod()!=null)
					setters.put(pd.getName(),pd.getWriteMethod());
			}
		}
		java.beans.MethodDescriptor[] mds=bi.getMethodDescriptors();
		for (java.beans.MethodDescriptor md : mds)
		{
			ops.add(md);
		}
		
        
        for (java.beans.MethodDescriptor m:ops) {
            final javax.management.MBeanOperationInfo opi = new javax.management.MBeanOperationInfo(m.getShortDescription(),m.getMethod());
            if (opi == null) continue;
            operations.add(m.getMethod());
            operationsInfo.add(opi);
        }
        return getMBeanInfo(resource,attributesInfo.
                toArray(new javax.management.MBeanAttributeInfo[attributesInfo.size()]),
                operationsInfo.
                toArray(new javax.management.MBeanOperationInfo[operationsInfo.size()]));
    }

//    protected javax.management.MBeanAttributeInfo getAttributeInfo(String attrName, 
//            java.lang.reflect.Method get, java.lang.reflect.Method set) throws javax.management.IntrospectionException {
//        return new javax.management.MBeanAttributeInfo(attrName,attrName,get,set);
//    }
//
//    protected javax.management.MBeanOperationInfo getOperationInfo(java.lang.reflect.Method m) {
//        if (m.getDeclaringClass()==Object.class) return null;
//        return new javax.management.MBeanOperationInfo(m.getName(),m);
//    }

    protected javax.management.MBeanInfo getMBeanInfo(Object resource, 
            javax.management.MBeanAttributeInfo[] attrs, javax.management.MBeanOperationInfo[] ops) {
        return new javax.management.MBeanInfo(resource.getClass().getName(),
                resource.getClass().getName(),attrs,null,ops,null);
    }

    public Object getAttribute(String attribute) 
        throws javax.management.AttributeNotFoundException, javax.management.MBeanException, javax.management.ReflectionException {
        final java.lang.reflect.Method get = getters.get(attribute);
        if (get == null) throw new javax.management.AttributeNotFoundException(attribute);
        try {
            return get.invoke(resource);
        } catch (IllegalArgumentException ex) {
            throw new javax.management.ReflectionException(ex);
        } catch (java.lang.reflect.InvocationTargetException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof Exception) 
                throw new javax.management.MBeanException((Exception)cause);
            throw new javax.management.RuntimeErrorException((Error)cause);
        } catch (IllegalAccessException ex) {
            throw new javax.management.ReflectionException(ex);
        }
    }

    public void setAttribute(javax.management.Attribute attribute) 
        throws javax.management.AttributeNotFoundException, javax.management.InvalidAttributeValueException, 
            javax.management.MBeanException, javax.management.ReflectionException {
        final java.lang.reflect.Method set = setters.get(attribute.getName());
        if (set == null) 
            throw new javax.management.AttributeNotFoundException(attribute.getName());
        try {
            set.invoke(resource,attribute.getValue());
        } catch (IllegalArgumentException ex) {
            throw new javax.management.ReflectionException(ex);
        } catch (java.lang.reflect.InvocationTargetException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof Exception) 
                throw new javax.management.MBeanException((Exception)cause);
            throw new javax.management.RuntimeErrorException((Error)cause);
        } catch (IllegalAccessException ex) {
            throw new javax.management.ReflectionException(ex);
        }
    }

    public javax.management.AttributeList getAttributes(String[] attributes) {
        if (attributes == null) return new javax.management.AttributeList();
        final java.util.List<javax.management.Attribute> result = 
                new java.util.ArrayList<javax.management.Attribute>(attributes.length);
        for (String attr : attributes) {
            final java.lang.reflect.Method get = getters.get(attr);
            try {
                result.add(new javax.management.Attribute(attr,get.invoke(resource)));
            } catch (Exception x) {
                continue;
            }
        }
        return new javax.management.AttributeList(result);
    }

    public javax.management.AttributeList setAttributes(javax.management.AttributeList attributes) {
        if (attributes == null) return new javax.management.AttributeList();
        final java.util.List<javax.management.Attribute> result = 
                new java.util.ArrayList<javax.management.Attribute>(attributes.size());
        for (Object item : attributes) {
            final javax.management.Attribute attr = (javax.management.Attribute)item;
	    final String name = attr.getName();
            final java.lang.reflect.Method set = setters.get(name);
            try {
		set.invoke(resource,attr.getValue());
		final java.lang.reflect.Method get = getters.get(name);
                final Object newval = 
		    (get==null)?attr.getValue():get.invoke(resource);
                result.add(new javax.management.Attribute(name,newval));
            } catch (Exception x) {
                continue;
            }
        }
        return new javax.management.AttributeList(result);
    }

    public Object invoke(String actionName, Object[] params, String[] signature) 
        throws javax.management.MBeanException, javax.management.ReflectionException {
        java.lang.reflect.Method toInvoke = null;
        if (params == null) params = new Object[0];
        if (signature == null) signature = new String[0];
        for (java.lang.reflect.Method m : operations) {
            if (!m.getName().equals(actionName)) continue;
            final Class[] sig = m.getParameterTypes();
            if (sig.length == params.length) {
                if (sig.length == 0) toInvoke=m;
                else if (signature.length == sig.length) {
                    toInvoke = m;
                    for (int i=0;i<sig.length;i++) {
                        if (!sig[i].getName().equals(signature[i])) {
                            toInvoke = null; 
                            break;
                        }
                    }
                }
            }
            if (toInvoke != null) break;
        }
        if (toInvoke == null) 
            throw new javax.management.ReflectionException(new NoSuchMethodException(actionName));
        try {
            return toInvoke.invoke(resource,params);
        } catch (IllegalArgumentException ex) {
            throw new javax.management.ReflectionException(ex);
        } catch (java.lang.reflect.InvocationTargetException ex) {
            final Throwable cause = ex.getCause();
            if (cause instanceof Exception) 
                throw new javax.management.MBeanException((Exception)cause);
            throw new javax.management.RuntimeErrorException((Error)cause);
        } catch (IllegalAccessException ex) {
            throw new javax.management.ReflectionException(ex);
        }
    }

    public javax.management.MBeanInfo getMBeanInfo() {
        return info;
    }
  
}