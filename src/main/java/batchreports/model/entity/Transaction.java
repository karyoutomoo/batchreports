package batchreports.model.entity;

public class Transaction {
	private Long id;
	private String buyer;
	private String store;
	private String item;
	private Long price;
	
	public Transaction() {
	}
 
	public Transaction(Long id, String buyer, String store, String item, Long price) {
		this.id=id;
		this.buyer=buyer;
		this.store=store;
		this.item=item;
		this.price=price;
	}
	
	public Transaction(Long id, String buyer, String store, String item) {
		this.id=id;
		this.buyer=buyer;
		this.store=store;
		this.item=item;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	@Override
    public String toString() {
        return "Transaction [id=" + id + ", buyer=" + buyer
          + ", store=" + store + ", item=" + item + ", price=" + price + "]";
    }
}
