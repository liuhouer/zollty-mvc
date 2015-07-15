/* 
 * Copyright (C) 2012-2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by Zollty Tsou [http://blog.csdn.net/zollty (or GitHub)]
 */
package org.zollty.framework.core.support.xml.parse;

import java.util.List;

import org.w3c.dom.Element;

import org.zollty.framework.core.support.xml.ManagedMap;
import org.zollty.framework.core.support.xml.ManagedValue;
import org.zollty.framework.util.dom.Dom;

/**
 * 解析map元素
 * 
 * @author zollty
 */
public class MapNodeParser extends AbstractXmlNodeParser {

	@Override
	public Object parse(Element ele, Dom dom) {
		// 获取key,value定义类型
		String typeName = ele.getAttribute(XmlNodeConstants.TYPE_ATTRIBUTE);
		ManagedMap<Object, Object> target = new ManagedMap<Object, Object>();
		target.setTypeName(typeName);

		// 获取所有entry元素
		List<Element> elements = dom.elements(ele);
		for (Element entry : elements) {
			Object key = null;
			Object value = null;
			if (entry.hasAttribute(XmlNodeConstants.KEY_ATTRIBUTE)) { // 如果有key属性
				key = new ManagedValue(entry
						.getAttribute(XmlNodeConstants.KEY_ATTRIBUTE));
			}

			if (entry.hasAttribute(XmlNodeConstants.VALUE_ATTRIBUTE)) { // 如果有value属性
				value = new ManagedValue(entry
						.getAttribute(XmlNodeConstants.VALUE_ATTRIBUTE));
			}

			// 获取key元素
			List<Element> keyEle = dom.elements(entry,
					XmlNodeConstants.MAP_KEY_ELEMENT);
			if (keyEle.size() > 1) {
				// 有且只能有一个key元素
				error("must not contain more than one key-element");
			} else if (keyEle.size() == 1) {
				if (key != null) {
					// key属性和key元素只能有一个
					error("only allowed to contain either 'key' attribute OR key-element");
				} else {
					// 获取key子元素
					List<Element> subKey = dom.elements(keyEle.get(0));
					if (subKey.size() == 0) {
						String keyText = dom.getTextValue(keyEle.get(0));
						if (keyText == null)
							error("must contain one key-sub-element");
						else
							key = new ManagedValue(keyText);
					} else {
						key = XmlNodeStateMachine.stateProcessor(subKey.get(0), dom);
					}
				}
			} else {
				if (key == null)
					error("not contain 'key' attribute OR key-element");
			}

			// 获取value元素
			List<Element> valueEle = dom.elements(entry,
					XmlNodeConstants.MAP_VALUE_ELEMENT);
			if (valueEle.size() > 1) {
				// 有且只能有一个value元素
				error("must not contain more than one value-element");
			} else if (valueEle.size() == 1) {
				if (value != null) {
					// value属性和value元素只能有一个
					error("only allowed to contain either 'value' attribute OR value-element");
				} else {
					// 获取value子元素
					List<Element> subValue = dom.elements(valueEle.get(0));
					if (subValue.size() != 1) {
						String valueText = dom.getTextValue(valueEle.get(0));
						if (valueText == null)
							error("must contain one value-sub-element");
						else
							value = new ManagedValue(valueText);
					} else {
						value = XmlNodeStateMachine.stateProcessor(subValue
								.get(0), dom);
					}
				}
			} else {
				if (value == null)
					error("not contain 'value' attribute OR value-element");
			}

			target.put(key, value);
		}
		return target;
	}

}
