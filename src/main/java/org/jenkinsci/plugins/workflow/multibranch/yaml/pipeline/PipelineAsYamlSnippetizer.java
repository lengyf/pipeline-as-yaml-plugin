package org.jenkinsci.plugins.workflow.multibranch.yaml.pipeline;

import hudson.Extension;
import hudson.model.AbstractItem;
import hudson.model.Action;
import jenkins.model.TransientActionFactory;
import jenkins.security.stapler.StaplerAccessibleType;
import org.jenkinsci.plugins.workflow.cps.Snippetizer;
import org.jenkinsci.plugins.workflow.cps.SnippetizerLink;
import org.jenkinsci.plugins.workflow.multibranch.yaml.pipeline.exceptions.PipelineAsYamlEmptyInputException;
import org.jenkinsci.plugins.workflow.multibranch.yaml.pipeline.exceptions.PipelineAsYamlNodeNotFoundException;
import org.jenkinsci.plugins.workflow.multibranch.yaml.pipeline.exceptions.PipelineAsYamlRuntimeException;
import org.jenkinsci.plugins.workflow.multibranch.yaml.pipeline.models.PipelineModel;
import org.jenkinsci.plugins.workflow.multibranch.yaml.pipeline.parsers.PipelineParser;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Extension
@StaplerAccessibleType
public class PipelineAsYamlSnippetizer extends Snippetizer {

    public static final String snippetizerLink = "payConverter";


    @Override
    public String getUrlName() {
        return snippetizerLink;
    }

    @JavaScriptMethod
    public String convertToPay(String pipelineDec) {
        PipelineParser pipelineParser = new PipelineParser();
        try {
            this.checkConverterInput(pipelineDec);
            return "implement";
        }
        catch (PipelineAsYamlEmptyInputException p) {
            return "";
        }
        catch (PipelineAsYamlRuntimeException p) {
            return "Exception happened while converting. Please check the logs";
        }
    }

    @JavaScriptMethod
    public String convertToDec(String pipelinePay) {
        PipelineParser pipelineParser = new PipelineParser();
        try {
            this.checkConverterInput(pipelinePay);
            Optional<PipelineModel> pipelineModel = pipelineParser.parseYaml(pipelinePay);
            if (pipelineModel.isPresent()) {
                return pipelineModel.get().toPrettyGroovy();
            } else {
                throw new PipelineAsYamlRuntimeException("Exception happened while converting. Please check the logs");
            }
        }
        catch (PipelineAsYamlRuntimeException p) {
            return "Exception happened while converting. Please check the logs";
        } catch (PipelineAsYamlNodeNotFoundException e) {
            return ""; //FIXME
        } catch (PipelineAsYamlEmptyInputException e) {
            return ""; //FIXME
        }
    }

    private void checkConverterInput(String converterInput) throws PipelineAsYamlEmptyInputException {
        if(converterInput == null || converterInput.trim().length() == 0)
            throw new PipelineAsYamlEmptyInputException("Input send from Converter Page is empty");
    }

    @Extension
    public static class ActionExtension extends TransientActionFactory<AbstractItem> {

        @Override
        public Class<AbstractItem> type() {
            return AbstractItem.class;
        }

        @Nonnull
        @Override
        public Collection<? extends Action> createFor(@Nonnull AbstractItem abstractItem) {
            return Collections.singleton(new PipelineAsYamlSnippetizer());
        }
    }

    @Extension
    public static class LinkExtension extends SnippetizerLink {

        @Nonnull
        @Override
        public String getUrl() {
            return snippetizerLink;
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.ProjectRecognizer_DisplayName();
        }
    }


}
