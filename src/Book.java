class Book {
    private String name;
    private String author;
    private String category;
    private String genre;
    private double price;
    private int quantity;

    public Book(String name, String author, String category, String genre, double price, int quantity) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.genre = genre;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getGenre() { return genre; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
