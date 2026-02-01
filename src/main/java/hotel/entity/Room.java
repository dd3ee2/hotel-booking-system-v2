package hotel.entity;

public class Room {
    private int id;
    private int roomNumber;
    private String categoryName;
    private double pricePerNight;

    public Room() {
    }

    public Room(int id, int roomNumber, String categoryName, double pricePerNight) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.categoryName = categoryName;
        this.pricePerNight = pricePerNight;
    }

    public int getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber=" + roomNumber +
                ", categoryName='" + categoryName + '\'' +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
}