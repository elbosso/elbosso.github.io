#!/usr/bin/python

#Copyright (c) 2013.

#Juergen Key. Alle Rechte vorbehalten.

#Weiterverbreitung und Verwendung in nichtkompilierter oder kompilierter Form, 
#mit oder ohne Veraenderung, sind unter den folgenden Bedingungen zulaessig:

#   1. Weiterverbreitete nichtkompilierte Exemplare muessen das obige Copyright, 
#die Liste der Bedingungen und den folgenden Haftungsausschluss im Quelltext 
#enthalten.
#   2. Weiterverbreitete kompilierte Exemplare muessen das obige Copyright, 
#die Liste der Bedingungen und den folgenden Haftungsausschluss in der 
#Dokumentation und/oder anderen Materialien, die mit dem Exemplar verbreitet 
#werden, enthalten.
#   3. Weder der Name des Autors noch die Namen der Beitragsleistenden 
#duerfen zum Kennzeichnen oder Bewerben von Produkten, die von dieser Software 
#abgeleitet wurden, ohne spezielle vorherige schriftliche Genehmigung verwendet 
#werden.

#DIESE SOFTWARE WIRD VOM AUTOR UND DEN BEITRAGSLEISTENDEN OHNE 
#JEGLICHE SPEZIELLE ODER IMPLIZIERTE GARANTIEN ZUR VERFUEGUNG GESTELLT, DIE 
#UNTER ANDEREM EINSCHLIESSEN: DIE IMPLIZIERTE GARANTIE DER VERWENDBARKEIT DER 
#SOFTWARE FUER EINEN BESTIMMTEN ZWECK. AUF KEINEN FALL IST DER AUTOR 
#ODER DIE BEITRAGSLEISTENDEN FUER IRGENDWELCHE DIREKTEN, INDIREKTEN, 
#ZUFAELLIGEN, SPEZIELLEN, BEISPIELHAFTEN ODER FOLGENDEN SCHAEDEN (UNTER ANDEREM 
#VERSCHAFFEN VON ERSATZGUETERN ODER -DIENSTLEISTUNGEN; EINSCHRAENKUNG DER 
#NUTZUNGSFAEHIGKEIT; VERLUST VON NUTZUNGSFAEHIGKEIT; DATEN; PROFIT ODER 
#GESCHAEFTSUNTERBRECHUNG), WIE AUCH IMMER VERURSACHT UND UNTER WELCHER 
#VERPFLICHTUNG AUCH IMMER, OB IN VERTRAG, STRIKTER VERPFLICHTUNG ODER 
#UNERLAUBTE HANDLUNG (INKLUSIVE FAHRLAESSIGKEIT) VERANTWORTLICH, AUF WELCHEM 
#WEG SIE AUCH IMMER DURCH DIE BENUTZUNG DIESER SOFTWARE ENTSTANDEN SIND, SOGAR, 
#WENN SIE AUF DIE MOEGLICHKEIT EINES SOLCHEN SCHADENS HINGEWIESEN WORDEN SIND.


from gi.repository import Unity, Gio, GObject, Dbusmenu,Gtk

import time

loop = GObject.MainLoop()

win = Gtk.Window()
win.set_title("Unity Integration test")
win.connect("delete-event", Gtk.main_quit)
win.show_all()
launcher = Unity.LauncherEntry.get_for_desktop_id ("te.desktop")
counter=0
launcher.set_property("count_visible", True)
launcher.set_property("progress_visible", True)
ql = Dbusmenu.Menuitem.new ()
item1 = Dbusmenu.Menuitem.new ()
item1.property_set (Dbusmenu.MENUITEM_PROP_LABEL, "Item 1")
item1.property_set_bool (Dbusmenu.MENUITEM_PROP_VISIBLE, True)
item2 = Dbusmenu.Menuitem.new ()
item2.property_set (Dbusmenu.MENUITEM_PROP_LABEL, "Item 2")
item2.property_set_bool (Dbusmenu.MENUITEM_PROP_VISIBLE, True)
ql.child_append (item1)
ql.child_append (item2)
launcher.set_property("quicklist", ql)

def update_urgency():
	global counter
	if launcher.get_property("urgent"):
		launcher.set_property("urgent", False)
		counter=0
	else:
		counter = counter +0.1
		if counter>1.0:
			launcher.set_property("urgent", True)
		launcher.set_property("count", counter*10)
		launcher.set_property("progress", counter)
	return True

GObject.timeout_add_seconds(1, update_urgency)

loop.run()


