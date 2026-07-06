package com.gift.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gift.app.entity.User;

/**
 * Userクラスのリポジトリ
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByName(String name);
}
