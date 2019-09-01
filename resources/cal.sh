#!/bin/sh
rootdir="/tmp/te"
#echo "ls -ht $rootdir"
#echo $(ls -t $rootdir)
#echo "##"
years=$(ls -ht "$rootdir")
#echo $years
#echo "--"
a=$(date "+%d" 2>/dev/null)
bn=~/.$(basename "$0")
#echo $bn
b=$(date --reference="$bn" +%d 2>/dev/null)
#echo $a
#echo $b
if [ "$a" != "$b" ];
then
	for year in $years
	do
	#echo "$year"
		if [ -d "$rootdir/$year" ];
		then
	#echo "$year"
			if [ "$year" = "forever" ] || [ "$year" = "$(date +%Y)" ];
			then
	#echo "$year"
				months=$(ls -ht "$rootdir/$year")
	#echo $months
	#echo "--"
				for month in $months
				do
	#echo "$month"
					if [ -d "$rootdir/$year/$month" ];
					then
	#echo "$month"
						if [ "$month" = "forever" ] || [ "$month" = "$(date +%m)" ];
						then
							days=$(ls -ht "$rootdir/$year/$month")
	#echo $month
	#echo $days
	#echo "--"
							for day in $days
							do
	#echo "$day"
								if [ -d "$rootdir/$year/$month/$day" ];
								then
	#echo "$day"
									if [ "$day" = "forever" ] || [ "$day" = "$(date +%d)" ];
									then
	 									echo "today $year/$month/$day"
										tasks=$(ls -ht "$rootdir/$year/$month/$day")
										for task in $tasks
										do
							#echo "$month"
											if [ -x "$rootdir/$year/$month/$day/$task" ];
											then
												cwd=$(pwd)
												cd "$rootdir/$year/$month/$day/" || return
												./"$task" 2> ./"$task".error 1> ./"$task".output
												cd "$cwd" || return
											fi
										done
									fi
								fi
							done #days

						fi
					fi
				done #months
			fi
		fi
	done #years
touch "$bn"
#echo $bn
#dd=$(date --reference=$bn +%d 2>/dev/null)
#echo $dd
fi

