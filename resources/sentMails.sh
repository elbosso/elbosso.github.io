#!/bin/bash

#Copyright (c) 2013.
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

a=$(date "+%d")
b=$(date --reference=/home/elbosso/.ensure_mail_sent_once +%d)
if (( a != b ))
then
dim[0]=00
dim[1]=31 
dim[2]=28 
dim[3]=31 
dim[4]=30 
dim[5]=31 
dim[6]=30 
dim[7]=31 
dim[8]=31 
dim[9]=30 
dim[10]=31 
dim[11]=30 
dim[12]=31
# one based, days in month

# capture information from date: day or week, month, day, year
dinfo=( $(date "+%w %m %d %Y" ) )
dow=${dinfo[0]}
mon=${dinfo[1]}
dnum=${dinfo[2]}
year=${dinfo[3]}

# the previous 5 lines can be reduced to this if you are using ksh
#date "+%w %m %d %Y" | read dow mon dnum year


if (( year % 4 == 0  &&  (year / 100 != 0 || year/400 == 0) ))  # adjust if leapyear
then
    (( dim[2]++ ))
fi

eom=0                       # default to end of month false
if (( dnum == dim[mon] ))
then
    eom=1
fi

if (( eom ))    # end of month check
then
    echo "it's end of month"
else
    echo "it's not end of month"
fi

if (( dow == 0 ))
then
    echo "Comics abholen!"|mail -s "Comics abholen" elbosso
else
    echo "it's not Sunday"
fi
touch .ensure_mail_sent_once
fi
