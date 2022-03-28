package com.onlinemed.servises.api;

import com.onlinemed.model.CalendarEvent;

import java.util.List;
import java.util.UUID;

public interface CalendarEventService extends BaseObjectService<CalendarEvent> {
    List<CalendarEvent> getUserEvents(UUID personId);
}
