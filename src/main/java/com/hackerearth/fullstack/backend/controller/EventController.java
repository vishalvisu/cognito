package com.hackerearth.fullstack.backend.controller;

import com.hackerearth.fullstack.backend.exception.CustomException;
import com.hackerearth.fullstack.backend.model.Event;
import com.hackerearth.fullstack.backend.model.Repo;
import com.hackerearth.fullstack.backend.payload.request.EventRequest;
import com.hackerearth.fullstack.backend.payload.response.EventResponse;
import com.hackerearth.fullstack.backend.repository.EventRepository;
import com.hackerearth.fullstack.backend.repository.RepoRepository;
import com.hackerearth.fullstack.backend.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    RepoRepository repoRepository;

    @PostMapping("/events")
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest){
        Optional<Repo> repoContainer=repoRepository.findById(eventRequest.getRepoId());
        Repo repo;
        if(!repoContainer.isPresent()) {
            repo=new Repo();
            repo.setId(eventRequest.getRepoId());
            repo=repoRepository.save(repo);
        } else{
            repo=repoContainer.get();
        }
        Event event=new Event();

        List<Event> eventList = repo.getEvents();
        
        Long maxx = 1L;
        for (int i=0; i<eventList.size(); i++){
            maxx = Long.max(eventList.get(i).getId(), maxx);
        }

        System.out.println(maxx);
        
        event.setActorId(eventRequest.getActorId());
        event.setType(eventRequest.getType());
        event.setPublic(eventRequest.isPublic());
        event.setRepo(repo);
        event.setId(maxx + 1);
        
        //Your Code here
        
        event=eventRepository.save(event);
        EventResponse eventResponse =new EventResponse();
        
        eventResponse.setId(Math.toIntExact(maxx) + 1);
        eventResponse.setType(eventRequest.getType());
        eventResponse.setPublic(eventRequest.isPublic());
        eventResponse.setRepoId((int) repo.getId());
        eventResponse.setActorId((int) eventRequest.getActorId());
        
        return ResponseEntity.status(Constants.EVENT_CREATED).body(eventResponse);
    }
    @GetMapping("/events")
    public Iterable<EventResponse> getAllEvents(){
        List<EventResponse> list=new ArrayList<>();
        for(Event e:eventRepository.findAll()){
            EventResponse eventResponse=new EventResponse();
            eventResponse.setActorId((int)e.getActorId());
            eventResponse.setRepoId((int)e.getRepo().getId());
            eventResponse.setId((int)e.getId());
            eventResponse.setType(e.getType());
            eventResponse.setPublic(e.isPublic());
            list.add(eventResponse);
        }
        return list;
    }

    @GetMapping("/repos/{repoId}/events")
    public Iterable<EventResponse> getEventsForRepo(@PathVariable long repoId)throws CustomException
    {
        List<EventResponse> list=new ArrayList<>();
        Optional<Repo> optionalRepo=repoRepository.findById(repoId);
        if(!optionalRepo.isPresent()){
            throw new CustomException(Constants.NO_SUCH_REPO_MESSAGE,Constants.NO_SUCH_REPO);
        }
        for(Event e:repoRepository.findById(repoId).get().getEvents()){
            EventResponse eventResponse=new EventResponse();
            eventResponse.setActorId((int)e.getActorId());
            eventResponse.setRepoId((int)e.getRepo().getId());
            eventResponse.setId((int)e.getId());
            eventResponse.setType(e.getType());
            eventResponse.setPublic(e.isPublic());
            list.add(eventResponse);
        }
        return list;
    }

    @GetMapping("/events/{eventId}")
    public EventResponse getEvent(@PathVariable long eventId)throws CustomException
    {
        Optional<Event> eventContainer=eventRepository.findById(eventId);
        if(!eventContainer.isPresent())
            throw new CustomException(Constants.NO_SUCH_EVENT_MESSAGE,Constants.NO_SUCH_EVENT);
        Event e=eventContainer.get();
        EventResponse eventResponse=new EventResponse();
        eventResponse.setActorId((int)e.getActorId());
        eventResponse.setRepoId((int)e.getRepo().getId());
        eventResponse.setId((int)e.getId());
        eventResponse.setType(e.getType());
        eventResponse.setPublic(e.isPublic());
        return eventResponse;
    }

}
