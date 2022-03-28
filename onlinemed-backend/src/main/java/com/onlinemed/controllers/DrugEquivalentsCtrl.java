package com.onlinemed.controllers;

import com.blueveery.core.ctrls.BaseCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.BaseObject;
import com.onlinemed.model.dto.DrugHints;
import com.onlinemed.model.dto.DrugInfo;
import com.onlinemed.servises.api.DrugEquivalentsService;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.onlinemed.model.SystemFunctionalities.DRUG_EQUIVALENTS;

/**
 * Class used to define methods that operate on objects of class DrugHints
 */
@RestController
@RequestMapping("/api/drug-equivalent")
@Secured({DRUG_EQUIVALENTS})
@JsonScope(positive = true, scope = {DrugHints.class})
public class DrugEquivalentsCtrl implements BaseCtrl<BaseObject> {

    private final DrugEquivalentsService drugEquivalentsService;

    public DrugEquivalentsCtrl(DrugEquivalentsService drugEquivalentsService) {
        this.drugEquivalentsService = drugEquivalentsService;
    }

    @Override
    public BaseService<BaseObject> getService() {
        throw new NotImplementedException("Method getService() not implemented in" + this.getClass().getSimpleName());
    }

    @RequestMapping(path = "/hints", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public List<DrugHints> searchDrugHints(@RequestParam(value = "search") String search) {
        return this.drugEquivalentsService.generateEquivalentsHints(search);
    }

    @RequestMapping(path = "/drug-info", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public List<DrugInfo> searchDrugInfo(@RequestParam(value = "url") String url) {
        return this.drugEquivalentsService.generateDrugInfoList(url);
    }


}
