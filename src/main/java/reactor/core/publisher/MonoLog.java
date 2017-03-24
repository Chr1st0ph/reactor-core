/*
 * Copyright (c) 2011-2017 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reactor.core.publisher;

import org.reactivestreams.Subscriber;
import reactor.core.Fuseable.ConditionalSubscriber;
import reactor.core.publisher.FluxPeekFuseable.PeekConditionalSubscriber;

/**
 * Peek into the lifecycle events and signals of a sequence.
 * <p>
 * <p>
 * The callbacks are all optional.
 * <p>
 * <p>
 * Crashes by the lambdas are ignored.
 *
 * @param <T> the value type
 * @see <a href="https://github.com/reactor/reactive-streams-commons">Reactive-Streams-Commons</a>
 */
final class MonoLog<T> extends MonoSource<T, T> {

	final SignalPeek<T> log;

	MonoLog(Mono<? extends T> source, SignalPeek<T> log) {
		super(source);
		this.log = log;
	}

	@Override
	public void subscribe(Subscriber<? super T> s) {
		if (s instanceof ConditionalSubscriber) {
			@SuppressWarnings("unchecked") // javac, give reason to suppress because inference anomalies
					ConditionalSubscriber<T> s2 = (ConditionalSubscriber<T>) s;
			source.subscribe(new PeekConditionalSubscriber<>(s2, log));
			return;
		}
		source.subscribe(new FluxPeek.PeekSubscriber<>(s, log));
	}

}