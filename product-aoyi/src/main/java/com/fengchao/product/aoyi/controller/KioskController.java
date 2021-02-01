package com.fengchao.product.aoyi.controller;

import com.fengchao.product.aoyi.bean.KioskQueryBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.model.Kiosk;
import com.fengchao.product.aoyi.model.KioskImg;
import com.fengchao.product.aoyi.model.KioskSolt;
import com.fengchao.product.aoyi.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author songbw
 * @date 2021/1/27 7:36
 */
@RestController
@RequestMapping(value = "/kiosk", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KioskController {

    private final KioskService service ;

    @Autowired
    public KioskController(KioskService service) {
        this.service = service;
    }

    @GetMapping
    private OperaResponse find(Integer id) {
        OperaResponse response = service.find(id) ;
        return response;
    }

    @DeleteMapping
    private OperaResponse delete(Integer id) {
        OperaResponse response = service.delete(id) ;
        return response;
    }

    @PostMapping
    private OperaResponse create(@RequestBody Kiosk kiosk) {
        OperaResponse response = service.add(kiosk) ;
        return response ;
    }

    @PutMapping
    private OperaResponse update(@RequestBody Kiosk kiosk) {
        OperaResponse response = service.update(kiosk) ;
        return response ;
    }

    @PostMapping("all")
    private OperaResponse findAll(@RequestBody KioskQueryBean queryBean) {
        OperaResponse response = service.findByPageable(queryBean) ;
        return response ;
    }

    @GetMapping("sync")
    private OperaResponse syncKiosk() {
        OperaResponse response = service.syncFusionKiosk() ;
        return response;
    }

    @GetMapping("/slot/sync")
    private OperaResponse syncKioskSlot(String status) {
        OperaResponse response = service.syncFusionKioskSlot(status) ;
        return response;
    }

    @GetMapping("/slot/kiosk")
    private OperaResponse findKioskSlotByKioskId(String kioskId) {
        OperaResponse response = service.findKioskSlotByKioskId(kioskId) ;
        return response;
    }

    @DeleteMapping("/slot")
    private OperaResponse deleteKioskSlot(Integer id) {
        OperaResponse response = service.deleteKioskSlot(id) ;
        return response;
    }

    @PostMapping("/slot")
    private OperaResponse createKioskSlot(@RequestBody KioskSolt addKioskSlot) {
        OperaResponse response = service.addKioskSlot(addKioskSlot) ;
        return response;
    }

    @PutMapping("/slot")
    private OperaResponse updateKioskSlot(@RequestBody KioskSolt addKioskSlot) {
        OperaResponse response = service.updateKioskSlot(addKioskSlot) ;
        return response;
    }

    @GetMapping("/slot")
    private OperaResponse findKioskSlotBy(Integer id) {
        OperaResponse response = service.findKioskSlot(id) ;
        return response;
    }

    @DeleteMapping("/img")
    private OperaResponse deleteKioskImg(Integer id) {
        OperaResponse response = service.deleteKioskImg(id) ;
        return response;
    }

    @PostMapping("/img")
    private OperaResponse createKioskImg(@RequestBody KioskImg kioskImg) {
        OperaResponse response = service.addKioskImg(kioskImg) ;
        return response;
    }

    @PutMapping("/img")
    private OperaResponse updateKioskImg(@RequestBody KioskImg kioskImg) {
        OperaResponse response = service.updateKioskImg(kioskImg) ;
        return response;
    }

    @DeleteMapping("/img/kiosk")
    private OperaResponse deleteKioskImgByKioskId(Integer kioskId) {
        OperaResponse response = service.deleteKioskImgByKioskId(kioskId) ;
        return response;
    }
}
