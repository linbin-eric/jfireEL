package cc.jfire.el.expression.format;

/**
 * 在输出这个 token 后，需要换行的字符
 */
public class NewLineToken implements FormatToken
{
    final String str;

    public NewLineToken(String str) {this.str = str;}

    @Override
    public void out(StringBuilder builder, FormatContext context)
    {
        builder.append(str).append("\r\n");
        int indent = context.getIndent();
        for (int i = 0; i < indent; i++)
        {
            builder.append(' ');
        }
        context.setFirst(true);
    }
}
