package com.onlinemed.controllers;

import com.blueveery.core.ctrls.*;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.ForumTopic;
import com.onlinemed.servises.api.ForumTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.onlinemed.model.SystemFunctionalities.FORUM;

/**
 * Class used to define methods that operate on objects of class ForumTopic
 */
@RestController
@RequestMapping("/api/forum-topic")
@Secured({FORUM})
@JsonScope(positive = true, scope = {ForumTopic.class})
public class ForumTopicCtrl implements BaseCtrl<ForumTopic>,
        GetObjectCtrl<ForumTopic>, CreateObjectCtrl<ForumTopic>,
        DeleteObjectCtrl<ForumTopic>, UpdateObjectCtrl<ForumTopic> {

    @Autowired
    private ForumTopicService forumTopicService;

    @Override
    public BaseService<ForumTopic> getService() {
        return this.forumTopicService;
    }

    @RequestMapping(path = "/get-newest", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public List<ForumTopic> getNewestTopics(
            @RequestParam(value = "sortBy", defaultValue = "timestamp", required = false) String sortBy,
            @RequestParam(value = "ascending", required = false, defaultValue = "true") Boolean ascending,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        return this.forumTopicService.findTopics(sortBy, ascending, pageNumber, pageSize);
    }

    @RequestMapping(path = {"/category/{id}"}, method = {RequestMethod.GET}, produces = {"application/json"})
    @ResponseBody
    public List<ForumTopic> getCategoryTopics(@PathVariable("id") UUID id) {
        return this.forumTopicService.getCategoryTopics(id);
    }
}
