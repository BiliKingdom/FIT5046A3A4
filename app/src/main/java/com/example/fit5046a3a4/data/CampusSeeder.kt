package com.example.fit5046a3a4.data

import android.content.Context
import com.example.fit5046a3a4.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CampusSeeder {

    fun seed(context: Context) {
        val db = AppDatabase.get(context)
        val campusDao = db.campusDao()
        val restaurantDao = db.restaurantDao()
        val foodDao = db.foodDao()

        CoroutineScope(Dispatchers.IO).launch {
            // 1. 清空旧数据
            campusDao.clearAll()
            restaurantDao.clearAll()
            foodDao.clearCategories()
            foodDao.clearItems()

            // 2. 插入校区
            val claytonId = campusDao.insertCampus(
                CampusEntity(name = "Clayton", latitude = -37.911, longitude = 145.134)
            )
            val caulfieldId = campusDao.insertCampus(
                CampusEntity(name = "Caulfield", latitude = -37.877, longitude = 145.043)
            )

            // 3. 插入餐厅
            val centralCafeId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Campus Central Café",
                    address = "Building 10, Clayton Campus",
                    latitude = -37.9113,
                    longitude = 145.1342,
                    campusId = claytonId
                )
            )
            val ltbCafeId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "LTB Café",
                    address = "LTB Ground Floor, Clayton",
                    latitude = -37.9119,
                    longitude = 145.135,
                    campusId = claytonId
                )
            )

            val GEBId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "The Grind Espresso Bar",
                    address = "Building 12, West Wing, Clayton",
                    latitude = -37.9108,
                    longitude = 145.1325,
                    campusId = claytonId
                )
            )

            val holtCafeId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Holt Café",
                    address = "Holt Building, Level 1, Clayton Campus",
                    latitude = -37.9116,
                    longitude = 145.1338,
                    campusId = claytonId
                )
            )

            val CkitchenId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Caulfield Kitchen",
                    address = "Level 2, Building H, Caulfield",
                    latitude = -37.8765,
                    longitude = 145.0450,
                    campusId = caulfieldId
                )
            )
            // 4. 全局插入四大分类，并保存 categoryId
            val categoryIds = listOf("Main", "Drink", "Side", "Dessert").map { name ->
                name to foodDao.insertCategory(FoodCategoryEntity(name = name))
            }.toMap()

            // 5. 按餐厅插入菜品：Campus Central Café
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Grilled Chicken Bowl",
                    description = "Tasty grilled chicken with rice",
                    price = 11.99,
                    imageRes = R.drawable.burrito,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = centralCafeId
                )
            )
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Veggie Delight",
                    description = "Stir-fried vegetables and tofu",
                    price = 9.49,
                    imageRes = R.drawable.taco,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = centralCafeId
                )
            )
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Fresh Juice",
                    description = "Orange or apple juice",
                    price = 3.99,
                    imageRes = R.drawable.coke,
                    categoryId = categoryIds["Drink"]!!,
                    restaurantId = centralCafeId
                )
            )

            // 6. 按餐厅插入菜品：LTB Café
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Garlic Bread",
                    description = "Crunchy and buttery garlic bread",
                    price = 4.99,
                    imageRes = R.drawable.chips,
                    categoryId = categoryIds["Side"]!!,
                    restaurantId = ltbCafeId
                )
            )
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Tomato Soup",
                    description = "Warm and creamy tomato soup",
                    price = 5.49,
                    imageRes = R.drawable.soup,
                    categoryId = categoryIds["Side"]!!,
                    restaurantId = ltbCafeId
                )
            )
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Chocolate Muffin",
                    description = "Rich and moist muffin",
                    price = 3.29,
                    imageRes = R.drawable.icecream,
                    categoryId = categoryIds["Dessert"]!!,
                    restaurantId = ltbCafeId
                )
            )

            // 7. 按餐厅插入菜品：Grind Espresso Bar
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Polenta CHips",
                    description = "Crispy polenta chips with gorgonzola dip",
                    price = 12.0,
                    imageRes = -1,
                    categoryId = categoryIds["Side"]!!,
                    restaurantId = GEBId
                )
            )
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Chicken",
                    description = "Roast spatchcock chicken with vegans",
                    price = 42.0,
                    imageRes = -1,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = GEBId
                )
            )
            // 8. 按餐厅插入菜品：Caulfield Kitchen
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Beef Rice",
                    description = "Sichuan Beef with vegans and rice",
                    price = 18.8,
                    imageRes = -1,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = CkitchenId
                )
            )

            // ======= 其他餐厅（不含菜单） =======
            restaurantDao.insertRestaurant(
                RestaurantEntity(3,"PappaRich Monash", "Shop G15/21 Chancellors Walk, Clayton Campus", -37.91152, 145.13399, claytonId)
            )
            restaurantDao.insertRestaurant(
                RestaurantEntity(4,"Wholefoods", "Level1/21Chancellors Walk,, Clayton Campus", -37.91177, 145.13290, claytonId)
            )
            restaurantDao.insertRestaurant(
                RestaurantEntity(5,"PAPA'S Korean Chicken Restaurant", "199 Clayton Rd, Clayton Campus", -37.91470, 145.12229, claytonId)
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(7,"The Common Ground", "Building B, Caulfield", -37.8772, 145.0428, caulfieldId)
            )
            restaurantDao.insertRestaurant(
                RestaurantEntity(8,"Korean Express", "Building K, Caulfield", -37.8770, 145.0419, caulfieldId)
            )
            restaurantDao.insertRestaurant(
                RestaurantEntity(9,"Bento & Sushi", "Level 1, Building A, Caulfield", -37.8768, 145.0435, caulfieldId)
            )
            restaurantDao.insertRestaurant(
                RestaurantEntity(10,"Ziweiyuan", "903 Princes Hwy Service Rd, Caulfield East", -37.87584, 145.04643, caulfieldId)
            )
            restaurantDao.insertRestaurant(
                RestaurantEntity(11,"Seng Hing Gourmet", "1 Derby Rd, Caulfield East", -37.87611, 145.04159, caulfieldId)
            )
            restaurantDao.insertRestaurant(
                RestaurantEntity(12,"The Glasshouse Caulfield", "31 Station St, Caulfield East", -37.87701, 145.04007, caulfieldId)
            )
        }
    }
}
