package com.yandex.scooter.data;

public class OrderData {
    private String name;
    private String surname;
    private String address;
    private String metroStation;
    private String phone;
    private String date;
    private String rentalPeriod;
    private String color;
    private String comment;

    public OrderData(String name, String surname, String address, String metroStation,
                     String phone, String date, String rentalPeriod, String color, String comment) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.date = date;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
    }

    // Геттеры
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getMetroStation() { return metroStation; }
    public String getPhone() { return phone; }
    public String getDate() { return date; }
    public String getRentalPeriod() { return rentalPeriod; }
    public String getColor() { return color; }
    public String getComment() { return comment; }

    // Тестовые данные
    public static OrderData getFirstOrder() {
        return new OrderData(
                "Иван",
                "Иванов",
                "ул. Ленина, д. 1",
                "Сокольники",
                "+79123456789",
                "25.12.2024",
                "сутки",
                "black",
                "Позвонить за час"
        );
    }

    public static OrderData getSecondOrder() {
        return new OrderData(
                "Мария",
                "Петрова",
                "пр. Мира, д. 10",
                "Черкизовская",
                "+79098765432",
                "30.12.2024",
                "трое суток",
                "grey",
                "Оставить у двери"
        );
    }
}