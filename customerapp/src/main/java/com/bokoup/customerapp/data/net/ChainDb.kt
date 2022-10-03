package com.bokoup.customerapp.data.net

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.model.Token

@Database(
    entities = [Token::class, Address::class], version = 1, exportSchema = false
)
abstract class ChainDb : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
    abstract fun addressDao(): AddressDao
}
