package batchreports.model.entity;

public class Transaction {
	private Integer id;
	private String buyer;
	private String store;
	private String item;
	private String price;
	
	public Transaction() {
	}
	
	public Transaction(String buyer, String store, String item) {
		this.buyer=buyer;
		this.store=store;
		this.item=item;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
    public String toString() {
        return "Transaction [id=" + id + ", buyer=" + buyer
          + ", store=" + store + ", item=" + item + ", price=" + price + "]";
    }
}
