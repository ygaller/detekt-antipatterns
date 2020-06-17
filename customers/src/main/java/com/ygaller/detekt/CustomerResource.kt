package com.ygaller.detekt

import com.fasterxml.jackson.databind.ObjectMapper

class CustomerResource(private val customerDao: CustomerDao) {

  fun getCustomer(id: String): CustomerDto =
      customerDao.getCustomer(id).toDto()

  fun getCustomerStatic(id: String): CustomerDto =
      customerDao.getCustomer(id).toDtoStatic()
}
