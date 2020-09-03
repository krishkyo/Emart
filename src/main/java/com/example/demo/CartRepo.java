package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, String>{

	Iterable<Cart> findByIdnum(String string);




	void deleteByidnum(String string);

	
	
}
