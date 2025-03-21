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
    private String link;
    private ImageIcon image;
    private boolean hidden;

    // Constructor
    public ClothingItem(int id, String name, String brand, String color, String category, double price, String material, String style, String fit, String link){
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
        this.link = link;
    }

    private ImageIcon loadImage(int id){
        String imagePath = "data/img/" + id + ".png";
        File imageFile = new File(imagePath);

        if(imageFile.exists()){
            // System.out.println("Image File exists");
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

    public String getlink(){return link;}
    public boolean getIfRemoved(){
        return this.hidden;
    }
    public void hide() {
        this.hidden = true;
    }
    public void unhide() {
        this.hidden = false;
    }


    @Override
    public String toString() {
        return id + "," + name + "," + brand + "," + color + "," + category + "," + price + "," + material + "," + style + "," + fit + "," + link;
    }


    public void setName(String text) {
        this.name = text;
    }

    public void setBrand(String text) {
        this.brand = text;
    }

    public void setColor(String text) {
        this.color = text;
    }

    public void setCategory(String text) {
        this.category = text;
    }

    public void setPrice(double v) {
        this.price =v;
    }

    public void setMaterial(String text) {
        this.material = text;
    }

    public void setStyle(String text) {
        this.style =text;
    }

    public void setFit(String text) {
        this.fit =text;
    }

    public void setLink(String text) {
        this.link = text;
    }
}
