package org.jenkinsci.plugins.pipeline.yaml.exceptions;

public class PipelineAsYamlUnexpectedNodeNumber extends RuntimeException {
    public PipelineAsYamlUnexpectedNodeNumber(String nodeName) {
        super(String.format("%s - defined less/more then expected.", nodeName));
    }


}