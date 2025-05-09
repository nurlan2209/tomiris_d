package com.diploma.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "medical_card")
public class MedicalCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "height")
    private Integer height; // в сантиметрах

    @Column(name = "weight")
    private Integer weight; // в килограммах

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "allergies", length = 1000)
    private String allergies;

    @Column(name = "chronic_diseases", length = 1000)
    private String chronicDiseases;

    @Column(name = "current_medications", length = 1000)
    private String currentMedications;

    @Column(name = "medical_history", length = 2000)
    private String medicalHistory;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(String chronicDiseases) {
        this.chronicDiseases = chronicDiseases;
    }

    public String getCurrentMedications() {
        return currentMedications;
    }

    public void setCurrentMedications(String currentMedications) {
        this.currentMedications = currentMedications;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    // Метод для расчета ИМТ (индекс массы тела)
    public Double calculateBMI() {
        if (height == null || weight == null || height <= 0) {
            return null;
        }
        // Формула: вес (кг) / (рост (м) * рост (м))
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    // Метод для интерпретации ИМТ
    public String getBMICategory() {
        Double bmi = calculateBMI();
        if (bmi == null) {
            return "Нет данных";
        }
        
        if (bmi < 16) {
            return "Выраженный дефицит массы тела";
        } else if (bmi < 18.5) {
            return "Недостаточная масса тела";
        } else if (bmi < 25) {
            return "Нормальная масса тела";
        } else if (bmi < 30) {
            return "Избыточная масса тела (предожирение)";
        } else if (bmi < 35) {
            return "Ожирение I степени";
        } else if (bmi < 40) {
            return "Ожирение II степени";
        } else {
            return "Ожирение III степени";
        }
    }
}