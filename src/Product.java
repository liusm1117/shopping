// 商品类
class Product {
    private int productCode;
    private String name;
    private String manufacturer;
    private String productionDate;
    private String model;
    private double purchasePrice;
    private double price;//零售价
    private int quantity;

    public Product(int productCode, String name, String manufacturer, String productionDate,
                   String model, double purchasePrice, double price, int quantity) {
        this.productCode = productCode;
        this.name = name;
        this.manufacturer = manufacturer;
        this.productionDate = productionDate;
        this.model = model;
        this.purchasePrice = purchasePrice;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductCode() {
        return productCode;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public String getModel() {
        return model;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductCode(String name) {
        this.productCode = productCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "商品编号: " + productCode +
                ", 商品名称: " + name +
                ", 生产厂家: " + manufacturer +
                ", 生产日期: " + productionDate +
                ", 型号: " + model +
                ", 进货价: " + purchasePrice +
                ", 零售价格: " + price +
                ", 数量: " + quantity;
    }
}
