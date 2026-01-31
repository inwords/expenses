package ru.commonex.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ReportDrawn
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.inwords.expenses.core.navigation.DeeplinkProvider
import com.inwords.expenses.core.ui.design.theme.AndroidExpensesTheme
import com.inwords.expenses.integration.base.MainNavHost


class MainActivity : ComponentActivity() {

    private val deeplinkProvider = DeeplinkProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deeplinkProvider.supplyIntent(intent)

        enableEdgeToEdge()
        setContent {
            AndroidExpensesTheme {
                MainNavHost(modifier = Modifier.fillMaxSize(), deeplinkProvider = deeplinkProvider)

                ReportDrawn()
            }
        }
    }

    @VisibleForTesting
    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        deeplinkProvider.supplyIntent(intent)
    }
}
