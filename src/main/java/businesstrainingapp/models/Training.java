package businesstrainingapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trainings")
public class Training {

    private static final String SEQ_NAME = "training_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String topic;
    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private LocalDateTime dateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "trainings_users",
            joinColumns = @JoinColumn(name = "training_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private List<MaterialEntity> materials;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private List<HomeworkEntity> homeworks;

    private float price;
    private int usersAmount;
    private int totalSeats;
    private boolean available;

    public void addUser(User user) {
        if (users == null) {
            users = new ArrayList<>();
        }

        users.add(user);
        usersAmount++;
    }

    public void deleteUser(User user) {
        users.remove(user);
        usersAmount--;
    }

    public void addMaterial(MaterialEntity materialEntity) {
        if (materialEntity == null) {
            materials = new ArrayList<>();
        }
        materials.add(materialEntity);
    }

    public void addHomework(HomeworkEntity homework) {
        if (homeworks == null) {
            homeworks = new ArrayList<>();
        }
        homeworks.add(homework);
    }
}
