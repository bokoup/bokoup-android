package com.bokoup.merchantapp.data

import com.bokoup.lib.Resource
import com.bokoup.merchantapp.PromoListQuery
import com.bokoup.merchantapp.model.AppId
import kotlinx.coroutines.flow.Flow

interface DataRepo {
 fun fetchPromos(): Flow<Resource<List<PromoListQuery.Promo>>>
 fun fetchAppId(): Flow<Resource<AppId>>
}