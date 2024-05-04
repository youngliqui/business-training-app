package businesstrainingapp.repositories;

import businesstrainingapp.models.Training;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findAllByAvailableIsTrueOrderByDateTimeAsc();

    List<Training> findAllByAvailableTrue();

    boolean existsByIdAndUsersId(Long trainingId, Long userId);

    Page<Training> findAllByOrderByDateTimeAsc(Pageable pageable);

    Page<Training> findAllByOrderByDateTimeDesc(Pageable pageable);

    Page<Training> findAllByTrainerNameContainingIgnoreCaseOrderByDateTimeAsc(String trainerName, Pageable pageable);

    Page<Training> findAllByTrainerNameContainingIgnoreCaseOrderByDateTimeDesc(String trainerName, Pageable pageable);

    Page<Training> findAllByTrainerNameContainingIgnoreCase(String trainerName, Pageable pageable);

    Page<Training> findAllByBranchContainingIgnoreCase(String branch, Pageable pageable);

    Page<Training> findAllByBranchContainingIgnoreCaseOrderByDateTimeAsc(String branch, Pageable pageable);

    Page<Training> findAllByBranchContainingIgnoreCaseOrderByDateTimeDesc(String branch, Pageable pageable);

    Page<Training> findAllByTrainerNameContainingIgnoreCaseAndBranchContainingIgnoreCase(
            String trainerName, String branch, Pageable pageable);

    Page<Training> findAllByTrainerNameContainingIgnoreCaseAndBranchContainingIgnoreCaseOrderByDateTimeAsc(
            String trainerName, String branch, Pageable pageable);

    Page<Training> findAllByTrainerNameContainingIgnoreCaseAndBranchContainingIgnoreCaseOrderByDateTimeDesc(
            String trainerName, String branch, Pageable pageable);
}
