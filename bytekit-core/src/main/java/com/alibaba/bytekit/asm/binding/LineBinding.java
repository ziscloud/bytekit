package com.alibaba.bytekit.asm.binding;

import com.alibaba.deps.org.objectweb.asm.Type;
import com.alibaba.deps.org.objectweb.asm.tree.AbstractInsnNode;
import com.alibaba.deps.org.objectweb.asm.tree.InsnList;
import com.alibaba.deps.org.objectweb.asm.tree.LineNumberNode;
import com.alibaba.bytekit.asm.location.Location;
import com.alibaba.bytekit.utils.AsmOpUtils;

/**
 * 
 * @author hengyunabc
 *
 */
public class LineBinding extends Binding {

    private boolean exact;

    public LineBinding(boolean exact) {
        this.exact = exact;
    }

    @Override
    public void pushOntoStack(InsnList instructions, BindingContext bindingContext) {
        Location location = bindingContext.getLocation();
        AbstractInsnNode insnNode = location.getInsnNode();

        int line = -1;
        if (exact) {
            if (insnNode instanceof LineNumberNode) {
                line = ((LineNumberNode) insnNode).line;
            } else {
                throw new IllegalArgumentException("LineBinding location is not LineNumberNode, insnNode: " + insnNode);
            }
        } else {
            if (location.isWhenComplete() == false) {
                while (insnNode != null) {
                    if (insnNode instanceof LineNumberNode) {
                        line = ((LineNumberNode) insnNode).line;
                        break;
                    }
                    insnNode = insnNode.getPrevious();
                }
            } else {
                while (insnNode != null) {
                    if (insnNode instanceof LineNumberNode) {
                        line = ((LineNumberNode) insnNode).line;
                        break;
                    }
                    insnNode = insnNode.getNext();
                }
            }
        }
        AsmOpUtils.push(instructions, line);
    }

    @Override
    public Type getType(BindingContext bindingContext) {
        return Type.getType(int.class);
    }

}
