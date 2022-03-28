package com.onlinemed.controllers;

import com.blueveery.core.ctrls.BaseCtrl;
import com.blueveery.core.ctrls.DeleteObjectCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.CalendarEvent;
import com.onlinemed.servises.api.CalendarEventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Class used to define methods that operate on objects of class CalendarEvent
 */
@RestController
@RequestMapping("/api/calendar-event")
@JsonScope(positive = true, scope = {CalendarEvent.class})
public class CalendarEventCtrl implements BaseCtrl<CalendarEvent>, UpdateObjectCtrl<CalendarEvent>,
        DeleteObjectCtrl<CalendarEvent> {

    private final CalendarEventService calendarEventService;

    public CalendarEventCtrl(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @Override
    public BaseService<CalendarEvent> getService() {
        return this.calendarEventService;
    }

    @RequestMapping(path = {"/user/{id}"}, method = {RequestMethod.GET}, produces = {"application/json"})
    @ResponseBody
    public List<CalendarEvent> getUserEvents(@PathVariable("id") UUID id) {
        return this.calendarEventService.getUserEvents(id);
    }

    @RequestMapping(path = {"/remove-many"}, method = {RequestMethod.POST})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserEvents(@RequestBody List<UUID> removeIdList) {
        removeIdList.forEach(this::doDeleteObject);
    }

    @RequestMapping(path = {"/update-many"}, method = {RequestMethod.POST}, produces = {"application/json"})
    @ResponseBody
    public List<CalendarEvent> updateUserEvents(@RequestBody List<CalendarEvent> events) {
        return events.stream().map(event -> this.calendarEventService.merge(event)).collect(Collectors.toList());
    }

}