package com.futanari.dao;

import com.futanari.model.UserType;

/**
 * Created by Totooria Hyperion on 2017/5/5.
 */
public interface UserTypeMapper {

	Integer insertOne(UserType userType);
	UserType selectByPrimaryKey(Long id);

}
