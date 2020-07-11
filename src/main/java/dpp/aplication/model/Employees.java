package dpp.aplication.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Employees {
	private int id;
	private String name;
	private String surname;
	private String phone;
	private String phoneICE;
	private Date birthday;
	private int salary;
	private boolean isEmployeeIdInWorkDone;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoneICE() {
		return phoneICE;
	}
	public void setPhoneICE(String phoneICE) {
		this.phoneICE = phoneICE;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public boolean isEmployeeIdInWorkDone() {
		return isEmployeeIdInWorkDone;
	}
	public void setEmployeeIdInWorkDone(boolean isEmployeeIdInWorkDone) {
		this.isEmployeeIdInWorkDone = isEmployeeIdInWorkDone;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return "Employees [id=" + id + ", name=" + name + ", surname=" + surname + ", phone=" + phone + ", phoneICE="
				+ phoneICE + ", birthday=" + (birthday==null? "NULL":sdf.format(birthday)) + ", salary=" + salary + "]";
	}
	
	
	
}
