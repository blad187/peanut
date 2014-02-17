import EVM.*;
import Utilities.*;
import Utilities.Error;
import Value.*;
import OperandStack.*;
import Instruction.*;
import java.util.*;
import java.io.*;

public class EVM {
    
    static private void binOp(int opcode, int type, OperandStack operandStack) {
        Value o1, o2;

        o1 = operandStack.pop();
        o2 = operandStack.pop();

        // Check that the operands have the right type
        if (!(o1.getType() == type && o2.getType() == type))
            Error.error("Error: Type mismatch - operands do not match operator.");

        switch (opcode) {
        case RuntimeConstants.opc_dadd: operandStack.push(new DoubleValue(((DoubleValue)o2).getValue() + ((DoubleValue)o1).getValue())); break;
        case RuntimeConstants.opc_dsub: operandStack.push(new DoubleValue(((DoubleValue)o2).getValue() + ((DoubleValue)o1).getValue())); break;
        case RuntimeConstants.opc_ddiv: operandStack.push(new DoubleValue(((DoubleValue)o2).getValue() + ((DoubleValue)o1).getValue())); break;
        case RuntimeConstants.opc_dmul: operandStack.push(new DoubleValue(((DoubleValue)o2).getValue() + ((DoubleValue)o1).getValue())); break;     
        case RuntimeConstants.opc_drem: operandStack.push(new DoubleValue(((DoubleValue)o2).getValue() + ((DoubleValue)o1).getValue())); break;

        case RuntimeConstants.opc_iadd: operandStack.push(new IntegerValue(((IntegerValue)o2).getValue() + ((IntegerValue)o1).getValue())); break;
        case RuntimeConstants.opc_isub: operandStack.push(new IntegerValue(((IntegerValue)o2).getValue() + ((IntegerValue)o1).getValue())); break;
        case RuntimeConstants.opc_idiv: operandStack.push(new IntegerValue(((IntegerValue)o2).getValue() + ((IntegerValue)o1).getValue())); break;
        case RuntimeConstants.opc_imul: operandStack.push(new IntegerValue(((IntegerValue)o2).getValue() + ((IntegerValue)o1).getValue())); break;
        case RuntimeConstants.opc_irem: operandStack.push(new IntegerValue(((IntegerValue)o2).getValue() + ((IntegerValue)o1).getValue())); break;

        case RuntimeConstants.opc_fadd: operandStack.push(new FloatValue(((FloatValue)o2).getValue() + ((FloatValue)o1).getValue())); break;
        case RuntimeConstants.opc_fsub: operandStack.push(new FloatValue(((FloatValue)o2).getValue() + ((FloatValue)o1).getValue())); break;
        case RuntimeConstants.opc_fdiv: operandStack.push(new FloatValue(((FloatValue)o2).getValue() + ((FloatValue)o1).getValue())); break;
        case RuntimeConstants.opc_fmul: operandStack.push(new FloatValue(((FloatValue)o2).getValue() + ((FloatValue)o1).getValue())); break;
        case RuntimeConstants.opc_frem: operandStack.push(new FloatValue(((FloatValue)o2).getValue() + ((FloatValue)o1).getValue())); break;

        case RuntimeConstants.opc_ladd: operandStack.push(new LongValue(((LongValue)o2).getValue() + ((LongValue)o1).getValue())); break;
        case RuntimeConstants.opc_lsub: operandStack.push(new LongValue(((LongValue)o2).getValue() + ((LongValue)o1).getValue())); break;
        case RuntimeConstants.opc_ldiv: operandStack.push(new LongValue(((LongValue)o2).getValue() + ((LongValue)o1).getValue())); break;
        case RuntimeConstants.opc_lmul: operandStack.push(new LongValue(((LongValue)o2).getValue() + ((LongValue)o1).getValue())); break;
        case RuntimeConstants.opc_lrem: operandStack.push(new LongValue(((LongValue)o2).getValue() + ((LongValue)o1).getValue())); break;
        }
    }
    
    static private void swap(OperandStack operandStack) {
        Value v1, v2;

        v1 = operandStack.pop();
        v2 = operandStack.pop();

        if(v1.getType() != Value.s_double && v2.getType() != Value.s_long || v2.getType() != Value.s_double && v1.getType() != Value.s_long)
            {
                operandStack.push(Value.makeValue(v1));
                operandStack.push(Value.makeValue(v2));
            }
        else
            {
                operandStack.push(Value.makeValue(v2));
                operandStack.push(Value.makeValue(v1));
            }

    }

    static private void negate(int type, OperandStack operandStack) {
        Value v1, v2;

        v1 = operandStack.pop();

        if(type == v1.getType() && type == Value.s_integer)
            {
                operandStack.push(new IntegerValue(-((IntegerValue)v1).getValue()));
            }
        else if(type == v1.getType() && type == Value.s_long)
            {
                operandStack.push(new LongValue(-((LongValue)v1).getValue()));
            }
        else if(type == v1.getType() && type == Value.s_float)
            {
                operandStack.push(new FloatValue(-((FloatValue)v1).getValue()));
            }
        else if(type == v1.getType() && type == Value.s_double)
            {
                operandStack.push(new DoubleValue(-((DoubleValue)v1).getValue()));
            }
        else
            operandStack.push(Value.makeValue(v1));

    }
    
    static private void cmp(int type, OperandStack operandStack) {
        Value v1, v2;

        IntegerValue v3;

        int change = 0;

        v1 = operandStack.pop();
        v2 = operandStack.pop();

        operandStack.push(v1);
        operandStack.push(v2);

        // Check that the operands have the right type
        if(v1.getType() == type && v2.getType() == type)
            {
                if(type == Value.s_float)
                    {
                        if(((FloatValue)v1).getValue() > ((FloatValue)v2).getValue())
                            operandStack.push(new IntegerValue(1));
                        else if(((FloatValue)v1).getValue() < ((FloatValue)v2).getValue())
                            operandStack.push(new IntegerValue(-1));
                        else
                            operandStack.push(new IntegerValue(0));
                    }

                else if(type == Value.s_double)
                    {
                        if(((DoubleValue)v1).getValue() > ((DoubleValue)v2).getValue())
                            operandStack.push(new IntegerValue(1));
                        else if(((DoubleValue)v1).getValue() < ((DoubleValue)v2).getValue())
                            operandStack.push(new IntegerValue(-1));
                        else
                            operandStack.push(new IntegerValue(0));
                    }

                else if(type == Value.s_long)
                    {
                        if(((LongValue)v1).getValue() > ((LongValue)v2).getValue())
                            operandStack.push(new IntegerValue(1));
                        else if(((LongValue)v1).getValue() < ((LongValue)v2).getValue())
                            operandStack.push(new IntegerValue(-1));
                        else
                            operandStack.push(new IntegerValue(0));
                    }
            }
    }

    static private void two(int from, int to, OperandStack operandStack) {

        Value e = operandStack.pop();
        if (e.getType() != from)
            Error.error("OperandStack.two: Type mismatch.");

        switch (from) {
        case Value.s_integer:
            int iv = ((IntegerValue)e).getValue();
            switch (to) {
            case Value.s_byte:   operandStack.push(new IntegerValue((int)((byte) iv))); break;
            case Value.s_char:   operandStack.push(new IntegerValue((int)((char) iv))); break;
            case Value.s_short:  operandStack.push(new IntegerValue((int)((short)iv))); break;
            case Value.s_double: operandStack.push(new DoubleValue((double)iv)); break;
            case Value.s_float:  operandStack.push(new FloatValue((float)iv)); break;
            case Value.s_long:   operandStack.push(new LongValue((long)iv)); break;
            }

        case Value.s_double:
            double dv = ((DoubleValue)e).getValue();
            switch (to) {
            case Value.s_integer: operandStack.push(new IntegerValue((int)dv)); break;
            case Value.s_float:   operandStack.push(new FloatValue((float)dv)); break;
            case Value.s_long:    operandStack.push(new LongValue((long)dv)); break;
            }

        case Value.s_long:
            long lv = ((LongValue)e).getValue();
            switch (to) {
            case Value.s_integer: operandStack.push(new IntegerValue((int)lv)); break;
            case Value.s_float:   operandStack.push(new FloatValue((float)lv)); break;
            case Value.s_double:  operandStack.push(new DoubleValue((double)lv)); break;
            }

        case Value.s_float:
            float fv = ((FloatValue)e).getValue();
            switch (to) {
            case Value.s_integer: operandStack.push(new IntegerValue((int)fv)); break;
            case Value.s_double:  operandStack.push(new DoubleValue((double)fv)); break;
            case Value.s_long:    operandStack.push(new LongValue((long)fv)); break;
            }
        }
    }
    
    
    static private void dup(int opCode, OperandStack operandStack) {
        // In real JVM a Double or a Long take up 2 stack words, but EVM Doubles and Longs
        // do not, so since dup2 can be used to either duplicate 2 single word values or
        // 1 double word value, we need to check the type of what is on the stack before
        // we decide if we should duplicate just one value or two.
        switch (opCode) {
        case RuntimeConstants.opc_dup:   operandStack.push(operandStack.peek(1)); break;
        case RuntimeConstants.opc_dup2: {
            Value o1 = operandStack.peek(1);
            Value o2;
            if ((o1 instanceof DoubleValue) || (o1 instanceof LongValue))
                operandStack.push(o1);
            else {
                o2 = operandStack.peek(2);
                operandStack.push(o2);
                operandStack.push(o1);
            }
        }
            break;
        case RuntimeConstants.opc_dup_x1: {
            Value o1 = operandStack.pop();
            Value o2 = operandStack.pop();
            if ((o1 instanceof DoubleValue) || (o1 instanceof LongValue) ||
                (o2 instanceof DoubleValue) || (o2 instanceof LongValue))
                Error.error("Error: dup_x1 cannot be used on value of type Double or Long.");
            operandStack.push(o1);
            operandStack.push(o2);
            operandStack.push(o1);
        }
            break;
        case RuntimeConstants.opc_dup_x2: {
            Value o1 = operandStack.pop();
            Value o2 = operandStack.pop();
            if ((o1 instanceof DoubleValue) || (o1 instanceof LongValue))
                Error.error("Error: dup_x2 cannot be used on value of type Double or Long.");
            if ((o2 instanceof DoubleValue) || (o2 instanceof LongValue)) {
                operandStack.push(o1);
                operandStack.push(o2);
                operandStack.push(o1);
            } else {
                Value o3 = operandStack.pop();
                if ((o3 instanceof DoubleValue) || (o3 instanceof LongValue))
                    Error.error("Error: word3 of dup_x2 cannot be  of type Double or Long.");
                operandStack.push(o1);
                operandStack.push(o3);
                operandStack.push(o2);
                operandStack.push(o1);
            }
        }
            break;
        case RuntimeConstants.opc_dup2_x1: {
            Value o1 = operandStack.pop();
            if ((o1 instanceof DoubleValue) || (o1 instanceof LongValue)) {
                Value o2 = operandStack.pop();
                if ((o2 instanceof DoubleValue) || (o2 instanceof LongValue))
                    Error.error("Error: word3 of dup2_x1 cannot be of type Double or Long.");
                operandStack.push(o1);
                operandStack.push(o2);
                operandStack.push(o1);
            } else {
                Value o2 = operandStack.pop();
                if ((o2 instanceof DoubleValue) || (o2 instanceof LongValue))
                    Error.error("Error: word2 of dup2_x1 cannot be of type Double or Long when word1 is not.");
                Value o3 = operandStack.pop();
                if ((o3 instanceof DoubleValue) || (o3 instanceof LongValue))
                    Error.error("Error: word3 of dup2_x1 cannot be of type Double or Long.");
                operandStack.push(o2);
                operandStack.push(o1);
                operandStack.push(o3);
                operandStack.push(o2);
                operandStack.push(o1);
            }
        }
            break;
        case RuntimeConstants.opc_dup2_x2: {
            Value o1 = operandStack.pop();
            if ((o1 instanceof DoubleValue) || (o1 instanceof LongValue)) {
                Value o2 = operandStack.pop();
                if (!((o2 instanceof DoubleValue) || (o2 instanceof LongValue)))
                    Error.error("Error: word3 of dup2_x2 must be of type Double or Long.");
                operandStack.push(o1);
                operandStack.push(o2);
                operandStack.push(o1);
            } else {
                Value o2 = operandStack.pop();
                if ((o2 instanceof DoubleValue) || (o2 instanceof LongValue))
                    Error.error("Error: word2 of dup2_x2 cannot be of type Double or Long when word1 is not.");
                Value o3 = operandStack.pop();
                if (!((o3 instanceof DoubleValue) || (o3 instanceof LongValue)))
                    Error.error("Error: word3/4 of dup2_x2 must be of type Double or Long.");
                operandStack.push(o2);
                operandStack.push(o1);
                operandStack.push(o3);
                operandStack.push(o2);
                operandStack.push(o1);
            }
        }
            break;
        }
    }
    
    
    static private void logic(int inst, OperandStack operandStack) {
        Value v1, v2;

        v1 = operandStack.pop();
        v2 = operandStack.pop();

        if(v1.getType() == v2.getType())
            {
                if(v1.getType == Value.s_integer)
                    {
                        switch(inst){
                        case 126: ; break; //iand
                        case 128: ; break; //ior
                        case 130: ; break; //ixor
                        } 
                    }
                 else if(v1.getType == Value.s_long)
                     {
                         switch(inst){
                         case 127: ; break; //land
                         case 129: ; break; //lor
                         case 131: ; break; //lxor
                     }

            }
    }
    
    static private void shift(int opCode, OperandStack operandStack) {

        // Your code goes here

    }

    



    public static void main(String argv[]) {
        OperandStack operandStack = new OperandStack(100, "Phase 2");
        Value v1, v2;
        IntegerValue v3, v4;

        operandStack.push(new IntegerValue(100));

        v1 = Value.makeValue((double)5.7);
        v2 = new DoubleValue(6);
        operandStack.push(v1);
        operandStack.push(v2);

        System.out.println(operandStack);

        binOp(RuntimeConstants.opc_dadd, Value.s_double, operandStack);
        System.out.println(operandStack.pop()); // ==> 11.7

    }
}
