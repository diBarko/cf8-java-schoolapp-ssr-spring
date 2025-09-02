package gr.aueb.cf.schoolapp.repository;

import gr.aueb.cf.schoolapp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository
        extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {

    // Derived Queries
    List<Teacher> findByRegionId(Long id);
    Optional<Teacher> findByUuid(String uuid);
    Optional<Teacher> findByVat(String vat);
    boolean existsById(Long id);

    // Custom Query - JPQL      -- used for complex queries, containing JOIN and aggregate functions(count, max, avg etc.)
    @Query("SELECT count(*) FROM Teacher t WHERE t.uuid = ?1")
    long getCount(String uuid);
}