package com.solofeed.genesis.shared.user.dao;

import com.solofeed.genesis.shared.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByNameOrEmail(String name, String email);
    User findByName(String name);
}
