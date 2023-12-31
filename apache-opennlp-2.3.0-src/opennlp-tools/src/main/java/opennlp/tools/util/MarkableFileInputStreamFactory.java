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

package opennlp.tools.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A factory that creates {@link MarkableFileInputStream} from a {@link File}
 */
public class MarkableFileInputStreamFactory implements InputStreamFactory {

  private final File file;

  /**
   * Initializes a {@link MarkableFileInputStreamFactory}.
   *
   * @param file The {@link File} used as input source.
   *
   * @throws FileNotFoundException Thrown if {@code file} could not be found.
   */
  public MarkableFileInputStreamFactory(File file) throws FileNotFoundException {
    if (!file.exists()) {
      throw new FileNotFoundException("File '" + file + "' cannot be found");
    }
    this.file = file;
  }

  @Override
  public InputStream createInputStream() throws IOException {
    return new MarkableFileInputStream(file);
  }
}
