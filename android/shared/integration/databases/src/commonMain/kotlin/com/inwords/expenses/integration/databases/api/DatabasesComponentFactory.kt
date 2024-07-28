package com.inwords.expenses.integration.databases.api

expect class DatabasesComponentFactory {

    interface Deps

    fun create(): DatabasesComponent
}