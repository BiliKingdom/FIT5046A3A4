package com.example.fit5046a3a4.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.fit5046a3a4.data.AppDatabase
import com.example.fit5046a3a4.data.CartItemEntity
import com.example.fit5046a3a4.data.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = CartRepository(AppDatabase.get(app).cartDao())

    val cartItems = repo.items           // Flow<List<CartItemEntity>>

    fun add(item: CartItemEntity) = viewModelScope.launch { repo.add(item) }
    fun remove(item: CartItemEntity) = viewModelScope.launch { repo.remove(item) }
    fun clear() = viewModelScope.launch { repo.clear() }
}
