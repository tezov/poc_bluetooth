package com.tezov.bluetooth.external_lib_kotlin.type.collection

import androidx.core.util.Supplier
import com.tezov.bluetooth.external_lib_kotlin.util.UtilsList
import com.tezov.bluetooth.external_lib_kotlin.type.primaire.Entry
import com.tezov.bluetooth.external_lib_kotlin.util.ExtensionCollection.find
import com.tezov.bluetooth.external_lib_kotlin.util.ExtensionCollection.findIndex
import com.tezov.bluetooth.external_lib_kotlin.util.ExtensionCollection.forEach
import com.tezov.bluetooth.external_lib_kotlin.util.UtilsList.isNotNullIndex
import java.lang.UnsupportedOperationException

class ListEntry<KEY, VALUE> private constructor(private val list: MutableList<Entry<KEY, VALUE>>) :
    MutableList<Entry<KEY, VALUE>> {

    constructor(supplier: ()->MutableList<Entry<KEY, VALUE>> = { ArrayList() }) : this(
        supplier()
    )

    fun hasKey(key: KEY, offset:Int = 0) = getEntry(key, offset) != null
    fun isUnique(key: KEY, offset:Int = 0): Boolean {
        var found = false
        list.forEach(offset) {
            if (it.key == key) {
                if (found) return false
                found = true
            }
        }
        return found
    }

    override fun isEmpty() = list.isEmpty()

    override val size: Int
        get() = list.size

    override fun indexOf(element: Entry<KEY, VALUE>) = list.indexOf(element)
    fun indexOf(element: Entry<KEY, VALUE>, offset: Int = 0) = list.findIndex(offset) {
        it == element
    } ?: UtilsList.NULL_INDEX

    fun indexOfValue(value: VALUE, offset: Int = 0) = list.findIndex(offset) {
        it.value == value
    } ?: UtilsList.NULL_INDEX

    fun indexOfKey(key: KEY, offset: Int = 0) = list.findIndex(offset) {
        it.key == key
    } ?: UtilsList.NULL_INDEX

    fun indexOf(offset: Int = 0, predicate: (Entry<KEY, VALUE>) -> Boolean) =
        list.findIndex(offset, predicate) ?: UtilsList.NULL_INDEX

    override fun get(index: Int) = list[index]
    override fun lastIndexOf(element: Entry<KEY, VALUE>): Int {
        throw UnsupportedOperationException()
    }

    fun getKey(value: VALUE, offset: Int = 0) = list.find(offset) { it.value == value }?.key
    fun getKeyAt(index: Int): KEY = list[index].key
    val getKeys: List<KEY>
        get() {
            val values: MutableList<KEY> = java.util.ArrayList(list.size)
            val iterator = iteratorKeys()
            while (iterator.hasNext()) {
                values.add(iterator.next())
            }
            return values
        }

    fun getValue(key: KEY, offset: Int = 0) = list.find(offset) { it.key == key }?.value
    fun getValueAt(index: Int) = list[index].value
    val getValues: List<VALUE>
        get() {
            val values: MutableList<VALUE> = java.util.ArrayList(list.size)
            val iterator = iteratorValues()
            while (iterator.hasNext()) {
                values.add(iterator.next())
            }
            return values
        }

    fun getEntry(key: KEY, offset: Int = 0) =
        list.find(offset) { it.key == key }

    fun getEntry(index: Int) = list[index]

    override fun add(element: Entry<KEY, VALUE>) = list.add(element)

    fun add(key: KEY, value: VALUE) = list.add(Entry(key, value))

    fun put(key: KEY, value: VALUE) = getEntry(key)?.let {
        it.value = value
        false
    } ?: run {
        list.add(Entry(key, value))
        true
    }

    override fun set(index: Int, element: Entry<KEY, VALUE>): Entry<KEY, VALUE> {
        throw UnsupportedOperationException()
    }

    override fun add(index: Int, element: Entry<KEY, VALUE>) {
        throw UnsupportedOperationException()
    }

    override fun removeAt(index: Int) = list.removeAt(index)

    fun removeKey(key: KEY, offset: Int = 0) =
        indexOfKey(key, offset).takeIf { it.isNotNullIndex() }?.let {
            list.removeAt(it).value
        }

    fun removeValue(value: VALUE, offset: Int = 0) =
        indexOfValue(value, offset).takeIf { it.isNotNullIndex() }?.let {
            list.removeAt(it).key
        }

    override fun remove(element: Entry<KEY, VALUE>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun clear() = list.clear()

    override fun contains(element: Entry<KEY, VALUE>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun containsAll(elements: Collection<Entry<KEY, VALUE>>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun retainAll(elements: Collection<Entry<KEY, VALUE>>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun removeAll(elements: Collection<Entry<KEY, VALUE>>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun addAll(elements: Collection<Entry<KEY, VALUE>>) = list.addAll(elements)

    override fun addAll(index: Int, elements: Collection<Entry<KEY, VALUE>>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Entry<KEY, VALUE>> {
        throw UnsupportedOperationException()
    }

    override fun listIterator() = list.listIterator()

    override fun listIterator(index: Int) = list.listIterator(index)

    override fun iterator() = list.iterator()

    fun iteratorKeys() = object : MutableIterator<KEY> {
        val iterator = this@ListEntry.iterator()
        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }
        override fun next(): KEY {
            return iterator.next().key
        }
        override fun remove() {
            iterator.remove()
        }
    }

    fun iterableKeys() = Iterable { iteratorKeys() }

    fun iteratorValues() = object : MutableIterator<VALUE> {
        val iterator = this@ListEntry.iterator()
        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }

        override fun next(): VALUE {
            return iterator.next().value
        }

        override fun remove() {
            iterator.remove()
        }
    }

    fun iterableValues() = Iterable { iteratorValues() }

    fun toTypedArray() = list.toTypedArray()


}


