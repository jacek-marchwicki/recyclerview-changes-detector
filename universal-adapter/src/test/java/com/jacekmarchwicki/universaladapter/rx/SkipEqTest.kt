package com.jacekmarchwicki.universaladapter.rx

import com.jacekmarchwicki.universaladapter.DefaultAdapterItem
import com.jacekmarchwicki.universaladapter.SkipEq
import com.jacekmarchwicki.universaladapter.skipEq
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SkipEqTest {

    data class SkipEqAdapterItem(
        val title: String,
        val testFun: SkipEq<() -> Int>,
        override val itemId: Any = title
    ) : DefaultAdapterItem()

    data class RegularAdapterItem(
        val title: String,
        val testFun: () -> Int,
        override val itemId: Any = title
    ) : DefaultAdapterItem()

    @Test
    fun `when two items differs only in SkipEq param, then both items are the same`() {
        val first = SkipEqAdapterItem("title", { 1 }.skipEq())
        val second = first.copy(testFun = { 2 }.skipEq())
        assertTrue { first.same(second) }
    }

    @Test
    fun `when two items differs NOT only in SkipEq param, then items are NOT the same`() {
        val first = SkipEqAdapterItem("title", { 1 }.skipEq())
        val second = first.copy(title = "title2", testFun = { 2 }.skipEq())
        assertFalse { first.same(second) }
    }

    @Test
    fun `when two items differs in a function param, then both items are NOT the same`() {
        val first = RegularAdapterItem("title", { 1 })
        val second = first.copy(testFun = { 2 })
        assertFalse { first.same(second) }
    }
}