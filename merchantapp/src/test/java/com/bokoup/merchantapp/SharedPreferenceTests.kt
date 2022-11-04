package com.bokoup.merchantapp

import com.bokoup.merchantapp.model.SharedPrefKeys
import com.bokoup.merchantapp.model.key
import org.junit.Test

class SharedPreferenceTests {
    @Test
    fun shared_preference_labels_work() {
        assert(SharedPrefKeys.GroupSeed.key == "groupSeed")
    }
}