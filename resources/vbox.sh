#!/bin/sh
#Copyright (c) 2016.
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

list="$(VBoxManage list vms 2>/dev/null|cut -d{ -f2 |cut -d} -f1)"
#quote=`$'\t'`
sysbrdgs="$(brctl show |sed 's/\t/#/g'|cut -d# -f1|grep .|tail -n +2)"
quote=\'
brdgifaces="$(VBoxManage list vms --long 2>/dev/null|grep NIC|cut -d, -f 2 |grep 'Bridged Interface'|cut -d\' -f2 |sort|uniq)"
#echo $list
#printf '%s\n' "$list" | while IFS= read -r machine
#do
#   echo "$machine"
#done
#printf '%s\n' "$brdgifaces" | while IFS= read -r iface
#do
#   echo "$iface"
#done
case "$1" in
intnet)
if [ $# -ne 2 ]; 
    then 
		echo "Usage: $0 intnet <network-name>"
		exit 1
fi
printf '%s\n' "$list" | while IFS= read -r machine
do
   part="$(VBoxManage showvminfo $machine 2>/dev/null|grep NIC|grep -v disabled |grep $2|wc -l)"
	if [ $part -ne 0 ]; 
    then 
		name="$(VBoxManage list vms 2>/dev/null|grep $machine)"
		echo "$name"
fi
done
;;
unplug)
if [ $# -ne 3 ]; 
    then 
		echo "Usage: $0 unplug <uuid> <network-name>"
		exit 1
fi
name="$(VBoxManage list vms 2>/dev/null|grep $2)"
	nic="$(VBoxManage showvminfo $2 2>/dev/null|grep NIC|grep "'$3'"|head -n 1|cut -d: -f1|cut -d' ' -f2)"
	echo "unplugging "$name" from" $3 "("$nic")"
	VBoxManage modifyvm $2 --nic$nic none
;;
plug)
if [ $# -ne 3 ]; 
    then 
		echo "Usage: $0 plug <uuid> <network-name>"
		exit 1
fi
name="$(VBoxManage list vms 2>/dev/null|grep $2)"
	nic="$(VBoxManage showvminfo $2|grep NIC|grep disabled|head -n 1|cut -d: -f1|cut -d' ' -f2)"
echo "plugging "$name" into" $3 "("$nic")"
	VBoxManage modifyvm $2 --nic$nic intnet
	VBoxManage modifyvm $2 --intnet$nic $3
;;
disconnect)
name="$(VBoxManage list vms 2>/dev/null|grep $2)"
	nics="$(VBoxManage showvminfo $2|grep NIC|grep -v disabled|cut -d: -f1|cut -d' ' -f2)"
printf '%s\n' "$nics" | while IFS= read -r nic
do
	net="$(VBoxManage showvminfo $2|sed 's/ /#/g'|grep NIC#$nic|cut -d: -f4|cut -d\' -f2)"
	echo "unplugging "$name" from " $net "("$nic")"
	VBoxManage modifyvm $2 --nic$nic none
done
;;
*)
  echo "Usage: $0 {intnet|unplug|plug|disconnect}" >&2
  exit 1
  ;;
esac

