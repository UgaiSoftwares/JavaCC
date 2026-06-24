# JavaCC ActionScript validator

This repository includes two JavaCC examples:

- an ActionScript validator
- a small arithmetic-expression parser sample

## What it does

- Accepts an optional metadata preface made of plain text lines
- Validates action rows in the form `[ACTION] <object> (id)`
- Accepts an optional avatar column in the form `<avatar>`
- Rejects malformed rows with a parser error
- Expects newline-terminated lines

## ActionScript Validator

- [`src/main/javacc/ActionScriptParser.jj`](src/main/javacc/ActionScriptParser.jj)

## Generate and run

1. Run the parser task with Gradle.
2. Pass a script with `-Pscript=...` if you want to validate your own input.

Example:

```bash
./gradlew runActionScriptParser
./gradlew runActionScriptParser -Pscript=$'Title\nDescription\n\n\n<char0> [RUN] <book> (12)\n'
```

## Arithmetic Sample

- [`src/main/javacc/ExpressionParser.jj`](src/main/javacc/ExpressionParser.jj)

Example:

```bash
./gradlew runExpressionParser
./gradlew runExpressionParser -Pexpression='10 + 2 * (3 + 4)'
```
