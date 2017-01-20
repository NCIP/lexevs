#!/bin/bash
#FILES=/Users/m029206/Desktop/valuesets/*
for f in 'find /Users/m029206/Desktop/valuesets/ -name *.xml'
do
echo "Processing $f file..."
java -Xmx1500m -cp "../runtime/lbPatch.jar:../runtime-components/extLib/*" org.lexgrid.valuesets.admin.LoadValueSetDefinition -in -i
done
