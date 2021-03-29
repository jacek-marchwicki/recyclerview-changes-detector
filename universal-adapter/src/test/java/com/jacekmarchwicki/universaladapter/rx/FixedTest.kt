package com.jacekmarchwicki.universaladapter.rx

import com.jacekmarchwicki.universaladapter.DefaultAdapterItem
import com.jacekmarchwicki.universaladapter.Fixed
import com.jacekmarchwicki.universaladapter.fixed
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FixedTest {

    data class FixedAdapterItem(
        val title: String,
        val testFun: Fixed<() -> Int>,
        override val itemId: Any = title
    ) : DefaultAdapterItem()

    data class RegularAdapterItem(
        val title: String,
        val testFun: () -> Int,
        override val itemId: Any = title
    ) : DefaultAdapterItem()

    @Test
    fun `when two items differs only in Fixed param, then both items are the same`() {
        val first = FixedAdapterItem("title", { 1 }.fixed())
        val second = first.copy(testFun = { 2 }.fixed())
        assertTrue { first.same(second) }
    }

    @Test
    fun `when two items differs NOT only in Fixed param, then items are NOT the same`() {
        val first = FixedAdapterItem("title", { 1 }.fixed())
        val second = first.copy(title = "title2", testFun = { 2 }.fixed())
        assertFalse { first.same(second) }
    }

    @Test
    fun `when two items differs in a function param, then both items are NOT the same`() {
        val first = RegularAdapterItem("title", { 1 })
        val second = first.copy(testFun = { 2 })
        assertFalse { first.same(second) }
    }
}