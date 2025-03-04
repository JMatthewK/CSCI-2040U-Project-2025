public class ClothingItem{
    // Initialize each attribute
    private int id;
    private String name;
    private String brand;
    private String color;
    private String category;
    private double price;
    private String material;
    private String style;
    private String fit;
    private boolean hidden;

    // Constructor
    public ClothingItem(int id, String name, String brand, String color, String category, double price, String material, String style, String fit){
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.color = color;
        this.category = category;
        this.price = price;
        this.material = material;
        this.style = style;
        this.fit = fit;
        this.hidden = false;
    }

    // Getters, no setters yet
    public int getId(){return id;}
    public String getName(){return name;}
    public String getBrand(){return brand;}
    public String getColor(){return color;}
    public String getCategory(){return category;}
    public double getPrice(){return price;}
    public String getMaterial(){return material;}
    public String getStyle(){return style;}
    public String getFit(){return fit;}
    public boolean getIfRemoved(){
        return this.hidden;
    }
    public void hide() {
        this.hidden = true;
    }
    public void unhide() {
        this.hidden = false;
    }
}