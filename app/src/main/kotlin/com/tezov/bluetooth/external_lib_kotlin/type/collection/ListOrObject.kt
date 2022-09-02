//package com.tezov.bluetooth.external_lib_kotlin.type.collection
//
//import com.tezov.bluetooth.external_lib_kotlin.util.UtilsList.NULL_INDEX
//import com.tezov.bluetooth.external_lib_kotlin.util.UtilsNull.NULL_OBJECT
//import java.lang.IndexOutOfBoundsException
//import java.util.ConcurrentModificationException
//import java.util.NoSuchElementException
//import java.util.function.Consumer
//
//class ListOrObject<T> private constructor(o: Any, isList: Boolean, supplier: ()->List<T>) :
//    MutableList<T?> {
//    private val listSupplier: ()->List<T>
//    private var `object`: Any?
//    private var isList: Boolean
//
//    constructor(supplier: ()->List<T> = { ArrayList() }) : this(
//        NULL_OBJECT,
//        false,
//        supplier
//    )
//
//    override fun isEmpty(): Boolean {
//        return `object` === NULL_OBJECT
//    }
//
//    override fun size(): Int {
//        return if (`object` === NULL_OBJECT) {
//            0
//        } else if (isList) {
//            (`object` as List<*>?)!!.size
//        } else {
//            1
//        }
//    }
//
//    override fun indexOf(o: Any): Int {
//        val iterator: Iterator<T?> = iterator()
//        var i = 0
//        while (iterator.hasNext()) {
//            if (iterator.next() == o) {
//                return i
//            }
//            i++
//        }
//        return NULL_INDEX
//    }
//
//    override fun indexOf(predicate: ()->Boolean): Int {
//        val iterator: Iterator<T?> = iterator()
//        var i = 0
//        while (iterator.hasNext()) {
//            if (predicate(iterator.next())) {
//                return i
//            }
//            i++
//        }
//        return NULL_INDEX
//    }
//
//    override fun lastIndexOf(o: Any): Int {
//        val iterator: Iterator<T?> = iterator()
//        var lastIndex: Int = NULL_INDEX
//        var i = 0
//        while (iterator.hasNext()) {
//            if (iterator.next() == o) {
//                lastIndex = i
//            }
//            i++
//        }
//        return lastIndex
//    }
//
//    fun get(): T? {
//        return if (`object` === NULL_OBJECT) {
//            null
//        } else if (!isList) {
//            `object` as T?
//        } else {
//            (`object` as List<*>?)!![0] as T
//        }
//    }
//
//    fun set(t: T?): ListOrObject<T> {
//        if (isList) {
////            DebugException.start().log("ListOrObject is a list, should not use set").end()
//        }
//        if (t == null) {
//            `object` = NULL_OBJECT
//        } else {
//            `object` = t
//        }
//        return this
//    }
//
//    override fun get(index: Int): T? {
//        return if (isList) {
//            (`object` as List<T>?)!![index]
//        } else if (`object` !== NULL_OBJECT && index == 0) {
//            `object` as T?
//        } else {
//            throw IndexOutOfBoundsException()
//        }
//    }
//
//    override fun add(t: T?): Boolean {
//        if (t == null) {
//            DebugException.start().log("value is null").end()
//        }
//        return if (`object` === NULL_OBJECT) {
//            `object` = t
//            true
//        } else if (isList) {
//            (`object` as MutableList<*>?)!!.add(t)
//        } else {
//            val list: MutableList<*> = listSupplier.get()
//            var result = true
//            result = result and list.add(`object`)
//            result = result and list.add(t)
//            `object` = list
//            isList = true
//            result
//        }
//    }
//
//    override fun set(index: Int, t: T?): T? {
//        if (t == null) {
//            DebugException.start().log("value is null").end()
//        }
//        return if (isList) {
//            (`object` as MutableList<T?>?)!!.set(index, t)
//        } else if (`object` !== NULL_OBJECT && index == 0) {
//            val old = `object` as T?
//            `object` = t
//            old
//        } else {
//            throw IndexOutOfBoundsException()
//        }
//    }
//
//    override fun add(index: Int, t: T?) {
//        if (t == null) {
//            DebugException.start().log("value is null").end()
//        }
//        if (isList) {
//            (`object` as List<T?>?).add(index, t)
//        } else if (`object` !== NULL_OBJECT) {
//            val list: MutableList<*> = listSupplier.get()
//            if (index == 1) {
//                list.add(`object`)
//                list.add(t)
//            } else if (index == 0) {
//                list.add(t)
//                list.add(`object`)
//            }
//            `object` = list
//            isList = true
//        } else if (index == 0) {
//            `object` = t
//        } else {
//            throw IndexOutOfBoundsException()
//        }
//    }
//
//    override fun remove(o: Any?): Boolean {
//        return if (`object` === NULL_OBJECT) {
//            false
//        } else if (isList) {
//            if (!(`object` as List<*>?).remove(o)) {
//                return false
//            }
//            if ((`object` as List<*>?)!!.size == 1) {
//                `object` = (`object` as List<*>?)!![0]
//                isList = false
//            }
//            true
//        } else if (`object` == o) {
//            `object` = NULL_OBJECT
//            true
//        } else {
//            false
//        }
//    }
//
//    override fun remove(index: Int): T {
//        var t: T? = null
//        if (isList) {
//            t = (`object` as List<T>?).removeAt(index)
//            if ((`object` as List<*>?)!!.size == 1) {
//                `object` = (`object` as List<*>?)!![0]
//                isList = false
//            }
//        } else if (index == 0) {
//            t = `object` as T?
//            `object` = NULL_OBJECT
//        }
//        return t
//    }
//
//    override fun clear() {
//        `object` = NULL_OBJECT
//        isList = false
//    }
//
//    override operator fun contains(o: Any): Boolean {
//        for (t in this) {
//            if (t == o) {
//                return true
//            }
//        }
//        return false
//    }
//
//    override fun containsAll(c: Collection<*>): Boolean {
//        return if (isList) {
//            (`object` as List<*>?)!!.containsAll(c)
//        } else {
//            for (p in c) {
//                if (p != `object`) {
//                    return false
//                }
//            }
//            `object` === NULL_OBJECT || c.size == 1
//        }
//    }
//
//    override fun addAll(c: Collection<T?>): Boolean {
//        return if (isList) {
//            (`object` as MutableList<*>?)!!.addAll(c)
//        } else {
//            val previousSize = size
//            for (t in c) {
//                add(t)
//            }
//            previousSize + c.size == size
//        }
//    }
//
//    override fun addAll(index: Int, c: Collection<T?>): Boolean {
//        var index = index
//        return if (isList) {
//            (`object` as MutableList<*>?)!!.addAll(index, c)
//        } else {
//            val previousSize = size
//            for (t in c) {
//                add(index++, t)
//            }
//            previousSize + c.size == size
//        }
//    }
//
//    override fun retainAll(c: Collection<*>): Boolean {
//        return if (isList) {
//            (`object` as List<*>?).retainAll(c)
//        } else {
//            for (p in c) {
//                if (p == `object`) {
//                    return false
//                }
//            }
//            clear()
//            true
//        }
//    }
//
//    override fun removeAll(c: Collection<*>): Boolean {
//        return if (isList) {
//            (`object` as List<*>?).removeAll(c)
//        } else {
//            for (p in c) {
//                if (p == `object`) {
//                    clear()
//                    return true
//                }
//            }
//            false
//        }
//    }
//
//    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T?> {
//        if (isList) {
//            return (`object` as List<*>?)!!.subList(fromIndex, toIndex)
//        } else if (`object` !== NULL_OBJECT) {
//            if (fromIndex != 0 || toIndex != 0) {
//                throw IndexOutOfBoundsException()
//            }
//            val l: MutableList<T?> = listSupplier.get()
//            l.add(`object` as T?)
//            return l
//        } else if (fromIndex != NULL_INDEX || toIndex != NULL_INDEX) {
//            throw IndexOutOfBoundsException()
//        }
//        return listSupplier.get()
//    }
//
//    open inner class Itr : MutableIterator<T> {
//        var limit = me().size
//        var cursor = 0
//        var lastRet = -1
//        override fun hasNext(): Boolean {
//            return cursor < limit
//        }
//
//        override fun next(): T {
//            val i = cursor
//            if (i >= limit) throw NoSuchElementException()
//            if (i >= me().size) throw ConcurrentModificationException()
//            cursor++
//            return me()[i.also { lastRet = it }]
//        }
//
//        override fun remove() {
//            check(lastRet >= 0)
//            try {
//                me().removeAt(lastRet)
//                cursor = lastRet
//                lastRet = -1
//                limit--
//            } catch (ex: IndexOutOfBoundsException) {
//                throw ConcurrentModificationException()
//            }
//        }
//
//        override fun forEachRemaining(consumer: Consumer<in T>) {
//            val size = me().size
//            var i = cursor
//            if (i >= size) {
//                return
//            }
//            while (i != size) {
//                consumer.accept(me()[i++])
//            }
//            cursor = i
//            lastRet = i - 1
//        }
//    }
//
//    inner class ListItr internal constructor(index: Int) : Itr(), MutableListIterator<T> {
//        override fun hasPrevious(): Boolean {
//            return cursor != 0
//        }
//
//        override fun nextIndex(): Int {
//            return cursor
//        }
//
//        override fun previousIndex(): Int {
//            return cursor - 1
//        }
//
//        override fun previous(): T {
//            val i: Int = cursor - 1
//            if (i < 0) throw NoSuchElementException()
//            if (i >= me().size) throw ConcurrentModificationException()
//            cursor = i
//            return me()[i.also { lastRet = it }]
//        }
//
//        override fun set(e: T) {
//            check(lastRet >= 0)
//            try {
//                me()[lastRet] = e
//            } catch (ex: IndexOutOfBoundsException) {
//                throw ConcurrentModificationException()
//            }
//        }
//
//        override fun add(e: T) {
//            try {
//                val i: Int = cursor
//                me().add(i, e)
//                cursor = i + 1
//                lastRet = -1
//                limit++
//            } catch (ex: IndexOutOfBoundsException) {
//                throw ConcurrentModificationException()
//            }
//        }
//
//        init {
//            cursor = index
//        }
//    }
//
//    override fun iterator(): MutableIterator<T?> {
//        return Itr()
//    }
//
//    override fun listIterator(): MutableListIterator<T?> {
//        return ListItr(0)
//    }
//
//    override fun listIterator(index: Int): MutableListIterator<T?> {
//        return ListItr(index)
//    }
//
//    override fun toArray(): Array<Any?> {
//        return if (isList) {
//            (`object` as List<*>?)!!.toTypedArray()
//        } else if (`object` !== NULL_OBJECT) {
//            arrayOf(`object`)
//        } else {
//            arrayOf()
//        }
//    }
//
//    override fun <T> toArray(a: Array<T?>): Array<T?> {
//        var a = a
//        return if (isList) {
//            (`object` as List<T>?).toArray<T>(a)
//        } else {
//            if (`object` !== NULL_OBJECT) {
//                a = java.lang.reflect.Array.newInstance(
//                    a.javaClass.componentType,
//                    1
//                ) as Array<T?>
//                a[0] = `object` as T?
//            }
//            a
//        }
//    }
//
//
//    companion object {
//        fun <T> with(t: T?): ListOrObject<T>? {
//            return if (t == null) {
//                null
//            } else {
//                ListOrObject(t, t is List<*>, null)
//            }
//        }
//    }
//
//    init {
//        `object` = o
//        this.isList = isList
//        listSupplier = supplier
//    }
//}