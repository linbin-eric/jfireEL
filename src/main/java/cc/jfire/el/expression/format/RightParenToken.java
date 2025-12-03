package cc.jfire.el.expression.format;

public class RightParenToken implements FormatToken
{
    @Override
    public void out(StringBuilder builder, FormatContext context)
    {
        int index = builder.length() - 1;
        if (index >= 0)
        {
            if (builder.charAt(index) == '(')
            {
                builder.append(")");
            }
            else
            {
                builder.append(" )");
            }
        }
        else
        {
            throw new IllegalStateException();
        }
    }
}
