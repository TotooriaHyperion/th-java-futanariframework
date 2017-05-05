package com.futanari.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by TotooriaHyperion on 2017/5/5.
 */
@Service
public class UsersServiceImpl implements UsersService {

	private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

	@Autowired
	private UserTypeServiceImpl userTypeService;

	public void testAutowired() {
		logger.info(userTypeService.toString());
	}

}
