package com.smartlab.tagman.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.smartlab.tagman.model.User;

public interface UserService {
	void save(User user);

	User findByUsername(String username);
	
	User findByBannerId(String bannerId);

	List<User> findAllUsers();
	
//	@Transactional
//	void addEntryForUser(Long userId, int count);

	int getCountForUser(Long userId);

	List<User> findFittingBannerId(String bannerId);

	User findOne(String userId);

	int updateUserCount(Long long1, Long long2);
}
