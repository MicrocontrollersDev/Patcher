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
package club.sk1er.patcher.asm

import club.sk1er.hookinjection.injectInstructions
import club.sk1er.patcher.hooks.ChatStyleHook
import club.sk1er.patcher.tweaker.transform.PatcherTransformer
import org.objectweb.asm.tree.ClassNode

class ChatStyleTransformer : PatcherTransformer {
    override fun getClassName() = arrayOf("net.minecraft.util.ChatStyle")

    override fun transform(classNode: ClassNode, name: String) {
        classNode.methods.first {
            val methodName = mapMethodName(classNode, it)
            methodName == "getChatHoverEvent" || methodName == "func_150210_i"
        }?.apply {
            clearInstructions(this)
            injectInstructions {
                of(ChatStyleHook::getChatHoverEvent)
                into(this@apply)
                param(0)
                keepReturns
            }
        }
    }
}