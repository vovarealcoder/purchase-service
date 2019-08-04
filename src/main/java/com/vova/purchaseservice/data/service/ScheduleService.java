package com.vova.purchaseservice.data.service;

import com.vova.purchaseservice.data.ScheduleSpecificationFactory;
import com.vova.purchaseservice.data.crud.ScheduleRepository;
import com.vova.purchaseservice.data.crud.UserRepository;
import com.vova.purchaseservice.data.model.Schedule;
import com.vova.purchaseservice.data.model.User;
import com.vova.purchaseservice.ex.PurchaseNotFoundException;
import com.vova.purchaseservice.ex.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class ScheduleService {
    private ScheduleRepository scheduleRepository;
    private UserRepository userRepository;

    public Schedule create(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule getByIdAndLogin(int idSchedule, String login) {
        return scheduleRepository.getScheduleByUser_LoginAndIdSchedule(login, idSchedule)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public Schedule change(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Page<Schedule> getSchedules(Pageable pageable, String login) {
        return scheduleRepository.getAllByUser_Login(login, pageable);
    }

    public Page<Schedule> filter(Pageable pageable, String login, Map<String, String> query) {
        User user = userRepository.findByLogin(login).orElseThrow(() -> new UserNotFoundException(login));
        return scheduleRepository.findAll(ScheduleSpecificationFactory.createFromGetQuery(query, user), pageable);
    }

    public void deleteSchedule(int idSchedule, String login) {
        Schedule purchase = scheduleRepository.getScheduleByUser_LoginAndIdSchedule(login, idSchedule)
                .orElseThrow(() -> new PurchaseNotFoundException(idSchedule, login));
        scheduleRepository.delete(purchase);
    }

    @Transactional
    @Scheduled(fixedDelay = 60000L)
    public void schedulePurchases() {
        scheduleRepository.findAllOrderByIdSchedule().forEach(e -> {
            Date lastPurchase = e.getLastPurchase();
            switch (e.getPeriod()) {
                case DAY:
                    //todo здесь написать создание
            }
        });
    }

    @Autowired
    public void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
