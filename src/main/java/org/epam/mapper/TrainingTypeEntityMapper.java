package org.epam.mapper;

import org.epam.dto.trainingType.TrainingTypeEntityDto;
import org.epam.entity.TrainingTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrainingTypeEntityMapper {

    TrainingTypeEntityMapper trainingTypeMapper = Mappers.getMapper(TrainingTypeEntityMapper.class);

    TrainingTypeEntityDto toDto(TrainingTypeEntity trainingTypeEntity);


}