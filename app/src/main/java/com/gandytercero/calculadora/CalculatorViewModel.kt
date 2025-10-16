package com.gandytercero.calculadora

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    // --- Estado que observa la UI ---
    var state by mutableStateOf(CalculatorState())
        private set

    // Clase de estado anidada
    data class CalculatorState(
        val display: String = "0"
    )

    // Eventos anidados
    sealed class CalculatorEvent {
        data class Number(val number: String) : CalculatorEvent()
        data class Operator(val operator: String) : CalculatorEvent()
        object Clear : CalculatorEvent()
        object AllClear : CalculatorEvent()
        object Calculate : CalculatorEvent()
        object Decimal : CalculatorEvent()
    }

    // --- Estado Interno del ViewModel (la lógica) ---
    private var number1: String = ""
    private var number2: String = ""
    private var operator: String? = null

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.Number -> enterNumber(event.number)
            is CalculatorEvent.Operator -> enterOperator(event.operator)
            is CalculatorEvent.Decimal -> enterDecimal()
            is CalculatorEvent.AllClear -> clearAll()
            is CalculatorEvent.Clear -> clearLast()
            is CalculatorEvent.Calculate -> performCalculation(fromEquals = true)
        }
    }

    private fun enterNumber(number: String) {
        val currentNumber = if (operator == null) number1 else number2
        // Evitar múltiples ceros al inicio o números demasiado largos
        if (currentNumber.length >= 10) return

        if (operator == null) {
            number1 += number
            state = state.copy(display = number1)
        } else {
            number2 += number
            state = state.copy(display = number2)
        }
    }

    private fun enterOperator(op: String) {
        // Si ya tenemos una operación completa (ej: 5 + 5), la calculamos primero
        if (number1.isNotBlank() && number2.isNotBlank()) {
            performCalculation()
        }
        // Establecemos el nuevo operador
        if (number1.isNotBlank()) {
            operator = op
        }
    }

    private fun enterDecimal() {
        val currentNumber = if (operator == null) number1 else number2
        if (!currentNumber.contains(".") && currentNumber.isNotBlank()) {
            if (operator == null) {
                number1 += "."
                state = state.copy(display = number1)
            } else {
                number2 += "."
                state = state.copy(display = number2)
            }
        } else if (currentNumber.isBlank()) {
             if (operator == null) {
                number1 = "0."
                state = state.copy(display = number1)
            } else {
                number2 = "0."
                state = state.copy(display = number2)
            }
        }
    }

    private fun performCalculation(fromEquals: Boolean = false) {
        val num1 = number1.toDoubleOrNull()
        val num2 = number2.toDoubleOrNull()

        if (num1 != null && num2 != null && operator != null) {
            val result = when (operator) {
                "+" -> num1 + num2
                "−" -> num1 - num2
                "×" -> num1 * num2
                "÷" -> if (num2 != 0.0) num1 / num2 else Double.NaN // Manejar división por cero
                else -> 0.0
            }

            val resultString = if (result.isNaN()) "Error" else formatResult(result)
            
            // El resultado se convierte en el nuevo number1 para la siguiente operación
            number1 = if (result.isNaN()) "" else resultString
            number2 = ""
            // Si la cálculo vino de un '=', limpiamos el operador para detener la secuencia
            if (fromEquals) {
                operator = null
            }
            state = state.copy(display = resultString)
        }
    }

    private fun clearLast() {
        if (operator == null) {
            if (number1.isNotBlank()) {
                number1 = number1.dropLast(1)
                state = state.copy(display = if (number1.isBlank()) "0" else number1)
            }
        } else {
            if (number2.isNotBlank()) {
                number2 = number2.dropLast(1)
                state = state.copy(display = if (number2.isBlank()) "0" else number2)
            } else {
                operator = null
                state = state.copy(display = number1)
            }
        }
    }

    private fun clearAll() {
        number1 = ""
        number2 = ""
        operator = null
        state = state.copy(display = "0")
    }

    private fun formatResult(number: Double): String {
        return if (number % 1 == 0.0) {
            number.toLong().toString()
        } else {
            number.toString()
        }
    }
}