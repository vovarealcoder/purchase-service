package com.vova.purchaseservice.rest.controller;

import com.vova.purchaseservice.data.model.Schedule;
import com.vova.purchaseservice.data.model.User;
import com.vova.purchaseservice.data.model.enums.ScheduleStatus;
import com.vova.purchaseservice.data.service.ScheduleService;
import com.vova.purchaseservice.data.service.UserService;
import com.vova.purchaseservice.rest.restmodel.request.schedule.CreateScheduleRequest;
import com.vova.purchaseservice.rest.restmodel.request.schedule.EditScheduleRequest;
import com.vova.purchaseservice.rest.restmodel.response.schedule.ScheduleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/service/schedule")
public class ScheduleController {
    private ScheduleService scheduleService;

    private UserService userService;

    @GetMapping("/list")
    public Page<ScheduleItem> getSchedules(@NotNull final Pageable pageable) {
        String userLogin = UserService.getLoginFromSecurityContext();
        Page<Schedule> purchases = scheduleService.getSchedules(pageable, userLogin);
        return ScheduleItem.convertPagable(purchases);
    }

    @PutMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleItem createSchedule(@Valid CreateScheduleRequest request) {
        Schedule purchase = request.toDbScheduled();
        purchase.setStatus(ScheduleStatus.ENABLED);
        User user = userService.getUserByLogin(UserService.getLoginFromSecurityContext());
        purchase.setUser(user);
        return ScheduleItem.fromDbSchedule(scheduleService.create(purchase));
    }

    @GetMapping("/filter")
    public Page<ScheduleItem> filter(@NotNull final Pageable pageable, @RequestParam Map<String, String> params) {
        String userLogin = UserService.getLoginFromSecurityContext();
        Page<Schedule> purchases = scheduleService.filter(pageable, userLogin, params);
        return ScheduleItem.convertPagable(purchases);
    }

    @PostMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public ScheduleItem editSchedule(@NotNull(message = "{validation.id-schedule.nullable}")
                                     @PathVariable("id") int scheduleId,
                                     @Valid EditScheduleRequest request) {
        String login = UserService.getLoginFromSecurityContext();
        Schedule originalPurchase = scheduleService.getByIdAndLogin(scheduleId, login);
        Schedule purchase = request.applyChanges(originalPurchase);
        Schedule changedPurchase = scheduleService.change(purchase);
        return ScheduleItem.fromDbSchedule(changedPurchase);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Integer> deleteSchedule(@NotNull(message = "{validation.id-purchase.nullable}")
                                               @PathVariable("id") int scheduleId) {
        scheduleService.deleteSchedule(scheduleId, UserService.getLoginFromSecurityContext());
        return Collections.singletonMap("deleted", scheduleId);
    }


    @Autowired
    public void setPurchaseService(ScheduleService purchaseService) {
        this.scheduleService = purchaseService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
