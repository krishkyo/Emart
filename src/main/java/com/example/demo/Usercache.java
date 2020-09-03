package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Usercache {
	@Id
	private String id;
	private String idnum;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	
}
