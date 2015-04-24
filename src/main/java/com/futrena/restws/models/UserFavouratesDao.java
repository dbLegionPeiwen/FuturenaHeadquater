package com.futrena.restws.models;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface UserFavouratesDao extends CrudRepository<UserFavourates, Long>{
	
	List<UserFavourates> findByEmail(String email);
	UserFavourates findByEmailAndProductID(String email, String productID);
}
