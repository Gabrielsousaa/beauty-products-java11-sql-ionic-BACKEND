package com.store.beautyproducts.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class tb_ItemOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @EmbeddedId//é um id embutido em um tipo de auxliar
    private tb_ItemOrderPK id = new tb_ItemOrderPK();

    private Double discount;
    private Integer quantity;
    private Float price;

    public tb_ItemOrder() {
    }

    public tb_ItemOrder(tb_Order order, tb_Product product, Double discount, Integer quantity, Float price) {
        super();
        id.setOrder(order);
        id.setProduct(product);
        this.discount = discount;
        this.quantity = quantity;
        this.price = price;
    }

        public double getSubtotal() {
        return (price - discount) * quantity;
    }
    @JsonIgnore
    public tb_Order getOrder(){
        return id.getOrder();
    }

    public void setOrder(tb_Order order){
        id.setOrder(order);
    }
    

    public tb_Product getProduct(){
        return id.getProduct();
    }

    public void setProduct(tb_Product product){
        id.setProduct(product);
    }

    public tb_ItemOrderPK getId() {
        return id;
    }

    public void setId(tb_ItemOrderPK id) {
        this.id = id;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        tb_ItemOrder other = (tb_ItemOrder) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

}
