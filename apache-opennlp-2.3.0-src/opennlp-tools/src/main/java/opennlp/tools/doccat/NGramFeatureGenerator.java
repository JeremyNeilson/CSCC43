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

package opennlp.tools.doccat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import opennlp.tools.util.InvalidFormatException;

/**
 * Generates ngram features for a document.
 *
 * @see FeatureGenerator
 */
public class NGramFeatureGenerator implements FeatureGenerator {

  private final int minGram;
  private final int maxGram;

  /**
   * Instantiates an {@link NGramFeatureGenerator} instance with configurable ngram parameters.
   *
   * @param minGram The minimum words in ngram features.
   * @param maxGram The maximum words in ngram features.
   * @throws InvalidFormatException Thrown if parameter values are invalid or inconsistent.
   */
  public NGramFeatureGenerator(int minGram, int maxGram) throws InvalidFormatException {
    if (minGram > 0 && maxGram > 0) {
      if (minGram <= maxGram) {
        this.minGram = minGram;
        this.maxGram = maxGram;
      } else {
        throw new InvalidFormatException(
            "Minimum range value (minGram) should be less than or equal to maximum range value (maxGram)!");
      }
    } else {
      throw new InvalidFormatException("Both minimum range value (minGram) & maximum " +
          "range value (maxGram) should be greater than or equal to 1!");
    }
  }

  /**
   * Instantiates an {@link NGramFeatureGenerator} instance with a Bi grams config.
   *
   * @throws InvalidFormatException Thrown if parameter values are invalid or inconsistent.
   */
  public NGramFeatureGenerator() throws InvalidFormatException {
    this(2, 2);
  }

  @Override
  public Collection<String> extractFeatures(String[] text, Map<String, Object> extraInfo) {
    Objects.requireNonNull(text, "text must not be null");
    List<String> features = new ArrayList<>();

    for (int i = 0; i <= text.length - minGram; i++) {
      final StringBuilder sb = new StringBuilder();
      sb.append("ng=");
      for (int y = 0; y < maxGram && i + y < text.length; y++) {
        sb.append(":");
        sb.append(text[i + y]);
        int gramCount = y + 1;
        if (maxGram >= gramCount && gramCount >= minGram) {
          features.add(sb.toString());
        }
      }
    }

    return features;
  }
}
