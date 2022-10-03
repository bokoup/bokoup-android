package com.bokoup.merchantapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TenderRow::class], version = 1, exportSchema = false
)
abstract class CloverDb : RoomDatabase() {
    abstract fun tenderDao(): TenderDao
}
