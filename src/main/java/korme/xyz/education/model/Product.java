package korme.xyz.education.model;

import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class Product {
    public String name;
    public int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
