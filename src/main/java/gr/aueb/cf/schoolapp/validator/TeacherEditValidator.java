package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.TeacherEditDTO;
import gr.aueb.cf.schoolapp.repository.RegionRepository;
import gr.aueb.cf.schoolapp.repository.TeacherRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class TeacherEditValidator implements Validator {
    private final TeacherRepository teacherRepository;
    private final RegionRepository regionRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherEditDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, Errors errors) {
        TeacherEditDTO editDTO = (TeacherEditDTO) target;


        if (editDTO.getVat() != null && teacherRepository.findByVat(editDTO.getVat()).isPresent()) {
            log.error("Save failed for teacher with vat={}. Teacher already exists.", editDTO.getVat());
            errors.rejectValue("vat", "Το Α.Φ.Μ. του καθηγητή υπάρχει ήδη.");
        }

        if (editDTO.getRegionId() == null || regionRepository.findById(editDTO.getRegionId()).isEmpty()) {
            log.error("Save failed for teacher with vat={}. Region id={} invalid.", editDTO.getVat(), editDTO.getRegionId());
            errors.rejectValue("regionId", "Η περιοχή του καθηγητή δεν μπορεί να είναι κενή.");
        }
    }
}