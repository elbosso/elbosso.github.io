#!/bin/sh
#Copyright (c) 2012-2018.
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

#Quelle Custom Icons under Gnome: http://ubuntuforums.org/showthread.php?t=1383270
#Quelle Custom icons under KDE: http://stackoverflow.com/questions/1036046/programmatically-set-custom-folder-directory-icon-in-linux
wd=`pwd`
if [ $# -eq 1 ]; then 
    wd=$1/
fi
directoryCount=`ls -l "$wd"| grep ^d | wc -l`
fileCount=`ls -l "$wd"| grep ^- | wc -l`
linkCount=`ls -l "$wd"| grep ^l | wc -l`
#echo $wd " holds " $directoryCount " directories"
#echo $wd " holds " $fileCount " Files"
#echo $wd " holds " $linkCount " Links"
flag=0
if [ $directoryCount -eq 1 ]; then
	if [ $fileCount -eq 0 ]; then
		if [ $linkCount -eq 0 ]; then
			aa=`readlink -f "$wd"`
			a=`find "$aa" -maxdepth 1 -mindepth 1 -type d`
			idirectoryCount=`ls -l "$a"| grep ^d | wc -l`
			ifileCount=`ls -l "$a"| grep ^- | wc -l`
			ilinkCount=`ls -l "$a"| grep ^l | wc -l`
			if [ $idirectoryCount -eq 0 ]; then
				if [ $ifileCount -gt 0 ]; then
					if [ $ilinkCount -eq 0 ]; then
#Verzeichnis gefunden, das einziges Unterverzeichnis ist und nur Dateien enthält
						flag=1
					fi
				fi
			fi
		fi
	fi
fi
if [ $directoryCount -eq 0 ]; then
	if [ $fileCount -gt 0 ]; then
		if [ $linkCount -eq 0 ]; then
#Verzeichnis gefunden, das nur Dateien enthält
			flag=2
		fi
	fi
fi
if [ $flag -eq 1 ]; then
	echo "Hit in " $wd
#Dateien ein Verzeichnis hochkopieren
	find "$wd" -wholename '*/*.*' -execdir mv \{\} .. \;
#nun leeres Verzeichnis löschen
	find "`readlink -f "$wd"`" -maxdepth 1 -mindepth 1 -type d -exec sh -c "rmdir \"{}\"" \;
#bequemer Weg, Icon für das Verzeichnis zu erzeugen
	$0 "$wd"
else
#Icon für das Verzeichnis erzeugen
	if [ $flag -eq 2 ]; then
		#echo "thumb " `readlink -f "$wd"`
#Produktion eines Thumbnails als PNG
		iname="`readlink -f "$wd"`/.directory_thumb.png"
		refinedwd=`readlink -f "$wd"`
		oname=`find "$refinedwd" -type f | head -n 1`
		djpeg -pnm "$oname"|pnmscale -xsize 128|pnmtopng > "$iname"
		finame="file://`readlink -f "$wd"`/.directory_thumb.png"
#Ab hier: KDE
		fname="`readlink -f "$wd"`/.directory"
		#echo $fname
		echo "[Desktop Entry]" >"$fname"
		#echo $iname
		echo -n "Icon=" >>"$fname"
		#`cat "$fname"`
		echo -n "$iname" >>"$fname"
#Ab hier: Gnome
		gvfs-set-attribute -t stringv "$refinedwd" metadata::custom-icon ""
		gvfs-set-attribute "$refinedwd" metadata::custom-icon "$finame"
		#echo "$fname"
		#find "`readlink -f "$wd"`" -type f | head -n 1
	fi
	find "`readlink -f "$wd"`" -maxdepth 1 -mindepth 1 -type d -exec sh -c "$0 \"{}\"" \;
fi
