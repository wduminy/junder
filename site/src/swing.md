---
title: Junder Swing module
layout: page.njk
---

## Architecture

This module uses a *document driven architecture*.  The document is a term we use to refer to the state of the application.  There are a number of different types of elements in the document:
  - The *domain model elements* are entirely independent from the user interface.  They are implemented in the [Model module](/model).
  - The *interface model elements* describe the current state of the user interface; such as which views are open and what objects are selected. 
  - The *task model elements* is the state that relates to [background threads](https://docs.oracle.com/javase/tutorial/uiswing/concurrency/worker.html).  


![Basic Architecture Idea](/images/swing_module.png)

### Design rules         
 - All changes to the document model be done via tasks.  It is better to assume that such changes will be time consuming.
 - Most changes to interface model elements do not need tasks.  These changes are initiated by the user interface and the user interface is updated as a consequence.  
 - Widget elements are contained by a view and are not aware of the document; changes are propagated to widgets via a view. 
 - A widget model interface provides state to the widget.  Interface model elements in the document provides this state to the widget. 
 - The interface model receives updates from the task model and the user interface elements and notifies the other user user interface elements of changes to the domain model and the selection state.
 - It is preferred that a widget that causes state changes updates its own view. However, this could cause a widget to be repainted more than once (when it is notified about its own change).  The solution to this problem is to be solved in the widget code. In order to bypass potential complexity, the widget can decide not to update its own view.
 
## Built using this software

  * [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing/)
  * [Eclipse Window Builder](https://eclipse.org/windowbuilder/) 
  * Using [Plant UML](http://www.plantuml.com/plantuml/uml/SyfFKj2rKt3CoKnELR1Io4ZDoSa70000) to draw diagrams
 