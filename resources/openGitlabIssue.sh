#!/bin/bash

rawurlencode() {
  local string="${1}"
  local strlen=${#string}
  local encoded=""
  local pos c o

  for (( pos=0 ; pos<strlen ; pos++ )); do
     c=${string:$pos:1}
     case "$c" in
        [-_.~a-zA-Z0-9] ) o="${c}" ;;
        * )               printf -v o '%%%02x' "'$c"
     esac
     encoded+="${o}"
  done
  echo "${encoded}"    # You can either set a return variable (FASTER) 
  REPLY="${encoded}"   #+or echo the result (EASIER)... or both... :p
}

usage()
{
    echo "usage: openIssue.sh -h host  -p projectid  [-l labels ]  -x token  -t title  [ -d description ] [ -a assignee_id ] [ -u due_date ] [ -m milestone_id ] | [-?]"
}

#HOST=gitlab.fritz.box
#PRIVATETOKEN=SxUM-SNxc-DKeH5kLUcv
#PROJECTID=6
#LABELS=To%20Do
LABELS=""
#LABELS=$( rawurlencode "$LABELS,automatic" )
#TITLE="Issues with auth - encoding test"
#TITLE=$( rawurlencode "$TITLE" )
DESCRIPTION="" 
#DESCRIPTION=$( rawurlencode "$DESCRIPTION" )

#http://linuxcommand.org/lc3_wss0120.php
while [ "$1" != "" ]; do
    case $1 in
        -h | --host )           shift
                                HOST="$1"
                                ;;
        -p | --projectid )      shift
                                PROJECTID="$1"
                                ;;
        -l | --labels )         shift
                                LABELS="$1"
                                ;;
        -x | --token )          shift
                                PRIVATETOKEN="$1"
                                ;;
        -t | --title )          shift
                                TITLE="$1"
                                ;;
        -d | --description )    shift
                                DESCRIPTION="$1"
                                ;;
        -a | --assignee_id )    shift
                                ASSIGNEE_ID="$1"
                                ;;
        -u | --due_date )       shift
                                DUE_DATE="$1"
                                ;;
        -m | --milestone_id )      shift
                                MILESTONE_ID="$1"
                                ;;
        -? | --help )           usage
                                exit
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done

if [ -z "${HOST+x}" ]; then echo "Error: host is unset" && exit 2; fi
if [ -z "${PRIVATETOKEN+x}" ]; then 
if [ -n "${GITLAB_ACCESS_TOKEN+x}" ]; then 
PRIVATETOKEN=$GITLAB_ACCESS_TOKEN
else
echo "Error: token is unset" && exit 2; 
fi
fi
if [ -z "${TITLE+x}" ]; then echo "Error: title is unset" && exit 2; fi
if [ -z "${PROJECTID+x}" ]; then echo "Error: projectid is unset" && exit 2; fi

LABELS=$( rawurlencode "$LABELS,automatic" )
TITLE=$( rawurlencode "$TITLE" )
DESCRIPTION=$( rawurlencode "$DESCRIPTION" )
CURL_OPTIONS=(-k --silent --location)

ISSUEID=$(curl "${CURL_OPTIONS[@]}" --request POST --header "PRIVATE-TOKEN: $PRIVATETOKEN" "http://$HOST/api/v4/projects/$PROJECTID/issues?title=$TITLE&labels=$LABELS&description=$DESCRIPTION"|jq .iid)

if [ -n "${ASSIGNEE_ID+x}" ]; then
curl "${CURL_OPTIONS[@]}" --request PUT --header "PRIVATE-TOKEN: $PRIVATETOKEN" "http://$HOST/api/v4/projects/$PROJECTID/issues/$ISSUEID?assignee_id=$ASSIGNEE_ID" >/dev/null
fi

if [ -n "${DUE_DATE+x}" ]; then
curl "${CURL_OPTIONS[@]}" --request PUT --header "PRIVATE-TOKEN: $PRIVATETOKEN" "http://$HOST/api/v4/projects/$PROJECTID/issues/$ISSUEID?due_date=$DUE_DATE" >/dev/null
fi

if [ -n "${MILESTONE_ID+x}" ]; then
curl "${CURL_OPTIONS[@]}" --request PUT --header "PRIVATE-TOKEN: $PRIVATETOKEN" "http://$HOST/api/v4/projects/$PROJECTID/issues/$ISSUEID?milestone_id=$MILESTONE_ID" >/dev/null
fi

ISSUEURL=$(curl "${CURL_OPTIONS[@]}" --header "PRIVATE-TOKEN: $PRIVATETOKEN" "http://$HOST/api/v4/projects/$PROJECTID/issues/$ISSUEID"|jq .web_url)

COMMENT="Created automatically at "$(LC_ALL=de_DE.utf8 date)
COMMENT=$( rawurlencode "$COMMENT" )

curl "${CURL_OPTIONS[@]}" --request POST --header "PRIVATE-TOKEN: $PRIVATETOKEN" "http://$HOST/api/v4/projects/$PROJECTID/issues/$ISSUEID/notes?body=$COMMENT" >/dev/null

echo "created $ISSUEURL"

