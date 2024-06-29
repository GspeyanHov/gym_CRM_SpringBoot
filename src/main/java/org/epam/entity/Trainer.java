package org.epam.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@Entity
@Table(name = "trainer")
@SuperBuilder
public class Trainer extends User {

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingTypeEntity specialization;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Trainee> trainees = new HashSet<>();

    @OneToMany(mappedBy = "trainer", fetch = FetchType.EAGER)
    private Set<Training> trainings = new HashSet<>();

}