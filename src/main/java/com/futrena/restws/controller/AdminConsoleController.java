package com.futrena.restws.controller;

import com.futrena.restws.models.Admin;
import com.futrena.restws.models.AdminDao;
import com.sun.org.apache.xpath.internal.operations.Bool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by haoqiao on 15-04-15.
 */

@RestController
public class AdminConsoleController {

    @Autowired
    private AdminDao adminDao;

    private String accessCode = "asd";

    @RequestMapping("/consoleAccess")
    @Secured("API")
    public Boolean ConsoleAccess(@RequestBody Admin admin) {


        Admin temp = adminDao.findByAdminName(admin.getAdminName());

        if (temp != null && temp.getPassword().equals(admin.getPassword()) && admin.getAccessCode().equals(this.accessCode))
        {
            return true;
        }

        return false;
    }


}
