#!/bin/bash 
ls -l /proc/$1/fd | tail -n +2 |wc -l
