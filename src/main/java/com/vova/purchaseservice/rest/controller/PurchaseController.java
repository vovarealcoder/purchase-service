package com.vova.purchaseservice.rest.controller;

import com.vova.purchaseservice.data.model.Purchase;
import com.vova.purchaseservice.data.model.User;
import com.vova.purchaseservice.data.model.enums.PurchaseStatus;
import com.vova.purchaseservice.data.service.PurchaseService;
import com.vova.purchaseservice.data.service.UserService;
import com.vova.purchaseservice.rest.restmodel.request.purchase.CreatePurchaseRequest;
import com.vova.purchaseservice.rest.restmodel.request.purchase.EditPurchaseRequest;
import com.vova.purchaseservice.rest.restmodel.response.purchase.PurchaseItem;
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
@RequestMapping("/service/purchases")
public class PurchaseController {

    private PurchaseService purchaseService;
    private UserService userService;


    @GetMapping("/list")
    public Page<PurchaseItem> getPurchases(@NotNull final Pageable pageable) {
        String userLogin = UserService.getLoginFromSecurityContext();
        Page<Purchase> purchases = purchaseService.getPurchases(pageable, userLogin);
        return PurchaseItem.convertPagable(purchases);
    }

    @GetMapping("/filter")
    public Page<PurchaseItem> filter(@NotNull final Pageable pageable, @RequestParam Map<String, String> params) {
        String userLogin = UserService.getLoginFromSecurityContext();
        Page<Purchase> purchases = purchaseService.getPurchases(pageable, userLogin, params);
        return PurchaseItem.convertPagable(purchases);
    }

    @PutMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseItem createPurchase(@Valid CreatePurchaseRequest request) {
        Purchase purchase = request.toDbPurchase();
        purchase.setStatus(PurchaseStatus.NEW);
        User user = userService.getUserByLogin(UserService.getLoginFromSecurityContext());
        purchase.setUser(user);
        return PurchaseItem.fromDbPurchase(purchaseService.create(purchase));
    }

    @PostMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseItem editPurchase(@NotNull(message = "{validation.id-purchase.nullable}")
                                     @PathVariable("id") int purchaseId,
                                     @Valid EditPurchaseRequest request) {
        String login = UserService.getLoginFromSecurityContext();
        Purchase originalPurchase = purchaseService.getPurchaseByIdAndLogin(purchaseId, login);
        Purchase purchase = request.applyChanges(originalPurchase);
        Purchase changedPurchase = purchaseService.edit(purchase);
        return PurchaseItem.fromDbPurchase(changedPurchase);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Integer> deletePurchase(@NotNull(message = "{validation.id-purchase.nullable}")
                                               @PathVariable("id") int purchaseId) {
        purchaseService.deletePurchase(purchaseId, UserService.getLoginFromSecurityContext());
        return Collections.singletonMap("deleted", purchaseId);
    }


    @Autowired
    public void setPurchaseService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
