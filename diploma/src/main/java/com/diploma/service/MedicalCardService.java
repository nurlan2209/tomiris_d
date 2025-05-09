package com.diploma.service;

import com.diploma.model.MedicalCard;
import com.diploma.model.User;
import com.diploma.model.request.MedicalCardRequest;
import com.diploma.repository.MedicalCardRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class MedicalCardService {

    private final MedicalCardRepository medicalCardRepository;
    private final UserService userService;

    public MedicalCardService(MedicalCardRepository medicalCardRepository, UserService userService) {
        this.medicalCardRepository = medicalCardRepository;
        this.userService = userService;
    }

    public MedicalCard getMedicalCardByUserId(Long userId) {
        User user = userService.getUserById(userId);
        return medicalCardRepository.findByUser(user)
                .orElse(new MedicalCard());
    }

    public MedicalCard createOrUpdateMedicalCard(Long userId, MedicalCardRequest request) {
        User user = userService.getUserById(userId);
        MedicalCard medicalCard = medicalCardRepository.findByUser(user)
                .orElse(new MedicalCard());

        medicalCard.setUser(user);
        medicalCard.setBirthDate(request.getBirthDate());
        medicalCard.setGender(request.getGender());
        medicalCard.setHeight(request.getHeight());
        medicalCard.setWeight(request.getWeight());
        medicalCard.setBloodType(request.getBloodType());
        medicalCard.setAllergies(request.getAllergies());
        medicalCard.setChronicDiseases(request.getChronicDiseases());
        medicalCard.setCurrentMedications(request.getCurrentMedications());
        medicalCard.setMedicalHistory(request.getMedicalHistory());

        return medicalCardRepository.save(medicalCard);
    }

    // Простой метод для предварительного диагноза на основе симптомов и медицинской карты
    public String getPreliminaryDiagnosis(MedicalCard medicalCard, String symptoms) {
        StringBuilder diagnosis = new StringBuilder("Возможные состояния на основе предоставленной информации:\n");
        
        // Анализ ИМТ
        Double bmi = medicalCard.calculateBMI();
        if (bmi != null) {
            diagnosis.append("- ИМТ: ").append(String.format("%.1f", bmi))
                    .append(" - ").append(medicalCard.getBMICategory()).append("\n");
        }
        
        // Проверка возраста для возрастных рисков
        if (medicalCard.getBirthDate() != null) {
            int age = calculateAge(medicalCard.getBirthDate());
            if (age > 60) {
                diagnosis.append("- Повышенные возрастные риски для сердечно-сосудистых заболеваний\n");
            }
        }
        
        // Анализ симптомов (простая имитация)
        if (symptoms != null) {
            String lowerSymptoms = symptoms.toLowerCase();
            
            if (lowerSymptoms.contains("кашель") || lowerSymptoms.contains("жөтел")) {
                diagnosis.append("- Возможно респираторное заболевание (простуда, бронхит, пневмония)\n");
                
                if (medicalCard.getChronicDiseases() != null && 
                    (medicalCard.getChronicDiseases().toLowerCase().contains("астма") || 
                     medicalCard.getChronicDiseases().toLowerCase().contains("бронхит"))) {
                    diagnosis.append("- Высокий риск обострения хронических респираторных заболеваний\n");
                }
            }
            
            if (lowerSymptoms.contains("головная боль") || lowerSymptoms.contains("бас ауру")) {
                diagnosis.append("- Возможно: мигрень, напряжение, гипертония\n");
                
                if (medicalCard.getChronicDiseases() != null && 
                    medicalCard.getChronicDiseases().toLowerCase().contains("гипертон")) {
                    diagnosis.append("- Рекомендуется контроль артериального давления\n");
                }
            }
            
            if (lowerSymptoms.contains("сыпь") || lowerSymptoms.contains("бөртпе") || 
                lowerSymptoms.contains("зуд") || lowerSymptoms.contains("қышу")) {
                diagnosis.append("- Возможно аллергическая реакция или дерматит\n");
                
                if (medicalCard.getAllergies() != null && !medicalCard.getAllergies().isEmpty()) {
                    diagnosis.append("- Рекомендуется проверить связь с известными аллергенами\n");
                }
            }
        }
        
        diagnosis.append("\nВажно: Это предварительная оценка. Для точного диагноза обратитесь к врачу.");
        
        return diagnosis.toString();
    }
    
    private int calculateAge(LocalDate birthDate) {
        return java.time.Period.between(birthDate, java.time.LocalDate.now()).getYears();
    }
}