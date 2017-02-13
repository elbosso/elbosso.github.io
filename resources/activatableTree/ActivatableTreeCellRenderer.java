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

package de.elbosso.ui.renderer.tree;

/**
 * Grundlage: http://www.java2s.com/Tutorial/Java/0240__Swing/CreatinganEditorJustforLeafNodes.htm
 *
 */
public class ActivatableTreeCellRenderer implements javax.swing.tree.TreeCellRenderer
{
	private final static org.apache.log4j.Logger CLASS_LOGGER = org.apache.log4j.Logger.getLogger(ActivatableTreeCellRenderer.class);
	private javax.swing.JCheckBox leafRenderer;
	private javax.swing.JPanel leafRendererContainer;
	private javax.swing.tree.DefaultTreeCellRenderer nonLeafRenderer;

	protected javax.swing.JCheckBox getLeafRenderer()
	{
		return leafRenderer;
	}

	public ActivatableTreeCellRenderer()
	{
		super();
		leafRenderer = new javax.swing.JCheckBox();
		nonLeafRenderer = new javax.swing.tree.DefaultTreeCellRenderer();
		java.awt.Font fontValue;
		fontValue = javax.swing.UIManager.getFont("Tree.font");
		if (fontValue != null)
		{
			leafRenderer.setFont(fontValue);
		}
		Boolean booleanValue = (Boolean) javax.swing.UIManager.get("Tree.drawsFocusBorderAroundIcon");
		leafRenderer.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));
		leafRenderer.setOpaque(true);
		nonLeafRenderer.setOpaque(true);
	}

	public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean selected,
			boolean expanded, boolean leaf, int row, boolean hasFocus)
	{

		java.awt.Component returnValue;
		if (de.elbosso.model.tree.ActivityToggleNode.class.isAssignableFrom(value.getClass()))
		{
			String stringValue = value.toString();
			leafRenderer.setText(stringValue);
			leafRenderer.setSelected(false);
			leafRenderer.setEnabled(tree.isEnabled());
			leafRenderer.setBackground(selected?nonLeafRenderer.getBackgroundSelectionColor():nonLeafRenderer.getBackgroundNonSelectionColor());
			leafRenderer.setForeground(selected?nonLeafRenderer.getTextSelectionColor():nonLeafRenderer.getTextNonSelectionColor());
			if(value!=null)
			{
				leafRenderer.setSelected(((de.elbosso.model.tree.ActivityToggleNode)value).isActive());
				leafRenderer.setText(value.toString());
			}
			returnValue = leafRenderer;
		}
		else if (javax.swing.tree.DefaultMutableTreeNode.class.isAssignableFrom(value.getClass()))
		{
			java.lang.Object obj = ((javax.swing.tree.DefaultMutableTreeNode) value).getUserObject();
			if (de.elbosso.util.lang.ActivityToggle.class.isAssignableFrom(obj.getClass()))
			{
				String stringValue = value.toString();
				leafRenderer.setText(stringValue);
				leafRenderer.setSelected(false);
				leafRenderer.setEnabled(tree.isEnabled());
				leafRenderer.setBackground(selected?nonLeafRenderer.getBackgroundSelectionColor():nonLeafRenderer.getBackgroundNonSelectionColor());
				leafRenderer.setForeground(selected?nonLeafRenderer.getTextSelectionColor():nonLeafRenderer.getTextNonSelectionColor());
				if ((value != null) && (value instanceof javax.swing.tree.DefaultMutableTreeNode))
				{
					Object userObject = ((javax.swing.tree.DefaultMutableTreeNode) value).getUserObject();
					if (userObject instanceof de.elbosso.util.lang.ActivityToggle)
					{
						de.elbosso.util.lang.ActivityToggle node = (de.elbosso.util.lang.ActivityToggle) userObject;
						leafRenderer.setText(value.toString());
						leafRenderer.setSelected(node.isActive());
					}
				}
				returnValue = leafRenderer;
				if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(4);
			}
			else
			{
				returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree, value, selected, expanded,
						leaf, row, hasFocus);
				if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(1);
			}
		}
		else
		{
			returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree, value, selected, expanded,
					leaf, row, hasFocus);
				if(CLASS_LOGGER.isTraceEnabled())CLASS_LOGGER.trace(2);
		}
		return returnValue;
	}
}
