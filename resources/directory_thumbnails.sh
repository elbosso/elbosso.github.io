#!/bin/bash
#Copyright (c) 2012-2016.
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

latch=`pwd`
for var in "$@"
do
#http://stackoverflow.com/questions/32429333/how-to-test-if-a-linux-directory-contain-only-one-subdirectory-and-no-other-file
counta=`find "$var" -maxdepth 1 -mindepth 1 -type d -printf 1 | wc -m`
countb=`find "$var" -maxdepth 1 -mindepth 1 ! -type d ! -name ".*" -printf 1 | wc -m`
if [ "$counta" -eq 1 ]; then #\
#        -a "$countb" -eq 0 ]; then
#    echo " It has only one subdirectory and no other content"
	dirtobedeleted=`find "$var" -maxdepth 1 -mindepth 1 -type d`
	echo $dirtobedeleted
	mv "$dirtobedeleted"/* "$var"
	rmdir "$dirtobedeleted"
fi
cd "$var"
#ls *.[jpg,JPG,JPEG,jpeg,PNG,png,gif,Gif]| sort -n | head -1
firstimagefile=`find . -maxdepth 1 -type f -exec file {} \; | awk -F: '{if ($2 ~/image/) print $1}'| sort -n | head -1`
if [ "$firstimagefile" != "./.directory_thumb.png" ]; then
#firstimagefile=`realpath $firstimagefile`
#echo $firstimagefile
anytopnm "$firstimagefile"|pnmscale -w 128|pnmtopng >.directory_thumb.png
if [ $? -ne 0 ]; then
djpeg "$firstimagefile"|pnmscale -w 128|pnmtopng >.directory_thumb.png
fi
if [ $? -eq 0 ]; then
#echo "doing"
rp=`realpath .directory_thumb.png`
#echo $rp
echo "[Desktop Entry]" > ./.directory
echo "ICON=$rp" >> ./.directory
gvfs-set-attribute -t stringv . metadata::custom-icon ""
gvfs-set-attribute . metadata::custom-icon "file://$rp"
fi
fi
cd $latch
done
