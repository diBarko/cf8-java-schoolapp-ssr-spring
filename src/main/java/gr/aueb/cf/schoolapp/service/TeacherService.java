package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.TeacherEditDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.model.static_data.Region;
import gr.aueb.cf.schoolapp.repository.RegionRepository;
import gr.aueb.cf.schoolapp.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j  // log
public class TeacherService implements ITeacherService {

//    private final Logger log = LoggerFactory.getLogger(TeacherService.class);     // This is done by the @Slf4j annotation
    private final TeacherRepository teacherRepository;
    private final RegionRepository regionRepository;
    private final Mapper mapper;


    @Override
//    @Transactional(rollbackOn = Exception.class)
    @Transactional(rollbackOn = {EntityInvalidArgumentException.class, EntityAlreadyExistsException.class })
    public Teacher saveTeacher(TeacherInsertDTO dTO) throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            if (teacherRepository.findByVat(dTO.getVat()).isPresent()) {
                throw new EntityAlreadyExistsException("Teacher", "Teacher with vat [" + dTO.getVat() + "] already exists.");
            }
            Region region = regionRepository.findById(dTO.getRegionId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Region", "Invalid region id."));

            Teacher teacher = mapper.mapToTeacherEntity(dTO);
            region.addTeacher(teacher);
            teacherRepository.save(teacher);
            log.info("Teacher with vat={} was successfully inserted.", teacher.getVat());  // structured logging vat={} - parametrized placeholder design pattern  required by loggers because they work that way.
            return teacher;
        } catch (EntityAlreadyExistsException e) {
            log.error("Save failed for teacher with vat={}. Teacher already exists", dTO.getVat(), e);
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.error("Save failed for teacher with vat={}. Region id={} invalid", dTO.getVat(), dTO.getRegionId(), e);
            throw e;

        }
    }

    @Override
    @Transactional
    public Page<TeacherReadOnlyDTO> getPaginatedTeachers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageable);
        log.trace("Get paginated teachers were returned successfully with page={} and size={}.", page, size);
        return teacherPage.map(mapper::mapToTeacherReadOnlyDTO);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Teacher updateTeacher(TeacherEditDTO dto) throws EntityAlreadyExistsException, EntityInvalidArgumentException, EntityNotFoundException {
        try {
            Teacher teacher = teacherRepository.findByUuid(dto.getUuid())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher", "Teacher not found."));

            if (!teacher.getVat().equals(dto.getVat())) {
                if (teacherRepository.findByVat(dto.getVat()).isEmpty()) {
                    teacher.setVat(dto.getVat());
                } else {
                    throw new EntityAlreadyExistsException("Teacher", "Teacher with vat " + dto.getVat() + " already exists.");
                }
            }

            teacher.setFirstname(dto.getFirstname());
            teacher.setLastname(dto.getLastname());

//            if (!teacher.getRegion().getId().equals(dto.getRegionId())) {         // We use Objects.equals() which is null safe.
            if (!Objects.equals(teacher.getRegion().getId(), dto.getRegionId())) {
                Region region = regionRepository.findById(dto.getRegionId())
                        .orElseThrow(() -> new EntityInvalidArgumentException("Region", "Invalid region id."));
                Region currentRegion = teacher.getRegion();
                if (currentRegion != null) {
//                    currentRegion.removeTeacher(teacher);
                }
                region.addTeacher(teacher);
            }

            teacherRepository.save(teacher);
            log.info("Teacher with vat={} update.", dto.getVat());
            return teacher;
        } catch (EntityNotFoundException e) {
            log.error("Update failed for teacher with vat={}. Entity not found", dto.getVat(), e);
            throw e;
        } catch (EntityAlreadyExistsException e) {
            log.error("Update failed for teacher with vat={}. Entity already exists found", dto.getVat(), e);
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.error("Update failed for teacher with vat={}. Region not found with id={}.", dto.getVat(), dto.getRegionId(), e);
            throw e;
        }
    }
}