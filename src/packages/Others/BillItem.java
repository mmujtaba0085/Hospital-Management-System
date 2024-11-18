package packages.Others;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;

public class BillItem {
    private StringProperty service;
    private DoubleProperty amount;

    public BillItem(String service, double amount) {
        this.service = new SimpleStringProperty(service);
        this.amount = new SimpleDoubleProperty(amount);
    }

    public String getService() {
        return service.get();
    }

    public void setService(String service) {
        this.service.set(service);
    }

    public StringProperty serviceProperty() {
        return service;
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public DoubleProperty amountProperty() {
        return amount;
    }
}
