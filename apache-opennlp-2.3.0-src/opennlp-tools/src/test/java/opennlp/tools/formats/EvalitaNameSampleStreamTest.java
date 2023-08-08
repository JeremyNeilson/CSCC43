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

package opennlp.tools.formats;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import opennlp.tools.formats.EvalitaNameSampleStream.LANGUAGE;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;

/**
 * Note:
 * Sample training data must be UTF-8 encoded and uncompressed!
 */
public class EvalitaNameSampleStreamTest extends AbstractSampleStreamTest {

  @Test
  void testParsingItalianSample() throws IOException {

    try (ObjectStream<NameSample> sampleStream = openData()) {
      NameSample personName = sampleStream.read();
      Assertions.assertNotNull(personName);

      Assertions.assertEquals(11, personName.getSentence().length);
      Assertions.assertEquals(1, personName.getNames().length);
      Assertions.assertTrue(personName.isClearAdaptiveDataSet());

      Span nameSpan = personName.getNames()[0];
      Assertions.assertEquals(8, nameSpan.getStart());
      Assertions.assertEquals(10, nameSpan.getEnd());
      Assertions.assertTrue(personName.isClearAdaptiveDataSet());

      Assertions.assertEquals(0, sampleStream.read().getNames().length);

      Assertions.assertNull(sampleStream.read());
    }
  }

  @Test
  void testReset() throws IOException {
    try (ObjectStream<NameSample> sampleStream = openData()) {
      NameSample sample = sampleStream.read();
      sampleStream.reset();
      Assertions.assertEquals(sample, sampleStream.read());
    }
  }

  private ObjectStream<NameSample> openData() throws IOException {
    return new EvalitaNameSampleStream(LANGUAGE.IT, getFactory("evalita-ner-it.sample"),
            EvalitaNameSampleStream.GENERATE_PERSON_ENTITIES);
  }
}
