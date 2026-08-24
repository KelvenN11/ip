---
name: test-ui
description: Run the console UI test cases in test/ui-test-plan.md against the compiled Duke program and report pass/fail with a full transcript. Use after any change to src/main/java to check for regressions.
---

# test-ui

Runs the black-box console test cases recorded in `test/ui-test-plan.md`
against the current `Duke` program, in order, stopping at the first
failure.

## Steps

1. Read `test/ui-test-plan.md` in full. It lists, for each test case: an
   aim, an **Input** block (one command per line, to be sent to the
   program's standard input in order) and an **Expected Output** block
   (the exact standard output the program must produce for that input).

2. Build the program fresh:
   ```
   javac -d out src/main/java/*.java
   ```
   If the build fails, stop and report the compiler error — do not run any
   test cases.

3. For each test case, in the order listed in the file:
   a. Run `java -cp out Duke`, feeding the test case's **Input** lines to
      standard input in order (e.g. via `printf 'line1\nline2\n... | java
      -cp out Duke`).
   b. Capture the full standard output produced.
   c. Compare it verbatim against the test case's **Expected Output**
      block. Ignore a difference in only the trailing newline at the very
      end; treat any other difference (including whitespace/indentation)
      as a failure.
   d. Print a transcript for this test case: the test case name/aim, the
      input lines exactly as sent, and the actual output produced — so the
      console session is visible.
   e. If the actual output does not match the expected output, stop
      immediately (do not run the remaining test cases) and report:
      - which test case failed,
      - the full expected output,
      - the full actual output,
      - if easy to identify, the first line at which they diverge.

4. If every test case passes, print a short summary (e.g. "N/N test cases
   passed") after the transcripts.

## Notes

- Test cases must be run independently — each one starts a fresh `java -cp
  out Duke` process, so task state never carries over between test cases.
- If `test/ui-test-plan.md` is missing or has no test cases, report that
  instead of silently doing nothing.
- Do not modify `src/main/java` or `test/ui-test-plan.md` as part of
  running this skill; it only builds, runs, and reports.
