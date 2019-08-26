package com.peng.basic.util

import java.lang.reflect.*
import java.util.*

object TypeUtils {

    fun getRawType(type: Type): Class<*> {

        if (type is Class<*>) {
            // Type is a normal class.
            return type
        }
        if (type is ParameterizedType) {
            val parameterizedType = type

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            val rawType = parameterizedType.rawType as? Class<*> ?: throw IllegalArgumentException()
            return rawType
        }
        if (type is GenericArrayType) {
            val componentType = type.genericComponentType
            return java.lang.reflect.Array.newInstance(getRawType(componentType), 0).javaClass
        }
        if (type is TypeVariable<*>) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Any::class.java
        }
        if (type is WildcardType) {
            return getRawType(type.upperBounds[0])
        }

        throw IllegalArgumentException(
            "Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + type.javaClass.getName()
        )
    }

    /** Returns true if `a` and `b` are equal.  */
    fun equals(a: Type, b: Type): Boolean {
        if (a === b) {
            return true // Also handles (a == null && b == null).

        } else if (a is Class<*>) {
            return a == b // Class already specifies equals().

        } else if (a is ParameterizedType) {
            if (b !is ParameterizedType) return false
            val pa = a
            val pb = b
            val ownerA = pa.ownerType
            val ownerB = pb.ownerType
            return ((ownerA === ownerB || (ownerA != null && ownerA == ownerB))
                    && pa.rawType == pb.rawType
                    && Arrays.equals(pa.actualTypeArguments, pb.actualTypeArguments))

        } else if (a is GenericArrayType) {
            if (b !is GenericArrayType) return false
            val ga = a
            val gb = b
            return equals(ga.genericComponentType, gb.genericComponentType)

        } else if (a is WildcardType) {
            if (b !is WildcardType) return false
            val wa = a
            val wb = b
            return (Arrays.equals(wa.upperBounds, wb.upperBounds) && Arrays.equals(
                wa.lowerBounds,
                wb.lowerBounds
            ))

        } else if (a is TypeVariable<*>) {
            if (b !is TypeVariable<*>) return false
            val va = a
            val vb = b
            return (va.genericDeclaration === vb.genericDeclaration && va.name == vb.name)

        } else {
            return false // This isn't a type we support!
        }
    }
}