package com.futrena.restws.models;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by haoqiao on 15-04-07.
 */


@Transactional
public interface UserDao extends CrudRepository<User, Long> {
    User findByEmail(String email);
}