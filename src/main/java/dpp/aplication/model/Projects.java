package dpp.aplication.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Projects {
	private int id;
	private Date dateOfStart;
	private String description;
	private int price;
	private Date endDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateOfStart() {
		return dateOfStart;
	}
	public void setDateOfStart(Date dateOfStart) {
		this.dateOfStart = dateOfStart;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return "Projects [id=" + id + ", dateOfStart=" + (dateOfStart==null? "NULL":sdf.format(dateOfStart)) + ", description=" + description + ", price="
				+ price + ", endDate=" + (endDate==null? "NULL":sdf.format(endDate)) + "]";
	}
	
	
}
