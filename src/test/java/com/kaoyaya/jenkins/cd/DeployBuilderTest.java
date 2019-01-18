package com.kaoyaya.jenkins.cd;

import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class DeployBuilderTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void testConfig() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(new DeployBuilder());
        project = jenkins.configRoundtrip(project);
        jenkins.assertEqualDataBoundBeans(new DeployBuilder(), project.getBuildersList().get(0));
    }

}