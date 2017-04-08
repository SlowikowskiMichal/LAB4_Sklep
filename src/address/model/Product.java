package address.model;

import javafx.beans.property.*;

public class Product {
    public String getName() {
        return name.get();
    }
    public Product() {
        this(null, 0, null);
    }
    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getQuantiy() {
        return quantiy.get();
    }

    public IntegerProperty quantiyProperty() {
        return quantiy;
    }

    public void setQuantiy(int quantiy) {
        this.quantiy.set(quantiy);
    }



    public ObjectProperty<ProductType> typeProperty() {
        return type;
    }

    public void setType(ProductType type) {
        this.type.set(type);
    }

    public void setType(String type) {
        if (type == "KEYBOARD")
            this.type.set(ProductType.KEYBOARD);
        else if (type == "MOUSE")
            this.type.set(ProductType.MOUSE);
        else
            this.type.set(ProductType.MONITOR);
    }

    public ProductType getType() {
        return type.get();
    }

    public boolean isAviable() {
        return aviable.get();
    }

    public BooleanProperty aviableProperty() {
        return aviable;
    }

    public void setAviable(boolean aviable) {
        this.aviable.set(aviable);
    }

    public String seeType()
    {
        if(this.getType() == ProductType.KEYBOARD)
        {
            return "KEYBOARD";
        }

        else if(this.getType() == ProductType.MOUSE)
        {
            return "MOUSE";
        }

        else
        {
            return "MONITOR";
        }
    }

    public String seeStore()
    {
        if (this.isAviable() == true)
        {
            return "DOSTĘPNY";
        }
        else
            return "NIEDOSTĘPNY";
    }

    private final StringProperty name;
    private final IntegerProperty quantiy;



    private final ObjectProperty<ProductType> type;
    private final BooleanProperty aviable;




    public Product(String firstName, int newQuantity, ProductType newType) {
        this.name = new SimpleStringProperty(firstName);
        this.quantiy = new SimpleIntegerProperty(newQuantity);
        type = new SimpleObjectProperty<ProductType>(newType);
        aviable = new SimpleBooleanProperty(true);
    }


}
