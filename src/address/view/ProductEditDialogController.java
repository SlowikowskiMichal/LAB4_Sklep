package address.view;


import address.model.ProductType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import address.model.Product;

public class ProductEditDialogController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField quantityField;
    //@FXML
    //private TextField typeField;
    //@FXML
    //private TextField boolField;

    @FXML
    private ComboBox<ProductType> typeBox;

    @FXML
    private CheckBox boolBox;


    private Stage dialogStage;
    private Product product;
    private boolean okClicked = false;


    @FXML
    private void initialize() {
        typeBox.getItems().addAll(ProductType.values());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setProduct(Product product) {
        this.product = product;

        nameField.setText(product.getName());
        quantityField.setText(Integer.toString(product.getQuantiy()));
        if(boolBox.isSelected())
        {
            product.setAviable(true);
        }
        else
        {
            product.setAviable(false);
        }
        product.setType((ProductType) typeBox.getSelectionModel().getSelectedItem());

    }

    public boolean isOkClicked() {
        return okClicked;
    }


    @FXML
    private void handleOk() {
        if (isInputValid()) {
            product.setName(nameField.getText());
            product.setType(typeBox.getValue());
            product.setQuantiy(Integer.parseInt(quantityField.getText()));
            if(boolBox.isSelected())
            {
                product.setAviable(true);
            }
            else
            {
                product.setAviable(false);
            }

            okClicked = true;
            dialogStage.close();
        }
    }


    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }

        if (quantityField.getText() == null || quantityField.getText().length() == 0) {
            errorMessage += "No valid quantity!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid postal code (must be an integer)!\n";
            }
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


}


