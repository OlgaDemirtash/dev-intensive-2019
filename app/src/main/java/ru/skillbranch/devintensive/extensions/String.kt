
package ru.skillbranch.devintensive.extensions

//Реализуй extension позволяющий очистить строку от html тегов и html escape последовательностей ("& < > ' ""), а так же удалить пустые символы (пробелы) между словами если их больше 1. Необходимо вернуть модифицированную строку
//Пример:
//"<p class="title">Образовательное IT-сообщество Skill Branch</p>".stripHtml() //Образовательное IT-сообщество Skill Branch
//"<p>Образовательное       IT-сообщество Skill Branch</p>".stripHtml() //Образовательное IT-сообщество Skill Branch


fun String.stripHtml() = this
    .replace("<[^>]*>".toRegex(), "")
    .replace("\\s{2,}".toRegex(), " ")


//Реализуй extension усекающий исходную строку до указанного числа символов (по умолчанию 16) и возвращающий усеченную строку с заполнителем "..." (если строка была усечена) если последний символ усеченной строки является пробелом - удалить его и добавить заполнитель
//Пример:
//"Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate() //Bender Bending R...
//"Bender Bending Rodriguez — дословно «Сгибальщик Сгибающий Родригес»".truncate(15) //Bender Bending...
//"A     ".truncate(3) //A

fun String.truncate(num: Int = 16) = when (this?.get(num + 1)) {
    ' ' -> this.substring(0, num)
    else -> this.substring(0, num) + "..."
}