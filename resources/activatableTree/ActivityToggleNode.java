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

package de.elbosso.model.tree;

import de.elbosso.util.lang.ActivityToggle;
import java.beans.PropertyChangeEvent;
import javax.swing.tree.MutableTreeNode;

public abstract class ActivityToggleNode<T> extends de.netsysit.model.tree.LazyNode implements
		java.beans.PropertyChangeListener
{
	private final static org.slf4j.Logger CLASS_LOGGER =org.slf4j.LoggerFactory.getLogger(ActivityToggleNode.class);
	private de.elbosso.util.lang.ActivityToggle<T> userObject;

	public ActivityToggleNode(de.elbosso.util.lang.ActivityToggle<T> userObject,ActivityToggleNode parent)
	{
		super(userObject.toString(),parent);
		this.userObject=userObject;
	}
	
	public void propertyChange(PropertyChangeEvent evt)
	{
		CLASS_LOGGER.trace(userObject+" propertyChange");
		reevaluateChildren();
	}

	private void reevaluateChildren()
	{
		boolean state=false;
		for (Object object : children)
		{
			ActivityToggleNode child=(ActivityToggleNode)object;
			if(child.isActive())
			{
				state=true;
				break;
			}
		}
		boolean old=isActive();
		userObject.setActive(state);
		if(state!=old)
		{
			CLASS_LOGGER.trace("changed :"+isActive()+userObject.isActive());
		}
	}
	public boolean isActive()
	{
		return userObject.isActive();
	}

	public ActivityToggle<T> getUserObject() {
		return userObject;
	}
	@Override
	public void setUserObject(Object obj)
	{
		ActivityToggle newone=(ActivityToggle)obj;
		if(getParent()!=null)
		{
			if(ActivityToggleNode.class.isAssignableFrom(getParent().getClass()))
			{
				if(userObject!=null)
					userObject.removePropertyChangeListener(((ActivityToggleNode)getParent()));
				newone.addPropertyChangeListener(((ActivityToggleNode)getParent()));
			}
		}
		this.userObject=newone;
	}

	@Override
	public String toString()
	{
		return getUserObject().getContent().toString();
	}
	
}
