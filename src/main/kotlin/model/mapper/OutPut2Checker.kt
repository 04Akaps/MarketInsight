package org.example.model.mapper

import org.example.interfaces.Checker
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object OutPut2Checker :  Checker<String, String> {
    override fun check(input: String, input2: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        if (input == "") {
            // "" 값이면 모두 저장을 해야 한다.
            return true
        }

        // input 값을 LocalDate로 변환
        val date1 = LocalDate.parse(input, formatter)
        val date2 = LocalDate.parse(input2, formatter)

        // date1가 date1보다 큰지 비교
        return date2.isAfter(date1)
    }
}