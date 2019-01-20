package com.kaoyaya.jenkins.cd.cs;

import java.util.Map;

public class Project {

    private String Name; // 应用名称
    private String Description; // 应用描述
    private String Template; // 应用Compose模板
    private String Version; // 应用版本
    private String DesiredState; // 期望状态 （如果当前状态是中间状态时，期望状态指明变迁终态）
    private String CurrentState; // 当前状态
    private Map<String, String> Environment; // 环境变量key/value

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTemplate() {
        return Template;
    }

    public void setTemplate(String template) {
        Template = template;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getDesiredState() {
        return DesiredState;
    }

    public void setDesiredState(String desiredState) {
        DesiredState = desiredState;
    }

    public String getCurrentState() {
        return CurrentState;
    }

    public void setCurrentState(String currentState) {
        CurrentState = currentState;
    }

    public Map<String, String> getEnvironment() {
        return Environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        Environment = environment;
    }

    @Override
    public String toString() {
        return "Project{" +
                "Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", Template='" + Template + '\'' +
                ", Version='" + Version + '\'' +
                ", DesiredState='" + DesiredState + '\'' +
                ", CurrentState='" + CurrentState + '\'' +
                ", Environment=" + Environment +
                '}';
    }
}
