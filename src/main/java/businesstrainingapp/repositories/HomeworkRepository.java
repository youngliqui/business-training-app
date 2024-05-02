package businesstrainingapp.repositories;

import businesstrainingapp.models.HomeworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<HomeworkEntity, Long> {
    List<HomeworkEntity> findAllByTrainingIdAndUserId(Long trainingId, Long userId);
    List<HomeworkEntity> findAllByTrainingId(Long trainingId);
}
