/*
 * ModeShape (http://www.modeshape.org)
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
package com.bpwizard.spring.boot.cluster.bus;

import com.bpwizard.spring.boot.cluster.common.collection.ring.SingleProducerCursor;
import com.bpwizard.spring.boot.cluster.common.collection.ring.WaitStrategy;

/**
 * An extension to the {@link SingleProducerCursor} cursor which adds some functionality required by the repository.
 * 
 * @author Horia Chiorean (hchiorea@redhat.com)
 */
public final class RepositoryCursor extends SingleProducerCursor {



    protected RepositoryCursor( int bufferSize, WaitStrategy waitStrategy) {
        super(bufferSize, waitStrategy);
    }

    @Override
    public boolean publish( long position ) {
        boolean result = super.publish(position);
        //if (result && statistics != null) {
        if (result) {
            // the cursor has successfully published `position` positions which is now the header
            long positionOfSlowestConsumer = super.positionOfSlowestConsumer();
            int bufferSize = getBufferSize();
            long stillToBeProcessedBySlowestConsumer = positionOfSlowestConsumer > position ?
                                                       bufferSize - positionOfSlowestConsumer + position :
                                                       position - positionOfSlowestConsumer;
            assert stillToBeProcessedBySlowestConsumer >= 0;
            // statistics.set(ValueMetric.EVENT_QUEUE_SIZE, stillToBeProcessedBySlowestConsumer);
            long availablePositions = bufferSize - stillToBeProcessedBySlowestConsumer;
            assert availablePositions >= 0;
            //statistics.set(ValueMetric.EVENT_BUFFER_AVAILABILITY, availablePositions);             
        }
        return result;
    }
}
