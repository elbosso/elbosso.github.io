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
package de.elbosso.ui.editor.tree;

import de.elbosso.ui.renderer.tree.ActivatableTreeCellRenderer;
import de.elbosso.util.lang.ActivityToggle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * Grundlage:
 * http://www.java2s.com/Tutorial/Java/0240__Swing/CreatinganEditorJustforLeafNodes.htm
 *
 */
public class ActivatableTreeCellEditor extends de.netsysit.model.tree.AbstractCellEditor implements javax.swing.tree.TreeCellEditor
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(ActivatableTreeCellEditor.class);
	private final ActivatableTreeCellRenderer renderer = new ActivatableTreeCellRenderer();
//	private javax.swing.event.ChangeEvent changeEvent = null;
	protected javax.swing.JTree tree;
	protected de.elbosso.util.lang.ActivityToggle at;
	private Manager manager;

	public ActivatableTreeCellEditor(javax.swing.JTree tree)
	{
		super();
		this.tree = tree;
//		setClickCountToStart(2);
		manager=new Manager();
	}

	public Object getCellEditorValue()
	{
		return at;
	}

	public boolean isCellEditable(java.util.EventObject event)
	{
		boolean returnValue = false;
//		if (returnValue)
		{
			returnValue = false;
			if (event instanceof java.awt.event.MouseEvent)
			{
				java.awt.event.MouseEvent mouseEvent = (java.awt.event.MouseEvent) event;
				javax.swing.tree.TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
				if (path != null)
				{
					Object node = path.getLastPathComponent();
					if ((node != null) && (node instanceof de.elbosso.model.tree.ActivityToggleNode))
					{
						de.elbosso.model.tree.ActivityToggleNode treeNode = (de.elbosso.model.tree.ActivityToggleNode) node;
//					Object userObject = treeNode.getUserObject();
//		  System.out.println(userObject+" "+(treeNode.isLeaf())+" "+(userObject instanceof de.elbosso.util.lang.ActivityToggle));
						returnValue = ((treeNode.isLeaf())&&(super.isCellEditable(event)));
					}
				}
			}
		}
//	System.out.println(returnValue);
		return returnValue;
	}

	public java.awt.Component getTreeCellEditorComponent(final javax.swing.JTree tree, Object value, boolean selected,
			boolean expanded, boolean leaf, int row)
	{
		at = ((de.elbosso.model.tree.ActivityToggleNode) value).getUserObject();
		java.awt.Component editor= renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf,row, true);
		if (editor instanceof javax.swing.JCheckBox)
		{
			((javax.swing.JCheckBox) editor).setSelected(at.isActive());
			((javax.swing.JCheckBox) editor).addActionListener(manager);

		}
//	System.out.println(editor);
		return editor;
	}
	private class Manager extends java.lang.Object implements
			java.awt.event.ActionListener
	{
		public void actionPerformed(ActionEvent itemEvent)
			{
				if (stopCellEditing())
				{
					at.setActive(((javax.swing.JCheckBox) itemEvent.getSource()).isSelected());
					if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(at);
					((javax.swing.JCheckBox) itemEvent.getSource()).removeActionListener(manager);
					fireEditingStopped();
					tree.repaint();
				}
			}
	}
}
