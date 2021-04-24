---
title: Markdown
layout: page.njk
---

# Markdown

This wiki uses the [Markdown](http://daringfireball.net/projects/markdown/) syntax. The [MarkDownDemo tutorial](https://bitbucket.org/tutorials/markdowndemo) shows how various elements are rendered. [Bitbucket documentation](https://confluence.atlassian.com/display/BITBUCKET/Use+a+wiki) has more information about how using a wiki.

## Linking
When linking to existing pages; keep in mind that the page name is case sensitive.  

There are two ways you can link to other markdown pages:

```
  - [home](Home)
  - [[Home]]
```
Looks like:
  - [home](Home)
  - [[Home]]

## Syntax highlighting


You can also highlight snippets of text (we use the excellent [Pygments][] library).

[Pygments]: http://pygments.org/


Here's an example of some Python code:

```
#!python

def wiki_rocks(text):
    formatter = lambda t: "funky"+t
    return formatter(text)
```


You can check out the source of this page to see how that's done, and make sure to bookmark [the vast library of Pygment lexers][lexers], we accept the 'short name' or the 'mimetype' of anything in there.
[lexers]: http://pygments.org/docs/lexers/

