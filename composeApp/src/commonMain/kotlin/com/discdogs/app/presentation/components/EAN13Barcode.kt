package com.discdogs.app.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Kotlin Multiplatform-friendly EAN-13 barcode renderer using Compose Multiplatform Canvas.
 *
 * Usage:
 * ```kotlin
 * EAN13Barcode(
 *   content = "869050403210", // 12 or 13 digits. If 12, checksum is auto-appended.
 *   modifier = Modifier.size(320.dp, 120.dp)
 * )
 * ```
 */
@Composable
fun EAN13Barcode(
    content: String,
    modifier: Modifier = Modifier,
    barColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    quietZoneModules: Int = 9, // recommended quiet zone per spec
    moduleMinWidth: Dp = 1.dp, // minimum width per module; canvas will scale up as needed
) {
    val normalized = remember(content) { sanitizeAndComplete(content) }
    val modules = remember(normalized) { encodeEan13ToModules(normalized) }

    androidx.compose.foundation.Canvas(
        modifier = modifier.background(backgroundColor)
    ) {
        drawBarcodeModules(
            modules = modules,
            barColor = barColor,
            backgroundColor = backgroundColor,
            quietZoneModules = quietZoneModules,
            moduleMinWidthPx = moduleMinWidth.toPx(),
        )
    }
}

// ——— EAN‑13 core ——————————————————————————————————————————————————————————————

private val L_CODES = mapOf(
    '0' to "0001101",
    '1' to "0011001",
    '2' to "0010011",
    '3' to "0111101",
    '4' to "0100011",
    '5' to "0110001",
    '6' to "0101111",
    '7' to "0111011",
    '8' to "0110111",
    '9' to "0001011",
)

private val G_CODES = mapOf(
    '0' to "0100111",
    '1' to "0110011",
    '2' to "0011011",
    '3' to "0100001",
    '4' to "0011101",
    '5' to "0111001",
    '6' to "0000101",
    '7' to "0010001",
    '8' to "0001001",
    '9' to "0010111",
)

private val R_CODES = mapOf(
    '0' to "1110010",
    '1' to "1100110",
    '2' to "1101100",
    '3' to "1000010",
    '4' to "1011100",
    '5' to "1001110",
    '6' to "1010000",
    '7' to "1000100",
    '8' to "1001000",
    '9' to "1110100",
)

// Parity pattern of the LEFT six digits determined by the first number.
// 'L' and 'G' denote which code set to use for each of the six left digits.
private val PARITY_TABLE = mapOf(
    '0' to "LLLLLL",
    '1' to "LLGLGG",
    '2' to "LLGGLG",
    '3' to "LLGGGL",
    '4' to "LGLLGG",
    '5' to "LGGLLG",
    '6' to "LGGGLL",
    '7' to "LGLGLG",
    '8' to "LGLGGL",
    '9' to "LGGLGL",
)

private fun sanitizeAndComplete(raw: String): String {
    require(raw.all { it.isDigit() }) { "EAN-13 accepts only digits." }
    require(raw.length in 12..13) { "EAN-13 needs 12 or 13 digits (got ${raw.length})." }

    return if (raw.length == 13) {
        // Validate checksum
        val expected = ean13Checksum(raw.substring(0, 12))
        require(raw.last().digitToInt() == expected) {
            "Invalid EAN-13 checksum. Expected last digit $expected for ${raw.substring(0, 12)}"
        }
        raw
    } else {
        raw + ean13Checksum(raw)
    }
}

private fun ean13Checksum(first12: String): Int {
    // Indexing from left, positions 1..12. Sum of odd positions + 3*sum of even positions.
    var sumOdd = 0
    var sumEven = 0
    first12.forEachIndexed { idx, ch ->
        val d = ch.digitToInt()
        if ((idx + 1) % 2 == 0) sumEven += d else sumOdd += d
    }
    val total = sumOdd + 3 * sumEven
    val mod = total % 10
    return if (mod == 0) 0 else 10 - mod
}

private fun encodeEan13ToModules(ean13: String): List<Boolean> {
    val first = ean13[0]
    val leftSix = ean13.substring(1, 7)
    val rightSix = ean13.substring(7)

    val parity = PARITY_TABLE[first] ?: error("No parity for first digit $first")

    val startGuard = "101"
    val centerGuard = "01010"
    val endGuard = "101"

    val leftBits = buildString {
        leftSix.forEachIndexed { i, ch ->
            val use = if (parity[i] == 'L') L_CODES else G_CODES
            append(use[ch] ?: error("Invalid digit $ch"))
        }
    }

    val rightBits = buildString {
        rightSix.forEach { ch -> append(R_CODES[ch] ?: error("Invalid digit $ch")) }
    }

    val full = startGuard + leftBits + centerGuard + rightBits + endGuard
    require(full.length == 95) { "EAN-13 must be 95 modules, got ${full.length}" }

    return full.map { it == '1' }
}

// ——— Drawing ————————————————————————————————————————————————————————————————

private fun DrawScope.drawBarcodeModules(
    modules: List<Boolean>,
    barColor: Color,
    backgroundColor: Color,
    quietZoneModules: Int,
    moduleMinWidthPx: Float,
) {
    val totalModules = modules.size + quietZoneModules * 2 // add quiet zones on each side

    // Compute the module width to fit the canvas, but never thinner than moduleMinWidthPx
    val moduleWidth = maxOf(size.width / totalModules, moduleMinWidthPx)
    val requiredWidth = moduleWidth * totalModules

    // If required width is wider than canvas, scale down height proportionally and center horizontally
    val offsetX = (size.width - requiredWidth).coerceAtLeast(0f) / 2f

    // Background
    drawRect(backgroundColor, size = size)

    val barHeight = size.height // full height; adjust if you want shorter data bars vs guards

    // Draw left quiet zone (empty)
    var x = offsetX + moduleWidth * quietZoneModules

    // Draw modules
    modules.forEach { isBar ->
        if (isBar) {
            drawRect(
                color = barColor,
                topLeft = androidx.compose.ui.geometry.Offset(x, 0f),
                size = Size(moduleWidth, barHeight)
            )
        }
        x += moduleWidth
    }
    // Right quiet zone is implicit (just empty space until edge)
}

// ——— Optional: helper to pretty-print numbers below the barcode (use a normal Text composable) ———

/** Returns the 13-digit EAN with spaces as `X XXXXX XXXXXX X` (first / left6 / right6 / checksum). */
fun formatEan13HumanReadable(ean13: String): String {
    val v = sanitizeAndComplete(ean13)
    return buildString {
        append(v[0])
        append(' ')
        append(v.substring(1, 7))
        append(' ')
        append(v.substring(7, 12))
        append(' ')
        append(v[12])
    }
}

// ——— Example wrapper with text (multiplatform friendly) ————————————————

@Composable
fun EAN13BarcodeWithLabel(
    content: String,
    modifier: Modifier = Modifier,
    barColor: Color = Color.Black,
    backgroundColor: Color = Color.White,
    labelSpacer: Dp = 4.dp,
    label: (@Composable (String) -> Unit)? = null,
) {
    val normalized = remember(content) { sanitizeAndComplete(content) }
    Column(modifier = modifier) {
        EAN13Barcode(
            content = normalized,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = true),
            barColor = barColor,
            backgroundColor = backgroundColor,
        )
        Spacer(Modifier.height(labelSpacer))
        val defaultLabel: @Composable (String) -> Unit = { text ->
            androidx.compose.material3.Text(text)
        }
        (label ?: defaultLabel).invoke(formatEan13HumanReadable(normalized))
    }
}
