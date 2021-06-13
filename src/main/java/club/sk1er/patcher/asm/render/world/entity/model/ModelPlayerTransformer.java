/*
 * Copyright © 2020 by Sk1er LLC
 *
 * All rights reserved.
 *
 * Sk1er LLC
 * 444 S Fulton Ave
 * Mount Vernon, NY
 * sk1er.club
 */

package club.sk1er.patcher.asm.render.world.entity.model;

import club.sk1er.patcher.tweaker.transform.PatcherTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ModelPlayerTransformer implements PatcherTransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.model.ModelPlayer"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            String methodName = mapMethodName(classNode, method);

            if (methodName.equals("postRenderArm") || methodName.equals("func_178718_a")) {
                clearInstructions(method);
                method.instructions.insert(fixedArmPosition());
                break;
            }
        }
    }

    private InsnList fixedArmPosition() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/model/ModelPlayer", "field_178735_y", "Z"));
        LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/model/ModelPlayer", "field_178723_h", "Lnet/minecraft/client/model/ModelRenderer;"));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/model/ModelPlayer", "field_178723_h", "Lnet/minecraft/client/model/ModelRenderer;"));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/model/ModelRenderer", "field_78800_c", "F"));
        list.add(new LdcInsnNode(0.5F));
        list.add(new InsnNode(Opcodes.FADD));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/model/ModelRenderer", "field_78800_c", "F"));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/model/ModelPlayer", "field_178723_h", "Lnet/minecraft/client/model/ModelRenderer;"));
        list.add(new VarInsnNode(Opcodes.FLOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/model/ModelRenderer", "func_78794_c", "(F)V", false));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/model/ModelPlayer", "field_178723_h", "Lnet/minecraft/client/model/ModelRenderer;"));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/model/ModelPlayer", "field_178723_h", "Lnet/minecraft/client/model/ModelRenderer;"));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/model/ModelRenderer", "field_78798_e", "F"));
        list.add(new LdcInsnNode(0.5F));
        list.add(new InsnNode(Opcodes.FSUB));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/model/ModelRenderer", "field_78798_e", "F"));
        LabelNode gotoInsn = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.GOTO, gotoInsn));
        list.add(ifeq);
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/model/ModelPlayer", "field_178723_h", "Lnet/minecraft/client/model/ModelRenderer;"));
        list.add(new VarInsnNode(Opcodes.FLOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/model/ModelRenderer", "func_78794_c", "(F)V", false));
        list.add(gotoInsn);
        list.add(new InsnNode(Opcodes.RETURN));
        return list;
    }
}