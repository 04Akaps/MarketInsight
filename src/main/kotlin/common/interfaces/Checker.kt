package org.example.interfaces

interface Checker<I, O> {
    fun check(input: I, input2: O): Boolean
}