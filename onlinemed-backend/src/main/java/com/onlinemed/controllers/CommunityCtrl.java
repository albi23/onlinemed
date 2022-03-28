package com.onlinemed.controllers;

import com.blueveery.core.ctrls.BaseCtrl;
import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.Community;
import com.onlinemed.servises.api.CommunityService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.onlinemed.model.SystemFunctionalities.PROFILE;

/**
 * Class used to define methods that operate on objects of class Community
 */
@RestController
@Secured({PROFILE})
@RequestMapping("/api/community")
@JsonScope(positive = true, scope = {Community.class})
public class CommunityCtrl implements BaseCtrl<Community>, UpdateObjectCtrl<Community>, GetObjectCtrl<Community> {

    private final CommunityService communityService;

    public CommunityCtrl(CommunityService communityService) {
        this.communityService = communityService;
    }

    @Override
    public BaseService<Community> getService() {
        return this.communityService;
    }
}
