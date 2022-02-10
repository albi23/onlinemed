package com.onlinemed.controllers;

import com.blueveery.core.ctrls.BaseCtrl;
import com.blueveery.core.ctrls.CreateObjectCtrl;
import com.blueveery.core.ctrls.DeleteObjectCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.model.BaseEntity;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.Community;
import com.onlinemed.model.ForumPost;
import com.onlinemed.model.Person;
import com.onlinemed.servises.api.CommunityService;
import com.onlinemed.servises.api.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static com.onlinemed.model.SystemFunctionalities.FORUM;

/**
 * Class used to define methods that operate on objects of class ForumPost
 */
@RestController
@RequestMapping("/api/forum-post")
@Secured({FORUM})
@JsonScope(positive = true, scope = {ForumPost.class})
public class ForumPostCtrl implements BaseCtrl<ForumPost>,
        CreateObjectCtrl<ForumPost>, UpdateObjectCtrl<ForumPost>,
        DeleteObjectCtrl<ForumPost> {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private CommunityService communityService;

    @Override
    public BaseService<ForumPost> getService() {
        return this.forumPostService;
    }

    @RequestMapping(path = "{id}/topic-posts", method = RequestMethod.GET, produces = {"application/json"})
    @JsonScope(positive = true, scope = {Community.class, ForumPost.class, Person.class})
    @ResponseBody
    public List<ForumPost> getPaginatedTopicPosts(
            @PathVariable("id") UUID topicId,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "sortBy", defaultValue = "timestamp", required = false) String sortBy,
            @RequestParam(value = "ascending", required = false, defaultValue = "true") Boolean ascending,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        final List<ForumPost> postFromTopic = this.forumPostService.findPostFromTopic(topicId, sortBy, ascending, pageNumber, pageSize);
        postFromTopic.forEach(post -> post.getPostCreator().getCommunity().touchObject());
        return postFromTopic;
    }

    @RequestMapping(path = "{id}/topic-posts/count", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    public Long getTopicPostCount(
            @PathVariable("id") UUID topicId,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "sortBy", defaultValue = "timestamp", required = false) String sortBy,
            @RequestParam(value = "ascending", required = false, defaultValue = "true") Boolean ascending,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return this.forumPostService.getTopicPostCount(topicId, sortBy, ascending, pageNumber, pageSize);
    }


    @Override
    public ForumPost doCreateObject(ForumPost entity) {
        final Community community = this.communityService.find(entity.getPostCreator().getId());
        community.setComments(community.getComments() + 1);
        this.communityService.merge(community);
        return this.getService().merge(entity);
    }


    @RequestMapping(path = {"/{id}"}, method = {RequestMethod.PUT}, consumes = {"application/json"}, produces = {"application/json"})
    @JsonScope(positive = true, scope = {Community.class, ForumPost.class, Person.class})
    @ResponseBody
    @Transactional
    @Override
    public ForumPost updateObject(@RequestBody BaseEntity entity) {
        return this.getService().merge(((ForumPost) entity));
    }
}
