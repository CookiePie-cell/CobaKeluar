package com.salugan.cobakeluar.utils

object StringProcessing {
    fun processLatexText(text: String): String {
        return text.replace(
            "<img\\s+src=\"(.*?)\"(\\s*/)?\\s*>".toRegex(),
            "<img src=\"$1\" style=\"max-width: 100%; max-height: 100%; width: auto; height: auto;\">"
        )
    }
}