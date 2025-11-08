package ru.commonex.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ReportDrawn
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.inwords.expenses.core.ui.design.theme.AndroidExpensesTheme
import com.inwords.expenses.integration.base.MainNavHost


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidExpensesTheme {
                MainNavHost(modifier = Modifier.fillMaxSize())

                ReportDrawn()
            }
        }
    }
}
