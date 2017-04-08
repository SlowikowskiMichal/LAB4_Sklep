package address;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import address.model.Product;
import address.model.ProductListWrapper;
import address.model.ProductType;
import address.view.ProductEditDialogController;
import address.view.ProductOverviewController;
import address.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Main extends Application {

    private ObservableList<Product> productData = FXCollections.observableArrayList();

    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SKLEP");

        initRootLayout();

        showProductOverview();
    }

    public Main()
    {
        productData.add(new Product("Logitech K360", 3, ProductType.KEYBOARD));
        productData.add(new Product("SteelSeries Rival 100", 8, ProductType.MOUSE));
        productData.add(new Product("BenQ XL2411Z", 6, ProductType.MONITOR));
        productData.add(new Product("Razer Deathstalker Chroma", 5, ProductType.KEYBOARD));
        productData.add(new Product("Dell U2515H", 7, ProductType.MONITOR));
        productData.add(new Product("Tesoro Sagitta", 1, ProductType.MOUSE));
    }

    public ObservableList<Product> getProductData() {
        return productData;
    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/rootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMain(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened person file.
        File file = getProductFilePath();
        if (file != null) {
            loadProductDataFromFile(file);
        }
    }

    public void showProductOverview() {
        try {
            // Load product overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/productOverview.fxml"));
            AnchorPane productOverview = (AnchorPane) loader.load();

            // Set item overview into the center of root layout.
            rootLayout.setCenter(productOverview);

            // Give the controller access to the main app.
            ProductOverviewController controller = loader.getController();
            controller.setMain(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showProductEditDialog(Product product) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/productEdit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the item into the controller.
            ProductEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProduct(product);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }



    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getProductFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setProductFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("AddressApp");
        }
    }

    /**
     * Loads person data from the specified file. The current person data will
     * be replaced.
     *
     * @param file
     */
    public void loadProductDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ProductListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            ProductListWrapper wrapper = (ProductListWrapper) um.unmarshal(file);//------------------------

            productData.clear();
            productData.addAll(wrapper.getProducts());

            // Save the file path to the registry.
            setProductFilePath(file);

        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * Saves the current person data to the specified file.
     *
     * @param file
     */
    public void saveProductDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ProductListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            ProductListWrapper wrapper = new ProductListWrapper();
            wrapper.setProducts(productData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setProductFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }
}
