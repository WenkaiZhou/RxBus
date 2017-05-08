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

import io.reactivex.functions.Consumer;

/**
 * Created by zhouwenkai on 2017-05-08 10:50:49.
 */

public abstract class RxBusConsumer<T> extends ParameterType<T> implements Consumer<T> {

}