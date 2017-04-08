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

package de.elbosso.util.lang;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ActivityToggle<T> extends java.lang.Object implements de.netsysit.util.beans.PropertyChangeSender
{
	private final T content;
	private boolean active;
	private java.beans.PropertyChangeSupport pcs;

	public ActivityToggle(T content)
	{
		super();
		if(content==null)
			throw new java.lang.IllegalArgumentException("content must not be null!");
		this.content =content;
		this.pcs = new java.beans.PropertyChangeSupport(this);
	}

	public synchronized boolean isActive()
	{
		return active;
	}

	public synchronized void setActive(boolean active)
	{
		boolean old=isActive();
		this.active = active;
		pcs.firePropertyChange("active", old, isActive());
	}

	public T getContent()
	{
		return content;
	}

	@Override
	public int hashCode()
	{
		return content.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		return content.equals(obj);
	}

	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		pcs.addPropertyChangeListener(l);
	}

	public void addPropertyChangeListener(String name, PropertyChangeListener l)
	{
		pcs.addPropertyChangeListener(name,l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		pcs.removePropertyChangeListener(l);
	}
	
	public void removePropertyChangeListener(String name, PropertyChangeListener l)
	{
		pcs.removePropertyChangeListener(name,l);
	}
	
	
}
