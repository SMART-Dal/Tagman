package com.smartlab.tagman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartlab.tagman.model.User;
import com.smartlab.tagman.repository.RoleRepository;
import com.smartlab.tagman.repository.UserRepository;

import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findAll()));
		user.setAdmin(false);
		userRepository.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findAllUsers() {
		return userRepository.findAllStudents();

	}

	@Override
	public User findOne(String userId) {
		System.out.println("Repo user Id" + userId);
		return userRepository.getOne(Long.parseLong(userId));
	}

//	@Transactional
//	@Override
//	public void addEntryForUser(Long userId, int count) {
//		userRepository.addUserCount(userId,count);
//	}
//	
	@Override
	public int getCountForUser(Long userId) {
		return userRepository.getCountForUser(userId);
	}

	@Override
	public List<User> findFittingBannerId(String bannerId) {
		// TODO Auto-generated method stub
		return userRepository.findAppropriateUsers("000000");
	}

//	@Override
//	public User findByBannerId(String bannerId) {
//		// TODO Auto-generated method stub
//		return userRepository.findByBannerId(bannerId);
//	}

	@Override
	public int updateUserCount(Long count, Long userId) {
		// TODO Auto-generated method stub
		return userRepository.updateUserCount(userId, new Integer(count.intValue()));
	}

}
