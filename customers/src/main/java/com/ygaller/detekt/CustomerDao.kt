package com.ygaller.detekt

interface CustomerDao {
  fun getCustomer(id: String): CustomerRecord
}

