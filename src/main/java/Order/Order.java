package Order;

import Product.Product;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity(name="ORDER_TABLE")
public class Order {

    @Id
    @GeneratedValue
    Integer id;
    String street;
    int streetHeight;
    String generalRemarks;
    @OneToMany(fetch= FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ORDER_ID")
    Set<ItemProduct> itemProducts = new HashSet<ItemProduct>();

    public Order(String street, int streetHeight){
        this.street = street;
        this.streetHeight = streetHeight;
    }

    public Order(){};

    public void addItemProduct(ItemProduct itemProduct){
        Optional<ItemProduct> itemWithSameProduct = this.searchItemByProduct(itemProduct.getProduct());

        if(itemWithSameProduct.isPresent()){
            itemWithSameProduct.get().incrementUnits(itemProduct.getUnits());
        }else{
            itemProducts.add(itemProduct);
        }
    }

    private Optional<ItemProduct> searchItemByProduct(Product product){
        return itemProducts.stream().filter(itemProduct -> itemProduct.getProduct() == product)
                .findFirst();
    }

    public double orderPrice(){
        return itemProducts.stream().map(itemProduct -> itemProduct.getPrice()).reduce(0.0, Double::sum);
    }

    public String getStreet() {
        return street;
    }

    public int getStreetHeight() {
        return streetHeight;
    }

    public String getGeneralRemarks() {
        return generalRemarks;
    }

    public Integer getId(){
        return id;
    }

    public void setGeneralRemarks(String generalRemarks) {
        this.generalRemarks = generalRemarks;
    }

    public Set<ItemProduct> getItemProducts() {
        return itemProducts;
    }

    private void setStreet(String street) {
        this.street = street;
    }

    private void setStreetHeight(int streetHeight) {
        this.streetHeight = streetHeight;
    }

    private void setItemProducts(Set<ItemProduct> itemProducts) {
        this.itemProducts = itemProducts;
    }
}
