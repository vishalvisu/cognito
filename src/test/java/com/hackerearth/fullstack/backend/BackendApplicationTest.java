package com.hackerearth.fullstack.backend;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerearth.fullstack.backend.exception.ErrorDetails;
import com.hackerearth.fullstack.backend.payload.request.EventRequest;
import com.hackerearth.fullstack.backend.payload.response.EventResponse;
import com.hackerearth.fullstack.backend.repository.*;
import com.hackerearth.fullstack.backend.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BackendApplicationTest extends AbstractTest {

	@Autowired
	EventRepository eventRepository;

	@Autowired
	RepoRepository repoRepository;

    @Override
    @BeforeEach
	public void setUp()throws Exception {
        super.setUp();
    }

	final String addEventsUri="/api/v1/events";

	@Test
	public void createEvent()throws Exception {

		EventRequest eventRequest=new EventRequest();
		eventRequest.setActorId(123);
		eventRequest.setPublic(true);
		eventRequest.setRepoId(12);
		eventRequest.setType("some type");
		Response<EventResponse,Integer> response=performRequest(eventRequest,EventResponse.class,addEventsUri);
		assertEquals(eventRequest.getActorId(),response.getResponse().getActorId());
		assertEquals(eventRequest.getRepoId(),response.getResponse().getRepoId());
		assertEquals(eventRequest.getType(),response.getResponse().getType());
		assertEquals(eventRequest.isPublic(),response.getResponse().isPublic());
		assertEquals(Constants.EVENT_CREATED,response.getResponseCode());

	}

	final String getAllEventsUri="/api/v1/events";
	@Test
	public void getAllEvents()throws Exception {
		createEvent();
		ListResponse<List<EventResponse>,Integer> allEventsResponse=performGetListRequest(getAllEventsUri);
		boolean found=false;
		for(int i=0;i<allEventsResponse.getResponse().size();i++)
		{
			EventResponse eventResponse=new ObjectMapper().convertValue(allEventsResponse.getResponse().get(i), EventResponse.class);
			if(eventResponse.getRepoId()==12&&eventResponse.isPublic()&&eventResponse.getActorId()==123&&eventResponse.getType().equals("some type"))
			{
				found=true;
				break;
			}
		}
		assertTrue(found);
	}

	final String getAllEventsForRepoUri="/api/v1/repos/12/events";
	@Test
	public void getAllEventsForRepo()throws Exception {
		createEvent();
		EventRequest eventRequest=new EventRequest();
		eventRequest.setActorId(1234);
		eventRequest.setPublic(false);
		eventRequest.setRepoId(12);
		eventRequest.setType("other type");
		Response<EventResponse,Integer> response=performRequest(eventRequest,EventResponse.class,addEventsUri);
		assertEquals(eventRequest.getActorId(),response.getResponse().getActorId());
		assertEquals(eventRequest.getRepoId(),response.getResponse().getRepoId());
		assertEquals(eventRequest.getType(),response.getResponse().getType());
		assertEquals(eventRequest.isPublic(),response.getResponse().isPublic());
		assertEquals(Constants.EVENT_CREATED,response.getResponseCode());
		ListResponse<List<EventResponse>,Integer> allEventsResponse=performGetListRequest(getAllEventsForRepoUri);
		boolean found1=false;
		boolean found2=false;
		for(int i=0;i<allEventsResponse.getResponse().size();i++)
		{
			EventResponse eventResponse=new ObjectMapper().convertValue(allEventsResponse.getResponse().get(i), EventResponse.class);
			if(eventResponse.getRepoId()==12&&eventResponse.isPublic()&&eventResponse.getActorId()==123&&eventResponse.getType().equals("some type"))
			{
				found1=true;
			}
			if(eventResponse.getRepoId()==12&&!eventResponse.isPublic()&&eventResponse.getActorId()==1234&&eventResponse.getType().equals("other type"))
			{
				found2=true;
			}
		}
		assertTrue(found1);
		assertTrue(found2);
	}

	final String getAllEventsForRepoFailUri="/api/v1/repos/121/events";
	@Test
	public void getAllEventsForRepoFail()throws Exception {
		Response<ErrorDetails,Integer> response=performGetRequest(ErrorDetails.class,getAllEventsForRepoFailUri);
		assertEquals(Constants.NO_SUCH_REPO,response.getResponseCode());
		assertEquals(Constants.NO_SUCH_REPO_MESSAGE,response.getResponse().getMessage());
	}

	final String getAnEventUri="/api/v1/events/";
	@Test
	public void getAnEvent()throws Exception {
		EventRequest eventRequest=new EventRequest();
		eventRequest.setActorId(1234);
		eventRequest.setPublic(false);
		eventRequest.setRepoId(12);
		eventRequest.setType("other type");
		Response<EventResponse,Integer> response=performRequest(eventRequest,EventResponse.class,addEventsUri);
		Response<EventResponse,Integer> response2=performGetRequest(EventResponse.class,getAnEventUri+response.getResponse().getId());
		assertEquals(eventRequest.getActorId(),response2.getResponse().getActorId());
		assertEquals(eventRequest.getRepoId(),response2.getResponse().getRepoId());
		assertEquals(eventRequest.getType(),response2.getResponse().getType());
		assertEquals(eventRequest.isPublic(),response2.getResponse().isPublic());
		assertEquals(response.getResponse().getId(),response2.getResponse().getId());
		assertEquals(200,response2.getResponseCode());
	}
	@Test
	public void getAnEventFail()throws Exception {
		Response<ErrorDetails,Integer> response=performGetRequest(ErrorDetails.class,getAnEventUri+Integer.MAX_VALUE/2);
		assertEquals(Constants.NO_SUCH_EVENT_MESSAGE,response.getResponse().getMessage());
		assertEquals(Constants.NO_SUCH_EVENT,response.getResponseCode());
	}
}