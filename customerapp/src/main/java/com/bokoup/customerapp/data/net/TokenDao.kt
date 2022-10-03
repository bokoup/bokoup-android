package com.bokoup.customerapp.data.net

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bokoup.customerapp.dom.model.Token
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenDao {
    @Query("SELECT * FROM token ORDER BY id ASC")
    fun getTokens(): Flow<List<Token>>

    @Query("SELECT * FROM token WHERE id = :id")
    fun getToken(id: String): Flow<Token>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToken(token: Token)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTokens(tokens: List<Token>)

}
