package com.prs.business.purchaserequest;

import javax.persistence.*;

import com.prs.business.product.Product;

@Entity
public class PurchaseRequestLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="purchaseRequestId")
    private PurchaseRequest purchaseRequest;
    @ManyToOne
    @JoinColumn(name="productId")
    private Product product;
    private int quantity;
    
	public PurchaseRequestLineItem(PurchaseRequest purchaseRequest, Product product, int quantity) {
		super();
		this.purchaseRequest = purchaseRequest;
		this.product = product;
		this.quantity = quantity;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PurchaseRequest getPurchaseRequest() {
        return purchaseRequest;
    }

    public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
        this.purchaseRequest = purchaseRequest;
    }

    public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

	@Override
	public String toString() {
		return "PurchaseRequestLineItem [id=" + id + ", Purchase Request: " + purchaseRequest.getId() + " " + purchaseRequest.getUser() + ", Product: "
				+ product + ", quantity=" + quantity + "]";
	}
    
    
}
