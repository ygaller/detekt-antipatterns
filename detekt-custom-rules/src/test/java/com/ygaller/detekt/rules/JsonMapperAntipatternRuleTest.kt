package com.ygaller.detekt.rules

import com.nhaarman.expect.expect
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.junit.jupiter.api.Test

internal class JsonMapperAntipatternRuleTest {

  @Test
  fun `compliant class should produce no findings`() {
    val result = JsonMapperAntipatternRule().compileAndLint(compliantClass)
    expect(result).toBeEmpty()
  }

  @Test
  fun `compliant object should produce no findings`() {
    val result = JsonMapperAntipatternRule().compileAndLint(compliantObject)
    expect(result).toBeEmpty()
  }

  @Test
  fun `compliant private in file scope`() {
    expect(JsonMapperAntipatternRule().compileAndLint(compliantPrivateInFile)).toBeEmpty()
    expect(JsonMapperAntipatternRule().compileAndLint(compliantPrivateObjectInFile)).toBeEmpty()
  }

  @Test
  fun `non compliant code should produce accurate findings`() {
    listOf(
        notInCompanionObject,
        partOfLargerExpression,
        classInObject,
        nonCompanionObjectInClass,
        notPrivateInFile
    ).forEach {
      val result = JsonMapperAntipatternRule().compileAndLint(it)
      expect(result.single().issue).toBe(Issue(
          id = "JsonMapperAntipatternRule",
          severity = Severity.Performance,
          debt = Debt.FIVE_MINS,
          description = "This rule reports on json mapper usage antipatterns"
      ))
    }
  }


}

private const val compliantClass: String =
    """
package com.ygaller

class TestedUnit {

  companion object {
    private val objectMapper = ObjectMapper()
  }
}
"""

private const val compliantObject: String =
    """
package com.ygaller

object TestedObject {
    private val objectMapper = ObjectMapper()
}
"""

private const val compliantPrivateInFile: String =
    """
package com.ygaller

private val objectMapper = ObjectMapper()

class TestedObject {
}
"""

private const val compliantPrivateObjectInFile: String =
    """
package com.ygaller

private val someObject = ObjectMapper().readValue("{}")

class TestedObject {
}
"""

private const val notInCompanionObject: String =
    """
package com.ygaller.enum

class TestedUnit {

  private val objectMapper = ObjectMapper()
}
"""

private const val partOfLargerExpression: String =
    """
package com.ygaller.enum

class TestedUnit {

  fun testFunc() {
    val result = ObjectMapper().readValue("{}")
  }
}
"""

private const val classInObject: String =
    """
package com.ygaller.enum

object External {
  class Internal {
      val notGood = ObjectMapper()
  }
}
"""

private const val nonCompanionObjectInClass: String =
    """
package com.ygaller.enum

class External {
  object Internal {
      val notGood = ObjectMapper()
  }
}
"""

private const val notPrivateInFile: String =
    """
package com.ygaller.enum

val notGood = ObjectMapper()

class External {
}
"""
