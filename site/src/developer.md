---
title: Junder Developer Info
layout: page.njk
---

## What are the components of Junder?

The Junder system consists of two components:

  * The [Model Component](/model) identifies dependencies between [Java classes](https://en.wikipedia.org/wiki/Java_class_file) and converts them to [Junder predicates](predicates).  It writes these dependencies as a list of [predicates](/predicates) to an output file.  This file is easy to parse; and you can write a program in any language to work with this information.

  * The [Swing Component](/swing) provides a user interface that allows you to browse the dependencies detected by the _model_.

    
## Footer
See the page on [markdown](Markdown) for more information about how to edit this wiki.
