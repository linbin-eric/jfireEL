package cc.jfire.el.expression.format;

/**
 * 常规的输出 Token，需要和前一个输出内容间隔一个空格
 */
public class RegularToken implements FormatToken
{
    private final String str;

    public RegularToken(String str) {this.str = str;}

    @Override
    public void out(StringBuilder builder, FormatContext context)
    {
        int index = builder.length() - 1;
        if (context.isFirst() || (index >= 0 && (builder.charAt(index) == '.' || builder.charAt(index) == ',')))
        {
            builder.append(str);
            context.setFirst(false);
        }
        else
        {
            builder.append(' ').append(str);
        }
    }
}
