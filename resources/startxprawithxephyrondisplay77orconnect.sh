#!/bin/sh

#Copyright (c) 2012-2015.
#
#Juergen Key. Alle Rechte vorbehalten.
#
#Weiterverbreitung und Verwendung in nichtkompilierter oder kompilierter Form, 
#mit oder ohne Veraenderung, sind unter den folgenden Bedingungen zulaessig:
#
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
#
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

start() {
DISPLAY=:77
if xterm -iconic -e "exit" > /dev/null 2>&1; then
  echo "user $user can connect to display $display"
  DISPLAY=:0 xpra attach :77 &
else
  echo "user $user cannot connect to display $display"
  xpra start :77 --start-child="Xephyr -ac -keybd ephyr,,,xkbmodel=evdev,xkblayout=de -screen 1280x1024 -br  :78&"
  DISPLAY=:78
  DISPLAY=:78 xterm -iconic -e "exit"
#  echo $?
  while [ $? -ne 0 ] ;do
	sleep 1
    DISPLAY=:78 xterm -iconic -e "exit"
#echo "waiting "$?
  done
echo "trying openbox"
  DISPLAY=:78 openbox &
  DISPLAY=:0 xpra attach :77 &
fi
}
stop() {
DISPLAY=:77
if xterm -iconic -e "exit" > /dev/null 2>&1; then
  echo "user $user can connect to display $display"
  DISPLAY=:0 xpra stop :77
fi
}
status() {
DISPLAY=:77
if xterm -iconic -e "exit" > /dev/null 2>&1; then
  echo "running"
else
  echo "not running"
fi

}
### main logic ###
case "$1" in
  start)
        start
        ;;
  stop)
        stop
        ;;
  status)
        status
        ;;
  restart|reload|condrestart)
        stop
        start
        ;;
  *)
        echo "Usage: $0 {start|stop|restart|reload|status}"
        exit 1
esac
exit 0
