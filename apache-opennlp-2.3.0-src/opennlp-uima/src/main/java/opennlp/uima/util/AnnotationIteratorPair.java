/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package opennlp.uima.util;

import org.apache.uima.cas.text.AnnotationFS;

/**
 * A pair of a {@link AnnotationFS UIMA annotation} and an
 * {@link Iterable<AnnotationFS> annotation iterator}.
 */
public final class AnnotationIteratorPair {
  private final AnnotationFS annot;
  private final Iterable<AnnotationFS> it;

  public AnnotationIteratorPair(AnnotationFS a, Iterable<AnnotationFS> it) {
    super();
    this.annot = a;
    this.it = it;
  }

  /**
   * @return Retrieves the {@link AnnotationFS}.
   */
  public AnnotationFS getAnnotation() {
    return this.annot;
  }

  /**
   * @return Retrieves the {@link Iterable<AnnotationFS> sub-iterator}.
   */
  public Iterable<AnnotationFS> getSubIterator() {
    return this.it;
  }
}
