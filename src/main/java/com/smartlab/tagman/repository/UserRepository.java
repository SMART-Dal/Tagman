package com.smartlab.tagman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.smartlab.tagman.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	User findByBannerId(String bannerId);
	
	@Query("SELECT u FROM User u WHERE u.isAdmin = 0 AND  u.isInstructor = 0")
	List<User> findAllStudents();

//	@Transactional
//	@Modifying
//	@Query("update User u set u.samplesAnswered = counting WHERE u.id = id")
//	void addUserCount(@Param("id") Long id, @Param("counting") int counting);

	@Modifying
	@Query("SELECT u.samplesAnswered FROM User u WHERE u.id = userId")
	int getCountForUser(@Param("userId") Long userId);

	@Query("SELECT u FROM User u WHERE u.isAdmin = 0 AND u.isInstructor = 0 AND u.bannerId LIKE %:bannerId%")
	List<User> findAppropriateUsers(@Param("bannerId") String bannerId);
	
	@javax.transaction.Transactional
	@Modifying
    @Query("UPDATE User u SET u.samplesAnswered = :samples WHERE u.id = :uId")
    int updateUserCount(@Param("uId") Long companyId, @Param("samples") Integer samples);
	
}
