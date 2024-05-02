package businesstrainingapp.models;

import businesstrainingapp.models.Enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    private static final String SEQ_NAME = "user_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    private String name;
    private String password;
    private String email;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Role role;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Float rating;
    private Integer ratesAmount;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<Training> trainerTrainings;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Training> userTrainings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<HomeworkEntity> homeworks;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Image profileImage;
    private Boolean isBlocked;

    public void addRate(Integer rate) {
        if (rating == null) {
            rating = Float.valueOf(rate);
        } else {
            rating = ((rating * ratesAmount) + rate) / (ratesAmount + 1);
        }
        ratesAmount++;
    }

    public void addUserTraining(Training training) {
        if (userTrainings == null) {
            userTrainings = new ArrayList<>();
        }
        userTrainings.add(training);
    }

    public void addTrainerTraining(Training training) {
        if (trainerTrainings == null) {
            trainerTrainings = new ArrayList<>();
        }
        trainerTrainings.add(training);
    }

    public void addHomework(HomeworkEntity homework) {
        if (homeworks == null) {
            homeworks = new ArrayList<>();
        }
        homeworks.add(homework);
    }
}
