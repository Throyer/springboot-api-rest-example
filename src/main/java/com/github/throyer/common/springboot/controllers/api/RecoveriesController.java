package com.github.throyer.common.springboot.controllers.api;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.github.throyer.common.springboot.domain.recovery.service.RecoveryConfirmService;
import com.github.throyer.common.springboot.domain.recovery.service.RecoveryService;
import com.github.throyer.common.springboot.domain.recovery.service.RecoveryUpdateService;
import com.github.throyer.common.springboot.domain.recovery.model.RecoveryConfirm;
import com.github.throyer.common.springboot.domain.recovery.model.RecoveryRequest;
import com.github.throyer.common.springboot.domain.recovery.model.RecoveryUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recoveries")
public class RecoveriesController {

    @Autowired
    private RecoveryService recoveryService;
    
    @Autowired
    private RecoveryConfirmService confirmService;
    
    @Autowired
    private RecoveryUpdateService updateService;

    @PostMapping
    @ResponseStatus(NO_CONTENT)
    public void index(@RequestBody RecoveryRequest request) {        
        recoveryService.recovery(request.getEmail());
    }

    @PostMapping("/confirm")
    public void confirm(@RequestBody RecoveryConfirm confirm) {        
        confirmService.confirm(confirm.getEmail(), confirm.getCode());
    }

    @PostMapping("/update")
    @ResponseStatus(NO_CONTENT)
    public void update(@RequestBody RecoveryUpdate update) {        
        updateService.update(update.getEmail(), update.getCode(), update.getPassword());
    }
}
