package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TempUser {
	@Id
	private String num;
	private String username;
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
