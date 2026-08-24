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
or malformed argument, and a mark with a missing/non-numeric/out-of-range
index each produce a specific "OOPS!!!" message (via `BotException`)
instead of crashing or silently doing nothing, and no task is added or
changed as a result.

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
     OOPS!!! I don't understand "blah" - try list, todo, deadline, event, mark, unmark, or bye.
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
     Bye. Hope to see you again soon!
    ____________________________________________________________
```
