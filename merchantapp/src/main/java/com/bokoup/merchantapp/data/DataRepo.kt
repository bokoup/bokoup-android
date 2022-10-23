package com.bokoup.merchantapp.data

import com.apollographql.apollo3.api.ApolloResponse
import com.bokoup.lib.Resource
import com.bokoup.merchantapp.DelegateTokenSubscription
import com.bokoup.merchantapp.PromoListSubscription
import com.bokoup.merchantapp.model.AppId
import com.bokoup.merchantapp.model.CreatePromoArgs
import com.bokoup.merchantapp.model.CreatePromoResult
import com.bokoup.merchantapp.model.PromoWithMetadataJson
import kotlinx.coroutines.flow.Flow

// gq https://shining-sailfish-15.hasura.app/v1/graphql --introspect > schema.graphql

interface DataRepo {

 val promoSubscriptionFlow: Flow<ApolloResponse<PromoListSubscription.Data>>
 val delegateTokenSubscriptionFlow: Flow<ApolloResponse<DelegateTokenSubscription.Data>>
 fun fetchPromos(): Flow<Resource<List<PromoWithMetadataJson>>>
 fun fetchAppId(): Flow<Resource<AppId>>
 fun createPromo(createPromoArgs: CreatePromoArgs): Flow<Resource<CreatePromoResult>>
}