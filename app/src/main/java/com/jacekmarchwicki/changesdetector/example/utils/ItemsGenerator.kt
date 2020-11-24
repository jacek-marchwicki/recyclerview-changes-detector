package com.jacekmarchwicki.changesdetector.example.utils

import android.graphics.Color
import java.util.*
import javax.annotation.Nonnull
import kotlin.math.abs
import kotlin.math.min

object ItemsGenerator {

    fun generateElements(): List<Element> {
        val random = Random()
        var hue = 0f
        val itemsCount = randomBetween(random, 10, 30)
        val items = mutableListOf<Element>()
        for (i in 0 until itemsCount) {
            hue += 0.12345f
            if (!randomWithGaussianBoolean(random, 2.0)) {
                continue
            }
            val realHue = hue % 1.0f
            val color = Color.HSVToColor(255, floatArrayOf(realHue * 360, 1f, 0.5f))
            val name = if (randomWithGaussianBoolean(random, 2.0)) "item$i" else "item$i - changed"
            items.add(Element(i.toLong(), name, color))
        }
        val swapStart = randomBetween(random, 0, items.size - 2)
        val swapEnd = min(items.size - 1, swapStart + 2)
        Collections.swap(items, swapStart, swapEnd)
        return items.toList()
    }

    private fun randomWithGaussianBoolean(@Nonnull random: Random, proablity: Double): Boolean {
        return abs(random.nextGaussian()) < proablity
    }

    private fun randomBetween(@Nonnull random: Random, start: Int, end: Int): Int {
        return random.nextInt(end - start) + start
    }
}