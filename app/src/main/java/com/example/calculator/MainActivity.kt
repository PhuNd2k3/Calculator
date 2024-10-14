package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private var currentInput = ""
    private var lastResult = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        val buttons = listOf(
            R.id.zero_button, R.id.one, R.id.two, R.id.three, R.id.four,
            R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
            R.id.plus, R.id.minus, R.id.multiply, R.id.divide,
            R.id.equals, R.id.ce, R.id.c, R.id.bs, R.id.dot, R.id.plus_minus
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onButtonClick(it as Button) }
        }
    }

    private fun onButtonClick(button: Button) {
        when (button.id) {
            R.id.zero_button, R.id.one, R.id.two, R.id.three, R.id.four,
            R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
            R.id.dot, R.id.plus, R.id.minus, R.id.multiply, R.id.divide -> {
                currentInput += button.text
                display.text = currentInput
            }
            R.id.equals -> {
                if (currentInput.isNotEmpty()) {
                    val result = performOperation(currentInput)
                    display.text = result
                    if (result != "Error") {
                        lastResult = result
                    }
                    currentInput = result
                }
            }
            R.id.ce -> {
                currentInput = lastResult
                display.text = lastResult.ifEmpty { "0" }
            }
            R.id.c -> {
                currentInput = ""
                lastResult = ""
                display.text = "0"
            }
            R.id.bs -> {
                if (currentInput == "Error") {
                    currentInput = lastResult
                    display.text = lastResult.ifEmpty { "0" }
                } else if (currentInput.isNotEmpty()) {
                    currentInput = currentInput.dropLast(1)
                    display.text = currentInput.ifEmpty { "0" }
                }
            }
            R.id.plus_minus -> {
                if (currentInput.isNotEmpty()) {
                    currentInput = if (currentInput.startsWith("-")) {
                        currentInput.drop(1)
                    } else {
                        "-$currentInput"
                    }
                    display.text = currentInput
                }
            }
        }
    }

    private fun performOperation(expression: String): String {
        return try {
            val result = ExpressionBuilder(expression.replace("x", "*")).build().evaluate()
            if (result % 1 == 0.0) result.toInt().toString() else result.toString()
        } catch (e: Exception) {
            "Error"
        }
    }
}