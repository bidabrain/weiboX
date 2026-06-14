package com.weibox.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weibox.app.data.db.dao.PostDao
import com.weibox.app.data.db.dao.UserDao
import com.weibox.app.data.db.entity.PostEntity
import com.weibox.app.data.db.entity.StringListConverter
import com.weibox.app.data.db.entity.UserEntity

@Database(entities = [UserEntity::class, PostEntity::class], version = 2, exportSchema = false)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
}
