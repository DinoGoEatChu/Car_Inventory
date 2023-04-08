package com.example.car_inventory

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class InventoryApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(InventoryApplication::class.java.getResource("InventoryView.fxml"))
        val scene = Scene(fxmlLoader.load(), 1590.0, 754.0)
        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(InventoryApplication::class.java)
}