/*
 * Copyright (c) 2011-2020 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.vertx.core.net;

import io.vertx.core.Vertx;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.X509KeyManager;
import java.util.function.Function;

/**
 * @author Hakan Altindag
 */
public class KeyCertOptionsWrapper implements KeyCertOptions {

  private final KeyManagerFactory keyManagerFactory;

  public KeyCertOptionsWrapper(KeyManagerFactory keyManagerFactory) {
    if (keyManagerFactory == null
      || keyManagerFactory.getKeyManagers() == null
      || keyManagerFactory.getKeyManagers().length == 0) {
      throw new IllegalArgumentException("KeyManagerFactory is not present or is not initialized yet");
    }
    this.keyManagerFactory = keyManagerFactory;
  }

  public KeyCertOptionsWrapper(KeyManager keyManager) {
    this(new KeyManagerFactoryWrapper(keyManager));
  }

  private KeyCertOptionsWrapper(KeyCertOptionsWrapper keyCertOptionsWrapper) {
    this.keyManagerFactory = keyCertOptionsWrapper.keyManagerFactory;
  }

  @Override
  public KeyCertOptions copy() {
    return new KeyCertOptionsWrapper(this);
  }

  @Override
  public KeyManagerFactory getKeyManagerFactory(Vertx vertx) {
    return keyManagerFactory;
  }

  @Override
  public Function<String, X509KeyManager> keyManagerMapper(Vertx vertx) {
    return keyManagerFactory.getKeyManagers()[0] instanceof X509KeyManager ? serverName -> (X509KeyManager) keyManagerFactory.getKeyManagers()[0] : null;
  }

}
