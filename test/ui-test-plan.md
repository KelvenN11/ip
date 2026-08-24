# UI Test Plan

This file drives the `test-ui` skill. It records how to build and run the
program, and the list of test cases the skill executes against it.

Each test case is self-contained: it starts the program fresh, feeds it the
listed input lines (one per line, in order, as if typed by a user), and
compares the program's full console output against the expected output.

## How to run

1. Build: `javac -d out src/main/java/*.java` (run from the repository root).
2. For each test case, run `java -cp out Duke`, sending the test case's
   **Input** lines to standard input in order (one command per line).
3. Compare the program's actual standard output, verbatim, against the test
   case's **Expected Output** block. A trailing newline difference at the
   very end of output is not considered a mismatch; anything else is.
4. Run test cases in the order listed below. Stop at the first failure.

## Test Case 1: Greet and exit

**Aim:** The program greets the user and exits cleanly when the first
command is `bye`, with no tasks added.

**Input:**
```
bye
```

**Expected Output:**
```
    ____________________________________________________________
 ____   ___  _____ 
| __ ) / _ \|_   _|
|  _ \| | | | | |  
| |_) | |_| | | |  
|____/ \___/  |_|  
     Hello! I'm BOT.
     What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test Case 2: Listing with no tasks

**Aim:** `list` on an empty task collection prints only the header line,
with no numbered entries.

**Input:**
```
list
bye
```

**Expected Output:**
```
    ____________________________________________________________
 ____   ___  _____ 
| __ ) / _ \|_   _|
|  _ \| | | | | |  
| |_) | |_| | | |  
|____/ \___/  |_|  
     Hello! I'm BOT.
     What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________
    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test Case 3: Add a todo, a deadline, and an event, then list them

**Aim:** Each of the three task types is added with the correct type icon,
date/time text, and running task count, and `list` shows them numbered in
insertion order.

**Input:**
```
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected Output:**
```
    ____________________________________________________________
 ____   ___  _____ 
| __ ) / _ \|_   _|
|  _ \| | | | | |  
| |_) | |_| | | |  
|____/ \___/  |_|  
     Hello! I'm BOT.
     What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 task in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Sunday)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] borrow book
     2.[D][ ] return book (by: Sunday)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test Case 4: Mark and unmark a task

**Aim:** `mark N` and `unmark N` flip a task's done status, the
confirmation message reflects the change, and `list` shows the updated
status icon afterwards.

**Input:**
```
todo read book
todo join club
mark 1
list
unmark 1
list
bye
```

**Expected Output:**
```
    ____________________________________________________________
 ____   ___  _____ 
| __ ) / _ \|_   _|
|  _ \| | | | | |  
| |_) | |_| | | |  
|____/ \___/  |_|  
     Hello! I'm BOT.
     What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 task in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] join club
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[T][ ] join club
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[T][ ] join club
    ____________________________________________________________
    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test Case 5: Error handling for invalid input

**Aim:** An unrecognized command word, a todo/deadline/event with a missing
or malformed argument, and a mark/delete with a missing/non-numeric/
out-of-range index each produce a specific "OOPS!!!" message (via
`BotException`) instead of crashing or silently doing nothing, and no
task is added, changed, or removed as a result — the trailing `list`
confirms the task collection is still empty after every failed command.
`mark`, `unmark`, and `delete` all validate their index through the same
`parseTaskIndex` helper, so exercising it via `mark` and `delete` here is
taken as sufficient coverage for `unmark` too, rather than repeating the
same three cases a third time.

**Input:**
```
blah
todo
deadline
deadline return book
event
event meeting /from Mon
mark
mark abc
mark 1
delete
delete abc
delete 1
list
bye
```

**Expected Output:**
```
    ____________________________________________________________
 ____   ___  _____ 
| __ ) / _ \|_   _|
|  _ \| | | | | |  
| |_) | |_| | | |  
|____/ \___/  |_|  
     Hello! I'm BOT.
     What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! I don't understand "blah" - try list, todo, deadline, event, mark, unmark, delete, or bye.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! A todo needs a description, e.g. "todo borrow book".
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! A deadline needs a "/by" date or time, e.g. "deadline return book /by Sunday".
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! A deadline needs a "/by" date or time, e.g. "deadline return book /by Sunday".
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! An event needs both "/from" and "/to", e.g. "event project meeting /from Mon 2pm /to 4pm".
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! An event needs both "/from" and "/to", e.g. "event project meeting /from Mon 2pm /to 4pm".
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! Tell me which task number to mark, e.g. "mark 2".
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! "abc" isn't a task number - try something like "mark 2".
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! There's no task number 1 in your list.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! Tell me which task number to delete, e.g. "delete 2".
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! "abc" isn't a task number - try something like "delete 2".
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! There's no task number 1 in your list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________
    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```

## Test Case 6: Delete a task

**Aim:** `delete N` removes the Nth task, prints its final state and the
new running task count, and a subsequent `list` shows the remaining
tasks renumbered with no gap left by the deleted task.

**Input:**
```
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
mark 1
mark 2
mark 4
list
delete 3
list
bye
```

**Expected Output:**
```
    ____________________________________________________________
 ____   ___  _____ 
| __ ) / _ \|_   _|
|  _ \| | | | | |  
| |_) | |_| | | |  
|____/ \___/  |_|  
     Hello! I'm BOT.
     What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 task in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] join sports club
     Now you have 4 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 5 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [D][X] return book (by: June 6th)
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] join sports club
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: June 6th)
     3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     4.[T][X] join sports club
     5.[T][ ] borrow book
    ____________________________________________________________
    ____________________________________________________________
     Noted. I've removed this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 4 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: June 6th)
     3.[T][X] join sports club
     4.[T][ ] borrow book
    ____________________________________________________________
    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
```
