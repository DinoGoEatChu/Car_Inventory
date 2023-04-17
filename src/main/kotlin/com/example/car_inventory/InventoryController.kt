package com.example.car_inventory

import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.*
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class InventoryController : Initializable {


    @FXML
    lateinit var unsoldCars: Label

    @FXML
    lateinit var carsSold: Label

    @FXML
    lateinit var exportToCSVButton: Button

    @FXML
    lateinit var importCSVButton: Button

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
    lateinit var purchaseDate: TableColumn<Car, LocalDate?>

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

    private lateinit var originalCarList: ObservableList<Car>

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        stockID.cellValueFactory = PropertyValueFactory("stockID")
        make.cellValueFactory = PropertyValueFactory("make")
        model.cellValueFactory = PropertyValueFactory("model")
        year.cellValueFactory = PropertyValueFactory("year")
        vin.cellValueFactory = PropertyValueFactory("vin")
        mileage.cellValueFactory = PropertyValueFactory("mileage")
        purchaseDate.cellValueFactory = PropertyValueFactory<Car, LocalDate>("purchaseDate")
        from.cellValueFactory = PropertyValueFactory("from")
        cost.cellValueFactory = PropertyValueFactory("cost")
        soldDate.cellValueFactory = PropertyValueFactory<Car, LocalDate?>("soldDate")
        salePrice.cellValueFactory = PropertyValueFactory("salePrice")
        soldTo.cellValueFactory = PropertyValueFactory("soldTo")
        invoices.cellValueFactory = PropertyValueFactory("invoices")
        total.cellValueFactory = PropertyValueFactory("total")
        towing.cellValueFactory = PropertyValueFactory("towing")
        check.cellValueFactory = PropertyValueFactory("check")
        labor.cellValueFactory = PropertyValueFactory("labor")
        title.cellValueFactory = PropertyValueFactory("title")
        financed.cellValueFactory = PropertyValueFactory("financed")

        //store original car list
        originalCarList = FXCollections.observableArrayList(tableView.items.toMutableList())

        //Load cars from file
        originalCarList = FXCollections.observableArrayList(CarStorage.loadCars())
        tableView.items.setAll(originalCarList)

        //adds listener function to text property, listener will be called when text content changes
        searchBar.textProperty().addListener { _, _, _ -> filterCars() }
        unsoldCheck.selectedProperty().addListener { _, _, _ -> filterCars() }
        soldCheck.selectedProperty().addListener { _, _, _ -> filterCars() }
        updateSoldUnsoldLabels()
    }

    // search function / filter function
    private fun filterCars() {
        val searchText = searchBar.text.lowercase()
        val showUnsold = unsoldCheck.isSelected
        val showSold = soldCheck.isSelected

        val filteredCars = originalCarList.filter { car ->
            val matchesSearchText = searchText.isBlank() ||
                    car.make.lowercase().contains(searchText) ||
                    car.model.lowercase().contains(searchText) ||
                    car.year.lowercase().contains(searchText) ||
                    car.vin.lowercase().contains(searchText)

            val matchesUnsoldSoldFilter =
                (!showUnsold && !showSold) || (showUnsold && car.soldDate == null) || (showSold && car.soldDate != null)

            matchesSearchText && matchesUnsoldSoldFilter
        }
        tableView.items.setAll(filteredCars)
        updateSoldUnsoldLabels()
    }


    @FXML
    fun onNew() {
        // Load the NewCar.fxml file
        val fxmlLoader = FXMLLoader(javaClass.getResource("newCar.fxml"))
        val newCarRoot = fxmlLoader.load<Parent>()

        // Get the NewCar controller instance
        val newCarController = fxmlLoader.getController<NewCar>()

        // Pass the parameters using init function
        newCarController.init(null) { updatedCar: Car ->
            // Add the updated car to the tableView
            tableView.items.add(updatedCar)
            originalCarList.add(updatedCar)
            updateSoldUnsoldLabels()
            saveInventory()
        }


        // Show the NewCar window
        val newCarStage = Stage()
        newCarStage.initModality(Modality.APPLICATION_MODAL)
        newCarStage.title = "New Car"
        newCarStage.scene = Scene(newCarRoot)
        newCarStage.show()
    }

    @FXML
    fun onChange() {
        val selectedCar = tableView.selectionModel.selectedItem
        val selectedIndex = tableView.selectionModel.selectedIndex

        if (selectedCar != null && selectedIndex >= 0) {
            // Pass a callback function to the ModifyCarController
            val modifyCar = ModifyCar(selectedCar) { updatedCar ->
                // Update the selected car in the tableView
                tableView.items[selectedIndex] = updatedCar
                originalCarList[selectedIndex] = updatedCar
                updateSoldUnsoldLabels()
                saveInventory()
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
    fun onDelete() {
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
                val selectedIndex = tableView.selectionModel.selectedIndex
                tableView.items.remove(selectedCar)
                originalCarList.removeAt(selectedIndex)
                updateSoldUnsoldLabels()
                saveInventory()
            } else if (result.get() == ButtonType.CANCEL) {
                return
            }
        }
    }

    fun saveInventory() {
        CarStorage.saveCars(originalCarList)
    }

    @FXML
    fun loadCarsFromCsv() {
        val fileChooser = FileChooser()
        fileChooser.title = "Select CSV File"
        fileChooser.extensionFilters.add(
            FileChooser.ExtensionFilter("CSV Files", "*.csv")
        )
        val selectedFile = fileChooser.showOpenDialog(null)

        val fileContent = selectedFile.readText()
        val lines = fileContent.lines().toMutableList()
        lines[0] = lines[0].lowercase()
        val csvContent = lines.joinToString(separator = "\n")

        if (selectedFile != null) {
            val cars = mutableListOf<Car>()
            val csvReader = CsvReader()
            val dateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
            csvReader.open(csvContent.byteInputStream()) {
                readAllWithHeaderAsSequence().map { it.mapKeys { entry -> entry.key.lowercase() } }.forEach { row: Map<String, String> ->
                    if (row.size == 19) {
                        val car = Car(
                            stockID = row["stock no."].orEmpty().trim(),
                            make = row["make"].orEmpty().trim(),
                            model = row["model"].orEmpty().trim(),
                            year = row["year"].orEmpty().trim(),
                            vin = row["v.i.n."].orEmpty().trim(),
                            mileage = row["milage"].orEmpty().trim(),
                            purchaseDate = row["date purchased"]?.trim()?.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it, dateFormatter) },
                            from = row["from"].orEmpty().trim(),
                            cost = row["cost"].orEmpty().trim(),
                            soldDate = row["date sold"]?.trim()?.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it, dateFormatter) },
                            salePrice = row["sale price"].orEmpty().trim(),
                            soldTo = row["sold to"].orEmpty().trim(),
                            invoices = row["parts & invoices"].orEmpty().trim(),
                            total = row["total"].orEmpty().trim(),
                            towing = row["towing"].orEmpty().trim(),
                            check = row["check"].orEmpty().trim(),
                            labor = row["labor"].orEmpty().trim(),
                            title = row["title"].orEmpty().trim(),
                            financed = row["financed"].orEmpty().trim(),
                        )
                        cars.add(car)
                    }
                }
            }

            originalCarList.addAll(cars)
            tableView.items.setAll(originalCarList)
            updateSoldUnsoldLabels()
            saveInventory()
        }
    }

    @Throws(IOException::class)
    fun exportCarsToFile(cars: List<Car>, outputFile: String) {
        FileWriter(outputFile).use { writer ->
            // Write header
            writer.append("stockID,make,model,year,vin,mileage,purchaseDate,from,cost,soldDate,salePrice,soldTo,invoices,total,towing,check,labor,title,financed\n")

            // Write data
            for (car in cars) {
                writer.append(car.stockID).append(',')
                    .append(car.make).append(',')
                    .append(car.model).append(',')
                    .append(car.year).append(',')
                    .append(car.vin).append(',')
                    .append(car.mileage).append(',')
                    .append(car.purchaseDate?.toString() ?: "").append(',')
                    .append(car.from).append(',')
                    .append(car.cost).append(',')
                    .append(car.soldDate?.toString() ?: "").append(',')
                    .append(car.salePrice).append(',')
                    .append(car.soldTo).append(',')
                    .append(car.invoices).append(',')
                    .append(car.total).append(',')
                    .append(car.towing).append(',')
                    .append(car.check).append(',')
                    .append(car.labor).append(',')
                    .append(car.title).append(',')
                    .append("\"").append(car.financed.replace("\"", "\"\"").replace("\n", "\r\n")).append("\"\n")
            }
        }
    }

    @FXML
    fun exportCarsToCsv() {
        // Create a file chooser to select the CSV file
        val fileChooser = FileChooser()
        fileChooser.title = "Save CSV File"
        fileChooser.extensionFilters.add(
            FileChooser.ExtensionFilter("CSV Files", "*.csv")
        )
        val selectedFile = fileChooser.showSaveDialog(null)

        // If a file is selected, export the cars to the CSV file
        if (selectedFile != null) {
            try {
                exportCarsToFile(tableView.items, selectedFile.absolutePath)
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Export Successful"
                alert.headerText = "Cars exported to CSV file successfully!"
                alert.showAndWait()
            } catch (e: IOException) {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Export Failed"
                alert.headerText = "Failed to export cars to CSV file."
                alert.showAndWait()
            }
        }
    }

    private fun updateSoldUnsoldLabels() {
        val soldCount = tableView.items.count { it.soldDate != null }
        val unsoldCount = tableView.items.count { it.soldDate == null }

        carsSold.text = "Sold: $soldCount"
        unsoldCars.text = "Unsold: $unsoldCount"
    }

}


data class Car(
    val stockID: String,
    val make: String,
    val model: String,
    val year: String,
    val vin: String,
    val mileage: String,
    val purchaseDate: LocalDate?,
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

