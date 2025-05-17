package com.example.fit5046a3a4.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CampusSeeder {

    fun seed(context: Context) {
        val db = AppDatabase.get(context)
        val campusDao = db.campusDao()
        val restaurantDao = db.restaurantDao()

        CoroutineScope(Dispatchers.IO).launch {
            campusDao.clearAll()
            restaurantDao.clearAll()

            val claytonId = campusDao.insertCampus(
                CampusEntity(name = "Clayton", latitude = -37.911, longitude = 145.134)
            )
            val caulfieldId = campusDao.insertCampus(
                CampusEntity(name = "Caulfield", latitude = -37.877, longitude = 145.043)
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Campus Central Café",
                    address = "Building 10, Clayton Campus",
                    latitude = -37.9113,
                    longitude = 145.1342,
                    campusId = claytonId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "LTB Café",
                    address = "LTB Ground Floor, Clayton",
                    latitude = -37.9119,
                    longitude = 145.135,
                    campusId = claytonId
                )
            )
            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "The Grind Espresso Bar",
                    address = "Building 12, West Wing, Clayton",
                    latitude = -37.9108,
                    longitude = 145.1325,
                    campusId = claytonId
                )
            )
            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Holt Café",
                    address = "Holt Building, Level 1, Clayton Campus",
                    latitude = -37.9116,
                    longitude = 145.1338,
                    campusId = claytonId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "PappaRich Monash",
                    address = "Shop G15/21 Chancellors Walk, Clayton Campus",
                    latitude = -37.91152,
                    longitude = 145.13399,
                    campusId = claytonId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Wholefoods",
                    address = "Level1/21Chancellors Walk,, Clayton Campus",
                    latitude = -37.91177,
                    longitude = 145.13290,
                    campusId = claytonId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "PAPA'S Korean Chicken Restaurant",
                    address = "199 Clayton Rd, Clayton Campus",
                    latitude = -37.91470,
                    longitude = 145.12229,
                    campusId = claytonId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Caulfield Kitchen",
                    address = "Level 2, Building H, Caulfield",
                    latitude = -37.8765,
                    longitude = 145.0450,
                    campusId = caulfieldId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "The Common Ground",
                    address = "Building B, Caulfield",
                    latitude = -37.8772,
                    longitude = 145.0428,
                    campusId = caulfieldId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Korean Express",
                    address = "Building K, Caulfield",
                    latitude = -37.8770,
                    longitude = 145.0419,
                    campusId = caulfieldId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Bento & Sushi",
                    address = "Level 1, Building A, Caulfield",
                    latitude = -37.8768,
                    longitude = 145.0435,
                    campusId = caulfieldId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Ziweiyuan",
                    address = "903 Princes Hwy Service Rd, Caulfield East",
                    latitude = -37.87584,
                    longitude = 145.04643,
                    campusId = caulfieldId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "Seng Hing Gourmet",
                    address = "1 Derby Rd, Caulfield East",
                    latitude = -37.87611,
                    longitude = 145.04159,
                    campusId = caulfieldId
                )
            )

            restaurantDao.insertRestaurant(
                RestaurantEntity(
                    name = "The Glasshouse Caulfield",
                    address = "31 Station St, Caulfield East",
                    latitude = -37.87701,
                    longitude = 145.04007,
                    campusId = caulfieldId
                )
            )
        }
    }
}
