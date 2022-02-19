package uz.pdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

	boolean existsByUserNameOrNumber(String userName, Integer number);
}
