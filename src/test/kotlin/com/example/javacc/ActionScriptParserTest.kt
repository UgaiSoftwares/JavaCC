package com.example.javacc

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.StringReader

class ActionScriptParserTest {
    @Test
    fun acceptsExampleScript() {
        val script =
            """
            Title
            Description


            <char0> [RUN] <book> (12)
            <char0> [FIND] <book> (12)
            <char0> [READ] <book> (12)
            <char0> [WALK] <sofa> (244)
            <char0> [SIT] <sofa> (244)
            """.trimIndent() + "\n"

        assertDoesNotThrow {
            ActionScriptParser(StringReader(script)).Input()
        }
    }

    @Test
    fun acceptsSentenceMetadataTitle() {
        val script =
            """
            This line is the title.
            Description


            [RUN] <book> (12)
            [FIND] <book> (12)
            [READ] <book> (12)
            [WALK] <sofa> (244)
            [SIT] <sofa> (244)
            """.trimIndent() + "\n"

        assertDoesNotThrow {
            ActionScriptParser(StringReader(script)).Input()
        }
    }

    @Test
    fun rejectsMalformedActionRow() {
        val script =
            """
            Title
            Description

            <char0> RUN <book> (12)
            """.trimIndent() + "\n"

        assertThrows(ParseException::class.java) {
            ActionScriptParser(StringReader(script)).Input()
        }
    }

    @Test
    fun rejectsUnsupportedActionName() {
        val script =
            """
            Title
            Description

            <char0> [JUMP] <book> (12)
            """.trimIndent() + "\n"

        assertThrows(ParseException::class.java) {
            ActionScriptParser(StringReader(script)).Input()
        }
    }

    @Test
    fun acceptsLowercaseActionName() {
        val script =
            """
            Title
            Description

            <char0> [run] <book> (12)
            """.trimIndent() + "\n"

        assertDoesNotThrow {
            ActionScriptParser(StringReader(script)).Input()
        }
    }
}
