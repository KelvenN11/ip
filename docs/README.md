# Bot User Guide

Bot is a command-line task manager. Type a command, press Enter, and Bot
tells you what it did — no mouse required.

```
    ____________________________________________________________
 ____   ___  _____
| __ ) / _ \|_   _|
|  _ \| | | | | |
| |_) | |_| | | |
|____/ \___/  |_|
     Hello! I'm Bot.
     What can I do for you?
    ____________________________________________________________
```

## Adding a todo: `todo`

Adds a task with just a description and no date attached.

Example: `todo borrow book`

```
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 task in the list.
```

## Adding a deadline: `deadline`

Adds a task that needs to be done by a specific date, or date and time.

Example: `deadline return book /by 2019-10-15`

```
     Got it. I've added this task:
       [D][ ] return book (by: Oct 15 2019)
     Now you have 2 tasks in the list.
```

A time can be included by appending it to the date as `HHmm` (24-hour
clock): `deadline return book /by 2019-10-15 1800`.

## Adding an event: `event`

Adds a task that starts and ends at specific dates, or dates and times.

Example: `event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600`

```
     Got it. I've added this task:
       [E][ ] project meeting (from: Oct 15 2019, 2:00PM to: Oct 15 2019, 4:00PM)
     Now you have 3 tasks in the list.
```

## Listing all tasks: `list`

Shows every task currently in the list, numbered in the order they were added.

Example: `list`

```
     Here are the tasks in your list:
     1.[T][ ] borrow book
     2.[D][ ] return book (by: Oct 15 2019)
     3.[E][ ] project meeting (from: Oct 15 2019, 2:00PM to: Oct 15 2019, 4:00PM)
```

## Marking a task as done: `mark`

Marks the task at the given list number as done.

Example: `mark 1`

```
     Nice! I've marked this task as done:
       [T][X] borrow book
```

## Marking a task as not done: `unmark`

Marks the task at the given list number as not done.

Example: `unmark 1`

```
     OK, I've marked this task as not done yet:
       [T][ ] borrow book
```

## Deleting a task: `delete`

Removes the task at the given list number.

Example: `delete 2`

```
     Noted. I've removed this task:
       [D][ ] return book (by: Oct 15 2019)
     Now you have 2 tasks in the list.
```

## Finding tasks by date: `on`

Lists every task occurring on a given date: a deadline matches only that
exact date, and an event matches any date within its start/end range.

Example: `on 2019-10-15`

```
     Here are the tasks on Oct 15 2019:
     1.[D][ ] return book (by: Oct 15 2019)
```

## Finding tasks by keyword: `find`

Lists every task whose description contains the given keyword, matched
case-insensitively.

Example: `find book`

```
     Here are the matching tasks in your list:
     1.[T][ ] borrow book
```

## Sorting tasks by date: `sort`

Reorders the task list: every todo first (keeping their original
relative order), then every deadline and event together in ascending
date order. The new order is permanent — it's saved and still applies
the next time Bot starts.

Example: `sort`

```
     Here are your tasks, sorted by date:
     1.[T][ ] borrow book
     2.[D][ ] return book (by: Oct 15 2019)
     3.[E][ ] project meeting (from: Oct 15 2019, 2:00PM to: Oct 15 2019, 4:00PM)
```

## Exiting the program: `bye`

Says goodbye and closes Bot.

Example: `bye`

```
     Bye. Hope to see you again soon!
```
