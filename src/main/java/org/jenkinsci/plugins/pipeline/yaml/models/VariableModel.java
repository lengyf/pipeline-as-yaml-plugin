package org.jenkinsci.plugins.pipeline.yaml.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jenkinsci.plugins.pipeline.yaml.interfaces.ParsableModelInterface;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VariableModel extends AbstractModel implements ParsableModelInterface {
    private String key;
    private String value;

    @Override
    public String toGroovy() {
        return new StringBuffer()
                .append(this.key)
                .append(this.getVariableOpen())
                .append(this.value)
                .append(this.getVariableClose())
                .toString();
    }
}
