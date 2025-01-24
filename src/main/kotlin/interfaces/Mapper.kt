package org.example.interfaces

interface Mapper<I, O> {
    fun map(input: I): O
}