package za.co.appwitch.avoapp.models;

public class StockModel {

    String stockQuantity;

    public StockModel() {
    }

    public StockModel(String stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(String stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
