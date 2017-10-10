package com.solofeed.shared.user.dao;

import com.solofeed.shared.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByNameOrEmail(String name, String email);

    User findByPasswordAndNameOrEmail(String password, String name, String email);
}
