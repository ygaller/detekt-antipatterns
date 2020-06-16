package com.ygaller.detekt

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue

data class CustomerRecord(
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val preferences: String // json
) {
  companion object {
    private val objectMapper = ObjectMapper()
        .registerModule(KotlinModule()) // To enable no default constructor
  }
  fun toDto(): CustomerDto {
    val preferences = ObjectMapper().registerModule(KotlinModule()).readValue<CustomerPreferences>(preferences)
    return CustomerDto(
        firstName,
        lastName,
        emailAddress,
        preferences.language,
        preferences.currency
    )
  }

  fun toDtoStatic(): CustomerDto {
    val preferences = objectMapper.readValue<CustomerPreferences>(preferences)
    return CustomerDto(
        firstName,
        lastName,
        emailAddress,
        preferences.language,
        preferences.currency
    )
  }
}
