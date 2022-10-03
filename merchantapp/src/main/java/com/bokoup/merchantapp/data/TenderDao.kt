package com.bokoup.merchantapp.data

import androidx.room.*

/**
 * The Data Access Object for the Tender class.
 */
@Dao
interface TenderDao {
    @Query("SELECT * from tenders ORDER BY label")
    fun getTenders(): List<TenderRow>

    @Query("DELETE from tenders")
    fun dropTenders()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tenders: List<TenderRow>)

}

@Entity(tableName = "tenders")
data class TenderRow(
    @PrimaryKey @ColumnInfo(name = "id") val tenderId: String,
    val contents: Int,
    val enabled: Boolean,
    val label: String,
    val labelKey: String,
    val opensCashDrawer: Boolean,
    val supportsTipping: Boolean,
) {
    override fun toString() = "$tenderId, $label, $contents, $labelKey, $enabled"
}