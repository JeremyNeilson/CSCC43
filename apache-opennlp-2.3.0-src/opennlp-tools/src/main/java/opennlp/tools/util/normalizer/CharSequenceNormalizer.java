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
package opennlp.tools.util.normalizer;

import java.io.Serializable;

/**
 * A char sequence normalizer, used to adjusting (prune, substitute, add, etc.)
 * characters in order to remove noise from text
 *
 * @see <a href="https://en.wikipedia.org/wiki/Text_normalization">Text normalization</a>
 */
public interface CharSequenceNormalizer extends Serializable {

  /**
   * Normalizes a sequence of characters.
   *
   * @param text The {@link CharSequence} to normalize.
   * @return The normalized {@link CharSequence}.
   */
  CharSequence normalize(CharSequence text);
}
