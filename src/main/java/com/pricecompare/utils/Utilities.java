package com.pricecompare.utils;

import org.springframework.ui.Model;

public class Utilities
{
    public static void setPageContent(Model model, String page, String content, String script)
    {
        model.addAttribute("page", page);
        model.addAttribute("content", content);
        model.addAttribute("script", script);
    }
}
