package com.example.demo.domain.user;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for User entity.
 *
 * @author szigeti.attila96@gmail.com
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByUserName(String userName);

}
