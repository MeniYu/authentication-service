package io.incondensable.application.business.repository;

import io.incondensable.application.business.domain.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * @author abbas
 */
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{username: '?0'}")
    Optional<User> findByUsername(String username);

    @Query("{email: '?0'}")
    Optional<User> findByEmail(String email);

    @Query("{ 'token.jwtString': '?0' }")
    Optional<User> findUserByToken(String token);

//    @Query("{}")
//    User findTokenByUserId(@Param("userId") Long userId);

}
