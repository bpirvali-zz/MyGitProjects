package org.madbit.hibernate.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="person")
public class Person {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String first_name;
	
	private String last_name;

	public int getId() {
		return id;
	}

	public void setId(int pid) {
		this.id = pid;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String firstname) {
		this.first_name = firstname;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String lastname) {
		this.last_name = lastname;
	}
}