package com.example.car_inventory

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File


object CarStorage {
    private const val fileName = "cars.json"
    private val objectMapper = ObjectMapper()
        .registerModule(KotlinModule.Builder().build())
        .registerModule(JavaTimeModule())

    fun saveCars(cars: List<Car>) {
        val file = File(fileName)
        objectMapper.writeValue(file, cars)
    }

    fun loadCars(): List<Car> {
        val file = File(fileName)
        return if (file.exists()) {
            val carListType = objectMapper.typeFactory.constructCollectionType(List::class.java, Car::class.java)
            objectMapper.readValue(file, carListType)
        } else {
            emptyList()
        }
    }
}