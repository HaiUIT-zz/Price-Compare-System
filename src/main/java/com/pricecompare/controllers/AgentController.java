package com.pricecompare.controllers;

import com.pricecompare.common.data.entities.AgentLoadMore;
import com.pricecompare.common.data.entities.Attribute;
import com.pricecompare.common.data.entities.Value;
import com.pricecompare.common.data.pojos.AgentDTO;
import com.pricecompare.common.data.pojos.AgentLoadMoreDTO;
import com.pricecompare.common.data.pojos.InputStyle;
import com.pricecompare.common.data.reopsitories.AgentLoadMoreRepository;
import com.pricecompare.common.data.reopsitories.AttributeRepository;
import com.pricecompare.common.data.reopsitories.PlaceHolderRepository;
import com.pricecompare.common.wrappergenerator.PhantomCrawler;
import com.pricecompare.entities.Agent;
import com.pricecompare.repositories.AgentRepository;
import com.pricecompare.utils.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/agent")
public class AgentController
{
    private final AgentRepository agentRepository;
    private final PlaceHolderRepository placeHolderRepository;
    private final AttributeRepository attributeRepository;
    private final AgentLoadMoreRepository agentLoadMoreRepository;
    private EntityManager em;
    private static final String QUERY_PLACHOLDER = "${query}";
    private static final String URL_REGEX = "^www\\..*\\..*$";
    private static final String SEARCH_URL_REGEX = "^www\\..*\\..*\\$\\{query\\}$";

    @Autowired
    public AgentController(AgentRepository agentRepository, PlaceHolderRepository placeHolderRepository,
                           AttributeRepository attributeRepository, AgentLoadMoreRepository agentLoadMoreRepository, EntityManager em)
    {
        this.agentRepository = agentRepository;
        this.placeHolderRepository = placeHolderRepository;
        this.attributeRepository = attributeRepository;
        this.agentLoadMoreRepository = agentLoadMoreRepository;
        this.em = em;
    }

    @RequestMapping(value = { "/"}, method = RequestMethod.GET)
    public String index(Model model)
    {
        try
        {
            //noinspection unchecked,JpaQueryApiInspection
            List<AgentDTO> agents = em.createNamedQuery("agentDTO").getResultList();
            if(agents != null)
            {
                model.addAttribute("agents", agents);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        Utilities.setPageContent(model, "agent-management", "content", "script");
        return "layout_admin";
    }

    @RequestMapping(value = {"/urlchecker"}, method = RequestMethod.POST)
    @ResponseBody
    public String urlCheck()
    {
        List<AgentDTO> agentDTOs = new ArrayList<>();
        boolean flag = false;
        try
        {
            List<Agent> agents = agentRepository.findAll();
            for (Agent agent: agents)
            {
                PhantomCrawler phantom = new PhantomCrawler(agent);
                String searchUrl = phantom.getSearchUrl(agent.getHomePage(), placeHolderRepository.findAll(), generateInputStyle());
                searchUrl = StringUtils.replace(searchUrl, PhantomCrawler.getJUNK_QUERY(), QUERY_PLACHOLDER);
                agent.setSearchUrl(searchUrl);
                agentRepository.save(agent);
                phantom.quit();
            }
            //noinspection unchecked,JpaQueryApiInspection
            agentDTOs = em.createNamedQuery("agentDTO").getResultList();
            if (agentDTOs.size() > 0)
            {
                flag = true;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            flag = false;
        }
        return Utilities.generateJSONwithFlag(agentDTOs, flag);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addAgent(@Valid AgentDTO agentDTO, BindingResult bindingResult)
    {
        try
        {
            if (!agentDTO.getCode().isEmpty() && !agentDTO.getName().isEmpty() &&
                    Utilities.regexMatcher(agentDTO.getName(), URL_REGEX))
            {
                if (agentDTO.getSearchUrl().isEmpty() || Utilities.regexMatcher(agentDTO.getSearchUrl(), SEARCH_URL_REGEX))
                {
                    Agent agent = new Agent();
                    agent.setCode(agentDTO.getCode());
                    agent.setName(agentDTO.getName());
                    agent.setSearchUrl(agentDTO.getSearchUrl());
                    agentRepository.save(agent);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/agent/";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String editAgent(@Valid AgentDTO agentDTO, BindingResult bindingResult)
    {
        try
        {
            if (!agentDTO.getCode().isEmpty() && !agentDTO.getName().isEmpty() &&
                    Utilities.regexMatcher(agentDTO.getName(), URL_REGEX))
            {
                if (agentDTO.getSearchUrl().isEmpty() || Utilities.regexMatcher(agentDTO.getSearchUrl(), SEARCH_URL_REGEX))
                {
                    Agent agent = agentRepository.findOne(agentDTO.getId());
                    agent.setCode(agentDTO.getCode());
                    agent.setName(agentDTO.getName());
                    agent.setSearchUrl(agentDTO.getSearchUrl());
                    agentRepository.save(agent);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/agent/";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteAgent(@PathVariable(name = "id") int id)
    {
        try
        {
            Agent agent = agentRepository.findOne(id);
            agent.setDeleted(true);
            agentRepository.save(agent);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/agent/";
    }

    @RequestMapping(value = {"/crawler"}, method = RequestMethod.GET)
    public String crawlingRule(Model model)
    {
        try
        {
            List<Agent> agents = agentRepository.findAllNotdeleted();
            if(agents != null)
            {
                model.addAttribute("agents", agents);
                model.addAttribute("selected_agent", 1);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        Utilities.setPageContent(model, "agent-crawler", "content", "script");
        return "layout_admin";
    }

    @RequestMapping(value = {"/agentloadmore/get/{agentId}"}, method = RequestMethod.GET)
    @ResponseBody
    public AgentLoadMore getAgentLoadMore(@PathVariable(name = "agentId") int agentId)
    {
        AgentLoadMore agentLoadMore = new AgentLoadMore();
        try
        {
            List<AgentLoadMore> agentLoadMores = agentLoadMoreRepository.findByAgentId(agentId);
            if (agentLoadMores.size() > 0)
            {
                agentLoadMore = agentLoadMores.get(0);
                agentLoadMore.setAgent(null);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return agentLoadMore;
    }

    @RequestMapping(value = {"/agentloadmore/put"}, method = RequestMethod.POST)
    @ResponseBody
    public boolean putAgentLoadMore(@RequestBody AgentLoadMoreDTO agentLoadMoreDTO)
    {
        boolean result = false;
        try
        {
            String method = agentLoadMoreDTO.getMethod() == 1 ? "ajax" : "url";
            AgentLoadMore agentLoadMore = agentLoadMoreRepository.findOne(agentLoadMoreDTO.getId());
            if (agentLoadMore == null)
            {
                agentLoadMore = new AgentLoadMore();
                Agent agent = agentRepository.findOne(agentLoadMoreDTO.getAgentId());
                agentLoadMore.setAgent(agent);
            }
            agentLoadMore.setMethod(method);
            agentLoadMore.setValue(agentLoadMoreDTO.getValue());
            agentLoadMore.setXpath(agentLoadMoreDTO.getPath());
            agentLoadMoreRepository.save(agentLoadMore);
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    private List<InputStyle> generateInputStyle()
    {
        List<InputStyle> inputStyles = new ArrayList<>();
        List<Attribute> attributes = attributeRepository.findAll();
        for (Attribute attribute: attributes)
        {
            for (Value value: attribute.getValues())
            {
                InputStyle temp = new InputStyle();
                temp.setAttribute(attribute.getAttribute());
                temp.setValue(value.getValue());
                inputStyles.add(temp);
            }
        }
        return inputStyles;
    }
}
