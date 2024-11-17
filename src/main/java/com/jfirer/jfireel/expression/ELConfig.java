package com.jfirer.jfireel.expression;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ELConfig
{
    public static final ELConfig DEFAULT_CONFIG                      = new ELConfig();
    /**
     * 实例属性的读取采用编译形式。
     * 在intel平台上，性能和默认的一致；在mac平台上，性能只有默认的一半。
     * 建议是关闭
     */
    private             boolean  propertyReadUseCompile              = false;
    private             boolean  sharePropertyRead                   = false;
    /**
     * 实例方法的调用采用编译形式
     * 在intel平台上性能测试能和通过lambda对方法引用一致，在mac平台上则是lambda性能的三分之二。
     * 建议是开启
     */
    private             boolean  methodInvokeUseCompile              = false;
    /**
     * 使用编译形式进行方法调用时，关闭方法入参值的兼容性转换。
     * 在方法入参值都是正确类型的情况下，这会提升方法调用的性能。但是如果类型不兼容，则会导致运行异常的错误。
     */
    private             boolean  disableCompileMethodCompatibleValue = false;
    private             boolean  mathUseCompile                      = false;
    private             boolean  plusUseCompile                      = false;
    private             boolean  minusUseCompile                     = false;
    private             boolean  multiplyUseCompile                  = false;
    private             boolean  divideUseCompile                    = false;
    private             boolean  remainUseCompile                    = false;
    private             boolean  geUseCompile                        = false;
    private             boolean  gtUseCompile                        = false;
    private             boolean  leUseCompile                        = false;
    private             boolean  ltUseCompile                        = false;
    private             boolean  eqUseCompile                        = false;
    private             boolean  neUseCompile                        = false;
}
