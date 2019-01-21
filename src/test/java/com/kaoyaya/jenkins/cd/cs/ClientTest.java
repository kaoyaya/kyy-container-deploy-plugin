package com.kaoyaya.jenkins.cd.cs;

import com.alibaba.fastjson.JSON;
import com.kaoyaya.jenkins.cd.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class ClientTest {

    private Client client;
    private static String TEST_PROJECT_NAME = "api-test";

    @Before
    public void setUp() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("testData/cert.json");
        File file = new File(Objects.requireNonNull(url).getFile());
        String content = FileUtils.readFileToString(file, "UTF-8");
        Cert cert = JSON.parseObject(content, Cert.class);
        client = new Client(cert.getEndPoint(), cert.getCa(), cert.getCert(), cert.getKey());
    }

    @Test
    public void getProjectList() {
        System.out.println(client.getProjectList());
    }

    @Test
    public void getProjectByName() {
        Project project = client.getProjectByName(TEST_PROJECT_NAME);
        String newVersion = Integer.toString(Integer.parseInt(project.getVersion()) + 1);
        System.out.println(new Utils().UpdateTemplateVersion(project.getTemplate(), newVersion));
        System.out.println(project);
    }

    @Test
    public void updateProjectByBlueGreen() {
        Project project = client.getProjectByName(TEST_PROJECT_NAME);
        String newVersion = Integer.toString(Integer.parseInt(project.getVersion()) + 1);
        boolean success = client.updateProjectByBlueGreen(project.getName(),
                new Utils().UpdateTemplateVersion(project.getTemplate(), newVersion),
                newVersion, project.getDescription());
        System.out.println(success);
    }

    @Test
    public void confirmUpdateProject() {
        Project project = client.getProjectByName(TEST_PROJECT_NAME);
        boolean success = client.confirmUpdateProject(project.getName());
        System.out.println(success);
    }
}