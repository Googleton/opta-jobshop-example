# opta-jobshop-example

Basic example of a JobShop problem using OptaPlanner.

Used to demonstrate a problem with Late Acceptance in OptaPlanner :

The Late Acceptance algorithm is unable to go back on its track "deep" enough to place tasks in Late Planning mode (where we want our jobs to end on their due date)

Meaning in some cases where tasks A,B,C,D,E,F,G have enough room to fit before the due date, we do not have the F and G tasks planified
