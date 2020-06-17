package com.ygaller.detekt

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import java.time.Instant.now

internal class CustomerResourceTest {

  companion object {
    private val objectMapper = ObjectMapper()
  }

  private val customer = CustomerRecord(
      "Herschel",
      "Krustofsky",
      "krusty@clown.com",
      objectMapper.writeValueAsString(CustomerPreferences("en", "usd", "credit","monthly"))
  )

  private val customerDao = object : CustomerDao {
    override fun getCustomer(id: String): CustomerRecord = customer
  }

  private val customerResource = CustomerResource(customerDao)

  @Test
  fun `Test customer`() {

    // Warmup
    repeat(10_000) {
      customerResource.getCustomer("1234")
    }

    val start = now().toEpochMilli()
    repeat(10_000) {
      customerResource.getCustomer("1234")
    }
    println("Took: ${now().toEpochMilli() - start}ms")
  }

  @Test
  fun `Test customer static`() {

    // Warmup
    repeat(10_000) {
      customerResource.getCustomerStatic("1234")
    }

    val start = now().toEpochMilli()
    repeat(10_000) {
      customerResource.getCustomerStatic("1234")
    }
    println("Took: ${now().toEpochMilli() - start}ms")
  }

}
