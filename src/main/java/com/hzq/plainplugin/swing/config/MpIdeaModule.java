package com.hzq.plainplugin.swing.config;

import java.io.Serializable;

/**
 * @author Huangzq
 * @description
 * @date 2023/8/4 09:57
 */
public class MpIdeaModule implements Serializable {
    private CodeGeneratorConfig codeGeneratorConfig;
    private FormatConfig formatConfig;

    public CodeGeneratorConfig getCodeGeneratorConfig() {
        return codeGeneratorConfig;
    }

    public void setCodeGeneratorConfig(CodeGeneratorConfig codeGeneratorConfig) {
        this.codeGeneratorConfig = codeGeneratorConfig;
    }

    public FormatConfig getFormatConfig() {
        return formatConfig;
    }

    public void setFormatConfig(FormatConfig formatConfig) {
        this.formatConfig = formatConfig;
    }
}
