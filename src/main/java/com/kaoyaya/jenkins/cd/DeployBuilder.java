package com.kaoyaya.jenkins.cd;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.kaoyaya.jenkins.cd.cs.Client;
import com.kaoyaya.jenkins.cd.cs.Project;
import com.kaoyaya.jenkins.cd.utils.CredentialsListBoxModel;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.ItemGroup;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.security.ACL;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.ListBoxModel;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.docker.commons.credentials.DockerServerCredentials;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.kaoyaya.jenkins.cd.utils.CredentialUtils.lookupSystemCredentials;

public class DeployBuilder extends Builder implements SimpleBuildStep {

    private String endPoint;
    private String credentialsId;
    private String appName;
    private String composeTemplate;

    @DataBoundConstructor
    public DeployBuilder() {
    }

    public String getEndPoint() {
        return endPoint;
    }

    @DataBoundSetter
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAppName() {
        return appName;
    }

    @DataBoundSetter
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getComposeTemplate() {
        return composeTemplate;
    }

    @DataBoundSetter
    public void setComposeTemplate(String composeTemplate) {
        this.composeTemplate = composeTemplate;
    }

    public String getCredentialsId() {
        return credentialsId;
    }

    @DataBoundSetter
    public void setCredentialsId(String credentialsId) {
        this.credentialsId = credentialsId;
    }

    /**
     * build 执行
     *
     * @param run
     * @param filePath
     * @param launcher
     * @param taskListener
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        Credentials credentials = lookupSystemCredentials(credentialsId);
        String compose = this.composeTemplate;
        if (credentials instanceof DockerServerCredentials) {
            final DockerServerCredentials cert = (DockerServerCredentials) credentials;
            Client client = new Client(this.endPoint, cert.getServerCaCertificate(), cert.getClientCertificate(), cert.getClientKey());
            Project project = client.getProjectByName(this.appName);
            if (project.getName().isEmpty()) {
                taskListener.getLogger().printf("应用 %s 不存在%n", this.appName);
            }
            if (this.composeTemplate.contains("$BUILD_NUMBER")) {
                compose = this.composeTemplate.replace("$BUILD_NUMBER", Integer.toString(run.number));
            }
            boolean success = client.updateProjectByBlueGreen(this.appName, compose, Integer.toString(run.number),
                    project.getDescription());
            if (!success) {
                taskListener.getLogger().println("蓝绿更新失败");
            }
            Thread.sleep(10000);
            boolean deploySuccess = client.confirmUpdateProject(this.appName);
            if (deploySuccess) {
                taskListener.getLogger().printf("应用 %s 部署成功%n", this.appName);
            } else {
                taskListener.getLogger().printf("应用 %s 部署失败%n", this.appName);
            }
        }
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public DescriptorImpl() {
            load();
        }

        public ListBoxModel doFillCredentialsIdItems(@AncestorInPath ItemGroup context) {
            List<StandardCredentials> credentials =
                    CredentialsProvider.lookupCredentials(StandardCredentials.class, context, ACL.SYSTEM,
                            Collections.<DomainRequirement>emptyList());
            return new CredentialsListBoxModel()
                    .withEmptySelection()
                    .withMatching(CredentialsMatchers.always(), credentials);
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            save();
            return super.configure(req, formData);
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return "Container Service Deploy";
        }
    }

}
