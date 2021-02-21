package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {
    var negIdx = 0
    val regex = Regex(pattern = """\d+""")
    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswear(answear: String): Pair<String, Triple<Int, Int, Int>> =

        if (question.answers.contains(answear)) {
            if (question == Question.IDLE) {

                "${validateAnswear(answear)}\nНа этом все, вопросов больше нет" to status.color
            } else
            {val valStr = validateAnswear(answear)
            status = Status.NORMAL
            question = question.nextQuestion()
            "$valStr\n${question.question}" to status.color}
        } else {
            negIdx += 1
            if (negIdx > 3) {

                status = Status.NORMAL
                question = Question.NAME
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            } else {
                status = status.nextStatus()
                "Это неправильный ответ\n${question.question}" to status.color
            }
        }


    fun validateAnswear(answear: String): String =
        when (question) {
            Question.NAME -> if (answear[0].isUpperCase()) "Отлично - ты справился" else "Имя должно начинаться с заглавной буквы"
            Question.PROFESSION -> if (answear[0].isLowerCase()) "Отлично - ты справился"  else "Профессия должна начинаться со строчной буквы"
            Question.MATERIAL -> if (regex.containsMatchIn(answear)) "Материал не должен содержать цифр" else "Отлично - ты справился"
            Question.BDAY -> if (regex.matches(answear)) "Отлично - ты справился"  else "Год моего рождения должен содержать только цифры"
            Question.SERIAL -> if (regex.matches(answear) && answear.length == 7) "Отлично - ты справился"  else "Серийный номер содержит только цифры, и их 7"
            Question.IDLE -> "Отлично - ты справился"
        }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else values()[0]

        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },

        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
    }

}