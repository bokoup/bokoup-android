package com.bokoup.customerapp.data.net

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.model.Token
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM address ORDER BY id ASC")
    fun getAddresses(): Flow<List<Address>>

    @Query("SELECT * FROM address WHERE id = :id")
    fun getAddress(id: String): Flow<Address>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: Address)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddresses(tokens: List<Address>)

}

