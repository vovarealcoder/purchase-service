package com.vova.purchaseservice.data.service;

import com.vova.purchaseservice.data.ScheduleSpecificationFactory;
import com.vova.purchaseservice.data.crud.PurchaseRepository;
import com.vova.purchaseservice.data.crud.ScheduleRepository;
import com.vova.purchaseservice.data.crud.UserRepository;
import com.vova.purchaseservice.data.model.Purchase;
import com.vova.purchaseservice.data.model.Schedule;
import com.vova.purchaseservice.data.model.User;
import com.vova.purchaseservice.data.model.enums.PurchaseStatus;
import com.vova.purchaseservice.ex.PurchaseNotFoundException;
import com.vova.purchaseservice.ex.ScheduleNotFoundException;
import com.vova.purchaseservice.ex.UserNotFoundException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class ScheduleService {
    private Logger logger = Logger.getLogger("scheduler-service");
    private ScheduleRepository scheduleRepository;
    private PurchaseRepository purchaseRepository;
    private UserRepository userRepository;

    public Schedule create(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public Schedule getByIdAndLogin(int idSchedule, String login) {
        return scheduleRepository.getScheduleByUser_LoginAndIdSchedule(login, idSchedule)
                .orElseThrow(() -> new ScheduleNotFoundException(login, idSchedule));
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
        Schedule schedule = scheduleRepository.getScheduleByUser_LoginAndIdSchedule(login, idSchedule)
                .orElseThrow(() -> new PurchaseNotFoundException(idSchedule, login));
        scheduleRepository.deleteById(schedule.getIdSchedule());
    }

    @Transactional
    @Scheduled(fixedDelay = 60000L)
    public void schedulePurchases() {
        logger.log(Level.INFO, "starting generate purchases...");
        scheduleRepository.streamAllSchedules().forEach(it -> {
            logger.log(Level.FINE, "analizing schedule " + it);
            Date lastPurchase = ObjectUtils.firstNonNull(it.getLastPurchase(), it.getStartDate());
            Date now = new Date();
            Date future;
            switch (it.getPeriod()) {
                case DAY:
                    future = DateUtils.addDays(lastPurchase, 1);
                    break;
                case WEEK:
                    future = DateUtils.addWeeks(lastPurchase, 1);
                    break;
                case MONTH:
                    future = DateUtils.addMonths(lastPurchase, 1);
                    break;
                case KVARTAL:
                    future = DateUtils.addMonths(lastPurchase, 3);
                    break;
                case YEAR:
                    future = DateUtils.addYears(lastPurchase, 1);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal type of periodic");
            }
            createPurchase(it, now, future);
        });
    }

    private void createPurchase(Schedule schedule, Date now, Date future) {
        Date futureDate = DateUtils.truncate(future, Calendar.DAY_OF_MONTH);
        Date nowDate = DateUtils.truncate(now, Calendar.DAY_OF_MONTH);
        if (futureDate.before(nowDate) || futureDate.equals(nowDate)) {
            Purchase purchase = new Purchase();
            purchase.setCount(schedule.getCount());
            purchase.setComment(schedule.getComment());
            purchase.setPlanPrice(schedule.getPlanPrice());
            purchase.setPlanDate(now);
            purchase.setUser(schedule.getUser());
            purchase.setName(schedule.getName());
            purchase.setStatus(PurchaseStatus.NEW);
            purchaseRepository.save(purchase);
            schedule.setLastPurchase(now);
            scheduleRepository.save(schedule);
            logger.log(Level.FINE, "created purchase " + purchase + " for schedule " + schedule);
        }
    }

    @Autowired
    public void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPurchaseRepository(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }
}
