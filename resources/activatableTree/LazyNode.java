/*
Copyright (c) 2014.

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

package de.netsysit.model.tree;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public abstract class LazyNode extends java.lang.Object implements
javax.swing.tree.MutableTreeNode
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(LazyNode.class);
	private java.lang.String client;
	protected Object[] children;
	private javax.swing.tree.TreeNode parent;
	private java.lang.Class stopClass;

	private LazyNode()
	{
		super();
	}
	public LazyNode(java.lang.String name,javax.swing.tree.TreeNode parent)
	{
		super();
		client=name;
		this.parent=parent;
		if(parent instanceof LazyNode)
			this.stopClass=((LazyNode)parent).getStopClass();
	}

	public Class getStopClass()
	{
		return stopClass;
	}

	public void setStopClass(Class stopClass)
	{
		this.stopClass = stopClass;
		if(children!=null)
		{
			for (Object object : children)
			{
				if(object instanceof LazyNode)
					((LazyNode)object).setStopClass(getStopClass());
			}
		}
	}
//	public LazyNode(java.lang.String name)
//	{
//		this(name,null);
//	}
	//Implementation of interface javax.swing.tree.TreeNode
	public javax.swing.tree.TreeNode getParent()
	{
		return parent;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public int getChildCount()
	{
		return getChildren()!=null?getChildren().length:0;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public int getIndex(javax.swing.tree.TreeNode treeNode0)
	{
		int rv=-1;
		for(int i=0;i<getChildCount();++i)
		{
			if(getChildren()[i]==treeNode0)
			{
				rv=i;
				break;
			}
		}
		return rv;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public javax.swing.tree.TreeNode getChildAt(int int0)
	{
		return (javax.swing.tree.TreeNode)getChildren()[int0];
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public boolean getAllowsChildren()
	{
		return this.getClass()!=stopClass;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public boolean isLeaf()
	{
		return (getAllowsChildren()==false)||getChildCount()<1;
	}
	//Implementation of interface javax.swing.tree.TreeNode
	public java.util.Enumeration children()
	{
		return java.util.Collections.enumeration(java.util.Arrays.asList(getChildren()));
	}
	public String toString()
	{
		return client;
	}
	protected Object[] getChildren()
	{
		if (children == null)
		{
			try
			{
				findChildren();
			}
			catch (java.lang.Exception exp)
			{
				exp.printStackTrace();
				children=new LazyNode[0];
			}
		}
		return children;
	}
	protected abstract void findChildren() throws java.lang.Exception;
	public void setParent(javax.swing.tree.MutableTreeNode newParent)
	{
		
	}
	public void removeFromParent()
	{
	}
	public void setUserObject(java.lang.Object obj)
	{
	}
	public void remove(javax.swing.tree.MutableTreeNode node)
	{
	}
	public void remove(int index)
	{
	}
	public void insert(javax.swing.tree.MutableTreeNode node, int index)
	{
	}
	public java.lang.String getPath()
	{
		java.lang.String rv="";
		javax.swing.tree.TreeNode parent=getParent();
		if((parent!=null)&&(LazyNode.class.isAssignableFrom(parent.getClass())))
			rv+=((LazyNode)parent).getPath();
		rv+="/"+toString();
		return rv;
	}
}

