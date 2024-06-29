package org.epam.mapper;

import org.epam.dto.trainer.TrainerRegistrationDto;
import org.epam.dto.trainer.TrainerUpdateDto;
import org.epam.entity.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TrainerMapper {

    TrainerMapper trainerMapper = Mappers.getMapper(TrainerMapper.class);

    Trainer toEntity(TrainerRegistrationDto dto);
    Trainer toEntity(TrainerUpdateDto dto);



}