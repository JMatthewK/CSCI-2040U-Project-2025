import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ClothingItem {
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
    private boolean imageLoaded = false;  // Track if image has been loaded
    private static final int MAX_IMAGE_LOAD_ATTEMPTS = 3;  // Max attempts to load image
    private static final int IMAGE_LOAD_DELAY_MS = 100;    // Delay between attempts

    // Constructor
    public ClothingItem(int id, String name, String brand, String color, String category,
                        double price, String material, String style, String fit, String link) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.color = color;
        this.category = category;
        this.price = price;
        this.material = material;
        this.style = style;
        this.fit = fit;
        this.link = link;
        this.hidden = false;
        this.image = new ImageIcon("data/img/placeholder.png"); // Default placeholder
    }

    /**
     * Loads the image for this item with retry logic
     */
    public synchronized void loadImage() {
        if (imageLoaded) return; // Already loaded

        String imagePath = "data/img/" + id + ".png";
        File imageFile = new File(imagePath);

        for (int attempt = 0; attempt < MAX_IMAGE_LOAD_ATTEMPTS; attempt++) {
            if (imageFile.exists()) {
                try {
                    ImageIcon newIcon = new ImageIcon(imagePath);
                    // Force image to load by getting dimensions
                    newIcon.getImage().flush();
                    if (newIcon.getIconWidth() > 0 && newIcon.getIconHeight() > 0) {
                        this.image = newIcon;
                        this.imageLoaded = true;
                        return;
                    }
                } catch (Exception e) {
                    System.err.println("Error loading image (attempt " + (attempt + 1) + "): " + e.getMessage());
                }
            }

            // Wait before retrying if not last attempt
            if (attempt < MAX_IMAGE_LOAD_ATTEMPTS - 1) {
                try {
                    Thread.sleep(IMAGE_LOAD_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        System.out.println("Using placeholder for item " + id);
        this.image = new ImageIcon("data/img/placeholder.png");
        this.imageLoaded = true;
    }

    /**
     * Gets the image, loading it if not already loaded
     */
    public ImageIcon getImage() {
        if (!imageLoaded) {
            loadImage();
        }
        return image;
    }

    /**
     * Manually set the image (for when we know we have a good image)
     */
    public void setImage(ImageIcon image) {
        this.image = image;
        this.imageLoaded = true;
    }

    // Method to check if image exists for the item
    public boolean hasImage() {
        if (!imageLoaded) {
            loadImage();
        }
        return !image.getDescription().equals("data/img/placeholder.png");
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getColor() { return color; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getMaterial() { return material; }
    public String getStyle() { return style; }
    public String getFit() { return fit; }
    public String getlink() { return link; }
    public boolean getIfRemoved() { return hidden; }

    // Setters
    public void setName(String text) { this.name = text; }
    public void setBrand(String text) { this.brand = text; }
    public void setColor(String text) { this.color = text; }
    public void setCategory(String text) { this.category = text; }
    public void setPrice(double v) { this.price = v; }
    public void setMaterial(String text) { this.material = text; }
    public void setStyle(String text) { this.style = text; }
    public void setFit(String text) { this.fit = text; }
    public void setLink(String text) { this.link = text; }
    public void hide() { this.hidden = true; }
    public void unhide() { this.hidden = false; }

    @Override
    public String toString() {
        return id + "," + name + "," + brand + "," + color + "," + category + "," +
                price + "," + material + "," + style + "," + fit + "," + link;
    }
}