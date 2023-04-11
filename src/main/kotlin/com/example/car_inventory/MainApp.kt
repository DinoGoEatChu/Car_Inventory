package com.example.car_inventory

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class InventoryApplication : Application() {
    private lateinit var inventoryController: InventoryController

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(InventoryApplication::class.java.getResource("InventoryView.fxml"))
        val scene = Scene(fxmlLoader.load(), 1590.0, 754.0)
        inventoryController = fxmlLoader.getController<InventoryController>()

        stage.title = "Car Inventory"
        stage.scene = scene
        stage.show()

        stage.setOnCloseRequest {
            inventoryController.saveInventory()
        }
    }
}

fun main() {
    Application.launch(InventoryApplication::class.java)
}