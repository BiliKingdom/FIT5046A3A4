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
            campusDao.clearAll()

            restaurantDao.clearAll()
            foodDao.clearCategories()
            foodDao.clearItems()

            campusDao.insertCampus(
                CampusEntity(
                    name = "Clayton",
                    latitude = -37.911,
                    longitude = 145.134
                )
            )
            campusDao.insertCampus(
                CampusEntity(
                    name = "Caulfield",
                    latitude = -37.877,
                    longitude = 145.043
                )
            )

            val centralCafeId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Campus Central Café",
                    address = "Building 10, Clayton Campus",
                    latitude = -37.9113,
                    longitude = 145.1342,
                    campusName = "Clayton"
                )
            )
            val ltbCafeId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "LTB Café",
                    address = "LTB Ground Floor, Clayton",
                    latitude = -37.9119,
                    longitude = 145.135,
                    campusName = "Clayton"
                )
            )

            val GEBId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "The Grind Espresso Bar",
                    address = "Building 12, West Wing, Clayton",
                    latitude = -37.9108,
                    longitude = 145.1325,
                    campusName = "Clayton"
                )
            )

            val holtCafeId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Holt Café",
                    address = "Holt Building, Level 1, Clayton Campus",
                    latitude = -37.9116,
                    longitude = 145.1338,
                    campusName = "Clayton"
                )
            )

            val PapperId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "PappaRich Monash",
                    address = "Shop G15/21 Chancellors Walk, Clayton Campus",
                    latitude = -37.9111,
                    longitude = 145.1337,
                    campusName = "Clayton"
                )
            )



            val CkitchenId = restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Caulfield Kitchen",
                    address = "Level 2, Building H, Caulfield",
                    latitude = -37.8765,
                    longitude = 145.0450,
                    campusName = "Caulfield"
                )
            )

            val categoryIds = listOf("Main", "Drink", "Side", "Dessert").map { name ->
                name to foodDao.insertCategory(FoodCategoryEntity(name = name))
            }.toMap()


            // centralCafe
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Grilled Chicken Bowl",
                    description = "Tasty grilled chicken with rice",
                    price = 11.99,
                    imageRes = R.drawable.grilledbowl,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = centralCafeId
                )
            )
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Mexico taco",
                    description = "Stir-fried vegetables and tofu",
                    price = 9.49,
                    imageRes = R.drawable.taco,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = centralCafeId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "spaghetti",
                    description = "began to feed in the spaghetti, carefully separating the strands",
                    price = 15.50,
                    imageRes = R.drawable.spaghetti,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = centralCafeId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "Fresh Juice",
                    description = "Orange or apple juice",
                    price = 5.00,
                    imageRes = R.drawable.juice,
                    categoryId = categoryIds["Drink"]!!,
                    restaurantId = centralCafeId
                )
            )
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Coke",
                    description = "The best coke in the world",
                    price = 3.99,
                    imageRes = R.drawable.coke,
                    categoryId = categoryIds["Drink"]!!,
                    restaurantId = centralCafeId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "Fries",
                    description = "Crispy golden french fries.",
                    price = 3.50,
                    imageRes = R.drawable.chips,
                    categoryId = categoryIds["Side"]!!,
                    restaurantId = centralCafeId
                )
            )
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Fried Chicken",
                    description = "Crispy chicken with golden oil",
                    price = 3.50,
                    imageRes = R.drawable.fc,
                    categoryId = categoryIds["Side"]!!,
                    restaurantId = centralCafeId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "Chocolate Cake",
                    description = "Rich and moist chocolate layered cake.",
                    price = 5.99,
                    imageRes = R.drawable.chocolate,
                    categoryId = categoryIds["Dessert"]!!,
                    restaurantId = centralCafeId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "Vanilla Ice Cream",
                    description = "Creamy vanilla-flavored ice cream.",
                    price = 4.50,
                    imageRes = R.drawable.icecream,
                    categoryId = categoryIds["Dessert"]!!,
                    restaurantId = centralCafeId
                )
            )


            // LTB Café
            // Main
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Hamburger",
                    description = "Classic grilled beef burger with lettuce and cheese.",
                    price = 12.99,
                    imageRes = R.drawable.burrito,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = ltbCafeId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "SuperRice",
                    description = "Stir-fried rice with vegetables and chicken.",
                    price = 9.99,
                    imageRes = R.drawable.taco,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = ltbCafeId
                )
            )

            // Side
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Chips",
                    description = "Golden crispy chips served with sauce.",
                    price = 3.50,
                    imageRes = R.drawable.chips,
                    categoryId = categoryIds["Side"]!!,
                    restaurantId = ltbCafeId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "Soup",
                    description = "Delicious daily soup made fresh.",
                    price = 4.00,
                    imageRes = R.drawable.soup,
                    categoryId = categoryIds["Side"]!!,
                    restaurantId = ltbCafeId
                )
            )

            // Drink
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Coke",
                    description = "Refreshing chilled Coca-Cola.",
                    price = 2.50,
                    imageRes = R.drawable.coke,
                    categoryId = categoryIds["Drink"]!!,
                    restaurantId = ltbCafeId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "Coffee",
                    description = "Freshly brewed coffee served hot.",
                    price = 2.80,
                    imageRes = R.drawable.coffee,
                    categoryId = categoryIds["Drink"]!!,
                    restaurantId = ltbCafeId
                )
            )

            // Dessert
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Cake",
                    description = "Soft and sweet sponge cake with icing.",
                    price = 5.99,
                    imageRes = R.drawable.churros,
                    categoryId = categoryIds["Dessert"]!!,
                    restaurantId = ltbCafeId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "Ice Cream",
                    description = "Creamy vanilla ice cream served cold.",
                    price = 4.50,
                    imageRes = R.drawable.icecream,
                    categoryId = categoryIds["Dessert"]!!,
                    restaurantId = ltbCafeId
                )
            )




            // Grind Espresso Bar
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Polenta CHips",
                    description = "Crispy polenta chips with gorgonzola dip",
                    price = 12.0,
                    imageRes = R.drawable.icecream,
                    categoryId = categoryIds["Side"]!!,
                    restaurantId = GEBId
                )
            )
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Chicken",
                    description = "Roast spatchcock chicken with vegans",
                    price = 42.0,
                    imageRes = R.drawable.icecream,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = GEBId
                )
            )
            //Caulfield Kitchen
            // Main
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Hamburger",
                    description = "Classic grilled beef burger with lettuce and cheese.",
                    price = 12.99,
                    imageRes = R.drawable.burrito,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = CkitchenId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "SuperRice",
                    description = "Stir-fried rice with vegetables and chicken.",
                    price = 9.99,
                    imageRes = R.drawable.taco,
                    categoryId = categoryIds["Main"]!!,
                    restaurantId = CkitchenId
                )
            )

            // Side
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Chips",
                    description = "Golden crispy chips served with sauce.",
                    price = 3.50,
                    imageRes = R.drawable.chips,
                    categoryId = categoryIds["Side"]!!,
                    restaurantId = CkitchenId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "Soup",
                    description = "Delicious daily soup made fresh.",
                    price = 4.00,
                    imageRes = R.drawable.soup,
                    categoryId = categoryIds["Side"]!!,
                    restaurantId = CkitchenId
                )
            )

            // Drink
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Coke",
                    description = "Refreshing chilled Coca-Cola.",
                    price = 2.50,
                    imageRes = R.drawable.coke,
                    categoryId = categoryIds["Drink"]!!,
                    restaurantId = CkitchenId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "Coffee",
                    description = "Freshly brewed coffee served hot.",
                    price = 2.80,
                    imageRes = R.drawable.coffee,
                    categoryId = categoryIds["Drink"]!!,
                    restaurantId = CkitchenId
                )
            )

            // Dessert
            foodDao.insertItem(
                FoodItemEntity(
                    name = "Cake",
                    description = "Soft and sweet sponge cake with icing.",
                    price = 5.99,
                    imageRes = R.drawable.churros,
                    categoryId = categoryIds["Dessert"]!!,
                    restaurantId = CkitchenId
                )
            )

            foodDao.insertItem(
                FoodItemEntity(
                    name = "Ice Cream",
                    description = "Creamy vanilla ice cream served cold.",
                    price = 4.50,
                    imageRes = R.drawable.icecream,
                    categoryId = categoryIds["Dessert"]!!,
                    restaurantId = CkitchenId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(7,"The Common Ground", "Building B, Caulfield", -37.8772, 145.0428, "Caulfield")
            )

        }
    }
}
