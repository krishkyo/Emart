package com.example.demo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ItemRepo extends CrudRepository<ItemData, String>{

	ItemData findByName(String name);

}
