package businesstrainingapp.repositories;

import businesstrainingapp.models.TrainingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRequestRepository extends JpaRepository<TrainingRequest, Long> {
}
