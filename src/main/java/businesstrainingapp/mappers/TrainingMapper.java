package businesstrainingapp.mappers;

import businesstrainingapp.DTO.ApplicationTrainingDTO;
import businesstrainingapp.DTO.TrainingInfoDTO;
import businesstrainingapp.DTO.TrainingRequestInfoDTO;
import businesstrainingapp.models.Training;
import businesstrainingapp.models.TrainingRequest;
import businesstrainingapp.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

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
}
