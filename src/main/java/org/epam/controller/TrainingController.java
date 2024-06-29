package org.epam.controller;

import org.epam.dto.training.TrainingDto;
import org.epam.mapper.TrainingMapper;
import org.epam.service.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper = TrainingMapper.trainingMapper;

    TrainingController(TrainingService trainingService){
        this.trainingService = trainingService;

    }

    @PostMapping("/add")
    public ResponseEntity<String> addTraining(@RequestBody TrainingDto trainingDto) {
        trainingService.create(trainingMapper.toEntity(trainingDto));
        return ResponseEntity.ok("200 OK");
    }

}