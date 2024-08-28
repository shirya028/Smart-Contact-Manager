package com.smartContactM.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="user")
public class User {

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private int id;
		
		@NotBlank(message="name field cannot be blank")
		@Size(min=2 , max=30 ,message="length of name should be between 2-30")
		private String name;
		
		@NotBlank(message="password field cannot be blank")
		@Size(min=5  ,message="password should be greater than 5")
		private String password;
		
		
		@Column(unique = true)
		@Pattern(regexp = "^(.+)@(.+)$", message="Invalid email")
		private String email;

		private String role;
		private boolean enabled;
		private String imageUrl;
		@Column(length=500)
		private String description;
		
//		@Override
//		public String toString() {
//			return "User [id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + ", role="
//					+ role + ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", description=" + description
//					+ ", contacts=" + contacts + "]";
//		}
		@OneToMany(cascade = CascadeType.ALL ,fetch =FetchType.LAZY,mappedBy ="user")
		private List<Contact> contacts=new ArrayList<>();
		
		
		public List<Contact> getContacts() {
			return contacts;
		}
		public void setContacts(List<Contact> contacts) {
			this.contacts = contacts;
		}
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
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public boolean isEnabled() {
			return enabled;
		}
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public User() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
}
