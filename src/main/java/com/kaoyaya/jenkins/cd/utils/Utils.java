package com.kaoyaya.jenkins.cd.utils;

public class Utils {

    public String UpdateTemplateVersion(String template, String version) {
        String newTemplate = "";
        String[] s = template.split("\n");
        if (s.length > 0) {
            String oldVersion = s[0].replaceAll("\\D", "");
            newTemplate = template.replaceFirst(oldVersion, version);
        }
        return newTemplate;
    }
}
