package uz.pdp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.User;

@Repository
public interface UserRepository extends  JpaRepository<User, Integer> {

	boolean existsByUserFullName(String name);
	Optional<User> findByUserFullName(String name);
}
