package businesstrainingapp.mappers;

import businesstrainingapp.DTO.ProfileUserDTO;
import businesstrainingapp.DTO.UserInfoDTO;
import businesstrainingapp.models.Image;
import businesstrainingapp.models.Training;
import businesstrainingapp.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

//    @Mapping(target = "username", source = "name")
//    @Mapping(target = "trainingArchive", source = "userTrainings", qualifiedByName = "mapTrainingsToTrainingNames")
//    @Mapping(target = "imageLink", source = "profileImage", qualifiedByName = "mapImageToImageLink")
//    ProfileUserDTO toProfileUserDTO(User user);

    default ProfileUserDTO toProfileUserDTO(User user) {
        return ProfileUserDTO.builder()
                .username(user.getName())
                .description(user.getDescription())
                .trainingArchive(getTrainingsArchive(user))
                .rating(user.getRating())
                .imageLink(mapImageToImageLink(user.getProfileImage()))
                .build();
    }

    UserInfoDTO toUserInfoDTO(User user);

    List<UserInfoDTO> toListUserInfoDTO(List<User> users);

    @Named("mapTrainingsToTrainingNames")
    default List<String> mapTrainingsToTrainingNames(List<Training> trainings) {
        return trainings.stream()
                .map(training -> training.getTopic() + " - " + training.getDateTime())
                .collect(Collectors.toList());
    }

    default List<String> getTrainingsArchive(User user) {
        if (user.getRole().name().equals("TRAINER")) {
            return mapTrainingsToTrainingNames(user.getTrainerTrainings());
        } else {
            return mapTrainingsToTrainingNames(user.getUserTrainings());
        }
    }

    @Named("mapImageToImageLink")
    default String mapImageToImageLink(Image image) {
        if (image == null) {
            return null;
        }

        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/images/db/" + image.getFilename())
                .toUriString();
    }
}
