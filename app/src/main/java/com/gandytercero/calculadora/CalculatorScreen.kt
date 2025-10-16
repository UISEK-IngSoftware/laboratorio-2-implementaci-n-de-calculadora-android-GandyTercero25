package com.gandytercero.calculadora

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel()
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = state.display,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.End,
            fontSize = 72.sp,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )

        CalculatorGrid(onEvent = viewModel::onEvent)
    }
}

@Composable
fun CalculatorGrid(onEvent: (CalculatorViewModel.CalculatorEvent) -> Unit) {
    val buttons = listOf(
        "AC", "C", "÷",
        "7", "8", "9", "×",
        "4", "5", "6", "−",
        "1", "2", "3", "+",
        "0", ".", "="
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = buttons,
            span = { label ->
                // El botón "AC" y "0" ocupan 2 espacios
                GridItemSpan(if (label == "AC" || label == "0") 2 else 1)
            }
        ) { label ->
            // El aspect ratio debe coincidir con el span
            val modifier = Modifier.aspectRatio(if (label == "AC" || label == "0") 2f / 1f else 1f)
            
            CalculatorButton(
                label = label,
                modifier = modifier,
                onClick = {
                    when (label) {
                        "AC" -> onEvent(CalculatorViewModel.CalculatorEvent.AllClear)
                        "C" -> onEvent(CalculatorViewModel.CalculatorEvent.Clear)
                        in "0".."9" -> onEvent(CalculatorViewModel.CalculatorEvent.Number(label))
                        "." -> onEvent(CalculatorViewModel.CalculatorEvent.Decimal)
                        "=" -> onEvent(CalculatorViewModel.CalculatorEvent.Calculate)
                        "÷", "×", "−", "+" -> onEvent(CalculatorViewModel.CalculatorEvent.Operator(label))
                    }
                }
            )
        }
    }
}


@Composable
fun CalculatorButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val (backgroundColor, textColor) = when (label) {
        in "0".."9", "." -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
        "÷", "×", "−", "+", "=" -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        "AC", "C" -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
        else -> Color.Transparent to Color.Transparent // Fallback, no debería ocurrir
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = if (label in listOf("AC", "C")) 28.sp else 36.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
