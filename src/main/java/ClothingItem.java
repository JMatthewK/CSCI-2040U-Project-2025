import javax.swing.*;
import java.awt.*;
import java.io.File;

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
    private ImageIcon image;
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
        this.image = loadImage(id);
    }

    private ImageIcon loadImage(int id){
        String imagePath = "data/img/" + id + ".png";
        File imageFile = new File(imagePath);

        if(imageFile.exists()){
            System.out.println("Image File exists");
            return new ImageIcon(imagePath);
        }
        else{
            System.out.println("Image file: " + imagePath + " does not exist");
            return new ImageIcon("data/img/placeholder.png");
        }
    }

    // Method to check if image exists for the item
    public boolean hasImage() {
        // Check if the ImageIcon's image is fully loaded by checking if it's not null and has a valid width/height
        return image.getImage() != null;
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
    public ImageIcon getImage(){return image;}
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
