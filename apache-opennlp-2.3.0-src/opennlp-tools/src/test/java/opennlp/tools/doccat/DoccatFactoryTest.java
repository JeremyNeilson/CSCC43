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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import opennlp.tools.formats.ResourceAsStreamFactory;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * Tests for the {@link DoccatFactory} class.
 */
public class DoccatFactoryTest {

  private static ObjectStream<DocumentSample> createSampleStream()
      throws IOException {

    InputStreamFactory isf = new ResourceAsStreamFactory(
        DoccatFactoryTest.class, "/opennlp/tools/doccat/DoccatSample.txt");

    return new DocumentSampleStream(new PlainTextByLineStream(isf, StandardCharsets.UTF_8));
  }

  private static DoccatModel train() throws IOException {
    return DocumentCategorizerME.train("x-unspecified", createSampleStream(),
        TrainingParameters.defaultParams(), new DoccatFactory());
  }

  private static DoccatModel train(DoccatFactory factory) throws IOException {
    return DocumentCategorizerME.train("x-unspecified", createSampleStream(),
        TrainingParameters.defaultParams(), factory);
  }

  @Test
  void testDefault() throws IOException {
    DoccatModel model = train();

    Assertions.assertNotNull(model);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    model.serialize(out);
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

    DoccatModel fromSerialized = new DoccatModel(in);

    DoccatFactory factory = fromSerialized.getFactory();

    Assertions.assertNotNull(factory);

    Assertions.assertEquals(1, factory.getFeatureGenerators().length);
    Assertions.assertEquals(BagOfWordsFeatureGenerator.class,
        factory.getFeatureGenerators()[0].getClass());

  }

  @Test
  void testCustom() throws IOException {
    FeatureGenerator[] featureGenerators = {new BagOfWordsFeatureGenerator(),
        new NGramFeatureGenerator(), new NGramFeatureGenerator(2, 3)};

    DoccatFactory factory = new DoccatFactory(featureGenerators);

    DoccatModel model = train(factory);

    Assertions.assertNotNull(model);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    model.serialize(out);
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

    DoccatModel fromSerialized = new DoccatModel(in);

    factory = fromSerialized.getFactory();

    Assertions.assertNotNull(factory);

    Assertions.assertEquals(3, factory.getFeatureGenerators().length);
    Assertions.assertEquals(BagOfWordsFeatureGenerator.class,
        factory.getFeatureGenerators()[0].getClass());
    Assertions.assertEquals(NGramFeatureGenerator.class,
        factory.getFeatureGenerators()[1].getClass());
    Assertions.assertEquals(NGramFeatureGenerator.class, factory.getFeatureGenerators()[2].getClass());
  }

}
