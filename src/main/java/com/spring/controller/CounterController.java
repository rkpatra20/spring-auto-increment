package com.spring.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.dao.CounterDao;

@RestController
@RequestMapping("/next")
public class CounterController {

	@Autowired
	private CounterDao counterDao;
	@RequestMapping("/")
	public Map<String,Object> getNextVal()
	{
		Integer nextVal=counterDao.getNextVal();
		Map<String,Object> map=new HashMap<>(1);
		map.put("nextVal", nextVal);
		return map;
	}
	
}
