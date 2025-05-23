package com.example.fit5046a3a4.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private val MIGRATION_6_7 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
      CREATE TABLE IF NOT EXISTS `food_categories_new` (
        `id`   INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
        `name` TEXT    NOT NULL
      )
    """.trimIndent())

        db.execSQL("""
      INSERT OR IGNORE INTO `food_categories_new` (name)
      SELECT DISTINCT name FROM `food_categories`
    """.trimIndent())

        db.execSQL("DROP TABLE `food_categories`")
        db.execSQL("ALTER TABLE `food_categories_new` RENAME TO `food_categories`")
    }
}




@Database(
    entities = [
        CartItemEntity::class,
        UserEntity::class,
        CampusEntity::class,
        RestaurantEntity::class,
        FoodCategoryEntity::class,
        FoodItemEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartItemDao
    abstract fun userDao(): UserDao


    abstract fun campusDao(): CampusDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun foodDao(): FoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
                .addMigrations(MIGRATION_6_7)
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = get(context).foodDao()
                            listOf("Main", "Drink", "Side", "Dessert").forEach { name ->
                                dao.insertCategory(FoodCategoryEntity(name = name))
                            }
                        }
                    }
                })
            .build()
        }
    }

