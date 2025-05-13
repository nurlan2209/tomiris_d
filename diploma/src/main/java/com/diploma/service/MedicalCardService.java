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
    StringBuilder diagnosis = new StringBuilder("Сіздің симптомдарыңыздың алдыңғы талдауы:\n");
    
    // Анализ ИМТ
    Double bmi = medicalCard.calculateBMI();
    if (bmi != null) {
        diagnosis.append("- ДСИ: ").append(String.format("%.1f", bmi));
        
        // Переводим категории ИМТ на казахский
        String bmiCategory = medicalCard.getBMICategory();
        String bmiCategoryKaz;
        
        if (bmi < 16) {
            bmiCategoryKaz = "Аса қатты салмақ жетіспеушілігі";
        } else if (bmi < 18.5) {
            bmiCategoryKaz = "Салмақ жетіспеушілігі";
        } else if (bmi < 25) {
            bmiCategoryKaz = "Қалыпты дене салмағы";
        } else if (bmi < 30) {
            bmiCategoryKaz = "Артық салмақ";
        } else if (bmi < 35) {
            bmiCategoryKaz = "I дәрежелі семіздік";
        } else if (bmi < 40) {
            bmiCategoryKaz = "II дәрежелі семіздік";
        } else {
            bmiCategoryKaz = "III дәрежелі семіздік";
        }
        
        diagnosis.append(" - ").append(bmiCategoryKaz).append("\n");
    }
    
    // Проверка возраста для возрастных рисков
    if (medicalCard.getBirthDate() != null) {
        int age = calculateAge(medicalCard.getBirthDate());
        if (age > 60) {
            diagnosis.append("- Жүрек-қан тамырлары ауруларына жоғары жас тәуекелдері\n");
        }
    }
    
    // Анализ симптомов (простая имитация)
    if (symptoms != null) {
        String lowerSymptoms = symptoms.toLowerCase();
        
        if (lowerSymptoms.contains("кашель") || lowerSymptoms.contains("жөтел")) {
            diagnosis.append("- Ықтимал респираторлық ауру (суық тию, бронхит, пневмония)\n");
            
            if (medicalCard.getChronicDiseases() != null && 
                (medicalCard.getChronicDiseases().toLowerCase().contains("астма") || 
                 medicalCard.getChronicDiseases().toLowerCase().contains("бронхит"))) {
                diagnosis.append("- Созылмалы респираторлық аурулардың асқыну қаупі жоғары\n");
            }
        }
        
        if (lowerSymptoms.contains("головная боль") || lowerSymptoms.contains("бас ауру")) {
            diagnosis.append("- Ықтимал: мигрень, шаршау, гипертония\n");
            
            if (medicalCard.getChronicDiseases() != null && 
                medicalCard.getChronicDiseases().toLowerCase().contains("гипертон")) {
                diagnosis.append("- Артериялық қысымды бақылау ұсынылады\n");
            }
        }
        
        if (lowerSymptoms.contains("сыпь") || lowerSymptoms.contains("бөртпе") || 
            lowerSymptoms.contains("зуд") || lowerSymptoms.contains("қышу")) {
            diagnosis.append("- Ықтимал аллергиялық реакция немесе дерматит\n");
            
            if (medicalCard.getAllergies() != null && !medicalCard.getAllergies().isEmpty()) {
                diagnosis.append("- Белгілі аллергендермен байланысты тексеру ұсынылады\n");
            }
        }
    }
    
    diagnosis.append("\nМаңызды: Бұл тек сіз берген ақпаратқа негізделген алдын ала талдау. ");
    diagnosis.append("Ол кәсіби медициналық диагнозды алмастырмайды. ");
    diagnosis.append("Егер симптомдар ауыр болса немесе нашарласа, дәрігерге хабарласыңыз.");
    
    return diagnosis.toString();
}
    
    private int calculateAge(LocalDate birthDate) {
        return java.time.Period.between(birthDate, java.time.LocalDate.now()).getYears();
    }
}