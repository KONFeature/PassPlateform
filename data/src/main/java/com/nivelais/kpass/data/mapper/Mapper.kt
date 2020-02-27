package com.nivelais.kpass.data.mapper

import java.util.stream.Collectors

abstract class Mapper<in From, out To> {

    abstract fun map(from: From): To

    fun mapList(from: List<From>): List<To> =
        from.stream()
            .map { map(it) }
            .collect(Collectors.toList())
}