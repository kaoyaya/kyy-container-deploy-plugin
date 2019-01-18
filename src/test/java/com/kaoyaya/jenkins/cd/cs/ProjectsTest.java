package com.kaoyaya.jenkins.cd.cs;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import com.alibaba.fastjson.JSON;

public class ProjectsTest {

    @Test
    public void getProjectList() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("testData/cert.json");
        File file = new File(Objects.requireNonNull(url).getFile());
        String content = FileUtils.readFileToString(file, "UTF-8");
        Cert cert = JSON.parseObject(content, Cert.class);
        Projects projects = new Projects(cert.getUrl(), cert.getCa(), cert.getCert(), cert.getKey());
        System.out.println(projects.projectNameList);
    }
}