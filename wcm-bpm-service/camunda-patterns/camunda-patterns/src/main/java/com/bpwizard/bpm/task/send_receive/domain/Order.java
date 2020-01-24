package com.bpwizard.bpm.task.send_receive.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {

	protected String id = UUID.randomUUID().toString();
	protected Customer customer = new Customer();
	protected List<OrderItem> items = new ArrayList<OrderItem>();

	public void addItem(String articleId, int amount) {
		// keep only one item, but increase amount accordingly
		OrderItem existingItem = removeItem(articleId);
		if (existingItem != null) {
			amount += existingItem.getAmount();
		}

		OrderItem item = new OrderItem();
		item.setAmount(amount);
		item.setArticleId(articleId);
		items.add(item);
	}

	public OrderItem removeItem(String articleId) {
		for (OrderItem item : items) {
			if (articleId.equals(item.getArticleId())) {
				items.remove(item);
				return item;
			}
		}
		return null;
	}

	public void addItem(OrderItem i) {
		items.add(i);
	}

	public int getTotalSum() {
		int sum = 0;
		for (OrderItem orderItem : items) {
			sum += orderItem.getAmount();
		}
		return sum;
	}

	public String getId() {
		return id;
	}

	@JsonProperty("orderId")
	public void setId(String id) {
		this.id = id;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", items=" + items + "]";
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
