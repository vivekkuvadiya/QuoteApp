package com.example.quoteapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quoteapp.data.model.QuoteRemoteKey

@Dao
interface RemoteKeyDao {

    @Query("SELECT * FROM quoteremotekey WHERE id = :id")
    suspend fun getRemoteKey(id:String):QuoteRemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKey(remoteKeys:List<QuoteRemoteKey>)

    @Query("DELETE FROM quoteremotekey")
    suspend fun deleteAllKey()

}