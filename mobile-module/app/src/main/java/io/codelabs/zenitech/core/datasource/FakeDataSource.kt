package io.codelabs.zenitech.core.datasource

import io.codelabs.zenitech.data.Product
import kotlin.random.Random

object FakeDataSource {

    fun loadProducts(): MutableList<Product> {
        val products = mutableListOf<Product>()
        for (i in 0 until 100) {
            val product = Product(
                "$i", "Laptop Item $i", Random.nextDouble(12.99,1999.99),
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                ""
            )
            products.add(product)
        }
        return products
    }

}