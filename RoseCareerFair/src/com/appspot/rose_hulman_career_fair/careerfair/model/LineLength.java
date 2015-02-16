/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-01-14 17:53:03 UTC)
 * on 2015-02-16 at 22:19:45 UTC 
 * Modify at your own risk.
 */

package com.appspot.rose_hulman_career_fair.careerfair.model;

/**
 * Model definition for LineLength.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the careerfair. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class LineLength extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("company_entity_key")
  private java.lang.String companyEntityKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String entityKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long length;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getCompanyEntityKey() {
    return companyEntityKey;
  }

  /**
   * @param companyEntityKey companyEntityKey or {@code null} for none
   */
  public LineLength setCompanyEntityKey(java.lang.String companyEntityKey) {
    this.companyEntityKey = companyEntityKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getEntityKey() {
    return entityKey;
  }

  /**
   * @param entityKey entityKey or {@code null} for none
   */
  public LineLength setEntityKey(java.lang.String entityKey) {
    this.entityKey = entityKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getLength() {
    return length;
  }

  /**
   * @param length length or {@code null} for none
   */
  public LineLength setLength(java.lang.Long length) {
    this.length = length;
    return this;
  }

  @Override
  public LineLength set(String fieldName, Object value) {
    return (LineLength) super.set(fieldName, value);
  }

  @Override
  public LineLength clone() {
    return (LineLength) super.clone();
  }

}
