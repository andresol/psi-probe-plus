/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.model.jmx;

/**
 * 
 * @author Vlad Ilyushchenko
 */
public class PooledClusterSender extends ClusterSender {
    private int maxPoolSocketLimit;

    public int getMaxPoolSocketLimit() {
        return maxPoolSocketLimit;
    }

    public void setMaxPoolSocketLimit(int maxPoolSocketLimit) {
        this.maxPoolSocketLimit = maxPoolSocketLimit;
    }
}
