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
package de.netsysit.util.beans;

import de.elbosso.util.lang.annotations.Event;

public interface PropertyChangeSender
{
	/**
	 * Fügt einen PropertyChangeListener für eine spezifische Property hinzu. Der Listener wird
	 * nur dann aufgerufen, wenn der Name der Property dem hier angegebenen entspricht.
	 * Derselbe Listener kann mehrfach hinzugefügt werden. Er wird dann genauso oft aufgerufen, wie er
	 * für die spezifische Property registriert wurde.
	 * @param name darf null sein - in diesem Fall hat der Aufruf der Methode keine Auswirkungen
	 * @param l darf null sein - in diesem Fall hat der Aufruf der Methode keine Auswirkungen
	 */
	public void addPropertyChangeListener(String name,java.beans.PropertyChangeListener l);
	/**
	 * Entfernt einen PropertyChangeListener für die spezifizierte Property aus der Menge der Listener.
	 * War der Listener mehrfach für diese Property registriert, wird er nach dem Aufruf dieser Methode bei jedem Event für
	 * diese Property einmal weniger aufgerufen als vor
	 * dem Aufruf.
	 * @param name darf null sein - in diesem Fall hat der Aufruf der Methode keine Auswirkungen
	 * @param l darf null sein - in diesem Fall hat der Aufruf der Methode keine Auswirkungen. Wenn der übergebene
	 * Listener nicht für die spezifizierte Property registriert war, hat diese Methode ebenfalls keine Auswirkungen.
	 */
	public void removePropertyChangeListener(String name,java.beans.PropertyChangeListener l);
	/**
	 *  This method registers listeners for PropertyChangeEvents.
	 *
	 * @param  l     The listener interested in changes in the named property.
	 */

	public void addPropertyChangeListener( java.beans.PropertyChangeListener l);


	/**
	 *  This method deregisters listeners from PropertyChangeEvents.
	 *
	 * @param  l     The listener to be deregistered.
	 */
	public void removePropertyChangeListener(java.beans.PropertyChangeListener l);

}
