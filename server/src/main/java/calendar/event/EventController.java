package calendar.event;

import calendar.event.dto.CreateEventDTO;
import calendar.event.dto.DeleteEventDTO;
import calendar.event.dto.GetEventDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @RequestMapping(method = RequestMethod.GET)
    public GetEventDTO getEvent(@RequestParam(value="get", required = true) String id) {
        EventDAO dao = new EventDAOMongo();
        Event event = dao.getEvent(id);
        return event.toGetEventDTO();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public GetEventDTO createEvent(@RequestBody CreateEventDTO createEventDTO) {
        Event event = new Event(createEventDTO);
        EventDAO dao = new EventDAOMongo();
        dao.createEvent(event);
        return event.toGetEventDTO();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void deleteEvent(@RequestBody DeleteEventDTO deleteEventDTO) {
        EventDAO dao = new EventDAOMongo();
        dao.deleteEvent(deleteEventDTO.getId());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Event updateEvent(@RequestBody Event event) {
        EventDAO dao = new EventDAOMongo();
        return dao.updateEvent(event);
    }
}