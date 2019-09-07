package com.oip.helpdesk.repository;

import com.oip.helpdesk.domain.entities.Area;
import com.oip.helpdesk.domain.entities.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository

public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    @Query(
            value = "SELECT * FROM parameters WHERE code = ?1",
            nativeQuery = true)
    Parameter findByCode(String code);

}
