package com.pricecompare.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.ui.Model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;

public class Utilities
{
    public static void setPageContent(Model model, String page, String content, String script)
    {
        model.addAttribute("page", page);
        model.addAttribute("content", content);
        model.addAttribute("script", script);
    }

    public static boolean regexMatcher(String src, String regex)
    {
        return Pattern.matches(regex, src);
    }

    public static <E> String generateJSONwithFlag(List<E> data, boolean flag)
    {
        Gson gson = new Gson();
        JsonObject finalData = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        finalData.addProperty("success", flag);
        try
        {
            if (data.size() > 0 && flag)
            {
                for (E e: data)
                {
                    JsonObject objJson = new JsonObject();
                    Class<?> eClass = e.getClass();
                    Field[] fields = eClass.getDeclaredFields();
                    for (Field field: fields)
                    {
                        field.setAccessible(true);
                        objJson.add(field.getName(), gson.toJsonTree(field.get(e)));
                    }
                    jsonArray.add(objJson);
                }
                finalData.addProperty("success", true);
                finalData.add("data", jsonArray);
            }
            else
            {
                finalData.addProperty("success", false);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            finalData.addProperty("success", false);
        }
        return gson.toJson(finalData);
    }
}
