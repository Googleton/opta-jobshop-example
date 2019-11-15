# opta-jobshop-example

Basic example of a JobShop problem using OptaPlanner.

Used to demonstrate a problem with Late Acceptance in OptaPlanner :

The Late Acceptance algorithm is unable to go back on its track "deep" enough to place tasks in Late Planning mode (where we want our jobs to end on their due date)

Meaning in some cases where tasks A,B,C,D,E,F,G have enough room to fit before the due date, we do not have the F and G tasks planified

## Model

Our model is a variant of the Job Shop Scheduling problem. 
We want to represent the manufacturing steps that could take place in a factory.
We have several Jobs, with Tasks. This could be considered a cooking recipe with steps to follow in order.

### Planification

Represents a Solution to the problem. Has a list of tasks, a list of machines and a start/end date.

### Task

A task is a step inside a manufacturing process. It has a start date, a machine on which it runs, and belongs to a Group (Job)

### Machine

Represents a machine inside a factory. It has a list of Tasks that run on it. Machines cannot run tasks concurrently.

### TaskGroup

Represents a Job. Contains a list of Tasks that belong to the job. 

## Variables

### Task.Machine

The machine the task is scheduled to run on

### Task.Start

The instant at which the task should start.
Updating the start date also updates the end date. (end = start + task duration)
Time is modelized as a VariableRange of Instants (OptaPlanner specific).

## Constraints
### Overlapping
Two tasks should not overlap as a machine can only have 1 task running at any given time.
If two tasks overlap, the solution is considered infeasible.

### Precedence
Tasks should be executed in the order they are created (named alphabetically for readability).
This represents the steps needed to produce items. Each task is a step, and they must be done in the proper order (you cannot assemble parts that have not been produced yet).
If task A of group 1 ends after task B of group 2 started, the solution is considered infeasible.

### Due date planning
Jobs (TaskGroups) should end on their due date.
If a Task ends after the due date of its group, the solution is considered infeasible.

### Force planification
Tasks should be planified.
If a task does not belong to a machine and does not have a start date, the solution is feasible, but loses medium score.
