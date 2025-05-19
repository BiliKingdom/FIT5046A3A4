package com.example.fit5046a3a4.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.CartItemDao
import com.example.fit5046a3a4.data.CartItemEntity
import com.example.fit5046a3a4.screens.MenuItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartDao: CartItemDao
) : ViewModel() {

    /** 把一个菜品加入购物车 */
    fun addToCart(item: MenuItem) {
        viewModelScope.launch(Dispatchers.IO) {
            // 构造 CartItemEntity
            val cartItem = CartItemEntity(
                name     = item.name,     // 菜品名称
                quantity = 1,             // 默认数量 1
                imageRes = item.imageRes, // 菜品图片资源
                price    = item.price.removePrefix("$").toDouble() // 单价
            )

            // 调用 DAO 的插入方法，返回新行的 id
            val newRowId = cartDao.insert(cartItem)
            Log.d("CartViewModel", "已加入购物车：rowId=$newRowId, item=$cartItem")
        }
    }
}
