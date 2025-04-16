package com.example.fit5046a3a4.data

class CartRepository(private val dao: CartItemDao) {
    val items = dao.getAll()
    suspend fun add(item: CartItemEntity)  = dao.insert(item)
    suspend fun remove(item: CartItemEntity) = dao.delete(item)
    suspend fun clear() = dao.clearAll()
}
