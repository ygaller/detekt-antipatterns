package com.ygaller.detekt

import com.ygaller.detekt.rules.JsonMapperAntipatternRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class CustomRulesProvider : RuleSetProvider {
  override val ruleSetId: String = "custom-rules" //this is the string used in detekt.yml

  override fun instance(config: Config): RuleSet = RuleSet(
      ruleSetId,
      listOf(JsonMapperAntipatternRule())
  )
}
