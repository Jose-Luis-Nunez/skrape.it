package it.skrape.selects

import it.skrape.SkrapeItDsl
import org.jsoup.nodes.Element

@Suppress("TooManyFunctions")
@SkrapeItDsl
class DocElement internal constructor(override val element: Element, override val relaxed: Boolean) : DomTreeElement() {
    constructor(element: Element) : this(element, false)

    /**
     * Get the name of the tag for this element. E.g. {@code div}.
     *
     * @return String of the tag's name
     */
    val tagName by lazy { element.tagName().orEmpty() }

    /**
     * Gets the text owned by this element only; does not get the combined text of all children.
     * For example, given HTML {@code <p>Hello <b>there</b> now!</p>}, {@code p.ownText()} returns {@code "Hello now!"},
     * whereas {@code text} returns {@code "Hello there now!"}.
     * Note that the text within the {@code b} element is not returned, as it is not a direct child of the {@code p} element.
     *
     * @return unencoded text, or empty string if none.
     * @see text
     */
    val ownText by lazy { element.ownText().orEmpty() }

    /**
     * Get all of the element's attributes.
     * @return Map<String, String>> of attribute key value pairs
     */
    val attributes: Map<String, String> by lazy { element.attributes().map { it.key to it.value }.toMap() }

    /**
     * Get all attribute keys of the element.
     * @return List<String>
     */
    val attributeKeys by lazy { attributes.map { it.key } }

    /**
     * Get all attribute values of the element.
     * @return List<String>
     */
    val attributeValues by lazy { attributes.map { it.value } }

    /**
     * Get the element's attribute value of a given attribute key.
     * @return String of attribute value or empty if non existing.
     */
    infix fun attribute(attributeKey: String): String = attributes[attributeKey].orEmpty()

    fun hasAttribute(attributeKey: String): Boolean = attribute(attributeKey).isNotBlank()

    /**
     * Check if the element is present thereby it will return true if the given node can be found otherwise false.
     * @return Boolean
     */
    val isPresent by lazy { allElements.isNotEmpty() }

    val isNotPresent by lazy { !isPresent }

    val className by lazy { element.className().orEmpty() }

    val cssSelector by lazy { element.cssSelector().orEmpty() }

    override val toCssSelector: String
        get() = cssSelector

    @Deprecated("use 'findAll(cssSelector: String) instead'", ReplaceWith("findAll(cssSelector)"))
    fun select(cssSelector: String) = element.select(cssSelector).map { DocElement(it, relaxed) }
}

val List<DocElement>.text
    get(): String = joinToString(separator = " ") { it.text }

val List<DocElement>.html
    get(): String = joinToString(separator = "\n") { it.outerHtml }

val List<DocElement>.isPresent
    get(): Boolean = size > 0

val List<DocElement>.isNotPresent
    get(): Boolean = !isPresent

val List<DocElement>.eachText
    get(): List<String> = map { it.text }

infix fun List<DocElement>.attribute(attributeKey: String): String =
        filter { it.hasAttribute(attributeKey) }
                .joinToString { it.attribute(attributeKey) }

infix fun List<DocElement>.eachAttribute(attributeKey: String): List<String> =
        map { it attribute attributeKey }
                .filter { it.isNotEmpty() }

val List<DocElement>.eachHref
    get(): List<String> = eachAttribute("href")
            .filter { it.isNotEmpty() }

val List<DocElement>.eachSrc
    get(): List<String> = eachAttribute("src")
            .filter { it.isNotEmpty() }

val List<DocElement>.eachLink
    get(): Map<String, String> =
        filter { it.hasAttribute("href") }
                .associate { it.text to it.attribute("href") }

val List<DocElement>.eachImage
    get(): Map<String, String> =
        filter { it.tagName == "img" }
                .filter { it.hasAttribute("src") }
                .associate { it.attribute("alt") to it.attribute("src") }


fun <T> List<DocElement>.forEachLink(init: (text: String, url: String) -> T) {
    filter { it.hasAttribute("href") }
            .forEach { init(it.text, it.attribute("href")) }
}

fun <T> List<DocElement>.forEachImage(init: (altText: String, url: String) -> T) {
    filter { it.tagName == "img" }
            .filter { it.hasAttribute("src") }
            .forEach { init(it.attribute("alt"), it.attribute("src")) }
}

