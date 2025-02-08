package org.example.advice

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

object TxAdvice {

    private val txAdvice: _TxAdvice = _TxAdvice() // 내부에서 _TxAdvice를 직접 생성

    // 트랜잭션을 시작하는 run 메서드
    fun <T> run(function: () -> T): T {
        return txAdvice.run(function)  // _TxAdvice의 run 메서드 호출
    }

    // 읽기 전용 트랜잭션을 시작하는 readOnly 메서드
    fun <T> readOnly(function: () -> T): T {
        return txAdvice.readOnly(function)  // _TxAdvice의 readOnly 메서드 호출
    }

    // _TxAdvice는 실제 트랜잭션을 처리하는 컴포넌트입니다.
    open class _TxAdvice {

        @Transactional
        open fun <T> run(function: () -> T): T {
            return function()  // 트랜잭션 내에서 실행
        }

        @Transactional(readOnly = true)
        open fun <T> readOnly(function: () -> T): T {
            return function()  // 읽기 전용 트랜잭션 내에서 실행
        }
    }
}