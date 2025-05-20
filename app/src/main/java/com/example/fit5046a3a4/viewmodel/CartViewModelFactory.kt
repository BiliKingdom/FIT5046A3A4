package com.example.fit5046a3a4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fit5046a3a4.data.CartItemDao

class CartViewModelFactory(private val dao: CartItemDao)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(dao) as T
    }
}
