package it.skrape.core

import it.skrape.SkrapeItDsl
import it.skrape.selects.Doc
import java.io.File
import java.nio.charset.Charset

internal class Reader(
        val file: File,
        val charset: Charset = Charsets.UTF_8
) {

    internal fun read(): Doc = Parser(file.readText(Charsets.UTF_8)).parse()
}

/**
 * Read and parse a html file from local file-system.
 * @param file
 * @param charset defaults to UTF-8
 */
@SkrapeItDsl
fun htmlDocument(file: File, charset: Charset = Charsets.UTF_8, init: Doc.() -> Unit): Doc {
    return Reader(file, charset).read().also(init)
}
