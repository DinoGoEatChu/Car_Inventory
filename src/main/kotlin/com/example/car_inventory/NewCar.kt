package com.example.car_inventory

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import java.net.URL
import java.util.*

class NewCar : Initializable {
    private var car: Car? = null
    private var onCarUpdated: ((Car) -> Unit)? = null

    fun init(car: Car?, onCarUpdated: ((Car) -> Unit)?) {
        this.car = car
        this.onCarUpdated = onCarUpdated
    }

    @FXML lateinit var cancelButton: Button
    @FXML lateinit var saveButton: Button
    @FXML lateinit var financed: TextArea
    @FXML lateinit var parts: TextArea
    @FXML lateinit var dateSold: DatePicker
    @FXML lateinit var datePur: DatePicker
    @FXML lateinit var towing: TextField
    @FXML lateinit var check: TextField
    @FXML lateinit var total: TextField
    @FXML lateinit var soldTo: TextField
    @FXML lateinit var labor: TextField
    @FXML lateinit var salePrice: TextField
    @FXML lateinit var title: TextField
    @FXML lateinit var cost: TextField
    @FXML lateinit var vin: TextField
    @FXML lateinit var from: TextField
    @FXML lateinit var model: TextField
    @FXML lateinit var miles: TextField
    @FXML lateinit var make: TextField
    @FXML lateinit var year: TextField
    @FXML lateinit var stock: TextField

    override fun initialize(location: URL?, resources: ResourceBundle?) {
    }



    @FXML
    fun onSave() {
        // Perform validation and create a new car object with the updated information
        val newCarObj = Car(
            stock.text,
            make.text,
            model.text,
            year.text,
            vin.text,
            miles.text,
            datePur.value,
            from.text,
            cost.text,
            dateSold.value,
            salePrice.text,
            soldTo.text,
            parts.text,
            total.text,
            towing.text,
            check.text,
            labor.text,
            title.text,
            financed.text,
        )

        onCarUpdated?.invoke(newCarObj)
        saveButton.scene.window.hide()
    }

    @FXML
    fun onCancel() {
        cancelButton.scene.window.hide()
    }

}





