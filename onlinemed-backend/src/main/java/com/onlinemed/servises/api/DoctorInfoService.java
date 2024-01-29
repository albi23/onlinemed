package com.onlinemed.servises.api;

import com.onlinemed.model.DoctorInfo;

import java.util.List;

public interface DoctorInfoService extends BaseObjectService<DoctorInfo> {

    List<DoctorInfo> findAll2();
}
