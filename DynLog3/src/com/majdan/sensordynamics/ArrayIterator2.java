package com.majdan.sensordynamics;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator2<T> implements Iterator<T> {

	private T[] array;
	private int index = 0;

	public ArrayIterator2(T[] array) {
		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return (index < array.length);
	}

	@Override
	public T next() throws NoSuchElementException {
		if (index >= array.length)
			throw new NoSuchElementException("Index "+index+" is out of size of array");
		T object = array[index];
		index++;
		return object;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}