import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class TestHealthCareService {
    private PatientInfoFileRepository patientInfoFileRepository;
    private SendAlertService sendAlertService;
    private MedicalService medicalService;
    private PatientInfo patientInfo;
    private static final String ID = UUID.randomUUID().toString();


    @BeforeEach
    void startInformation() {
        patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        sendAlertService = Mockito.mock(SendAlertService.class);
        patientInfo = new PatientInfo("Иван", "Сидоров", LocalDate.of(1978, 5, 26),
                new HealthInfo(new BigDecimal("36.6"), new BloodPressure(120, 80)));
        Mockito.when(patientInfoFileRepository.getById(Mockito.any()))
                .thenReturn(patientInfo);
        medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
    }

    @Test
    public void testBloodPressureIsNotNormal() {
        BloodPressure currentPressure = new BloodPressure(200, 50);
        medicalService.checkBloodPressure(ID, currentPressure);
        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.any());
    }

    @Test
    public void testBloodPressureIsNormal() {
        BloodPressure currentPressure = new BloodPressure(120, 80);
        medicalService.checkBloodPressure(ID, currentPressure);
        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.any());
    }

    @Test
    public void testTemperatureIsNotNormal() {
        BigDecimal currentTemperature = new BigDecimal("33.0");
        medicalService.checkTemperature(ID, currentTemperature);
        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.any());
    }

    @Test
    public void testTemperatureIsNormal() {
        BigDecimal currentTemperature = new BigDecimal("36.6");
        medicalService.checkTemperature(ID, currentTemperature);
        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.any());
    }
}
