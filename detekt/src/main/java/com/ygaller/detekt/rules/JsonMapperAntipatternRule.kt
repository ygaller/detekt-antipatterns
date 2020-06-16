package com.ygaller.detekt.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtModifierListOwner
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.psiUtil.isPrivate
import org.jetbrains.kotlin.psi.psiUtil.parents

class JsonMapperAntipatternRule: Rule() {

  private val objectMapperExpressionString = "ObjectMapper()"

  override val issue: Issue = Issue(javaClass.simpleName,
      Severity.Performance,
      "This rule reports on json mapper usage antipatterns",
      Debt.FIVE_MINS)

  override fun visitCallExpression(expression: KtCallExpression) {
    super.visitCallExpression(expression)

    if (expression.text != objectMapperExpressionString) return

    // Make sure there's an object that is a parent and that there's no class that's a closer parent
    val objectIndex = expression.parents.indexOfFirst { it is KtObjectDeclaration }
    val classIndex = expression.parents.indexOfFirst { it is KtClass }

    val inFileScope = objectIndex == -1 && classIndex == -1
    if (inFileScope) {
      validateFileScope(expression)
    } else {
      validateObjectScope(objectIndex, classIndex, expression)
    }

  }

  private fun validateObjectScope(objectIndex: Int, classIndex: Int, expression: KtCallExpression) {
    val noObjectClass = objectIndex == -1
    val classWithinObject = objectIndex != -1 && classIndex != -1 && objectIndex > classIndex
    val nonCompanionObjectInClass =
        objectIndex != -1 &&
            classIndex != -1 &&
            objectIndex < classIndex &&
            !(expression.parents.find { it is KtObjectDeclaration }!! as KtObjectDeclaration).isCompanion()

    if (noObjectClass || classWithinObject || nonCompanionObjectInClass) {
      report(CodeSmell(
          issue = issue,
          entity = Entity.from(expression),
          message = "ObjectMapper() should be declared in a companion object"
      ))
    }
  }

  private fun validateFileScope(expression: KtCallExpression) {
    if (expression.parent !is KtModifierListOwner) return

    val isPrivate = (expression.parent as KtModifierListOwner).isPrivate()
    if (!isPrivate) {
      report(CodeSmell(
          issue = issue,
          entity = Entity.from(expression),
          message = "ObjectMapper() in file scope should be private"
      ))
    }
  }

}
