package com.bokoup.merchantapp.domain

import android.net.Uri
import com.apollographql.apollo3.api.ApolloResponse
import com.bokoup.lib.Resource
import com.bokoup.merchantapp.DelegateTokenSubscription
import com.bokoup.merchantapp.PromoListSubscription
import com.bokoup.merchantapp.model.*
import com.dgsd.ksol.core.model.KeyPair
import kotlinx.coroutines.flow.Flow

// gq https://shining-sailfish-15.hasura.app/v1/graphql --introspect > schema.graphql

interface PromoRepo {

 val promoSubscriptionFlow: Flow<ApolloResponse<PromoListSubscription.Data>>
 val delegateTokenSubscription: Flow<ApolloResponse<DelegateTokenSubscription.Data>>
 fun fetchPromos(): Flow<Resource<List<PromoWithMetadata>>>
 fun fetchEligibleTokenAccounts(tokenOwner: String, orderId: String): Flow<Resource<List<TokenAccountWithMetadata>>>
 fun fetchAppId(): Flow<Resource<AppId>>
 fun createPromo(promo: PromoType, uri: Uri, memo: String?, payer: String, groupSeed: String): Flow<Resource<TxApiResponse>>
 fun signAndSend(transaction: String, keyPair: KeyPair): Flow<Resource<String>>
}