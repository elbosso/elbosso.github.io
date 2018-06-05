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


import java.util.Arrays;
import java.util.Vector;

public abstract class LazyNode extends javax.swing.tree.DefaultMutableTreeNode
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(LazyNode.class);
	private java.lang.String client;
	protected Object[] children;
	private javax.swing.tree.TreeNode parent;
	private java.lang.Class stopClass;
	private javax.swing.tree.DefaultTreeModel treeModel;
//	private javax.swing.JTree tree;
	private java.lang.Runnable rble;

	private LazyNode()
	{
		super();
	}
	public LazyNode(java.lang.String name,javax.swing.tree.TreeNode parent)
	{
		super();
		client=name;
		this.parent=parent;
//		this.tree=tree;
		if(parent instanceof LazyNode)
		{
			LazyNode lazyParent=(LazyNode)parent;
			this.stopClass=lazyParent.getStopClass();
//			this.tree=lazyParent.getTree();
			this.treeModel=lazyParent.getTreeModel();
		}
	}
	protected javax.swing.tree.DefaultTreeModel produceTreeModel()
	{
		javax.swing.tree.DefaultTreeModel rv=treeModel;
		if(rv==null)
		{
			if (getParent() == null)
			{
				treeModel = new javax.swing.tree.DefaultTreeModel(this);
				rv = treeModel;
			}
			else
			{
				if (LazyNode.class.isAssignableFrom(getParent().getClass()))
				{
					treeModel = ((LazyNode) getParent()).produceTreeModel();
					rv=treeModel;
				}
			}
		}
		return rv;
	}
	public javax.swing.tree.DefaultTreeModel getTreeModel()
	{
		return treeModel;
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
		boolean rv=getAllowsChildren()==false;
		if(rv==false)
		{
			Boolean b = hasAtLeastOneChild();
			if (b == null)
				rv = getChildCount() < 1;
			else
				rv=b.booleanValue()==false;
		}
		return rv;
	}

	protected Boolean hasAtLeastOneChild()
	{
		return Boolean.FALSE;
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
				if(treeModel==null)
				{
					children = findChildren();
				}
				else
				{
//					children=new Object[0];
					if(rble==null)
					{
						rble = new java.lang.Runnable()
						{
							public void run()
							{
								try
								{
									Object[] childs = findChildren();
									if (childs != null)
									{
										for (Object child : childs)
										{
											//										LazyNode.this.add((javax.swing.tree.MutableTreeNode)child);
											treeModel.insertNodeInto((javax.swing.tree.MutableTreeNode) child, LazyNode.this, LazyNode.this.getChildCount());
										}
									}
								} catch (java.lang.Throwable t)
								{
t.printStackTrace();
								}
							}
						};
						new java.lang.Thread(rble).start();
					}
				}
			}
			catch (java.lang.Exception exp)
			{
				exp.printStackTrace();
				children=new LazyNode[0];
			}
		}
		return children;
	}
	protected abstract Object[] findChildren() throws java.lang.Exception;
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
	public void insert(javax.swing.tree.MutableTreeNode newChild, int index)
	{
	}
	public java.lang.String getPathAsString()
	{
		java.lang.String rv="";
		javax.swing.tree.TreeNode parent=getParent();
		if((parent!=null)&&(LazyNode.class.isAssignableFrom(parent.getClass())))
			rv+=((LazyNode)parent).getPathAsString();
		rv+="/"+toString();
		return rv;
	}
}

