#!/bin/sh

#Copyright (c) 2012-2019.
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
  DISPLAY=:0 xmodmap -pke > /tmp/my_xmodmap
  scp /tmp/my_xmodmap "$XPRA_SSH_USER"@"$XPRA_SSH_SERVER":/tmp
if ssh "$XPRA_SSH_USER"@"$XPRA_SSH_SERVER" 'DISPLAY=:78 xmodmap /tmp/my_xmodmap' > /dev/null 2>&1; then
  echo "user $XPRA_SSH_USER can connect to display $DISPLAY"
  xpra attach --clipboard=yes --clipboard-direction=both ssh/"$XPRA_SSH_USER"@"$XPRA_SSH_SERVER"/77&
  pkill -f ".*/tmp/start_xpra_remote.sh.*"
else
  echo "user $XPRA_SSH_USER cannot connect to display $DISPLAY"
cat <<EOM >/tmp/start_xpra_remote.sh
  xpra start :77 --clipboard=yes --clipboard-direction=both --start-child="Xephyr -ac -keybd ephyr,xkbmodel=pc105,xkblayout='de(nodeadkeys)',xkbrules=evdev,xkboption=grp:alts_toogle -screen 1280x1024 -br  :78&"
  DISPLAY=:78 xterm -iconic -e "exit"
  while [ \$? -ne 0 ] ;do
    echo "waiting "\$?
    sleep 1
    DISPLAY=:78 xterm -iconic -e "exit"
  done
  echo "trying openbox"
  DISPLAY=:78 xterm &
#just install a minimal lightweight window manager i thought...
#turns out that full-fledged web browssers (firefox as well as chromium-browser) 
#open ghost windows that dont get redrawn and can not be closed except for
#a restart of the window manager. So i had to resort to fluxbox where this
# is not so big a problem because those windows open iconified there...
# The browsers mentioned above were not the real problem - the script
#start_xpra_remote running amok was it!
  DISPLAY=:78 blackbox &
#  DISPLAY=:78 openbox &
#  DISPLAY=:78 fluxbox &
EOM
  scp /tmp/start_xpra_remote.sh "$XPRA_SSH_USER"@"$XPRA_SSH_SERVER":/tmp
  ssh "$XPRA_SSH_USER"@"$XPRA_SSH_SERVER" '/bin/bash /tmp/start_xpra_remote.sh> /dev/null 2>&1'
  echo "user $XPRA_SSH_USER can connect to display $DISPLAY"
  ssh "$XPRA_SSH_USER"@"$XPRA_SSH_SERVER" 'DISPLAY=:78 xmodmap /tmp/my_xmodmap'
  xpra attach --clipboard=yes --clipboard-direction=both ssh/"$XPRA_SSH_USER"@"$XPRA_SSH_SERVER"/77&
  pkill -f ".*/tmp/start_xpra_remote.sh.*"
fi
}
detach() {
DISPLAY=:77
if ssh "$XPRA_SSH_USER"@"$XPRA_SSH_SERVER" 'DISPLAY=:78 xterm -iconic -e "exit"' > /dev/null 2>&1; then
   echo "user $XPRA_SSH_USER can connect to display $DISPLAY"
  xpra detach ssh/"$XPRA_SSH_USER"@"$XPRA_SSH_SERVER"/77&
fi
}
stop() {
if ssh "$XPRA_SSH_USER"@"$XPRA_SSH_SERVER" 'DISPLAY=:78 xterm -iconic -e "exit"' > /dev/null 2>&1; then
  echo "user $XPRA_SSH_USER can connect to display $DISPLAY"
  ssh "$XPRA_SSH_USER"@"$XPRA_SSH_SERVER" 'xpra stop :77'
fi
}
status() {
DISPLAY=:77
if ssh "$XPRA_SSH_USER"@"$XPRA_SSH_SERVER" 'DISPLAY=:78 xterm -iconic -e "exit"' > /dev/null 2>&1; then
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
  detach)
        detach
        ;;
  status)
        status
        ;;
  restart|reload)
        stop
        start
        ;;
  *)
        echo "Usage: $0 {start|stop|detach|restart|status}"
        exit 1
esac
exit 0
