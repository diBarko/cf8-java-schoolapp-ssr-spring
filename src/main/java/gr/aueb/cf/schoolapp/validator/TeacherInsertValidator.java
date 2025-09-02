//package gr.aueb.cf.schoolapp.validator;
//
//import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
//import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
//import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
//import gr.aueb.cf.schoolapp.model.static_data.Region;
//import gr.aueb.cf.schoolapp.repository.RegionRepository;
//import gr.aueb.cf.schoolapp.repository.TeacherRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.lang.NonNull;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.Errors;
//import org.springframework.validation.Validator;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class TeacherInsertValidator implements Validator {
//    private final TeacherRepository teacherRepository;
//    private final RegionRepository regionRepository;
//
//    @Override
//    public boolean supports(@NonNull Class<?> clazz) {
//        return TeacherInsertDTO.class == clazz;
//    }
//
//    @Override
//    public void validate(@NonNull Object target,@NonNull Errors errors) {
//        TeacherInsertDTO dTO = (TeacherInsertDTO) target;
//
//        if (teacherRepository.findByVat(dTO.getVat()).isPresent()) {
//            log.error("Save failed for teacher with vat={}. Teacher already exists", dTO.getVat());
//            errors.rejectValue("vat", "Το Α.Φ.Μ. του καθηγητή υπάρχει ήδη.");
//        }
////        if (regionRepository.findById(dTO.getRegionId()).isEmpty()) {
////            log.error("Save failed for teacher with vat={}. Region id={} invalid", dTO.getVat(), dTO.getRegionId());
////            errors.rejectValue("regionId", "Η περιοχή του Καθηγητή δεν μπορεί να είναι κενή.");
////        }
//        if (dTO.getRegionId() == null) {
//            log.error("Save failed for teacher with vat={}. Region id is null", dTO.getVat());
//            errors.rejectValue("regionId", "Η περιοχή του Καθηγητή δεν μπορεί να είναι κενή.");
//        } else if (regionRepository.findById(dTO.getRegionId()).isEmpty()) {
//            log.error("Save failed for teacher with vat={}. Region id={} invalid", dTO.getVat(), dTO.getRegionId());
//            errors.rejectValue("regionId", "Η περιοχή του Καθηγητή δεν μπορεί να είναι κενή.");
//        }
//    }
//}
package gr.aueb.cf.schoolapp.validator;

import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class TeacherInsertValidator implements Validator {

    private static final Logger logger = LoggerFactory.getLogger(TeacherInsertValidator.class);
    private final RegionRepository regionRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherInsertDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TeacherInsertDTO dto = (TeacherInsertDTO) target;

        // Check if regionId is null first
        if (dto.getRegionId() == null) {
            errors.rejectValue("regionId", "regionId.null", "Η περιοχή του Καθηγητή δεν μπορεί να είναι κενή.");
            return;
        }

        // Check if region exists in database
        try {
            if (!regionRepository.existsById(dto.getRegionId())) {
                logger.error("Save failed for teacher with vat={}. Region id={} invalid", dto.getVat(), dto.getRegionId());
                errors.rejectValue("regionId", "regionId.invalid", "Η περιοχή του Καθηγητή δεν μπορεί να είναι κενή.");
            }
        } catch (Exception e) {
            logger.error("Database error when validating region id {}: {}", dto.getRegionId(), e.getMessage());
            errors.rejectValue("regionId", "regionId.databaseError", "Σφάλμα κατά τον έλεγχο της περιοχής.");
        }
    }
}