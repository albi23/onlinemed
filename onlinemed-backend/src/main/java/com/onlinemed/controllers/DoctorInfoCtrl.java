package com.onlinemed.controllers;

import com.blueveery.core.ctrls.BaseCtrl;
import com.blueveery.core.ctrls.FindAllCtrl;
import com.blueveery.core.model.BaseEntity;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.*;
import com.onlinemed.servises.api.DoctorInfoService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.onlinemed.model.SystemFunctionalities.DOCTORS_PROFILE;

/**
 * Class used to define methods that operate on objects of class DoctorInfo
 */
@RestController
@RequestMapping("/api/doctor-info")
@Secured({DOCTORS_PROFILE})
@JsonScope(positive = true, scope = {DoctorInfo.class})
public class DoctorInfoCtrl implements BaseCtrl<DoctorInfo>, FindAllCtrl<DoctorInfo> {

    private final DoctorInfoService doctorInfoServiceImpl;

    public DoctorInfoCtrl(DoctorInfoService doctorInfoServiceImpl) {
        this.doctorInfoServiceImpl = doctorInfoServiceImpl;
    }

    @Override
    public BaseService<DoctorInfo> getService() {
        return this.doctorInfoServiceImpl;
    }

    @RequestMapping(path = {"/{id}"}, method = {RequestMethod.PUT}, consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    @JsonScope(positive = true, scope = {DoctorInfo.class, FacilityLocation.class})
    public DoctorInfo updateObject(@RequestBody BaseEntity entity) {
        final DoctorInfo merge = getService().merge((DoctorInfo) entity);
        merge.getFacilityLocations().forEach(f -> f.getVisitsPriceList().forEach((k, v) -> {
        }));
        return merge;
    }

    @RequestMapping(method = {RequestMethod.GET}, produces = {"application/json"})
//    @JsonScope(positive = true, scope = {Person.class, DoctorInfo.class, FacilityLocation.class, CalendarEvent.class})
    @ResponseBody
    public List<DoctorInfo> findAll() {
//        this.getService().findAll();
//        final List<DoctorInfo> doctorInfos = this.doFindAll();
//        doctorInfos.forEach(d -> {
//            d.getFacilityLocations().forEach(loc -> loc.getVisitsPriceList().size());
//            d.getPerson().getCalendarEvents().forEach(BaseObject::touchObject);
//        });
        return this.getService().findAll();
    }

}
