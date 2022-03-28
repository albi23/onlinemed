package com.onlinemed.controllers.core;

import com.blueveery.core.ctrls.BaseCtrl;
import com.onlinemed.model.BaseObject;
import com.onlinemed.servises.api.BaseObjectService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The interface defines the process of counting all entities in the table
 *
 * @param <E> A class that maps to a database entity
 */
public interface CountAll<E extends BaseObject> extends BaseCtrl<E> {

    @Transactional
    @RequestMapping(
            path = {"/countAll"},
            method = {RequestMethod.GET},
            produces = {"application/json"}
    )
    @ResponseBody
    default Long countAll() {
        return this.doCountAll();
    }

    default Long doCountAll() {
        return ((BaseObjectService<E>) this.getService()).countAll();
    }
}
