package businesstrainingapp.mappers;

import businesstrainingapp.DTO.*;
import businesstrainingapp.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Mapper
public interface TrainingMapper {
    TrainingMapper TRAINING_MAPPER = Mappers.getMapper(TrainingMapper.class);

    @Mapping(target = "trainerName", source = "trainer", qualifiedByName = "mapTrainerToTrainerName")
    TrainingRequestInfoDTO toTrainingRequestInfoDTO(TrainingRequest trainingRequest);

    List<TrainingRequestInfoDTO> toListTrainingRequestsInfoDTO(List<TrainingRequest> trainingRequests);

    @Mapping(target = "dateTime", source = "dateTime", dateFormat = "yyyy-MM-dd HH:mm")
    TrainingRequest toTrainingRequest(ApplicationTrainingDTO trainingDTO);

    @Named("mapTrainerToTrainerName")
    default String mapTrainerToTrainerName(User user) {
        return user.getName();
    }

    Training toTraining(TrainingRequest trainingRequest);

    @Mapping(target = "trainerName", source = "trainer", qualifiedByName = "mapTrainerToTrainerName")
    TrainingInfoDTO toTrainingInfoDTO(Training training);

    List<TrainingInfoDTO> toListTrainingInfoDTO(List<Training> trainings);


    default TrainingInfoForTrainerDTO toTrainingForTrainerDTO(Training training) {
        return TrainingInfoForTrainerDTO.builder()
                .id(training.getId())
                .topic(training.getTopic())
                .branch(training.getBranch())
                .usersAmount(training.getUsersAmount())
                .finance(String.valueOf(training.getPrice() * training.getUsersAmount()))
                .dateTime(String.valueOf(training.getDateTime()))
                .seatsLeft(training.getTotalSeats() - training.getUsersAmount())
                .materials(mapMaterialsToDTO(training.getMaterials()))
                .build();
    }


    @Mapping(target = "link", source = "id", qualifiedByName = "mapMaterialToLink")
    MaterialInfoDTO mapMaterialToDTO(MaterialEntity material);

    List<MaterialInfoDTO> mapMaterialsToDTO(List<MaterialEntity> materials);


    @Named("mapMaterialToLink")
    default String mapMaterialToLink(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/files/materials/" + id)
                .toUriString();
    }

    @Mapping(target = "username", source = "user", qualifiedByName = "mapTrainerToTrainerName")
    @Mapping(target = "link", source = "id", qualifiedByName = "mapHomeworkToLink")
    HomeworkInfoDTO toHomeworkInfoDTO(HomeworkEntity homework);

    List<HomeworkInfoDTO> toListHomeworkInfoDTO(List<HomeworkEntity> homeworks);

    @Named("mapHomeworkToLink")
    default String mapHomeworkToLink(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/files/homeworks/" + id)
                .toUriString();
    }
}
