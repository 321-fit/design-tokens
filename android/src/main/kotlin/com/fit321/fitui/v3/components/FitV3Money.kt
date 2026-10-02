package com.fit321.fitui.v3.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fit321.fitui.v3.theme.LocalFitV3Palette
import com.fit321.fitui.v3.tokens.FitV3Type

data class FitMoneyFact(val value: String, val label: String)

@Composable
fun FitMoneyWidget(
    amount: String,
    modifier: Modifier = Modifier,
    badge: (@Composable () -> Unit)? = null,
    context: String? = null,
    due: Boolean = false,
    leading: (@Composable () -> Unit)? = null,
    facts: List<FitMoneyFact> = emptyList(),
    onClick: (() -> Unit)? = null,
) {
    val palette = LocalFitV3Palette.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(20.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            if (leading != null) {
                Box(modifier = Modifier.padding(top = 8.dp)) { leading() }
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = amount,
                        style = FitV3Type.moneyHeadline,
                        color = if (due) palette.textError else palette.textPrimary,
                    )
                    badge?.invoke()
                }
                if (context != null) {
                    Text(
                        text = context,
                        style = FitV3Type.rowSub,
                        color = palette.textTertiary,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            }
        }
        if (facts.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 12.dp)
                    .height(1.dp)
                    .background(palette.divider),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                facts.forEach { fact ->
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = fact.value,
                            style = FitV3Type.nextWhen,
                            color = palette.textPrimary,
                        )
                        Text(
                            text = fact.label,
                            style = FitV3Type.rowSub,
                            color = palette.textTertiary,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FitMoneyHero(
    amount: String,
    modifier: Modifier = Modifier,
    currency: String? = null,
    label: String? = null,
    sub: String? = null,
) {
    val palette = LocalFitV3Palette.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (label != null) {
            Text(
                text = label,
                style = FitV3Type.rowSub,
                color = palette.textTertiary,
                textAlign = TextAlign.Center,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = 6.dp),
        ) {
            if (currency != null) {
                Text(
                    text = currency,
                    style = FitV3Type.moneyHeroCurrency,
                    color = palette.textSecondary,
                )
            }
            Text(
                text = amount,
                style = FitV3Type.moneyHero,
                color = palette.textPrimary,
            )
        }
        if (sub != null) {
            Text(
                text = sub,
                style = FitV3Type.rowSub,
                color = palette.textTertiary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}
