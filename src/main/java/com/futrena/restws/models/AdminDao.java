package com.futrena.restws.models;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by haoqiao on 15-04-15.
 */

public interface AdminDao extends CrudRepository<Admin, Long> {

    Admin findByAdminName(String adminName);
}
