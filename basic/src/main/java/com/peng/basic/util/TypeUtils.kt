package com.peng.basic.util

import java.lang.reflect.*
import java.util.*

object TypeUtils {

    fun getRawType(type: Type): Class<*> {

        if (type is Class<*>) {
            // Type is a normal class.
            return type as Class<*>
        }
        if (type is ParameterizedType) {
            val parameterizedType = type as ParameterizedType

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            val rawType = parameterizedType.getRawType()
            if (rawType !is Class<*>) throw IllegalArgumentException()
            return rawType as Class<*>
        }
        if (type is GenericArrayType) {
            val componentType = (type as GenericArrayType).getGenericComponentType()
            return java.lang.reflect.Array.newInstance(getRawType(componentType), 0).javaClass
        }
        if (type is TypeVariable<*>) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Any::class.java
        }
        if (type is WildcardType) {
            return getRawType((type as WildcardType).getUpperBounds()[0])
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
            return a.equals(b) // Class already specifies equals().

        } else if (a is ParameterizedType) {
            if (b !is ParameterizedType) return false
            val pa = a as ParameterizedType
            val pb = b as ParameterizedType
            val ownerA = pa.getOwnerType()
            val ownerB = pb.getOwnerType()
            return ((ownerA === ownerB || (ownerA != null && ownerA == ownerB))
                    && pa.getRawType().equals(pb.getRawType())
                    && Arrays.equals(pa.getActualTypeArguments(), pb.getActualTypeArguments()))

        } else if (a is GenericArrayType) {
            if (b !is GenericArrayType) return false
            val ga = a as GenericArrayType
            val gb = b as GenericArrayType
            return equals(ga.getGenericComponentType(), gb.getGenericComponentType())

        } else if (a is WildcardType) {
            if (b !is WildcardType) return false
            val wa = a as WildcardType
            val wb = b as WildcardType
            return (Arrays.equals(wa.getUpperBounds(), wb.getUpperBounds()) && Arrays.equals(
                wa.getLowerBounds(),
                wb.getLowerBounds()
            ))

        } else if (a is TypeVariable<*>) {
            if (b !is TypeVariable<*>) return false
            val va = a as TypeVariable<*>
            val vb = b as TypeVariable<*>
            return (va.getGenericDeclaration() === vb.getGenericDeclaration() && va.getName().equals(vb.getName()))

        } else {
            return false // This isn't a type we support!
        }
    }
}