package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LoginStatus {
	@Id
	private String num;
	private String status;
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
