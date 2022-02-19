package uz.pdp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.pdp.entity.Outcome;

@Repository
public interface OutcomeRepository extends JpaRepository<Outcome, Integer> {

	List<Outcome> findAllById(Integer id);
}
