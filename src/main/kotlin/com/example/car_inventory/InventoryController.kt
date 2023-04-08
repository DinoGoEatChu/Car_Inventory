package com.example.car_inventory

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.Modality
import javafx.stage.Stage
import java.net.URL
import java.time.LocalDate
import java.util.*

class InventoryController : Initializable {


    @FXML
    lateinit var deleteCar: Button
    @FXML
    lateinit var changeCar: Button
    @FXML
    lateinit var newCar: Button
    @FXML
    lateinit var unsoldCheck: CheckBox
    @FXML
    lateinit var soldCheck: CheckBox
    @FXML
    lateinit var searchButton: Button
    @FXML
    lateinit var searchBar: TextField
    @FXML
    lateinit var financed: TableColumn<Car, String>
    @FXML
    lateinit var title: TableColumn<Car, String>
    @FXML
    lateinit var labor: TableColumn<Car, String>
    @FXML
    lateinit var check: TableColumn<Car, String>
    @FXML
    lateinit var towing: TableColumn<Car, String>
    @FXML
    lateinit var total: TableColumn<Car, String>
    @FXML
    lateinit var invoices: TableColumn<Car, String>
    @FXML
    lateinit var soldTo: TableColumn<Car, String>
    @FXML
    lateinit var salePrice: TableColumn<Car, String>
    @FXML
    lateinit var soldDate: TableColumn<Car, LocalDate?>
    @FXML
    lateinit var cost: TableColumn<Car, String>
    @FXML
    lateinit var from: TableColumn<Car, String>
    @FXML
    lateinit var purchaseDate: TableColumn<Car, LocalDate>
    @FXML
    lateinit var mileage: TableColumn<Car, String>
    @FXML
    lateinit var vin: TableColumn<Car, String>
    @FXML
    lateinit var year: TableColumn<Car, String>
    @FXML
    lateinit var model: TableColumn<Car, String>
    @FXML
    lateinit var make: TableColumn<Car, String>
    @FXML
    lateinit var stockID: TableColumn<Car, String>
    @FXML
    lateinit var tableView: TableView<Car>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        stockID.cellValueFactory = PropertyValueFactory<Car, String>("stockID")
        make.cellValueFactory = PropertyValueFactory<Car, String>("make")
        model.cellValueFactory = PropertyValueFactory<Car, String>("model")
        year.cellValueFactory = PropertyValueFactory<Car, String>("year")
        vin.cellValueFactory = PropertyValueFactory<Car, String>("vin")
        mileage.cellValueFactory = PropertyValueFactory<Car, String>("mileage")
        purchaseDate.cellValueFactory = PropertyValueFactory<Car, LocalDate>("purchaseDate")
        from.cellValueFactory = PropertyValueFactory<Car, String>("from")
        cost.cellValueFactory = PropertyValueFactory<Car, String>("cost")
        soldDate.cellValueFactory = PropertyValueFactory<Car, LocalDate?>("soldDate")
        salePrice.cellValueFactory = PropertyValueFactory<Car, String>("salePrice")
        soldTo.cellValueFactory = PropertyValueFactory<Car, String>("soldTo")
        invoices.cellValueFactory = PropertyValueFactory<Car, String>("invoices")
        total.cellValueFactory = PropertyValueFactory<Car, String>("total")
        towing.cellValueFactory = PropertyValueFactory<Car, String>("towing")
        check.cellValueFactory = PropertyValueFactory<Car, String>("check")
        labor.cellValueFactory = PropertyValueFactory<Car, String>("labor")
        title.cellValueFactory = PropertyValueFactory<Car, String>("title")
        financed.cellValueFactory = PropertyValueFactory<Car, String>("financed")

    }

    @FXML
    fun onNew(actionEvent: ActionEvent) {
        // Load the NewCar.fxml file
        val fxmlLoader = FXMLLoader(javaClass.getResource("newCar.fxml"))
        val newCarRoot = fxmlLoader.load<Parent>()

        // Get the NewCar controller instance
        val newCarController = fxmlLoader.getController<NewCar>()

        // Pass the parameters using init function
        newCarController.init(null) { updatedCar: Car ->
            // Add the updated car to the tableView
            tableView.items.add(updatedCar)
        }


        // Show the NewCar window
        val newCarStage = Stage()
        newCarStage.initModality(Modality.APPLICATION_MODAL)
        newCarStage.title = "New Car"
        newCarStage.scene = Scene(newCarRoot)
        newCarStage.show()
    }

    @FXML
    fun onChange(actionEvent: ActionEvent) {
        val selectedCar = tableView.selectionModel.selectedItem

        if (selectedCar != null) {
            // Pass a callback function to the ModifyCarController
            val modifyCar = ModifyCar(selectedCar) { updatedCar ->
                // Update the selected car in the tableView
                tableView.items[tableView.selectionModel.selectedIndex] = updatedCar
            }

            // Load the modify car FXML file and set the controller factory
            val loader = FXMLLoader(ModifyCar::class.java.getResource("modifyCar.fxml"))
            loader.setControllerFactory { modifyCar }
            val root = loader.load<Parent>()

            // Set up the scene and show the modify car screen
            val scene = Scene(root)
            val stage = Stage()
            stage.scene = scene
            stage.show()

        } else {
            // Show an alert if no car is selected
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "No Car Selected"
            alert.headerText = "Please select a Car to change."
            alert.showAndWait()
        }
    }


    @FXML
    fun onDelete(actionEvent: ActionEvent) {
        // Handle the delete car button click event
        val selectedCar = tableView.selectionModel.selectedItem
        if (selectedCar != null) {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.title = "DELETE CAR"
            alert.headerText = "Are you sure you want to DELETE CAR?"
            val okButton = ButtonType.OK
            val cancelButton = ButtonType.CANCEL
            alert.buttonTypes.setAll(okButton, cancelButton)
            val result = alert.showAndWait()


            if (result.get() == ButtonType.OK) {
                tableView.items.remove(selectedCar)
            } else if (result.get() == ButtonType.CANCEL) {
                return
            }
        }
    }
}

data class Car(
    val stockID: String,
    val make: String,
    val model: String,
    val year: String,
    val vin: String,
    val mileage: String,
    val purchaseDate: LocalDate,
    val from: String,
    val cost: String,
    val soldDate: LocalDate?,
    val salePrice: String,
    val soldTo: String,
    val invoices: String,
    val total: String,
    val towing: String,
    val check: String,
    val labor: String,
    val title: String,
    val financed: String,
)

