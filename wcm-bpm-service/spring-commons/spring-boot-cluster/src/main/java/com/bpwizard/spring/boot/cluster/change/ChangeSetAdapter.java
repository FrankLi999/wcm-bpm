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

package com.bpwizard.spring.boot.cluster.change;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An adapter class that processes {@link ChangeSet} instances and for each {@link Change} calls the appropriate protected method
 * that, by default, do nothing. Implementations simply override at least one of the relevant methods for the kind of changes they
 * expect.
 *
 * @author Randall Hauch (rhauch@redhat.com)
 */
public abstract class ChangeSetAdapter implements ChangeSetListener {
    
    @Override
    public void notify( ChangeSet changeSet ) {
    	beginChanges();
        for (Change change : changeSet) {
    		 
    	}
        completeChanges();
        
    }

    /**
     * Signals the beginning of processing for the changes in a transaction described by a single {@link ChangeSet}.
     *
     * @see #completeChanges()
     */
    protected void beginChanges() {
    }

    /**
     * Signals the end of processing for the changes in a transaction described by a single {@link ChangeSet}.
     *
     * @see #beginChanges()
     */
    protected void completeChanges() {
    }

    

}
