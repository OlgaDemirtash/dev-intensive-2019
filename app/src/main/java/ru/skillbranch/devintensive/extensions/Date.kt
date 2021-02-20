package ru.skillbranch.devintensive.extensions


import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR


fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time
    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

enum class TimeUnits {
    SECOND {
        override fun plural(number: Int): String = "${abs(number)} ${when (number.toString().takeLast(1).toInt()) {
            1 -> "секунду"
            in 2..4 -> "секунды"
            in 5..9, 0 -> "секунд"

            else -> "секунд"
        }}"
    },

    MINUTE {
        override fun plural(number: Int): String = "${abs(number)} ${when (number.toString().takeLast(1).toInt()) {
            1 -> "минуту"
            in 2..4 -> "минуты"
            in 5..9, 0 -> "минут"

            else -> "минут"
        }}"
    },

    HOUR {
        override fun plural(number: Int): String = "${abs(number)} ${when (number.toString().takeLast(1).toInt()) {
            1 -> "час"
            in 2..4 -> "часа"
            in 5..9, 0 -> "часов"

            else -> "часов"
        }}"
    },

    DAY {
        override fun plural(number: Int): String = "${abs(number)} ${when (number.toString().takeLast(1).toInt()) {
            1 -> "день"
            in 2..4 -> "дня"
            in 5..9, 0 -> "дней"

            else -> "секунд"
        }}"
    };

    abstract fun plural(number: Int): String
}


fun Date.humanizeDiff(date: Date = Date()): String {

    var time = this.time
    val diffL: Long = (Date().time - time) / 1000
    val diff: Int = diffL.toInt()
    var humDate: String = ""
    humDate = when (diff) {
        in 0..1 -> {
            "только что"
        }
        in 1..45 -> {
            "несколько секунд назад"
        }
        in 45..75 -> {
            "минуту назад"
        }
        in 75..45 * 60 -> {
            "${TimeUnits.MINUTE.plural(diff / 60)} назад"
        }
        in 45 * 60..75 * 60 -> {
            "час назад"
        }
        in 75 * 60..22 * 3600 -> {
            "${TimeUnits.HOUR.plural(diff / 3600)} назад"
        }
        in 22 * 3600..26 * 3600 -> {
            "день назад"
        }
        in 26 * 3600..360 * 86400 -> {
            "${TimeUnits.DAY.plural(diff / 86400)} назад"
        }

        in -45..-1 -> {
            "через несколько секунд"
        }
        in -75..-45 -> {
            "через минуту"
        }
        in -45 * 60..-75 -> {
            "через ${TimeUnits.MINUTE.plural(diff / 60)}"
        }
        in -75 * 60..-45 * 60 -> {
            "через час"
        }
        in -22 * 3600..-75 * -60 -> {
            "через ${TimeUnits.HOUR.plural(diff / 3600)}"
        }
        in -26 * 3600..-22 * 3600 -> {
            "через день"
        }
        in -360 * 86400..-26 * 3600 -> {
            "через ${TimeUnits.DAY.plural(diff / 86400)}"

        }

         in 360 * 86400..Integer.MAX_VALUE -> { "более года назад"}
         in Integer.MIN_VALUE..-360 * 86400 -> { "более чем через год"}
        else   -> "неизвестно"
    }

    return humDate


}

