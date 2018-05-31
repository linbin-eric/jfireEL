package com.jfireframework.jfireel.lexer.node.impl;

import java.util.Collection;
import java.util.Map;
import com.jfireframework.baseutil.collection.StringCache;
import com.jfireframework.jfireel.lexer.node.AssociationNode;
import com.jfireframework.jfireel.lexer.node.CalculateNode;
import com.jfireframework.jfireel.lexer.token.CalculateType;
import com.jfireframework.jfireel.lexer.token.Expression;
import com.jfireframework.jfireel.syntax.Syntax;

public class ForEachNode implements AssociationNode
{
	private String			itemName;
	private CalculateNode	collection;
	private Syntax			associationSynTax;
	
	public ForEachNode(String itemName, CalculateNode collection)
	{
		this.itemName = itemName;
		this.collection = collection;
	}
	
	@Override
	public Object calculate(Map<String, Object> variables)
	{
		Object value = collection.calculate(variables);
		if (value == null)
		{
			return null;
		}
		if (value instanceof Collection<?>)
		{
			StringCache cache = new StringCache();
			for (Object each : (Collection<?>) value)
			{
				variables.put(itemName, each);
				cache.append(associationSynTax.calculate(variables));
			}
			variables.remove(itemName);
			return cache.toString();
		}
		else
		{
			throw new IllegalArgumentException("无法支持的foreach类型:" + value.getClass().getName());
		}
	}
	
	@Override
	public CalculateType type()
	{
		return Expression.FOR;
	}
	
	@Override
	public void check()
	{
	}
	
	@Override
	public void setAssociation(Syntax syntax)
	{
		associationSynTax = syntax;
	}
	
}
