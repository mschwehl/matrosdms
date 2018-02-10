package net.schwehla.matrosdms.domain.search.parser;

import net.schwehla.matrosdms.domain.search.parser.expression.part.BiExpression;
import net.schwehla.matrosdms.domain.search.parser.expression.part.ConstantExpression;
import net.schwehla.matrosdms.domain.search.parser.expression.part.VariableExpression;

public class Sample {

	public static void main(String[] args) {
		
	

//		
//		  MatrosTokenizer tokenizer = new MatrosTokenizer();
//		    tokenizer.add("item", 1);
//		    tokenizer.add("\\.", 2);
//		    
//		    tokenizer.add("=", 3);
//		    tokenizer.add("and", 4);
//		    tokenizer.add("wer", 5);
//		    
//	//	    tokenizer.add("'", 4);
//		    
//		    
//		    tokenizer.add("\\(", 6);
//		    tokenizer.add("\\)", 7);
//		    
//		    tokenizer.add("\\[", 8);
//		    tokenizer.add("\\]", 9);
//		    
//		    
////		    tokenizer.add("\\+|-", 5);
////		    tokenizer.add("\\*|/", 6);
//		    tokenizer.add("[0-9]+",10);			// number
//		    tokenizer.add("[a-zA-Z'][a-zA-Z0-9_']*", 11);
//
//		    try
//		    {
//		      tokenizer.tokenize(" item.name = 'HALLO' and wer=Martin[val=003434]  ");
//
//		      for (MatrosTokenizer.Token tok : tokenizer.getTokens())
//		      {
//	//	        System.out.println("" + tok.token + " " + tok.sequence);
//		      }
//		    }
//		    catch (MatrosParserException e)
//		    {
//		      System.out.println(e.getMessage());
//		    }
//		    
//		    
		    
//			IExpressionNode node = parse(tokenizer);
		    
		//	 System.out.println(node.eval());
		    
		    
		
		BiExpression e = new BiExpression("=");
		e.setLeft( new VariableExpression("name") );
		e.setRight( new ConstantExpression("hallo"));

		BiExpression and = new BiExpression("and");
		and.setLeft(e);
		and.setRight(e);

		

		// System.out.println(and.eval());
//		
//		CONTEXT_ID,
//		DATEARCHIVED,
//		DATECREATED,
//		DATEUPDATED,
//		DESCRIPTION,
//		ICON,NAME
//		,UUID,
//		STAGE,
//		ITEM_ID,
//		CONTEXT_ID,FILE_ID,STORE_STORE_ID,USER_ID,ISSUEDATE,ATTRIBUTE_ID,ATTR_SUBTPYE,DATEARCHIVED,DATECREATED,DATEUPDATED,DESCRIPTION,ICON,NAME,RELEVANCEFROM,RELEVANCETO,UUID,ITEM_ID,ATTRIBUTETYPE_ATTRIBUTETYPE_ID,BOOLEANVALUE,DATEVALUE,INTERNALURL,URL,NUMBERVALUE,TEXTVALUE,ITEM_ID,KATEGORY_ID,CONTEXT_ID,KATEGORY_ID
//		
//	
		
		
	}

//	private static IExpressionNode parse(MatrosTokenizer tokenizer) 
//	{
//		
//	
//		
//		Stack<MatrosTokenizer.Token> s = new Stack<>();
//		Stack<IExpressionNode> node = new Stack<>();
//		
//		java.util.Iterator<MatrosTokenizer.Token> it =  tokenizer.getTokens().iterator();
//		
//		while (it.hasNext()) {
//			
//			MatrosTokenizer.Token tok = it.next();
//			
//			switch(tok.token) {
//			
//			case 3:
//				
//				  BiExpression bi = new BiExpression("=");
//				  bi.setLeft( new VariableExpression(s.pop().sequence));
//				  bi.setRight(new ConstantExpression(it.next().sequence));
//				  
//				  node.push(bi);
//				  
//				  break;
//				  
//				  // and
//			case 4:
//			
//				BiExpression and  = new BiExpression("and");
//				and.setLeft(node.pop());
//				
//	//			result = and;
//				
//				break;
//				
//				default: 
//					s.push(tok);
//			
//			
//			}
//		}
//		
//		return result;
//		
//	}
}
