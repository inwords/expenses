package com.inwords.expenses

import android.content.Context
import com.inwords.expenses.core.network.NetworkComponentFactory
import com.inwords.expenses.core.utils.IO
import com.inwords.expenses.feature.events.data.network.Test
import com.inwords.expenses.feature.events.domain.model.Person
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object NetworkTest {

    fun sendTest(context: Context) {
        GlobalScope.launch(IO) {
            val client = NetworkComponentFactory(context).create().getHttpClient()
//            val a = client.get("https://quic.nginx.org/test")
//            a.readBytes()
//            delay(100)
//            val b = client.get("https://quic.nginx.org/test")
//            b.readBytes()
//            delay(100)
//            val c = client.get("https://www.google.com/favicon.ico")
//            c.readBytes()
//            delay(100)
//            val d = client.get("https://www.google.com/favicon.ico")
//            d.readBytes()

            val a = Test(client)
            a.getEvent()
            a.createEvent("test", 1, listOf(Person(0L, "Vasilii"), Person(0L, "Artem")), "1234")
        }
    }

}