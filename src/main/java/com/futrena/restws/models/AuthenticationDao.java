package com.futrena.restws.models;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Julien Lu
 */

public interface AuthenticationDao extends CrudRepository<Authentication, Long> 
{
//    List<Authentication> findByEmail(String email);   

    Authentication findByEmail(String email);
}
