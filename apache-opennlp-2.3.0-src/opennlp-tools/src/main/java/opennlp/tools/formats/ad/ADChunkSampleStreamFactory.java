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

package opennlp.tools.formats.ad;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import opennlp.tools.chunker.ChunkSample;
import opennlp.tools.cmdline.ArgumentParser;
import opennlp.tools.cmdline.ArgumentParser.OptionalParameter;
import opennlp.tools.cmdline.ArgumentParser.ParameterDescription;
import opennlp.tools.cmdline.CmdLineUtil;
import opennlp.tools.cmdline.StreamFactoryRegistry;
import opennlp.tools.commons.Internal;
import opennlp.tools.formats.LanguageSampleStreamFactory;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 * A Factory to create a Arvores Deitadas ChunkStream from the command line
 * utility.
 * <p>
 * <b>Note:</b>
 * Do not use this class, internal use only!
 */
@Internal
public class ADChunkSampleStreamFactory<P> extends LanguageSampleStreamFactory<ChunkSample, P> {

  interface Parameters {
    //all have to be repeated, because encoding is not optional,
    //according to the check if (encoding == null) { below (now removed)
    @ParameterDescription(valueName = "charsetName",
        description = "encoding for reading and writing text, if absent the system default is used.")
    Charset getEncoding();

    @ParameterDescription(valueName = "sampleData", description = "data to be used, usually a file name.")
    File getData();

    @ParameterDescription(valueName = "language", description = "language which is being processed.")
    String getLang();

    @ParameterDescription(valueName = "start", description = "index of first sentence")
    @OptionalParameter
    Integer getStart();

    @ParameterDescription(valueName = "end", description = "index of last sentence")
    @OptionalParameter
    Integer getEnd();
  }

  public static void registerFactory() {
    StreamFactoryRegistry.registerFactory(ChunkSample.class,
        "ad", new ADChunkSampleStreamFactory<>(Parameters.class));
  }

  protected ADChunkSampleStreamFactory(Class<P> params) {
    super(params);
  }

  @Override
  public ObjectStream<ChunkSample> create(String[] args) {

    Parameters params = ArgumentParser.parse(args, Parameters.class);
    language = params.getLang();

    InputStreamFactory sampleDataIn = CmdLineUtil.createInputStreamFactory(params.getData());
    ObjectStream<String> lineStream = null;
    try {
      lineStream = new PlainTextByLineStream(sampleDataIn, params.getEncoding());
    } catch (IOException ex) {
      CmdLineUtil.handleCreateObjectStreamError(ex);
    }

    ADChunkSampleStream sampleStream = new ADChunkSampleStream(lineStream);
    if (params.getStart() != null && params.getStart() > -1) {
      sampleStream.setStart(params.getStart());
    }

    if (params.getEnd() != null && params.getEnd() > -1) {
      sampleStream.setEnd(params.getEnd());
    }

    return sampleStream;
  }
}
