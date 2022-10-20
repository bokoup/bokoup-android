package com.bokoup.merchantapp.data

import com.bokoup.lib.Resource
import com.bokoup.merchantapp.PromoListQuery
import com.bokoup.merchantapp.model.AppId
import com.bokoup.merchantapp.model.CreatePromoArgs
import com.bokoup.merchantapp.model.CreatePromoResult
import kotlinx.coroutines.flow.Flow

interface DataRepo {
 fun fetchPromos(): Flow<Resource<List<PromoListQuery.Promo>>>
 fun fetchAppId(): Flow<Resource<AppId>>
 fun createPromo(createPromoArgs: CreatePromoArgs): Flow<Resource<CreatePromoResult>>
}