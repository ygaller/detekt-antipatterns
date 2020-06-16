package com.ygaller.detekt

data class CustomerPreferences(
    val language: String,
    val currency: String,
    val paymentType: String, // create / debit
    val paymentPlan: String // yearly / monthly
)
