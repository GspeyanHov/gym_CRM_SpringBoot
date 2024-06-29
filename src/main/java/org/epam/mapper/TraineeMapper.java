package org.epam.mapper;

import org.epam.dto.trainee.TraineeRegistrationDto;
import org.epam.dto.trainee.TraineeUpdateDto;
import org.epam.dto.trainer.TrainerDto;
import org.epam.entity.Trainee;
import org.epam.entity.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TraineeMapper {

    TraineeMapper traineeMapper = Mappers.getMapper(TraineeMapper.class);

    Trainee toEntity(TraineeRegistrationDto dto);

    Trainee toEntity(TraineeUpdateDto dto);

    TrainerDto toDto(Trainer trainer);


}