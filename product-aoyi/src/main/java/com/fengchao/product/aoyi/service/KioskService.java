package com.fengchao.product.aoyi.service;

import com.fengchao.product.aoyi.bean.KioskQueryBean;
import com.fengchao.product.aoyi.bean.OperaResponse;
import com.fengchao.product.aoyi.model.Kiosk;
import com.fengchao.product.aoyi.model.KioskSolt;

/**
 * @author songbw
 * @date 2021/1/27 7:18
 */
public interface KioskService {

    OperaResponse findByPageable(KioskQueryBean queryBean) ;

    OperaResponse add(Kiosk kiosk) ;

    OperaResponse update(Kiosk kiosk) ;

    OperaResponse delete(Integer id) ;

    OperaResponse find(Integer id) ;

    OperaResponse syncFusionKiosk() ;

    OperaResponse syncFusionKioskSlot(String status) ;

    OperaResponse findKioskSlotByKioskId(String kioskId) ;

    OperaResponse deleteKioskSlot(Integer id) ;

    OperaResponse addKioskSlot(KioskSolt solt) ;

    OperaResponse updateKioskSlot(KioskSolt solt) ;

    OperaResponse findKioskSlot(Integer id) ;
}
