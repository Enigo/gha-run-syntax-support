package com.gharunsyntaxsupport

import com.intellij.lang.Language
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.psi.*
import org.jetbrains.annotations.NotNull

class GithubActionsRunHighlighterInjector : MultiHostInjector {
    override fun getLanguagesToInject(@NotNull registrar: MultiHostRegistrar, @NotNull context: PsiElement) {
        val virtualFile = context.containingFile.virtualFile ?: return
        if (!virtualFile.path.contains(".github/")) return

        if (context is YAMLScalar) {
            val runKeyValue = context.parent as? YAMLKeyValue ?: return
            if (runKeyValue.keyText != "run") return

            val parentMapping = runKeyValue.parent as? YAMLMapping ?: return
            val shellValue = parentMapping.keyValues.find { it.keyText == "shell" }?.valueText

            // Determine which language to inject based on shell
            val language = when {
                shellValue == null -> Language.findLanguageByID("Shell Script")
                shellValue.contains("pwsh", true) -> Language.findLanguageByID("PowerShell")
                shellValue.contains("python", true) -> Language.findLanguageByID("Python")
                shellValue.contains("cmd", true) -> Language.findLanguageByID("Batch")
                shellValue.contains("bash", true) -> Language.findLanguageByID("Shell Script")
                shellValue.contains("sh", true) -> Language.findLanguageByID("Shell Script")
                else -> Language.findLanguageByID("Shell Script")
            } ?: return

            // Skip GitHub Expressions
            val text = context.text
            val bashRanges = mutableListOf<TextRange>()
            val exprRegex = Regex("\\$\\{\\{.*?}}")

            var last = 0
            for (match in exprRegex.findAll(text)) {
                val start = match.range.first
                val end = match.range.last + 1
                if (last < start) {
                    bashRanges.add(TextRange(last, start))
                }
                last = end
            }
            if (last < text.length) {
                bashRanges.add(TextRange(last, text.length))
            }

            if (bashRanges.isNotEmpty()) {
                registrar.startInjecting(language)
                for (range in bashRanges) {
                    registrar.addPlace(null, null, context, range)
                }
                registrar.doneInjecting()
            }

            // Inject GitHub Expressions separately
            val expressionLanguage = Language.findLanguageByID("GithubExpressionLanguage") ?: return
            for (match in exprRegex.findAll(text)) {
                val exprRange = TextRange(match.range.first, match.range.last + 1)
                registrar.startInjecting(expressionLanguage)
                registrar.addPlace(null, null, context, exprRange)
                registrar.doneInjecting()
            }
        }
    }

    override fun elementsToInjectIn(): List<Class<out PsiElement>> {
        return listOf(YAMLScalar::class.java)
    }
}
