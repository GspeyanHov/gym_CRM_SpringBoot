package org.epam.controller;

import org.epam.dto.trainingType.TrainingTypeEntityDto;
import org.epam.entity.TrainingTypeEntity;
import org.epam.mapper.TrainingTypeEntityMapper;
import org.epam.service.TrainingTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/training-types")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeEntityMapper trainingTypeEntityMapper = TrainingTypeEntityMapper.trainingTypeMapper;


    TrainingTypeController(TrainingTypeService trainingTypeService){
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping
    public ResponseEntity<List<TrainingTypeEntityDto>> getAllTrainingTypes() {
        List<TrainingTypeEntity> trainingTypes = trainingTypeService.getAllTrainingTypes();
        List<TrainingTypeEntityDto> trainingTypeDtos = new ArrayList<>();

        for (TrainingTypeEntity trainingType : trainingTypes) {
            trainingTypeDtos.add(trainingTypeEntityMapper.toDto(trainingType));
        }

        return ResponseEntity.ok(trainingTypeDtos);
    }
}