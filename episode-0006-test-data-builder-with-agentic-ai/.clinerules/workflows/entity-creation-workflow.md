# Workflow: Work on a task
If you work an any task follow this step by step execution!

## Step 1)
Delegate the main task to a subagent (tool: use_subagent)

## Step 2)
If step 1) is done delegate the following command execution to a subagent (tool: use_subagent):
`mvn clean package -DskipTests | tail -n 20`
If any error occurs add the error to the main task.
Continue with step 1)

## Step 3)
If step 2) is done delegate the following command execution to a subagent (tool: use_subagent):
`mvn test | tail -n 20`
If any error occurs add the error to the main task.
Continue with step 1)

## Step 4)
Create a 'docs/task-summary-<short_description>.md' file.
Use maximum 5 words to describe the task you did and use these 5 words as <short_description>.
If the file already exist add a sequential number at the end of the filename.
Use `task-summary-format-rule.md` rule to format the content.

## Step 5)
Verify if the task-summary md was created.
If the file does not exist continue with step 7)