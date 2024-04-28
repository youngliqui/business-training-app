package businesstrainingapp.repositories;

import businesstrainingapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    List<User> findUsersByIsBlockedTrue();

    @Query(value = "SELECT EXISTS(SELECT trainer_id " +
            "FROM trainings_users " +
            "JOIN trainings ON trainings_users.training_id = trainings.id " +
            "JOIN users ON trainings_users.user_id = users.id " +
            "WHERE users.id = :userId AND trainings.trainer_id = :trainerId)",
            nativeQuery = true)
    boolean existsByTrainingsTrainerIdAndUserId(@Param("userId") Long userId, @Param("trainerId") Long trainerId);
}
