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
package de.netsysit.model.table;

import javax.swing.event.TableModelListener;

public abstract class EventHandlingSupport  extends java.lang.Object implements 
javax.swing.table.TableModel
{
	private java.util.List<javax.swing.event.TableModelListener> tablemodellistenerlist;

	protected EventHandlingSupport()
	{
		super();
		tablemodellistenerlist=new java.util.LinkedList<javax.swing.event.TableModelListener>();
	}
	
	
	public void addTableModelListener(javax.swing.event.TableModelListener l)
	{
		tablemodellistenerlist.add(l);
	}
	public void removeTableModelListener(javax.swing.event.TableModelListener l)
	{
		tablemodellistenerlist.remove(l);
	}
	protected void fireTableChanged() 
	{	
		if(tablemodellistenerlist.isEmpty()==false)
		{
			javax.swing.event.TableModelEvent event=new javax.swing.event.TableModelEvent(this);
			fireTableChanged(event);
		}
	}
	protected void fireTableChanged(final javax.swing.event.TableModelEvent e) 
	{	
		if(tablemodellistenerlist.isEmpty()==false)
		{
			final java.util.List<javax.swing.event.TableModelListener> tml=new java.util.LinkedList(tablemodellistenerlist);
			if(javax.swing.SwingUtilities.isEventDispatchThread())
			{
				for (TableModelListener tableModelListener : tml)
				{
					tableModelListener.tableChanged(e);
				}	
			}
			else
			{
				javax.swing.SwingUtilities.invokeLater(new java.lang.Runnable()
				{
					public void run()
					{
						for (TableModelListener tableModelListener : tml)
						{
							tableModelListener.tableChanged(e);
						}	
					}
				});
			}
		}
	}

}
