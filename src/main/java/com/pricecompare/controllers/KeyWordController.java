package com.pricecompare.controllers;

import com.pricecompare.common.data.entities.FormatTag;
import com.pricecompare.common.data.entities.IgnoredTag;
import com.pricecompare.common.data.entities.IgnoredWord;
import com.pricecompare.common.data.reopsitories.FormatTagRepository;
import com.pricecompare.common.data.reopsitories.IgnoredTagRepository;
import com.pricecompare.common.data.reopsitories.IgnoredWordRepository;
import com.pricecompare.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/admin/keyword")
public class KeyWordController
{
    private final FormatTagRepository formatTagRepository;
    private final IgnoredTagRepository ignoredTagRepository;
    private final IgnoredWordRepository ignoredWordRepository;

    @Autowired
    public KeyWordController(FormatTagRepository formatTagRepository, IgnoredTagRepository ignoredTagRepository, IgnoredWordRepository ignoredWordRepository)
    {
        this.formatTagRepository = formatTagRepository;
        this.ignoredTagRepository = ignoredTagRepository;
        this.ignoredWordRepository = ignoredWordRepository;
    }

    @RequestMapping(value = { "/"}, method = RequestMethod.GET)
    public String index(Model model)
    {
        try
        {
            List<IgnoredTag> ignoredTags = ignoredTagRepository.findAll();
            List<IgnoredWord> ignoredWords = ignoredWordRepository.findAll();
            List<FormatTag> formatTags = formatTagRepository.findAll();
            if (ignoredTags != null)
            {
                model.addAttribute("tags", ignoredTags);
            }

            if (ignoredWords != null)
            {
                model.addAttribute("words", ignoredWords);
            }

            if (formatTags != null)
            {
                model.addAttribute("formats", formatTags);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        Utilities.setPageContent(model, "keyword-management","content", "script");
        return "layout_admin";
    }

    @RequestMapping(value = {"/formattags/add"}, method = RequestMethod.POST)
    public String addFormat(FormatTag formatTag)
    {
        try
        {
            formatTagRepository.save(formatTag);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/keyword/";
    }

    @RequestMapping(value = {"/ignoredtags/add"}, method = RequestMethod.POST)
    public String addTag(IgnoredTag ignoredTag)
    {
        try
        {
            ignoredTagRepository.save(ignoredTag);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/keyword/";
    }

    @RequestMapping(value = {"/ignoredwords/add"}, method = RequestMethod.POST)
    public String addWord(IgnoredWord ignoredWord)
    {
        try
        {
            ignoredWordRepository.save(ignoredWord);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/keyword/";
    }

    @RequestMapping(value = {"/formattags/delete/{id}"}, method = RequestMethod.GET)
    public String deleteFormat(@PathVariable(name = "id") int id)
    {
        try
        {
            formatTagRepository.delete(id);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/keyword/";
    }

    @RequestMapping(value = {"/ignoredtags/delete/{id}"}, method = RequestMethod.GET)
    public String deleteTag(@PathVariable(name = "id") int id)
    {
        try
        {
            ignoredTagRepository.delete(id);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/keyword/";
    }

    @RequestMapping(value = {"/ignoredwords/delete/{id}"}, method = RequestMethod.GET)
    public String deleteWord(@PathVariable(name = "id") int id)
    {
        try
        {
            ignoredWordRepository.delete(id);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/keyword/";
    }
}
