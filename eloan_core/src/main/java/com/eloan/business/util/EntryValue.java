package com.eloan.business.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryValue<K,T> {
	
	private K key;
	private T value;
	
	public EntryValue(K key,T value) {
		super();
		this.key = key;
		this.value = value;
	}

}
