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

package opennlp.tools.postag;

import java.util.Arrays;

import opennlp.tools.util.SequenceValidator;

/**
 * The default POS tagger {@link SequenceValidator} implementation.
 */
public class DefaultPOSSequenceValidator implements SequenceValidator<String> {

  private final TagDictionary tagDictionary;

  /**
   * @param tagDictionary A {@link TagDictionary} used for the new {@link SequenceValidator}.
   */
  public DefaultPOSSequenceValidator(TagDictionary tagDictionary) {
    this.tagDictionary = tagDictionary;
  }

  @Override
  public boolean validSequence(int i, String[] inputSequence,
      String[] outcomesSequence, String outcome) {
    if (tagDictionary == null) {
      return true;
    } else {
      String[] tags = tagDictionary.getTags(inputSequence[i]);
      return tags == null || Arrays.asList(tags).contains(outcome);
    }
  }
}
