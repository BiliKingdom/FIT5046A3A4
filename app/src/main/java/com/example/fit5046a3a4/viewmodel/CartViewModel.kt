package com.example.fit5046a3a4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.CartItemDao
import com.example.fit5046a3a4.data.CartItemEntity
import com.example.fit5046a3a4.screens.MenuItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartDao: CartItemDao
) : ViewModel() {

    // 所有购物车项目
    val cartItems: Flow<List<CartItemEntity>> = cartDao.getAll()

    // 添加已有的 CartItemEntity
    fun add(item: CartItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.insert(item)
        }
    }

    // 通过菜单项添加进购物车（若已存在则叠加数量）
    fun addToCart(item: MenuItem, quantity: Int = 1) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingItem = cartDao.findByNameAndImage(item.name, item.imageRes)
            if (existingItem != null) {
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + quantity)
                cartDao.update(updatedItem)
            } else {
                val newItem = CartItemEntity(
                    name = item.name,
                    quantity = quantity,
                    imageRes = item.imageRes,
                    price = item.price.removePrefix("$").toDouble()
                )
                cartDao.insert(newItem)
            }
        }
    }

    // 删除指定项目
    fun remove(item: CartItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.delete(item)
        }
    }

    // 清空购物车
    fun clear() {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.clearAll()
        }
    }
}
