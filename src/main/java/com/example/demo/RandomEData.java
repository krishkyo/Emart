package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RandomEData {
	@Id
	private String n;
	private String num;
	public String getN() {
		return n;
	}
	public void setN(String n) {
		this.n = n;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
}
