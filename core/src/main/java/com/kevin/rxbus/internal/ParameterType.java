/*
 * Copyright 2017 Kevin zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevin.rxbus.internal;

import com.kevin.rxbus.RxBusException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by zhouwenkai on 2017-05-08 11:00:10
 */

public abstract class ParameterType<T> {

    protected Class<T> mType;

    static Class getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RxBusException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return (Class) parameterized.getActualTypeArguments()[0];
    }

    public Class<T> getType() {
        mType = null == mType ? mType = getSuperclassTypeParameter(getClass()) : mType;
        return mType;
    }

}
