package com.ge.cassandra.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

	@RequestMapping(value="/data", method=RequestMethod.POST)
	public void insertData(){
		
	}
	
	@RequestMapping(value="/data", method=RequestMethod.GET)
	public String getData(){
		
		System.out.println("hello data");
		
		
		return "hello data";
	}
	
	
}
