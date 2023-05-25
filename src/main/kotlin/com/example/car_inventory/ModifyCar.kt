package com.example.car_inventory

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import java.net.URL
import java.util.*


class ModifyCar(private val selectedIndex: Int, private val selectedCar: Car, private val callbackFn: (Int, Car) -> Unit) : Initializable {
    private var car: Car? = null
    private var onUpdate: ((Int, Car) -> Unit)? = null

    init {
        car = selectedCar
        onUpdate = callbackFn
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
        // Initialize the fields with the selected car's information
        stock.text = selectedCar.stockID
        make.text = selectedCar.make
        model.text = selectedCar.model
        year.text = selectedCar.year
        vin.text = selectedCar.vin
        miles.text = selectedCar.mileage
        datePur.value = selectedCar.purchaseDate
        from.text = selectedCar.from
        cost.text = selectedCar.cost
        dateSold.value = selectedCar.soldDate
        salePrice.text = selectedCar.salePrice
        soldTo.text = selectedCar.soldTo
        parts.text = selectedCar.invoices
        total.text = selectedCar.total
        towing.text = selectedCar.towing
        check.text = selectedCar.check
        labor.text = selectedCar.labor
        title.text = selectedCar.title
        financed.text = selectedCar.financed
    }

    @FXML
    fun onCancel() {
        // Close the modify car window
        cancelButton.scene.window.hide()
    }

    @FXML
    fun onSave() {
        // Perform validation and create a new car object with the updated information
        val updatedCar = Car(
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



        // Call the onUpdate callback to pass the updated car information back to InventoryController
        callbackFn.invoke(selectedIndex, updatedCar)

        // Close the modify car window
        saveButton.scene.window.hide()
    }

}

