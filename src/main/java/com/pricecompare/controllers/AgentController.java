package com.pricecompare.controllers;

import com.pricecompare.common.data.entities.Attribute;
import com.pricecompare.common.data.entities.Value;
import com.pricecompare.common.data.pojos.AgentDTO;
import com.pricecompare.common.data.pojos.InputStyle;
import com.pricecompare.common.data.reopsitories.AttributeRepository;
import com.pricecompare.common.data.reopsitories.PlaceHolderRepository;
import com.pricecompare.common.wrappergenerator.PhantomCrawler;
import com.pricecompare.entities.Agent;
import com.pricecompare.repositories.AgentRepository;
import com.pricecompare.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/agent")
public class AgentController
{
    private final AgentRepository agentRepository;
    private final PlaceHolderRepository placeHolderRepository;
    private final AttributeRepository attributeRepository;
    private EntityManager em;

    @Autowired
    public AgentController(AgentRepository agentRepository, PlaceHolderRepository placeHolderRepository, 
                           AttributeRepository attributeRepository, EntityManager em)
    {
        this.agentRepository = agentRepository;
        this.placeHolderRepository = placeHolderRepository;
        this.attributeRepository = attributeRepository;
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
    public List<AgentDTO> urlCheck()
    {
        List<AgentDTO> agentDTOs = null;
        try
        {
            List<Agent> agents = agentRepository.findAll();
            for (Agent agent: agents)
            {
                PhantomCrawler phantom = new PhantomCrawler(agent);
                String searchUrl = phantom.getSearchUrl(agent.getHomePage(), placeHolderRepository.findAll(), generateInputStyle());
                agent.setSearchUrl(searchUrl);
                agentRepository.save(agent);
            }
            //noinspection unchecked,JpaQueryApiInspection
            agentDTOs = em.createNamedQuery("agentDTO").getResultList();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return agentDTOs;
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
