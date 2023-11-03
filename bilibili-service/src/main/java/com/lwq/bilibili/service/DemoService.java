package com.lwq.bilibili.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lwq.bilibili.dao.DemoDao;

@Service
public class DemoService{

 @Autowired
 DemoDao demoDao ;  
 public String query(String name) {
    return demoDao.query(name);
 }
}