package org.epam.mapper;

import org.epam.dto.training.TrainingDto;
import org.epam.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrainingMapper {

    TrainingMapper trainingMapper = Mappers.getMapper(TrainingMapper.class);

    Training toEntity(TrainingDto dto);

}