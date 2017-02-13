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
package de.elbosso.model.tree.redux;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

/**
 *
 * @author elbosso
 */

public class ReduxTree
{
	private java.util.Map<String, Object> container;
	private java.util.Map<String,Holder> holders;
	private javax.swing.tree.DefaultMutableTreeNode root;
	private javax.swing.tree.DefaultTreeModel model;

	public ReduxTree()
	{
		super();
		container=new java.util.HashMap();
		holders=new java.util.HashMap();
	}
@java.beans.ConstructorProperties("container")
	public ReduxTree(java.util.Map<String, Object> container)
	{
		super();
		this.container=new java.util.HashMap();
		holders=new java.util.HashMap();
		for (String key : container.keySet())
		{
//			System.out.println(container.keySet());
//			System.out.println(key);
			Object data=container.get(key);
			java.lang.Class cls=data.getClass();
			this.container.put(key, data);
			Holder h=new Holder(key,this,cls);
			java.lang.String id=key+cls.getName();
			holders.put(id,h);
		}
	}
	public javax.swing.tree.TreeModel getModel()
	{
		ensureModelConstruction();
		return model;
	}
	public Map<String, Object> getContainer()
	{
		return container;
	}
	public synchronized <T> Holder<T> add(java.lang.String key,T data,Class<T> cls)
	{
		//data
		container.put(key, data);
		Holder h=new Holder(key,this,cls);
		java.lang.String id=key+cls.getName();
		holders.put(id,h);
		//tree model
		ensureModelConstruction();
		String[] path=key.split("/");
		javax.swing.tree.DefaultMutableTreeNode n=root;
		for(int i=0;i<path.length;++i)
		{
			n=getNodeForName(n, path[i]);
		}
		javax.swing.tree.DefaultMutableTreeNode vn=new javax.swing.tree.DefaultMutableTreeNode(h);
		n.add(vn);
		model.nodesWereInserted(n, new int[]{n.getChildCount()-1});
		return h;
	}
	private void ensureModelConstruction()
	{
		if(model==null)
		{
			root=new javax.swing.tree.DefaultMutableTreeNode();
			model=new javax.swing.tree.DefaultTreeModel(root);
			for (String key : container.keySet())
			{
//				System.out.println(key);
				Object data=container.get(key);
				java.lang.Class cls=data.getClass();
				String[] path=key.split("/");
				javax.swing.tree.DefaultMutableTreeNode n=root;
				for(int i=0;i<path.length;++i)
				{
					n=getNodeForName(n, path[i]);
				}
				java.lang.String id=key+cls.getName();
				Holder h=get(key, cls);
				javax.swing.tree.DefaultMutableTreeNode vn=new javax.swing.tree.DefaultMutableTreeNode(h);
				n.add(vn);
				model.nodesWereInserted(n, new int[]{n.getChildCount()-1});
			}
		}
	}
	javax.swing.tree.DefaultMutableTreeNode getNodeForName(javax.swing.tree.DefaultMutableTreeNode wheretoLook,String name)
	{
		javax.swing.tree.DefaultMutableTreeNode rv=null;
		for(int i=0;i<wheretoLook.getChildCount();++i)
		{
			javax.swing.tree.DefaultMutableTreeNode cn=((javax.swing.tree.DefaultMutableTreeNode)wheretoLook.getChildAt(i));
			if(cn.getUserObject().equals(name))
			{
				rv=cn;
				break;
			}
		}
		if(rv==null)
		{
			rv=new javax.swing.tree.DefaultMutableTreeNode(name);
			wheretoLook.add(rv);
			model.nodesWereInserted(wheretoLook, new int[]{wheretoLook.getChildCount()-1});
		}
		return rv;
	}
	public synchronized <T> Holder<T> get(java.lang.String key,Class<T> cls)
	{
		java.lang.String id=key+cls.getName();
		Holder<T> h=null;
		if(holders.containsKey(id))
			h=(Holder<T>)holders.get(id);
		else
		{
			if(container.containsKey(key))
			{
				if(cls.isAssignableFrom(container.get(key).getClass()))
				{
					h=new Holder(key,this,cls);
					holders.put(id,h);
				}
			}
		}
		return h;
	}
	public synchronized <T> T fetch(java.lang.String key,Class<T> cls)
	{
		Object o;
		o=container.get(key);
		return cls.cast(o);
	}
	public synchronized <T> void update(java.lang.String key,T value)
	{
		container.put(key,value);
	}
}
