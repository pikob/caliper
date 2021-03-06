/*
 * Copyright (C) 2016 Google Inc.
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

package com.google.caliper.worker;

import com.google.caliper.model.InstrumentType;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import java.util.Map;
import javax.inject.Provider;

/** Module providing dependencies that should only be present when running on the JVM. */
@Module
abstract class JvmWorkerModule {

  @Binds
  @IntoMap
  @InstrumentTypeKey(InstrumentType.ALLOCATION_MICRO)
  abstract Worker abstractMicrobenchmarkAllocationWorker(MicrobenchmarkAllocationWorker impl);

  @Binds
  @IntoMap
  @InstrumentTypeKey(InstrumentType.ALLOCATION_MACRO)
  abstract Worker bindsMacrobenchmarkAllocationWorker(MacrobenchmarkAllocationWorker impl);

  @Provides
  static AllocationRecorder provideAllocationRecorder(
      @WorkerOptions Map<String, String> workerOptions,
      Provider<AllAllocationsRecorder> allAllocationsRecorderProvider,
      Provider<AggregateAllocationsRecorder> aggregateAllocationsRecorderProvider) {

    return Boolean.valueOf(workerOptions.get("trackAllocations"))
        ? allAllocationsRecorderProvider.get()
        : aggregateAllocationsRecorderProvider.get();
  }
}
