package com.example.fit5046a3a4.viewmodel

import android.util.Log
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

    val cartItems: Flow<List<CartItemEntity>> = cartDao.getAll()


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
                val newRowId = cartDao.insert(newItem)

            }
        }
    }



    fun remove(item: CartItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.delete(item)
        }
    }

    fun clear() {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.clearAll()
        }
    }
}
