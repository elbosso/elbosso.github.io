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

package de.elbosso.model.table;

public class ActivityToggleModel<T> extends de.netsysit.model.table.EventHandlingSupport
{
	private final static java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("de.netsysit.model.i18n", java.util.Locale.getDefault());
	private java.util.List <de.elbosso.util.lang.ActivityToggle <T> > container;
	private java.lang.String leftColumnName;
	
	public ActivityToggleModel()
	{
		this(i18n.getString("ActivityToggleModel.columnTitle.left"));
	}

	public ActivityToggleModel(java.lang.String leftColumnName)
	{
		super();
		this.leftColumnName=leftColumnName;
		container=new java.util.LinkedList();
	}

	public int getRowCount()
	{
		return container.size();
	}

	public int getColumnCount()
	{
		return 2;
	}

	public String getColumnName(int column)
	{
		return (column==0?leftColumnName:i18n.getString("ActivityToggleModel.columnTitle.right"));
	}

	public Class<?> getColumnClass(int column)
	{
		return (column==0?java.lang.Object.class:java.lang.Boolean.class);
	}

	public boolean isCellEditable(int row, int column)
	{
		return (column==0?false:true);
	}

	public Object getValueAt(int row, int column)
	{
		return (column==0?container.get(row).getContent():java.lang.Boolean.valueOf(container.get(row).isActive()));
	}

	public void setValueAt(Object o, int row, int column)
	{
		de.elbosso.util.lang.ActivityToggle toggle =container.get(row);
		if(column==1)
		{
			toggle.setActive(((java.lang.Boolean)o).booleanValue());
		}
		fireTableChanged(new javax.swing.event.TableModelEvent(this, row));
	}
	public boolean isActive(int row)
	{
		return container.get(row).isActive();
	}
	public T getContent(int row)
	{
		return container.get(row).getContent();
	}
	public void add(T newItem)
	{
		add(new de.elbosso.util.lang.ActivityToggle(newItem));
	}
	public void add(de.elbosso.util.lang.ActivityToggle<T> newItem)
	{
		container.add(newItem);
		fireTableChanged(new javax.swing.event.TableModelEvent(this));
	}
	public void add(T newItem, int row)
	{
		add(new de.elbosso.util.lang.ActivityToggle(newItem),row);
	}
	public void add(de.elbosso.util.lang.ActivityToggle<T> newItem, int row)
	{
		container.add(row,newItem);
		fireTableChanged(new javax.swing.event.TableModelEvent(this));
	}
	public void remove(int row)
	{
		container.remove(row);
		fireTableChanged(new javax.swing.event.TableModelEvent(this));
	}
	public void remove(de.elbosso.util.lang.ActivityToggle<T> item)
	{
		container.remove(item);
		fireTableChanged(new javax.swing.event.TableModelEvent(this, getRowCount()-1, getRowCount()-1));
	}
	public java.util.Collection<T> getActive()
	{
		java.util.Collection<T> rv=new java.util.LinkedList();
		for (de.elbosso.util.lang.ActivityToggle<T> at: container)
		{
			if(at.isActive())
				rv.add(at.getContent());
		}
		return rv;
	}
}
