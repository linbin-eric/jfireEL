package com.jfirer.jfireel.expression;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ELConfig
{
    public static final ELConfig DEFAULT_CONFIG         = new ELConfig();
    private             boolean  propertyReadUseCompile = false;
}
