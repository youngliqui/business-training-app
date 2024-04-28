package businesstrainingapp.repositories;

import businesstrainingapp.models.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findAllByOrderByDateTimeAsc();

    List<Training> findAllByAvailableIsTrueOrderByDateTimeAsc();

    List<Training> findAllByAvailableTrue();

    boolean existsByIdAndUsersId(Long trainingId, Long userId);
}
