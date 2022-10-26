package com.bokoup.merchantapp.data

import com.apollographql.apollo3.api.ApolloResponse
import com.bokoup.lib.Resource
import com.bokoup.merchantapp.DelegateTokenSubscription
import com.bokoup.merchantapp.PromoListSubscription
import com.bokoup.merchantapp.TokenAccountListQuery
import com.bokoup.merchantapp.model.AppId
import com.bokoup.merchantapp.model.CreatePromoArgs
import com.bokoup.merchantapp.model.CreatePromoResult
import com.bokoup.merchantapp.model.PromoWithMetadataJson
import kotlinx.coroutines.flow.Flow

// gq https://shining-sailfish-15.hasura.app/v1/graphql --introspect > schema.graphql

interface DataRepo {

 val promoSubscriptionFlow: Flow<ApolloResponse<PromoListSubscription.Data>>
 val delegateTokenSubscription: Flow<ApolloResponse<DelegateTokenSubscription.Data>>
 fun fetchPromos(): Flow<Resource<List<PromoWithMetadataJson>>>

 fun fetchTokenAccounts(owner: String): Flow<Resource<List<TokenAccountListQuery.TokenAccount>>>
 fun fetchAppId(): Flow<Resource<AppId>>
 fun createPromo(createPromoArgs: CreatePromoArgs): Flow<Resource<CreatePromoResult>>
}