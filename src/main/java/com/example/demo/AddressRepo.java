package com.example.demo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends CrudRepository<AddressDetail, String>{

	AddressDetail findByUsername(String username);

	List<AddressDetail> findAllByUsername(String username);

	Iterable<AddressDetail> findByStatus(String string);

}
