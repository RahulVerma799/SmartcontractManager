package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="CONTACT")
public class Contact {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int Cid;
	private String name;
	private String secondname;
	private String email;
	private String work;
	private String phone;
	//private byte[] image;
	@Column(length=5000)
	private String description;
	
	@ManyToOne
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getCid() {
		return Cid;
	}
	public void setCid(int cid) {
		Cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSecondname() {
		return secondname;
	}
	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
//	public String getImage() {
//		return image;
//	}
//	public void setImage(String image) {
//		this.image = image;
//	}
//	public byte[] getImage() {
//		return image;
//	}
//	public void setImage(byte[] image) {
//		this.image = image;
//	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	@Override
	public String toString() {
		return "Contact [Cid=" + Cid + ", name=" + name + ", secondname=" + secondname + ", email=" + email + ", work="
				+ work + ", phone=" + phone + ", description=" + description + "]";
	}
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
