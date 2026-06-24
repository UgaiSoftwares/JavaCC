package com.example.javacc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;
import org.junit.jupiter.api.Test;

class ActionScriptParserTest {
  @Test
  void acceptsExampleScript() {
    String script =
        """
        Title
        Description


        <char0> [RUN] <book> (12)
        <char0> [FIND] <book> (12)
        <char0> [READ] <book> (12)
        <char0> [WALK] <sofa> (244)
        <char0> [SIT] <sofa> (244)
        """;

    assertDoesNotThrow(() -> new ActionScriptParser(new StringReader(script)).Input());
  }

  @Test
  void acceptsSentenceMetadataTitle() {
    String script =
        """
        This line is the title.
        Description


        [RUN] <book> (12)
        [FIND] <book> (12)
        [READ] <book> (12)
        [WALK] <sofa> (244)
        [SIT] <sofa> (244)
        """;

    assertDoesNotThrow(() -> new ActionScriptParser(new StringReader(script)).Input());
  }

  @Test
  void rejectsMalformedActionRow() {
    String script =
        """
        Title
        Description

        <char0> RUN <book> (12)
        """;

    assertThrows(
        ParseException.class, () -> new ActionScriptParser(new StringReader(script)).Input());
  }

  @Test
  void rejectsUnsupportedActionName() {
    String script =
        """
        Title
        Description

        <char0> [JUMP] <book> (12)
        """;

    assertThrows(
        ParseException.class, () -> new ActionScriptParser(new StringReader(script)).Input());
  }

  @Test
  void acceptsLowercaseActionName() {
    String script =
        """
        Title
        Description

        <char0> [run] <book> (12)
        """;

    assertDoesNotThrow(() -> new ActionScriptParser(new StringReader(script)).Input());
  }
}
