/*
 * Copyright 2022 Tigris Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tigrisdata.db.client;

import com.tigrisdata.db.client.error.TigrisException;
import com.tigrisdata.db.type.TigrisDocumentCollectionType;
import com.tigrisdata.db.type.TigrisMessageCollectionType;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/** Tigris async database */
public interface TigrisAsyncDatabase {

  /**
   * Return list of {@link CollectionInfo}
   *
   * @return a future to the {@link List} of {@link CollectionInfo} representing collection
   */
  CompletableFuture<List<CollectionInfo>> listCollections();

  /**
   * Creates or updates collections
   *
   * @param collectionModelTypes an array of collection model classes
   * @return future to the {@link CreateOrUpdateCollectionsResponse}
   */
  CompletableFuture<CreateOrUpdateCollectionsResponse> createOrUpdateCollections(
      Class<? extends TigrisDocumentCollectionType>... collectionModelTypes);

  /**
   * Creates or updates topics
   *
   * @param topicModelTypes an array of topic model classes
   * @return future to the {@link CreateOrUpdateTopicResponse}
   */
  CompletableFuture<CreateOrUpdateTopicResponse> createOrUpdateTopics(
      Class<? extends TigrisMessageCollectionType>... topicModelTypes);

  /**
   * Creates or updates collections by scanning classpath packages and applying user's filter *
   * (optionally) The alternate method {@link TigrisDatabase#createOrUpdateCollections(Class[])} is
   * recommended where user specifies fixed list of classes to avoid classpath scan at runtime.
   *
   * @param packagesToScan an array of Java packages to scan for collection model.
   * @param filter optional filter to filter out classes from scanned set of classes
   * @return future to the {@link CreateOrUpdateCollectionsResponse}
   */
  CompletableFuture<CreateOrUpdateCollectionsResponse> createOrUpdateCollections(
      String[] packagesToScan,
      Optional<Predicate<Class<? extends TigrisDocumentCollectionType>>> filter);

  /**
   * Drops the collection.
   *
   * @param documentCollectionTypeClass Class type of the collection
   * @param <T> type of the collection that is of type {@link TigrisDocumentCollectionType}
   * @return the future to the {@link DropCollectionResponse}
   */
  <T extends TigrisDocumentCollectionType> CompletableFuture<DropCollectionResponse> dropCollection(
      Class<T> documentCollectionTypeClass);

  /**
   * Return an instance of {@link TigrisCollection}
   *
   * @param documentCollectionTypeClass Class type of the collection
   * @param <C> type of the collection that is of type {@link TigrisDocumentCollectionType}
   * @return an instance of {@link TigrisAsyncCollection}
   */
  <C extends TigrisDocumentCollectionType> TigrisAsyncCollection<C> getCollection(
      Class<C> documentCollectionTypeClass);

  /**
   * Return an instance of {@link TigrisAsyncTopic}
   *
   * @param messageCollectionTypeClass Class type of the messages collection
   * @param <C> type of the message collection that is of type {@link TigrisMessageCollectionType}
   * @return an instance of {@link TigrisAsyncTopic}
   */
  <C extends TigrisMessageCollectionType> TigrisAsyncTopic<C> getTopic(
      Class<C> messageCollectionTypeClass);

  /**
   * Begins the transaction on current database
   *
   * @param transactionOptions options
   * @return the future to the {@link TransactionSession}
   */
  CompletableFuture<TransactionSession> beginTransaction(TransactionOptions transactionOptions);

  /**
   * @return the future to the {@link DatabaseDescription} containing description of database.
   * @throws TigrisException in case of an error.
   */
  CompletableFuture<DatabaseDescription> describe() throws TigrisException;

  /** @return name of the current database */
  String name();
}
