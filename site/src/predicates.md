---
title: Developer
layout: page.njk
---

# Junder Predicates
The Junder predicates produced by the [Model Component](/model) are described below.  This description uses the [SWI notation](http://www.swi-prolog.org/pldoc/man?section=preddesc).

## is_class(?T)
True when the type ```T``` is a Java class.

## is_interface(?T)
True when the type ```T``` is a Java interface.

## is_package(?A)
True when the atom ```A``` is a Java package.

## is_subtype_of(?TA,?TB)
True when ```TA``` extends or implements ```TB```

## is_in(?Type, ?Container)
True when the ```Type``` is declared in ```Container```. ```Container``` can be a package, a class or an interface.
 
## has_name(?A, ?N)
True when the name of ```A``` is ```N```.

# is_nested(?T)

# is_root(?T)

# uses(?TA,?TB)