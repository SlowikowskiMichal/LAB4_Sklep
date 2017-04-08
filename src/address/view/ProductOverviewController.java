package address.view;

import address.model.ProductType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import address.Main;
import address.model.Product;

public class ProductOverviewController {
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> nameColumn;


    @FXML
    private Label nameLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label boolLabel;


    // Reference to the main application.
    private Main main;

    public ProductOverviewController() {
    }


    @FXML
    private void initialize() {
        // Initialize table with the two columns.
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        // Clear product details.
        showProductDetails(null);

        // Listen for selection changes and show details when changed.
        productTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showProductDetails(newValue));
    }


    public void setMain(Main mainApp) {
        main = mainApp;

        // Add observable list data to the table
        productTable.setItems(mainApp.getProductData());
    }

    private void showProductDetails (Product product)
    {
        if (product != null)
        {
            nameLabel.setText(product.getName());
            quantityLabel.setText(Integer.toString(product.getQuantiy()));
            typeLabel.setText(product.seeType());
            boolLabel.setText(product.seeStore());
        }
        else
        {
            nameLabel.setText("");
            quantityLabel.setText("");
            typeLabel.setText("");
            boolLabel.setText("");
        }
    }

    @FXML
    private void handleDeleteProduct() {
        int selectedIndex = productTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            productTable.getItems().remove(selectedIndex);
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Item Selected");
            alert.setContentText("Please select a item in the table.");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleNewProduct() {
        Product tempProduct = new Product("", 0, ProductType.KEYBOARD);
        boolean okClicked = main.showProductEditDialog(tempProduct);
        if (okClicked) {
            main.getProductData().add(tempProduct);
        }
    }

    @FXML
    private void handleEditProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            boolean okClicked = main.showProductEditDialog(selectedProduct);
            if (okClicked) {
                showProductDetails(selectedProduct);
            }

        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Item Selected");
            alert.setContentText("Please select a item in the table.");

            alert.showAndWait();
        }
    }
}

