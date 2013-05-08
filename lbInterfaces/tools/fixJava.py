#!/usr/bin/env python

# Function to massage java files that are output from the EA module
import os
import sys
import re
import string
from os.path import join

# Convert first character of string l to upper case
def upperIt(l):
	return string.upper(l[0]) + l[1:]

# Upper case the first non-white character in l after the
# supplied text in str
def upperAfter(l,str):
	sstr = r'\s'+str+r'\s'
	mstr = re.search(sstr,l)
	if mstr:
		sstr = mstr.group()
		linechunk = l.split(sstr,1)
		return linechunk[0] + sstr + upperIt(linechunk[1])
	else:
		return l

# Replace all java identifier instances of arg with repl
def fixStringArg(line, arg, repl="String"):
	line = re.sub(r'\(' + arg + ' ', '(' + repl + ' ', line)
	line = re.sub(' ' + arg + ' ', ' ' + repl + ' ', line)
	arg = upperIt(arg)
	line = re.sub(r'\(' + arg + ' ', '(' + repl + ' ', line)
	line = re.sub(' ' + arg + ' ', ' ' + repl + ' ', line)
	return line

# Fix the java file
def fixJava(root, name):
	fileName = join(root, name)
	print "Fixing: ", fileName
	ifile = open(fileName)
	lines = ifile.readlines()
	outlines = ""
	ifile.close()
	for line in lines:
		outline = ""
		if re.match(r'import', line):
			linechunk = line.split(r'.')
			for x in linechunk[0:-1]:
				outline = outline + x + '.'
			outline = outline + upperIt(linechunk[-1])
		else:
			outline = upperAfter(line,"class")
			outline = upperAfter(outline,"extends")
			if re.search(r'Set<', outline):
				linechunk = outline.split(r'Set<',1)
				outline = linechunk[0] + 'Set<' + upperIt(linechunk[1])
			if not re.match(r'^\s*public void', outline):
				if not re.match(r'^\s*public int', outline):
					outline = upperAfter(outline,"public")
		outline = re.sub(r'import XSDDatatypes.String;','',outline)
		outline = re.sub(r'import XSDDatatypes.AnyURI;','',outline)
		outline = re.sub(r'import XSDDatatypes.AnyType;','',outline)
		outline = re.sub(r'import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeURNorName;','',outline)
		outline = re.sub(r'import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeTag;','',outline)
		outline = re.sub(r'import org.LexGrid.builtins.LocalName;','',outline)
		outline = fixStringArg(outline,'anyURI', 'java.net.URI')
		outline = fixStringArg(outline,'tsURN', 'java.net.URI')
		outline = fixStringArg(outline,'localName')
		outline = fixStringArg(outline,'tsCaseSensitiveDirectoryString')
		outline = fixStringArg(outline,'tsCaseIgnoreIA5String')
		outline = fixStringArg(outline,'string')
		outline = fixStringArg(outline,'version')
		outline = fixStringArg(outline,'tsTimestamp')
		outline = fixStringArg(outline,'entityDescription')
		outline = fixStringArg(outline,'conceptCode')
		outline = fixStringArg(outline,'CodingSchemeURNorName')
		outline = fixStringArg(outline,'CodingSchemeTag')
		outline = fixStringArg(outline,'anyType', 'java.lang.Object')
		outlines = outlines + outline
	if re.match(r'[a-z]',name):
		os.remove(fileName)
		fileName = join(root,upperIt(name))
	ofile = open(fileName, 'w')
	ofile.write(outlines)
	ofile.close()


if len(sys.argv) != 2:
	print( "Usage " + sys.argv[0] + " <directory>")
	sys.exit()
	
for root, dirs, files in os.walk(sys.argv[1]):
	for name in files:
		if name.endswith('.java'):
			fixJava(root,name)