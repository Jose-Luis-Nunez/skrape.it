package it.skrape.matchers

import io.mockk.every
import io.mockk.mockk
import it.skrape.selects.DocElement
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MatchersKtTest {

    private val anInt = 200
    private val aString = "test"
    private val aNullableString: String? = null

    @Test
    internal fun `toBe is working type safe for Int`() {
        anInt toBe 200
    }

    @Test
    internal fun `toBe is working type safe for String`() {
        aString toBe "test"
    }

    @Test
    internal fun `toBe can handle expected is null`() {
        Assertions.assertThrows(AssertionError::class.java) {
            "null" toBe null
        }
    }

    @Test
    internal fun `toBe can handle actual is null`() {
        Assertions.assertThrows(AssertionError::class.java) {
            aNullableString toBe "foo"
        }
    }

    @Test
    internal fun `toBe is working type safe for null String`() {
        aNullableString toBe null
    }

    @Test
    internal fun `toNotBe is working type safe for null String`() {
        aString toBeNot null
    }

    @Test
    internal fun `toContain is working type safe for String`() {
        aString toContain "es"
    }

    @Test
    internal fun `toContain is throwing exception on assertion failure`() {
        Assertions.assertThrows(AssertionError::class.java) {
            aString toContain "foo"
        }
    }

    @Test
    internal fun `toNotContain is working type safe for String`() {
        aString toNotContain "foo"
    }

    @Test
    internal fun `toNotContain is throwing exception on assertion failure`() {
        Assertions.assertThrows(AssertionError::class.java) {
            aString toNotContain "es"
        }
    }

    @Test
    internal fun `toContain is working with lists`() {
        listOf("foo", "bar") toContain "bar"
    }

    @Test
    internal fun `toContain on lists is throwing exception on assertion failure`() {
        Assertions.assertThrows(AssertionError::class.java) {
            listOf("foo", "bar") toContain "schnitzel"
        }
    }

    @Test
    internal fun `toBePresent can handle multiple presents of matching ELEMENTS`() {
        val elements = mockk<List<DocElement>> { every { size } returns 2 }
        elements.toBePresent
    }

    @Test
    internal fun `toBePresent can handle single occurrence of matching ELEMENTS`() {
        val elements = mockk<List<DocElement>> { every { size } returns 1 }
        elements.toBePresent
    }

    @Test
    internal fun `toBePresent is throwing exception if no ELEMENTS matches`() {
        val elements = mockk<List<DocElement>> { every { size } returns 0 }
        Assertions.assertThrows(AssertionError::class.java) {
            elements.toBePresent
        }
    }

    @Test
    internal fun `toBePresent can handle multiple occurrence of an ELEMENT`() {
        val element = mockk<DocElement> {
            every { isPresent } returns true
            every { cssSelector } returns ".foo"
        }
        element.toBePresent
    }

    @Test
    internal fun `toBePresent can handle single occurrence of an ELEMENT`() {
        val element = mockk<DocElement> {
            every { isPresent } returns true
            every { cssSelector } returns ".foo"
        }
        element.toBePresent
    }

    @Test
    internal fun `toBePresent is throwing exception if no ELEMENT matches`() {
        val element = mockk<DocElement> {
            every { isPresent } returns false
            every { cssSelector } returns ".foo"
        }
        Assertions.assertThrows(AssertionError::class.java) {
            element.toBePresent
        }
    }

    @Test
    internal fun `toBeNotPresent can handle multiple presents of matching ELEMENTS`() {
        val elements = mockk<List<DocElement>> { every { size } returns 2 }
        elements.toBePresent
    }

    @Test
    internal fun `toBeNotPresent can handle non existent ELEMENTS`() {
        val elements = mockk<List<DocElement>> { every { size } returns 0 }
        elements.toBeNotPresent
    }

    @Test
    internal fun `toBeNotPresent is throwing exception on single occurrence of matching ELEMENTS`() {
        val elements = mockk<List<DocElement>> { every { size } returns 1 }
        Assertions.assertThrows(AssertionError::class.java) {
            elements.toBeNotPresent
        }
    }

    @Test
    internal fun `toBeNotPresent is throwing exception on multiple presents of matching ELEMENTS`() {
        val elements = mockk<List<DocElement>> { every { size } returns 2 }
        Assertions.assertThrows(AssertionError::class.java) {
            elements.toBeNotPresent
        }
    }

    @Test
    internal fun `toBeEmpty can handle empty list`() {
        emptyList<Any>().toBeEmpty
    }

    @Test
    internal fun `toBeEmpty is throwing exception NON empty list`() {
        Assertions.assertThrows(AssertionError::class.java) {
            listOf(1, 2, 3).toBeEmpty
        }
    }

    @Test
    internal fun `toBeNotEmpty can handle NON empty list`() {
        listOf(1, 2, 3).toBeNotEmpty
    }

    @Test
    internal fun `toBeNotEmpty is throwing exception empty list`() {
        Assertions.assertThrows(AssertionError::class.java) {
            emptyList<Any>().toBeNotEmpty
        }
    }

    @Test
    internal fun `isNumeric returns true if a string contains a number`() {
        val elements = mutableListOf(DocElement(org.jsoup.nodes.Element("$11%/")))
        elements.isNumeric
    }
}
