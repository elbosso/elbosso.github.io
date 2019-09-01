#!/bin/bash
I="$1"
PORT="$2"
RATE="$3"
DELAY="$4"
tc qdisc del dev "$I" root
tc qdisc add dev "$I" handle 1: root htb
tc class add dev "$I" parent 1: classid 1:1 htb rate "$RATE"
tc qdisc add dev "$I" parent 1:1 handle 10: netem delay "$DELAY"
tc filter add dev "$I" protocol ip parent 1: prio 1 u32 match ip dport "$PORT" 0xffff flowid 1:1
tc filter add dev "$I" protocol ip parent 1: prio 1 u32 match ip sport "$PORT" 0xffff flowid 1:1

