package com.onlinemed.controllers;

import com.blueveery.core.ctrls.FindAllCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.ForumCategory;
import com.onlinemed.servises.api.ForumCategoryService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.onlinemed.model.SystemFunctionalities.FORUM;

/**
 * Class used to define methods that operate on objects of class ForumCategoryCtrl
 */
@RestController
@RequestMapping("/api/forum-category")
@Secured({FORUM})
@JsonScope(positive = true, scope = {ForumCategory.class})
public class ForumCategoryCtrl implements FindAllCtrl<ForumCategory> {

    private final ForumCategoryService forumCategoryService;

    public ForumCategoryCtrl(ForumCategoryService forumCategoryService) {
        this.forumCategoryService = forumCategoryService;
    }

    @Override
    public BaseService<ForumCategory> getService() {
        return this.forumCategoryService;
    }
}
