package cc.jfire.el.expression.format;

/**
 * 独占一行的 token
 */
public class SingleLineAndAddIndentToken implements FormatToken
{
    final String str;
    final int    addIndent;

    public SingleLineAndAddIndentToken(String str, int addIndent)
    {
        this.str       = str;
        this.addIndent = addIndent;
    }

    @Override
    public void out(StringBuilder builder, FormatContext context)
    {
        int indent            = context.getIndent();
        int lastNotIndentChar = builder.length() - 1 - indent;
        if (lastNotIndentChar > 0)
        {
            if (builder.charAt(lastNotIndentChar) == '\n')
            {
                builder.setLength(lastNotIndentChar+1);
            }
            else
            {
                builder.append("\r\n");
            }
        }
        else
        {
            ;
        }
        indent = context.getIndent();
        for (int i = 0; i < indent; i++)
        {
            builder.append(' ');
        }
        builder.append(str).append("\r\n");
        context.setIndent(context.getIndent() + addIndent);
        indent = context.getIndent();
        for (int i = 0; i < indent; i++)
        {
            builder.append(' ');
        }
        context.setFirst(true);
    }
}
