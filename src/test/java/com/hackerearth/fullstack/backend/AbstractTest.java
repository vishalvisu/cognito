package com.hackerearth.fullstack.backend;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public abstract class AbstractTest {
    protected MockMvc mvc;
    protected char[]token;
    protected boolean setupSuccess;
    protected long userId;

    @Autowired
    WebApplicationContext webApplicationContext;


    protected void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
    public static <E> List<E> mapListFromJSON(final TypeReference<List<E>> type,
                                 final String jsonPacket)throws Exception {
        return new ObjectMapper().convertValue(new ObjectMapper().readValue(jsonPacket, type),new TypeReference<List<E>>() { });
    }
    public class Response<A,B>
    {
        A response;
        B responseCode;
        Response(A response,B responseCode)
        {
            this.response=response;
            this.responseCode=responseCode;
        }

        public A getResponse() {
            return response;
        }

        public void setResponse(A response) {
            this.response = response;
        }

        public B getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(B responseCode) {
            this.responseCode = responseCode;
        }
    }
    public class ListResponse<List,B>
    {
        List response;
        B responseCode;
        ListResponse(List response,B responseCode)
        {
            this.response=response;
            this.responseCode=responseCode;
        }

        public List getResponse() {
            return response;
        }

        public void setResponse(List response) {
            this.response = response;
        }

        public B getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(B responseCode) {
            this.responseCode = responseCode;
        }
    }
    public <T,G> Response<T,Integer> performRequest(G request,Class<T> responseClass,String uri)throws Exception
    {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(request))).andReturn();
        return new Response<>(mapFromJson(result.getResponse().getContentAsString(),responseClass),result.getResponse().getStatus());
    }

    public <T,G> Response<T,Integer> performGetRequest(Class<T> responseClass,String uri)throws Exception
    {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        return new Response<>(mapFromJson(result.getResponse().getContentAsString(),responseClass),result.getResponse().getStatus());
    }

    public <E,G> ListResponse<List<E>,Integer> performListRequest(G request,String uri)throws Exception
    {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(request))).andReturn();
        List<E> properties = mapListFromJSON(new TypeReference<List<E>>() {},result.getResponse().getContentAsString());

        return new ListResponse<>(properties,result.getResponse().getStatus());
    }
    public <E> ListResponse<List<E>,Integer> performGetListRequest(String uri)throws Exception
    {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
        List<E> properties = mapListFromJSON(new TypeReference<List<E>>() {},result.getResponse().getContentAsString());
        return new ListResponse<>(properties,result.getResponse().getStatus());
    }
}