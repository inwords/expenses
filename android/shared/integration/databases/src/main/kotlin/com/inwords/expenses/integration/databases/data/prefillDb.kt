package com.inwords.expenses.integration.databases.data

import android.content.Context
import com.inwords.expenses.integration.databases.api.DatabasesComponent

// FIXME costyl
lateinit var appContext: Context

val dbComponent = DatabasesComponent(object : DatabasesComponent.Deps {
    override val context get() = appContext
})
