package com.futrena.restws.controller;

/**
 * @author haoqiao;
 * @Date 2015/04/24;
 * 
 * UserFavouratesController
 * 
 * API:
 *
 * "/{email}/getFavourates"
 * Return JSON: {List<userFavourates>}
 * 
 * "/{email}/addFavourate"
 * Get JSON: {UserFavourates}
 * Return JSON: {Message}
 * 
 * "/{email}/{productID}/deleteFavourate"
 * Return JSON: {Message} 
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futrena.restws.models.UserFavourates;
import com.futrena.restws.models.UserFavouratesDao;
import com.futrena.restws.response.ResponseMessage;

@RestController
public class UserFavourateController {

	@Autowired
	private UserFavouratesDao userFavouratesDao;

	@RequestMapping("/{email}/getFavourates")
	public ResponseEntity<Object> getFavourates(@PathVariable String email) {

		List<UserFavourates> favourates = null;

		try {

			favourates = userFavouratesDao.findByEmail(email);

		} catch (Exception ex) {
			return new ResponseEntity<Object>(ex, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(favourates, HttpStatus.OK);
	}

	@RequestMapping("/{email}/addFavourate")
	public ResponseEntity<Object> addFavourate(@PathVariable String email,
			@RequestBody UserFavourates favourate) {
		
		try {
			
			userFavouratesDao.save(favourate);

		} catch (Exception ex) {
			return new ResponseEntity<Object>(ex, HttpStatus.OK);
		}
		
		ResponseMessage responseMessage = new ResponseMessage("200","success");
		return new ResponseEntity<Object>(responseMessage, HttpStatus.OK) ;
	}
	
	@RequestMapping("/{email}/{productID}/deleteFavourate")
	public ResponseEntity<Object> deleteFavourate(@PathVariable String email, @PathVariable String productID){
		UserFavourates favourate = new UserFavourates();
		try {
			
			favourate = userFavouratesDao.findByEmailAndProductID(email, productID);
			userFavouratesDao.delete(favourate);
		}
		catch(Exception ex){
			ResponseMessage responseMessage = new ResponseMessage("404","Favourate not found");
			return new ResponseEntity<Object>(responseMessage, HttpStatus.OK);
		}
		ResponseMessage responseMessage = new ResponseMessage("200","success");
		return new ResponseEntity<Object>(responseMessage, HttpStatus.OK) ;
	}
	
	@RequestMapping("/{email}/{productID}/check")
	public ResponseEntity<Object> Check(@PathVariable String email, @PathVariable String productID){
		
		try {		
			UserFavourates favourate = userFavouratesDao.findByEmailAndProductID(email, productID);
			return new ResponseEntity<Object>(favourate, HttpStatus.OK) ;
		}
		catch(Exception ex){
			return new ResponseEntity<Object>(ex, HttpStatus.OK);
		}
		
		
	}

}
