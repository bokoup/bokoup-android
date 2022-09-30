package com.bokoup.customerapp.data.net

import androidx.room.*
import com.bokoup.customerapp.dom.model.Address
import com.bokoup.customerapp.dom.model.Resource
import com.bokoup.customerapp.dom.model.Token
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM address ORDER BY id ASC")
    fun getAddresses(): List<Address>

    @Query("SELECT * FROM address WHERE id = :id")
    fun getAddress(id: String): Address

    @Query("SELECT * FROM address WHERE active")
    fun getActiveAddress(): Address

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: Address)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddresses(tokens: List<Address>)

    @Query("UPDATE address SET active = NULL WHERE active")
    fun clearActive()

    @Query("UPDATE address SET active = TRUE WHERE id = :id")
    fun setActive(id: String)



}

