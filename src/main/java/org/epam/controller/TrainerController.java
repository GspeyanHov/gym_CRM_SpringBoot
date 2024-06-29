package org.epam.controller;

import org.epam.dto.trainer.TrainerRegistrationDto;
import org.epam.dto.trainer.TrainerUpdateDto;
import org.epam.entity.Trainee;
import org.epam.entity.Trainer;
import org.epam.mapper.TrainerMapper;
import org.epam.service.TrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper = TrainerMapper.trainerMapper;

    TrainerController(TrainerService trainerService){
        this.trainerService = trainerService;
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerTrainerProfile(@RequestBody TrainerRegistrationDto
                                                                                  trainerRegistrationDTO) {

        Trainer createdTrainer = trainerService.create(trainerMapper.toEntity(trainerRegistrationDTO));
        Map<String, String> response = new HashMap<>();
        response.put("username", createdTrainer.getUsername());
        response.put("password", createdTrainer.getPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getTrainerProfile(@PathVariable("username") String username) {
        Optional<Trainer> trainerOpt = trainerService.findTrainerByUsername(username);

        if (!trainerOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Trainer trainer = trainerOpt.get();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("firstName", trainer.getFirstName());
        response.put("lastName", trainer.getLastName());
        response.put("trainingType", trainer.getSpecialization().getTrainingTypeName());
        response.put("isActive", trainer.isActive());

        List<Map<String, Object>> traineesList = new ArrayList<>();
        for (Trainee trainee : trainer.getTrainees()) {
            Map<String, Object> traineeMap = new LinkedHashMap<>();
            traineeMap.put("username", trainee.getUsername());
            traineeMap.put("firstName", trainee.getFirstName());
            traineeMap.put("lastName", trainee.getLastName());
            traineesList.add(traineeMap);
        }
        response.put("trainees", traineesList);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateTrainerProfile(@PathVariable("id") long id,
                                                                    @RequestBody TrainerUpdateDto trainerUpdateDto) {
        Trainer trainer = trainerMapper.toEntity(trainerUpdateDto);
        trainer.setId(id);
        Trainer updatedTrainer = trainerService.update(trainer.getId(), trainer, trainerUpdateDto.getUsername(),
                trainerUpdateDto.getPassword());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("firstName", updatedTrainer.getFirstName());
        response.put("lastName", updatedTrainer.getLastName());
        response.put("username", updatedTrainer.getUsername());
        response.put("trainingType", updatedTrainer.getSpecialization().getTrainingTypeName());
        response.put("isActive", updatedTrainer.isActive());

        List<Map<String, Object>> traineesList = new ArrayList<>();
        if (updatedTrainer.getTrainees() != null) {
            for (Trainee trainee : updatedTrainer.getTrainees()) {
                Map<String, Object> traineeMap = new LinkedHashMap<>();
                traineeMap.put("username", trainee.getUsername());
                traineeMap.put("firstName", trainee.getFirstName());
                traineeMap.put("lastName", trainee.getLastName());
                traineesList.add(traineeMap);
            }
        }
        response.put("trainees", traineesList);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/updateStatus")
    public ResponseEntity<String> updateTrainerStatus(@RequestParam("username") String username,
                                                      @RequestParam("isActive") boolean isActive) {
        try {
            trainerService.setActiveStatus(username, isActive);
            return ResponseEntity.ok("Trainer status updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}