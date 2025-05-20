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

    // ✅ 暴露 cartItems 给 UI 使用
    val cartItems: Flow<List<CartItemEntity>> = cartDao.getAll()

    /** 把一个菜品加入购物车 */
    fun addToCart(item: MenuItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val cartItem = CartItemEntity(
                name     = item.name,
                quantity = 1,
                imageRes = item.imageRes,
                price    = item.price.removePrefix("$").toDouble()
            )
            val newRowId = cartDao.insert(cartItem)
            Log.d("CartViewModel", "已加入购物车：rowId=$newRowId, item=$cartItem")
        }
    }

    /** ✅ 可选：添加删除方法，供 CartScreen/Payment 用 */
    fun remove(item: CartItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.delete(item)
        }
    }

    /** ✅ 可选：支付成功后清空购物车 */
    fun clear() {
        viewModelScope.launch(Dispatchers.IO) {
            cartDao.clearAll()
        }
    }
}
