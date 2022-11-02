package com.bokoup.merchantapp.domain

import com.apollographql.apollo3.api.ApolloResponse
import com.bokoup.lib.Resource
import com.bokoup.merchantapp.DelegateTokenSubscription
import com.bokoup.merchantapp.PromoListSubscription
import com.bokoup.merchantapp.model.*
import kotlinx.coroutines.flow.Flow

// gq https://shining-sailfish-15.hasura.app/v1/graphql --introspect > schema.graphql

interface DataRepo {

 val promoSubscriptionFlow: Flow<ApolloResponse<PromoListSubscription.Data>>
 val delegateTokenSubscription: Flow<ApolloResponse<DelegateTokenSubscription.Data>>
 fun fetchPromos(): Flow<Resource<List<PromoWithMetadata>>>
 fun fetchEligibleTokenAccounts(tokenOwner: String, promoOwner: String, orderId: String): Flow<Resource<List<TokenAccountWithMetadata>>>
 fun fetchAppId(): Flow<Resource<AppId>>
 fun createPromo(promo: BasePromo, payer: String, groupSeed: String): Flow<Resource<CreatePromoResult>>
}