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

package opennlp.tools.chunker;

import opennlp.tools.util.BeamSearchContextGenerator;
import opennlp.tools.util.TokenTag;

/**
 * Interface for a {@link BeamSearchContextGenerator} used in syntactic chunking.
 */
public interface ChunkerContextGenerator extends BeamSearchContextGenerator<TokenTag> {

  /**
   * Retrieves the contexts for chunking of the specified {@code idx}.
   *
   * @param idx The index of the token in the specified {@code toks} array for which the context
   *            should be constructed.
   * @param toks The tokens of the sentence. The {@code toString} methods of these objects
   *             should return the token text.
   * @param tags The POS tags for the specified tokens.
   * @param preds The previous decisions made in the tagging of this sequence.
   *              Only indices less than {@code idx} will be examined.
   * @return An array of predictive contexts on which a model basis its decisions.
   */
  String[] getContext(int idx, String[] toks, String[] tags, String[] preds);
}
