package com.kaoyaya.jenkins.cd.cs;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
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
        System.out.println(client.getProjectByName(TEST_PROJECT_NAME));
    }

    @Test
    public void updateProjectByBlueGreen() {
        Project project = client.getProjectByName(TEST_PROJECT_NAME);
        boolean success = client.updateProjectByBlueGreen(project.getName(), project.getTemplate(), project.getVersion());
        System.out.println(success);
    }

    @Test
    public void confirmUpdateProject() {
        Project project = client.getProjectByName(TEST_PROJECT_NAME);
        boolean success = client.confirmUpdateProject(project.getName());
        System.out.println(success);
    }
}