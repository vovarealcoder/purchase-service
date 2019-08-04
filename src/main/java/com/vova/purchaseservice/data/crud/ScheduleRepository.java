package com.vova.purchaseservice.data.crud;

import com.vova.purchaseservice.data.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.stream.Stream;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer>, JpaSpecificationExecutor<Schedule> {
    Page<Schedule> getAllByUser_Login(String login, Pageable pageable);

    Optional<Schedule> getScheduleByUser_LoginAndIdSchedule(String login, int idSchedule);

    Stream<Schedule> findAllOrderByIdSchedule();

}
