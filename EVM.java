+	import java.util.*;
+	import Utilities.Error;
+	import OperandStack.*;
+	import Value.*;
+	public class EVM {
+	  public static void main(String argv[]) {
+	
+	      // This is a small example of how to use the stack
+	      
+	      OperandStack operandStack = new OperandStack(100, "OperandStack Test");
+	
+	      // we have an expression line 32 - 7 * (54 + 32) / 2 we can translate that into postfix 
+	      // like this: 32 7 54 32 + * 2 / -
+	
+	      operandStack.push(new IntegerValue(32));
+	      operandStack.push(new IntegerValue(7));
+	      operandStack.push(new IntegerValue(54));
+	      operandStack.push(new IntegerValue(32));
+	
+	      Value v1, v2;           // Note, since the stack holds 'Value' values we have to cast the 
+	      IntegerValue v3, v4;    // values popped to the right kind.
+	      
+	      v2 = operandStack.pop();
+	      v1 = operandStack.pop();
+	      v4 = (IntegerValue)v2;
+	      v3 = (IntegerValue)v1;
+	      operandStack.push(new IntegerValue(v3.getValue() + v4.getValue()));    // 54 + 32 = 86
+	
+	      v2 = operandStack.pop();
+	      v1 = operandStack.pop();
+	      v4 = (IntegerValue)v2;
+	      v3 = (IntegerValue)v1;
+	      operandStack.push(new IntegerValue(v3.getValue() * v4.getValue()));    // 7 * 86 = 602
+	
+	      operandStack.push(new IntegerValue(2));
+	
+	      v2 = operandStack.pop();
+	      v1 = operandStack.pop();
+	      v4 = (IntegerValue)v2;
+	      v3 = (IntegerValue)v1;
+	      operandStack.push(new IntegerValue(v3.getValue() / v4.getValue()));    // 602 / 2 = 301
+	
+	      v2 = operandStack.pop();
+	      v1 = operandStack.pop();
+	      v4 = (IntegerValue)v2;
+	      v3 = (IntegerValue)v1;
+	      operandStack.push(new IntegerValue(v3.getValue() - v4.getValue()));    // 32 - 301 = -269
+	
+	      System.out.println("Answer is " + operandStack.pop()); // ===> answer
+	
+	      System.out.println(operandStack.pop()); // ===> Error message & program termination
+	      System.out.println("This line should never be printed out");
+	
+	  }
+	
+	}
