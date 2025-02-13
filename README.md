# Quarkus GraphQL Quickstart + MinIO as Object Store S3

[![Keep a Changelog v1.1.0 badge](https://img.shields.io/badge/changelog-Keep%20a%20Changelog%20v1.1.0-%23E05735)](CHANGELOG.md)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![code of conduct](https://img.shields.io/badge/Conduct-Contributor%20Covenant%202.1-purple.svg)](CODE_OF_CONDUCT.md)
![Build with Maven](https://github.com/amusarra/quarkus-graphql-quickstart/actions/workflows/build_via_quarkus_cli.yml/badge.svg)
![CI Docker build](https://github.com/amusarra/quarkus-graphql-quickstart/actions/workflows/docker_publish.yml/badge.svg)
![CI Docker build native amd64](https://github.com/amusarra/quarkus-graphql-quickstart/actions/workflows/docker_publish_native_quarkus_cli.yml/badge.svg)

This project is a demonstration of a Quarkus application that exposes data through a traditional RESTful API (`quarkus-rest`) and a GraphQL API (`quarkus-smallrye-graphql`). The project uses Hibernate ORM with Panache (`quarkus-hibernate-orm-panache`) for data persistence and includes configurations for H2 database (for development), PostgreSQL (for production profiles), and MinIO as an S3 Object Store (`io.quarkiverse.minio:quarkus-minio`).

[MinIO](https://min.io/) is a high-performance, distributed object storage server compatible with Amazon S3, easily integrable with Quarkus via the `quarkus-minio` extension and DevServices.

[GraphQL](https://graphql.org/) is a query language for APIs that allows clients to request exactly the data they need, avoiding over-fetching or under-fetching. Unlike REST APIs, it offers a single endpoint through which the client can obtain data structured according to their needs.

Main advantages over custom APIs:
1. Flexibility: The client decides which data to obtain, reducing unnecessary payloads.
2. Performance: Fewer calls, thanks to data aggregation from multiple sources in a single query.
3. Evolvability: Backend changes can be introduced without breaking existing queries.
4. Standardization: Using GraphQL reduces the need to design custom APIs, simplifying development and maintenance.

This approach improves user experience and accelerates the development of client and server applications.

| **Pros**                                                                            | **Cons**                                                                                |
|------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------|
| **Customized requests**: clients get only the data they need.                      | **Initial complexity**: requires a steeper learning curve compared to REST.               |
| **Single Endpoint**: a single access point for all queries and mutations.          | **More complex caching**: less intuitive compared to REST (which uses unique URLs).       |
| **Data aggregation**: allows combining data from multiple sources in one call.     | **Server overhead**: complex queries can overload the server if not well managed.         |
| **Evolvability**: fields can be deprecated without breaking the client.            | **Debugging tools**: less common compared to those for REST.                              |
| **Integrated documentation**: the schema serves as documentation for the APIs.     | **Complex authorizations**: more intricate permission management for dynamic queries.     |
| **Reduced calls**: optimizes client-server communications.                         | **Initial setup**: requires more configuration and tools compared to REST.                |

> Note: This project has an educational and demonstrative approach. There are intentionally incomplete parts of the code with comments to guide the user in completing the implementation. For example:
> ```java
>    @Mutation
>    @Description("Create a new author")
>    @Transactional
>    public Author createAuthor(Author author) {
>        // The author is persisted automatically by Panache
>        // because it is a Panache entity.
>        // Extend this method to handle the detached entity as needed.
>        author.persist();
>        return author;
>    }
>```

The project follows the classic three-layer architecture.

* Persistence Layer (ORM/Panache): uses Hibernate ORM with Panache for managing and persisting data in the database.
* Service/API Layer (GraphQL and REST): exposes data through GraphQL APIs and REST APIs, providing an interface for interactions.
* Presentation Layer (Optional): the project does not include a dedicated user interface layer.

The diagram below highlights the layering of the application.

![Stratificazione dell'applicazione](src/main/docs/resources/images/architettura-applicazione-quarkus-graphql-1.webp)

Figure 1 - Layering of the Quarkus GraphQL application

## Features

* Implementation of a GraphQL API with Quarkus.
* Use of Panache for data persistence with Hibernate ORM.
* GraphQL schema defined with types, queries, and mutations.
* Integration of pagination with GraphQL.
* Example queries to retrieve books and authors.
* Example mutations to create, update, and delete books, authors, and publishers.
* Storage of book covers on MinIO, both in development (with Dev Services) and in production.
* Example tests.

Below are the main endpoints of the application.

| Endpoint       | Type    | Description                                                              |
|----------------|---------|--------------------------------------------------------------------------|
| `/api/graphql` | GraphQL | Endpoint for the GraphQL API                                             |
| `/api/books`   | REST    | Endpoint for the REST API                                                |
| `/api/s3/files`| REST    | Endpoint for the REST API for uploading and downloading files to MinIO   |
| `/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/schema-yaml` | Schema  | Endpoint for the OpenAPI schema in YAML format      |
| `/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/schema-json` | Schema  | Endpoint for the OpenAPI schema in JSON format      |
| `/q/dev-ui/io.quarkus.quarkus-smallrye-graphql/graphql-schema` | Schema  | Endpoint for the GraphQL schema                      |

Table 1 - Main endpoints of the Quarkus GraphQL application

## Requirements

To run or develop the project, make sure you have the following tools installed.

* Git 2.33+
* JDK 21+, [GraalVM](https://www.graalvm.org/downloads/) 21+ (for native build)
* Container tools like Docker or Podman
* Apache Maven 3.9.9 (optional if using the Maven wrapper integrated with the sample project)
* [Quarkus CLI](https://quarkus.io/guides/cli-tooling) 3.18.x (optional, but recommended)

## Quickstart

To launch the project in development (or dev) mode and test the GraphQL and REST functionalities, follow these steps:

1. Clone the project's git repository;
2. Start the application in development mode;
3. Test the GraphQL and REST APIs.
4. Test the MinIO APIs for file upload and download.

To clone the project's git repository, run the command:

```shell
# Clone the project's git repository
git clone https://github.com/amusarra/quarkus-graphql-quickstart.git
```

To start the application in development mode, use one of the following methods:

```shell
# Start the application in development mode
# by using the Maven wrapper integrated with the project.
./mvnw quarkus:dev
```

```shell
# By using the Quarkus CLI.
# This command requires the Quarkus CLI to be installed on your system.
quarkus dev
```

> **Note**: Before starting the application in dev mode, make sure you have correctly installed and configured Docker or Podman on your system. In case of incorrect configuration or lack of Docker or Podman, the application will not start correctly and you may encounter errors like those reported below:
>
> ```shell
> 2025-01-29 09:33:31,911 WARN  [org.tes.doc.DockerClientProviderStrategy] (build-26) DOCKER_HOST unix:///var/run/docker.sock is not listening: java.io.IOException: com.sun.jna.LastErrorException: [61] Connection refused
> 2025-01-29 09:33:31,929 ERROR [org.tes.doc.DockerClientProviderStrategy] (build-26) Could not find a valid Docker environment. Please check configuration. Attempted configurations were:
>	DockerDesktopClientProviderStrategy: failed with exception NullPointerException (Cannot invoke "java.nio.file.Path.toString()" because the return value of "org.testcontainers.dockerclient.DockerDesktopClientProviderStrategy.getSocketPath()" is null)As no valid configuration was found, execution cannot continue.
> See https://java.testcontainers.org/on_failure.html for more details.
> 2025-01-29 09:33:31,969 WARN  [io.qua.dep.uti.ContainerRuntimeUtil] (build-26) Command "docker info" exited with error code 1. Rootless container runtime detection might not be reliable or the container service is not running at all.
> 2025-01-29 09:33:31,971 INFO  [org.tes.DockerClientFactory] (build-26) Testcontainers version: 1.20.4 
> ```

To test the GraphQL and REST APIs, open your browser and visit the following URLs:

* GraphQL API using the [GraphiQL UI](https://github.com/graphql/graphiql): <http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-graphql/graphql-ui>
* REST API using the Swagger UI: <http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/swagger-ui>

Using the GraphiQL UI, you can execute queries and [mutations](https://graphql.org/learn/mutations/) to retrieve and modify database data. Below is a demo example of how to execute GraphQL queries to retrieve and create books, authors, and publishers, as well as create and retrieve S3 objects from the MinIO Object Store.

![Demo di esempio su come eseguire query GraphQL](src/main/docs/resources/images/demo-graphql-ui.gif)

Animation 1 - Example demo on how to execute GraphQL queries

Below are the example queries used in the demo:

```graphql
# Get all Books
query allBooks {
   allBooks {
    id
    title
    editor {
      id
      name
    }
    authors {
      id
      firstName
      lastName
    }
  }
}

# Get Book by Id
query getBook {
  book(bookId: 5) {
    id
    title
    subTitle
    frontCoverImageUrl
    backCoverImageUrl
    pages
    summary
    languages
    formats
    languages
    authors {
      firstName
      lastName
    }
    editor {
      name
    }
  }
}

# Get Author by Id
query getAuthor {
  author(authorId: 5) {
    firstName
    lastName
    sex
    birthDate
    books {
      id
      title
    }
  }
}

# Get Editor by Id
query getEditor {
  editor(editorId: 5) {
    name
    books {
      title
      subTitle
      summary
    }
  }
}

# Mutation to create a Book with an Author and an Editor that already exist
mutation createBook {
  createBook(
    book: {title: "Libro collaborativo", 
      isbn: "7650986575646", 
      pages: 567, 
      summary: "Summary of the book", 
      publication: "2025-01-28", 
      genre: "fantasy", 
      languages: ["IT"], 
      formats: ["EPUD", "PDF"], 
      keywords: ["key1"], 
      authors: [{id: 5}], 
      editor: {id: 5}}
  ) {
    id
    title
  }
}

# Mutation to create a Book with a new Author and an Editor that already exist
mutation createBookWithNewAuthor {
  createBook(
    book: {title: "Montare a cavallo con Quarkus + GraphQL", 
      isbn: "110986575646", 
      pages: 120, 
      summary: "Summary of the book", 
      publication: "2025-01-28", 
      genre: "equitazione", 
      languages: ["IT"], 
      formats: ["EPUD", "PDF"], 
      keywords: ["sport", "equitazione"], 
      authors: [
        {
          firstName: "Antonio",
          lastName: "Musarra",
          sex: "M",
          birthDate: "2025-01-28"
        }
      ], 
      editor: {id: 5}}
  ) {
    id
    title
  }
}

# Mutation to upload a file to MinIO
mutation uploadFile {
  uploadFile(
    objectName: "example.txt", 
    bucketName: "my-bucket" 
    content: "Q29udGVudG8gZGVsIGZpbGUgZXNlbXBpbGU=") {
    
    objectName
    url
  }
}

# Mutation to download a file from MinIO
query getFile {
  getFile(objectName: "example.txt", bucketName: "my-bucket-1") {
    url
    size
    eTag
    contentType
    content
  }
}
```

You can also test GraphQL queries and mutations using cURL, Postman, or any other HTTP client. Below is an example of a GraphQL query to retrieve all books:

```shell
curl -k -X POST http://localhost:8080/api/graphql \
  -H "Content-Type: application/json" \
  -d '{
    "query": "query { allBooks {
          title
          isbn
          publication
          genre
          pages
          summary
          frontCoverImageUrl
          frontCoverImageUrl
          languages
          formats
          authors {
            firstName
            lastName
          }
          editor {
            name
          }
        }
      }"
  }'
```

Here is an example of a GraphQL mutation to create a book:

```shell
curl -X POST http://localhost:8080/api/graphql \
     -H "Content-Type: application/json" \
     -d '{
       "query": "
         mutation createBook {
           createBook(
             book: {
               title: \"Libro da author e editor esistenti\",
               subTitle: \"Creato con Quarkus + GraphQL\",
               isbn: \"7650986575646\",
               pages: 567,
               summary: \"Summary of the book\",
               publication: \"2025-01-28\",
               genre: \"fantasy\",
               languages: [\"IT\"],
               formats: [\"EPUB\", \"PDF\"],
               keywords: [\"key1\"],
               authors: [{ id: 5 }],
               editor: { id: 5 }
             }
           ) {
             id
             title
           }
         }
       "
     }'
```

The output of the mutation will be similar to the following:

```json
{
   "data": {
      "createBook": {
         "id": 54,
         "title": "Libro da author e editor esistenti"
      }
   }
}
```

You can execute the query to retrieve a book by ID using the following cURL command:

```shell
curl -X POST "http://localhost:8080/api/graphql" \
     -H "Content-Type: application/json" \
     -d '{
       "query": "query getBook {
                   book(bookId: 54) {
                     id
                     title
                     subTitle
                     authors {
                       firstName
                       lastName
                     }
                     editor {
                       name
                     }
                   }
                 }"
     }'
```

Output of the query to retrieve a book by ID:

```json
{
   "data": {
      "book": {
         "id": 54,
         "title": "Libro da author e editor esistenti",
         "subTitle": "Creato con Quarkus + GraphQL",
         "authors": [
            {
               "firstName": "David",
               "lastName": "Jones"
            }
         ],
         "editor": {
            "name": "Future Books Inc."
         }
      }
   }
}
```

Using the Swagger UI, you can perform REST requests to retrieve, create, update, and delete books and authors, as well as upload and download files to MinIO. From the Swagger UI, you can also view the OpenAPI schema in JSON and YAML formats and the possible calls you can execute via cURL.

![OpenAPI SwaggerUI per eseguire richieste REST](src/main/docs/resources/images/openapi-ui.jpg)

Figure 2 - OpenAPI SwaggerUI for executing REST requests

## Application Configuration
The key points of the application configuration include:

1. Enabling and configuring the HTTPS protocol
2. Configuring the GraphQL endpoint
3. Configuring the database (H2 for dev profile and PostgreSQL for prod profile)
4. Configuring ORM
5. Configuring MinIO
6. Configuring the OpenShift extension

To apply changes to the current application configuration, modify the `application.properties` file located at `src/main/resources/application.properties`.

### Application lifecycle
At startup, the application, load the data from the `sample_data.sql` file located in the `src/main/resources` directory. This file contains the initial data to populate the database tables. The data is loaded automatically by Quarkus at startup by setting the `quarkus.hibernate-orm.sql-load-script` property to `sample_data.sql`.

At startup, the application also creates the MinIO bucket `book-cover` to store the files uploaded to MinIO. The method `BookFrontBackCoverLoader#onBookFrontCoverUpload` is an event listener that triggers on application startup to upload book front and back cover images to Minio. It uses the @Observes annotation to listen for the StartupEvent.

After the upload files to MinIO is completed, the call `uploadEvent.fire(new UploadEvent(bucketName, uploadedFiles))` fire an UploadEvent to notify the listeners that the files have been uploaded. The `uploadEvent.fire` method is called with a new instance of the `UploadEvent` class, which contains the `bucketName` and the list of `uploadedFiles`. This is done to trigger any event listeners that are observing the UploadEvent. See the method `BookFrontBackCoverLoader#onUploadEvent(@Observes UploadEvent event)`.

When you start application in development mode, should see the following output in the console.

```shell
2025-02-01 23:57:23,945 DEBUG [it.don.lab.qua.gra.s3.ser.MinioService] (Quarkus Main Thread) Uploading object '9780810885720_algorithmic_pattern_analysis_back_cover.jpg' with byte array content to bucket 'book-cover'...
2025-02-01 23:57:23,951 DEBUG [it.don.lab.qua.gra.s3.ser.MinioService] (Quarkus Main Thread) Uploaded object '9780810885720_algorithmic_pattern_analysis_back_cover.jpg' to bucket 'book-cover'
2025-02-01 23:57:23,952 DEBUG [it.don.lab.qua.gra.app.lif.s3.BookFrontBackCoverLoader] (Quarkus Main Thread) Uploaded book front and back cover image: {9780810885720_algorithmic_pattern_analysis_back_cover.jpg} to Minio into bucket name {book-cover}
2025-02-01 23:57:23,968 INFO  [it.don.lab.qua.gra.app.lif.s3.BookFrontBackCoverLoader] (Quarkus Main Thread) Received upload event for bucket name {book-cover} with uploaded files: {[algorithmic_pattern_analysis_front_cover.jpg, 9780785316371_networked_neural_strategy_back_cover.jpg, 9781501582327_introduction_to_data_science_back_cover.jpg, introduction_to_data_science_back_cover.jpg, 9781501582327_introduction_to_data_science_front_cover.jpg, 9780321967974_the_art_of_software_engineering_front_cover.jpg, networked_neural_strategy_front_cover.jpg, 9780596805190_machine_learning_algorithms_front_cover.jpg, the_art_of_software_engineering_back_cover.jpg, networked_neural_strategy_back_cover.jpg, machine_learning_algorithms_back_cover.jpg, 9780596805190_machine_learning_algorithms_back_cover.jpg, 9780785316371_networked_neural_strategy_front_cover.jpg, 9780810885720_algorithmic_pattern_analysis_front_cover.jpg, machine_learning_algorithms_front_cover.jpg, the_art_of_software_engineering_front_cover.jpg, 9780321967974_the_art_of_software_engineering_back_cover.jpg, introduction_to_data_science_front_cover.jpg, algorithmic_pattern_analysis_back_cover.jpg, 9780810885720_algorithmic_pattern_analysis_back_cover.jpg]}
...
2025-02-01 23:57:24,178 DEBUG [it.don.lab.qua.gra.app.lif.s3.BookFrontBackCoverLoader] (Quarkus Main Thread) Updated Book entity with ISBN {9780321967974} with front cover URL {http://localhost:39869/book-cover/9780321967974_the_art_of_software_engineering_front_cover.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioaccess%2F20250201%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250201T225724Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=3265bdd47f230528930148c3fdf5d1cfbeff5f1d28851b9af2c360e77b75fb36} and back cover URL {http://localhost:39869/book-cover/9780321967974_the_art_of_software_engineering_back_cover.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioaccess%2F20250201%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250201T225724Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=a772677a0c30ab1c10e93393d4e5d0e701f2d056517d5135fd2e8ffff23fdad7}
```

## Implementing Pagination Support
When working with large datasets, pagination is essential to optimize performance and improve user experience. With GraphQL, we can easily implement pagination using either the **cursor-based** model or the traditional **offset-based** approach.

This comparative table will help you quickly visualize the differences between cursor-based pagination and offset-based pagination.

| **Feature**                     | **Cursor-Based Pagination**        | **Offset-Based Pagination**        |
|---------------------------------|------------------------------------|------------------------------------|
| **Principle**                   | Navigation based on unique cursors | Navigation based on LIMIT and OFFSET |
| **Main Use**                    | Evolving datasets or infinite scrolling | Fixed pages, stable datasets |
| **Performance**                 | More efficient on large datasets and search operations | Good for small to medium datasets |
| **Handling Data Changes**       | Not affected by changes during navigation | Data may change between pages (inserts/deletes) |
| **Ease of Implementation**      | Requires cursor management | Simple to implement, similar to SQL |
| **Query Example**               | `find().range(start, end)` with Base64 cursors | `find().range(offset, limit)` (SQL LIMIT and OFFSET) |

Table 2 - Comparison between Cursor-Based and Offset-Based Pagination

Expanding on the table content, we can highlight the following points:

**Cursor-Based Pagination**
* Principle: Each element has a unique identifier (cursor) used to navigate between pages.
* Advantages:
    * Optimal for continuously changing datasets (no distortion due to changes between requests).
    * More efficient in infinite scrolling applications.
    * Ideal for avoiding gaps in pages.

**Offset-Based Pagination**
* Principle: Pagination is managed via LIMIT and OFFSET, similar to SQL queries, where you can define which page and how many items to load.
* Advantages:
    * Easy to implement and understand.
    * Convenient for situations with a fixed amount of data.
    * Good for scenarios where data order does not change frequently between requests.

This project uses [Panache](https://quarkus.io/guides/hibernate-orm-panache) and [Hibernate ORM](https://quarkus.io/guides/hibernate-orm), making it simple to implement both types of pagination. With Panache, pagination is managed through methods like `range(startIndex, endIndex)` and cursors for cursor-based pagination.

For this project, the choice fell on cursor-based pagination, as it is more efficient for large datasets and search operations. Additionally, it is more suitable for continuously changing datasets or infinite scrolling.

**What are the steps to implement cursor-based pagination?**

1. **Calculate the cursors**: Each element must have a unique identifier (cursor) used to navigate between pages.
2. **Add the cursors to the query**: Use the cursors to retrieve data based on position.
3. **Return the cursors and pagination information**: In addition to the data, return the cursors for each element and pagination information (e.g., hasNextPage, endCursor).
4. **Handle the cursors in the next query**: Use the returned cursors to retrieve data in the next query.
5. **Implement the pagination logic**: Manage the pagination logic in the GraphQL resolver.

To implement cursor-based pagination, we need three main elements: `BookConnection`, `BookEdge`, and `PageInfo`.
These elements play a fundamental role in cursor-based pagination in GraphQL. These objects represent the response structure, which includes the data and the necessary information to navigate between result pages.

### What is BookConnection
The `BookConnection` object represents a **connection of elements that can contain multiple edges** (in this case, multiple books). It contains data related to the edges (the individual books with their cursors) and pagination information (e.g., if there are more pages of data).

The main roles of BookConnection are:
* Aggregating the edges (list elements, in this case, books).
* Including a PageInfo object to provide pagination details (if there are more pages, end cursor, etc.).

Below is an example implementation of `BookConnection`, which is a GraphQL Type (see the `@Type` annotation). The complete code is available in the example project in `BookConnection.java`.

```java
package it.dontesta.labs.quarkus.graphql.pagination.type;

import org.eclipse.microprofile.graphql.Type;
import java.util.List;

/**
 * Represents a connection to a list of Book edges with pagination information.
 */
@Type
public class BookConnection {

  /**
   * A list of Book edges.
   */
  private final List<BookEdge> edges;

  /**
   * Pagination information for the connection.
   */
  private final PageInfo pageInfo;

  /**
   * Constructs a new BookConnection instance.
   *
   * @param edges a list of Book edges
   * @param pageInfo pagination information for the connection
   */
  private BookConnection(List<BookEdge> edges, PageInfo pageInfo) {
    this.edges = edges;
    this.pageInfo = pageInfo;
  }

  /**
   * Creates a new BookConnection instance.
   *
   * @param edges a list of Book edges
   * @param pageInfo pagination information for the connection
   * @return a new BookConnection instance
   */
  public static BookConnection create(List<BookEdge> edges, PageInfo pageInfo) {
    return new BookConnection(edges, pageInfo);
  }

  /**
   * Returns the list of Book edges.
   *
   * @return the list of Book edges
   */
  public List<BookEdge> getEdges() {
    return edges;
  }

  /**
   * Returns the pagination information for the connection.
   *
   * @return the pagination information
   */
  public PageInfo getPageInfo() {
    return pageInfo;
  }
}
```

### What is BookEdge
Each `BookEdge` represents a single book within a data connection. The edge is the link between the book and the cursor that allows you to navigate between pages.

The main roles of `BookEdge` are:
* Contains the node, which represents the actual book (the `Book` object).
* Contains the cursor, which **is a unique identifier** encoded (usually in Base64) that allows pagination between subsequent pages.

Below is an example implementation of `BookEdge`, which is a GraphQL Type (see the `@Type` annotation). The complete code is available in the example project in `BookEdge.java`.

```java
package it.dontesta.labs.quarkus.graphql.pagination.type;

import it.dontesta.labs.quarkus.graphql.orm.panache.entity.Book;
import org.eclipse.microprofile.graphql.Type;

/**
 * Represents an edge in a connection, containing a node and a cursor.
 */
@Type
public class BookEdge {

  /**
   * The node of type Book.
   */
  private final Book node;

  /**
   * The cursor for this edge.
   */
  private final String cursor;

  /**
   * Constructs a new BookEdge instance.
   *
   * @param node the Book node
   * @param cursor the cursor for this edge
   */
  private BookEdge(Book node, String cursor) {
    this.node = node;
    this.cursor = cursor;
  }

  /**
   * Creates a new BookEdge instance.
   *
   * @param node the Book node
   * @param cursor the cursor for this edge
   * @return a new BookEdge instance
   */
  public static BookEdge create(Book node, String cursor) {
    return new BookEdge(node, cursor);
  }

  /**
   * Returns the Book node.
   *
   * @return the Book node
   */
  public Book getNode() {
    return node;
  }

  /**
   * Returns the cursor for this edge.
   *
   * @return the cursor
   */
  public String getCursor() {
    return cursor;
  }
}
```

### What is PageInfo
The `PageInfo` object contains crucial information about pagination, such as whether there are more pages of data available and what the end cursor will be for navigating to the next page.

The main roles of `PageInfo` are:
* hasNextPage: indicates if there are more pages to load. If the list of books contains fewer items than requested (e.g., 10 books out of 20), this field will be true, indicating that more pages are available.
* endCursor: is the cursor of the last element in the current list, which you can use as `after` in subsequent queries to get the next page.

Below is an example implementation of `PageInfo`, which is a GraphQL Type (see the `@Type` annotation). The complete code is available in the example project in `PageInfo.java`.

```java
package it.dontesta.labs.quarkus.graphql.pagination.type;

import org.eclipse.microprofile.graphql.Type;

/**
 * Represents pagination information for a connection.
 */
@Type
public class PageInfo {

  /**
   * Indicates if there is a next page.
   */
  private final boolean hasNextPage;

  /**
   * The end cursor for the current page.
   */
  private final String endCursor;

  /**
   * Constructs a new PageInfo instance.
   *
   * @param hasNextPage indicates if there is a next page
   * @param endCursor the end cursor for the current page
   */
  private PageInfo(boolean hasNextPage, String endCursor) {
    this.hasNextPage = hasNextPage;
    this.endCursor = endCursor;
  }

  /**
   * Creates a new PageInfo instance.
   *
   * @param hasNextPage indicates if there is a next page
   * @param endCursor the end cursor for the current page
   * @return a new PageInfo instance
   */
  public static PageInfo create(boolean hasNextPage, String endCursor) {
    return new PageInfo(hasNextPage, endCursor);
  }

  /**
   * Returns if there is a next page.
   *
   * @return true if there is a next page, false otherwise
   */
  public boolean isHasNextPage() {
    return hasNextPage;
  }

  /**
   * Returns the end cursor for the current page.
   *
   * @return the end cursor
   */
  public String getEndCursor() {
    return endCursor;
  }
}
```

### How BookConnection, BookEdge, and PageInfo Work Together

1. **BookConnection**: It is the central point of the response, containing both `BookEdge` (the actual elements) and pagination information through the `PageInfo` object.
2. **BookEdge**: Each edge contains the book itself (the `Book` object) and a unique cursor that allows navigation through the pages.
3. **PageInfo**: Provides critical information for the client, such as whether there are subsequent pages and which cursor to use for the next page.

### Modifying the BookGraphQL Resolver
To implement cursor-based pagination, we need to make some changes to the `BookGraphQL` resolver. Specifically, we need to add a new `books` method that accepts pagination parameters like `first` (for the number of books to retrieve) and `after` (for the cursor of the previous element). Then, use Panache to retrieve the books in a paginated manner and return a list of `BookEdge` with their respective cursors.

Below is the implementation of the `books` method that accepts pagination parameters and returns a `BookConnection` with the books and pagination information. The complete code is available in the example project in `BookGraphQL.java`.

```java
    /**
 * Retrieves a paginated list of books.
 *
 * @param first the number of books to retrieve
 * @param after the cursor after which to start retrieving books
 * @return a BookConnection containing the list of books and pagination information
 * @throws GraphQLException if an error occurs during retrieval
 */
@Query
public BookConnection books(@Name("first") int first,
                            @NotEmpty @NotNull @Name("after") String after)
    throws GraphQLException {

  int startIndex;

  // Decode the cursor to get the start index
  try {
    String decoded = new String(Base64.getDecoder().decode(after));
    startIndex = Integer.parseInt(decoded) + 1;
  } catch (IllegalArgumentException e) {
    throw new GraphQLException("Invalid cursor format", e);
  }

  // Query Panache to get the books
  PanacheQuery<Book> query = Book.findAll();
  List<Book> books = query.range(startIndex, startIndex + first - 1).list();

  // Create the edges response with the cursor
  List<BookEdge> edges = books.stream()
      .map(book -> {
        String cursor = Base64.getEncoder().encodeToString(String.valueOf(book.id).getBytes());
        return BookEdge.create(book, cursor);
      })
      .toList();

  // Check if there are more pages
  String endCursor = edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor();
  boolean hasNextPage = (startIndex + first) < query.count();

  return BookConnection.create(edges, PageInfo.create(hasNextPage, endCursor));
}
```

Using the `range(startIndex, startIndex + first - 1)` method of Panache, we can retrieve books in a paginated manner. We add cursors (encoded in Base64) for each book and return a list of `BookEdge` along with pagination information. As a final step, we check if there are more pages available and return a `BookConnection` with the books and pagination information.

The cursors are encoded in Base64 to ensure they are unique and safe to use in queries. The `encodeCursor` function encodes the book ID, while `decodeCursor` decodes it for use in the next query.

These changes to `BookGraphQL` allow you to implement cursor-based pagination in GraphQL. The response structure includes a `BookConnection` object, which contains the `BookEdge` (with books and cursors) and pagination information via `PageInfo`.

Similarly, you can implement cursor-based pagination for other data types, such as authors, publishers, or any other entity. The structure is similar, with a `Connection` object containing the `Edge` and pagination information.

### Testing cursor-based pagination
To test cursor-based pagination, we can use the `books` query to retrieve, for example, the first three books. The `books` query accepts two parameters: `first` (the number of items to retrieve) and `after` (the cursor for navigation where MA== means 0).

```graphql
query {
  books(first: 3, after: "MA==") {
    edges {
      node {
        id
        title
        frontCoverImageUrl
        backCoverImageUrl
        authors {
          firstName
          lastName
        }
        isbn
      }
      cursor
    }
    pageInfo {
      hasNextPage
      endCursor
    }
  }
}
```

The expected output will be similar to the one shown below; where `edges` contains the retrieved books with their cursors (encoded in Base64), `pageInfo` indicates that there are more pages available (`hasNextPage`: true) and provides the `endCursor` to get the next page.

```json
{
  "data": {
    "books": {
      "edges": [
        {
          "node": {
            "id": 4,
            "title": "Enhanced multimedia interface",
            "frontCoverImageUrl": null,
            "backCoverImageUrl": null,
            "authors": [
              {
                "firstName": "David",
                "lastName": "Jones"
              },
              {
                "firstName": "Sarah",
                "lastName": "Miller"
              }
            ],
            "isbn": "9780230278458"
          },
          "cursor": "NA=="
        },
        {
          "node": {
            "id": 5,
            "title": "Optimized data throughput",
            "frontCorverImageUrl": null,
            "backCorverImageUrl": null,
            "authors": [
              {
                "firstName": "Michael",
                "lastName": "Davis"
              },
              {
                "firstName": "Jessica",
                "lastName": "Wilson"
              }
            ],
            "isbn": "9781473689271"
          },
          "cursor": "NQ=="
        },
        {
          "node": {
            "id": 6,
            "title": "Decentralized systems integration",
            "frontCoverImageUrl": null,
            "backCoverImageUrl": null,
            "authors": [
              {
                "firstName": "Christopher",
                "lastName": "Garcia"
              },
              {
                "firstName": "Ashley",
                "lastName": "Rodriguez"
              }
            ],
            "isbn": "9781607436911"
          },
          "cursor": "Ng=="
        }
      ],
      "pageInfo": {
        "hasNextPage": true,
        "endCursor": "Ng=="
      }
    }
  }
}
```

The following output shows the query to retrieve the next page of books using the `endCursor` returned from a previous query. In this result, it is evident that `hasNextPage` is `false`, indicating that there are no more pages available.

```json
{
  "data": {
    "books": {
      "edges": [
        {
          "node": {
            "id": 16,
            "title": "Machine Learning Algorithms",
            "frontCoverImageUrl": "http://localhost:39869/book-cover/9780596805190_machine_learning_algorithms_front_cover.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioaccess%2F20250201%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250201T233159Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=8015c92ed39ca24596dff47d9dadb5f31d85e297b3a802b2914d90e6b64b2120",
            "backCorverImageUrl": "http://localhost:39869/book-cover/9780596805190_machine_learning_algorithms_back_cover.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioaccess%2F20250201%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250201T233159Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=4542ade0ca4a540b39deaf834c4e65f20637540de527cd0a10ef782943faeffd",
            "authors": [],
            "isbn": "9780596805190"
          },
          "cursor": "MTY="
        }
      ],
      "pageInfo": {
        "hasNextPage": false,
        "endCursor": "MTY="
      }
    }
  }
}
```

In this case, the book with ID 16 has been the front cover and back cover images uploaded to MinIO. Below is show the preview of the front cover image of the book with ID 16.

![Machine Learning Algorithms Front Cover](src/main/docs/resources/images/demo-with-image-preview-graphql-ui.jpg)

Figure 3 - Machine Learning Algorithms Front Cover stored in MinIO

## Subscriptions with GraphQL
[Subscriptions in GraphQL](https://graphql.org/learn/subscriptions/) allow you to receive real-time updates whenever a specific event occurs on the server. Unlike queries and mutations, which are single requests, subscriptions keep an open connection and send data to the client whenever the monitored event occurs.

GraphQL uses the [WebSocket](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API) protocol to handle subscriptions, allowing the server to send push updates to the client without the client having to make repeated requests ([polling](https://en.wikipedia.org/wiki/Polling_(computer_science))). This is particularly useful for real-time applications such as chat, news feeds, monitoring dashboards, and much more.

**How Do Subscriptions Work?**
1. The client executes a **subscription** specifying which type of event it wants to monitor.
2. The server keeps the WebSocket connection open.
3. Whenever a relevant event occurs (e.g., creation of a new book), the server sends the updated data to the client in real-time.

In Quarkus, you can implement subscriptions with SmallRye GraphQL, which supports WebSocket for real-time communication. To implement subscriptions in the simplest way possible, we will act directly on the `BookGraphQL` resolver to send real-time updates whenever a new book is created.

### Implementation of Subscriptions
To implement subscriptions, we need to follow these steps:
1. Add a `bookProcessor`: a `BroadcastProcessor` from Munity that notifies clients when a new book is created.
2. Modify the `createBook` method to send a book creation event to the `bookProcessor`.
3. Create a `bookCreated` method that returns a `Publisher` to receive real-time updates.

Below is the implementation of the steps described above. The complete code is available in the example project in `BookGraphQL.java`.

```java
    // Broadcast processor to notify subscribers
    private final BroadcastProcessor<Book> processor = BroadcastProcessor.create();

    /**
     * Creates a new book and notifies subscribers.
     *
     * @param book the book to create
     * @return the created book
     * @throws GraphQLException if an error occurs during creation
     */
    @Mutation
    @Description("Create a new book")
    @Transactional
    public Book createBook(Book book) throws GraphQLException {

        // Handle the editor and authors
        handleEditor(book);
        handleAuthors(book);

        // Persist the book and flush to get the ID
        entityManager.persist(book);
        entityManager.flush();

        // Notify subscribers
        processor.onNext(book);

        return book;
    }

    /**
     * Subscription method to notify subscribers when a new book is created.
     *
     * @return a Multi stream of Book objects representing the created books
     */
    @Subscription
    public Multi<Book> bookCreated() {
      return processor;
    }
```

After creating a new book, we send the book creation event to the `processor` using `processor.onNext(book)`. This notifies clients subscribed to the `bookCreated` subscription whenever a new book is created. The `bookCreated` method returns a `Multi` stream of books, representing the books created in real-time.

### Testing Subscriptions
To test subscriptions, we can use a WebSocket client like [GraphiQL](http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-graphql/graphql-ui) or [Apollo Client](https://www.apollographql.com/docs/react/get-started/). In this example, we will use GraphiQL to test subscriptions since it is already integrated with SmallRye GraphQL. Another client that can be used is [Altair GraphQL Client](https://altairgraphql.dev/).

To test subscriptions, follow these steps:
1. Start the application in Dev mode with the command `./mvnw quarkus:dev` or `quarkus dev`.
2. Open GraphiQL by visiting the URL [http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-graphql/graphql-ui](http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-graphql/graphql-ui).
3. Execute the `bookCreated` subscription query to receive real-time updates.
4. Create a new book using the `createBook` mutation and verify that GraphiQL receives the real-time update.

The `bookCreated` subscription query is similar to the one shown below:

```graphql
subscription ListenForNewBook {
  bookCreated {
    id
    title
    subTitle
    authors {
      firstName
      lastName
    }
  }
}
```

Once the subscription query is executed, GraphiQL will wait for real-time updates. Whenever a new book is created, GraphiQL will receive the real-time update with the book details.

Using cURL, we send a mutation to create a new book:

```shell
curl -X POST http://localhost:8080/api/graphql \
     -H "Content-Type: application/json" \
     -d '{
       "query": "
         mutation createBook {
           createBook(
             book: {
               title: \"Libro delle Subscription GraphQL\",
               subTitle: \"Creato con Quarkus + GraphQL\",
               isbn: \"7650986575646\",
               pages: 567,
               summary: \"Summary of the book\",
               publication: \"2025-01-28\",
               genre: \"fantasy\",
               languages: [\"IT\"],
               formats: [\"EPUB\", \"PDF\"],
               keywords: [\"key1\"],
               authors: [{ id: 5 }],
               editor: { id: 5 }
             }
           ) {
             id
             title
           }
         }
       "
     }'
```

Once the mutation is executed, GraphiQL will receive the real-time update with the details of the created book. Below is a demo showing how to test subscriptions with GraphiQL.

![Demo Subscription con GraphiQL](src/main/docs/resources/images/demo-subscription-graphql.gif)

From the demo, it is evident that GraphiQL receives real-time updates via WebSocket whenever a new book is created. This demonstrates how GraphQL subscriptions are useful for receiving real-time updates without having to make repeated requests.

## Accessing the MinIO Console
When you start the application in Dev mode, you can access the MinIO console to upload and download files or simply to check the status of the Object Store. To access the MinIO console, open your browser and visit the URL: <http://localhost:8080/q/dev-ui/extensions>, identify the box named Minio Client extension, and access the console from there.

![Minio Client extension for accessing the MinIO Console](src/main/docs/resources/images/box-ui-accesso-minio-console.jpg)

Figure 4 - Minio Client extension for accessing the MinIO Console

The following figure shows the MinIO console, where you can upload and download files, create and delete buckets, and view information about the Object Store.

![Console di MinIO](src/main/docs/resources/images/view-bucket-minio-dev.jpg)

Figure 5 - MinIO Console

## Creating the artifact and running the application

The application can be packaged using the command:

```bash
./mvnw package
```

This command generates the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Note that this is not an über-jar, as the dependencies are copied into the `target/quarkus-app/lib/` directory.

> If you want run the application via jar remember that you need a database and MinIO running.
> You can see the docker-compose file in the `src/main/docker` directory for more information
> on how to start the services.

The application can now be run using the command:

```bash
java -jar target/quarkus-app/quarkus-run.jar.
```

If you want to create an über-jar, run the following command:

```bash
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an über-jar, can be run with:

```bash
java -jar target/*-runner.jar.
```

## Creating a native executable

You can create a native executable using the command:

```bash
./mvnw package -Dnative
```

Alternatively, if you do not have GraalVM installed, you can create the native executable using a container with the command:

```bash
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

After creation, you can run your native executable with:

```bash
./target/quarkus-graphql-quickstart-1.0.0-SNAPSHOT-runner
```

## Running the application via Docker Compose and Podman Compose
The container images of the application are created through two GitHub actions defined in the files in the `.github/workflows` directory:

1. `docker_publish.yml` builds the container image in JVM mode for both ARM64 and AMD64 architectures and publishes it to Docker Hub.
2. `docker_publish_native_quarkus_cli.yml` builds the container image in Native mode for both ARM64 and AMD64 architectures and publishes it to Docker Hub. Using the Quarkus CLI, the native image is built and then packaged into a container image.

The created images are available on Docker Hub at the following link: 

1. JVM Mode - https://hub.docker.com/r/amusarra/quarkus-graphql-quickstart/tags
2. Native Mode - https://hub.docker.com/r/amusarra/quarkus-graphql-quickstart-native/tags

To build the container image in JVM mode, the reference Dockerfile is `Dockerfile.jvm` (in `src/main/docker/Dockerfile.jvm`) and for the Native mode container image, the reference Dockerfile is `Dockerfile.native` (in `src/main/docker/Dockerfile.native`).

The file [docker-compose.yml](src/main/docker/docker-compose.yml) was created to run the application in container mode. To run the application in container mode, execute the following commands:

```shell
# Export the environment variable to enable and enable the HTTPS protocol
export APP_KEYSTORE_PATH=$(pwd)/src/main/resources/app-keystore.p12
```

```shell
# Start the application via Podman Compose
# This command must be executed from the root directory of the project
podman compose -f src/main/docker/docker-compose.yml up
```

```shell
# Start the application via Docker Compose
# This command must be executed from the root directory of the project
docker-compose -f src/main/docker/docker-compose.yml up
```

```shell
# From Docker Compose version 2.0, the command is as follows.
docker compose -f src/main/docker/docker-compose.yml up
```

Once the stack of services configured in `docker-compose.yml` has started, the application will be available at: [http://localhost:8080](http://localhost:8080) and [https://localhost:8443](https://localhost:8443). The PostgreSQL database is available on the standard port 5432, while MinIO is available on port 9090. You can see the status of the service stack by running the `docker ps` or `podman ps` command. Below is an example of the command output.

The Quarkus application will be started when the PostgreSQL and MinIO services are available (see the `depends_on` and the `healthcheck`). 

```shell
CONTAINER ID  IMAGE                                                 COMMAND               CREATED       STATUS        PORTS                                                                   NAMES
35d4f6504190  quay.io/minio/minio:latest                            server /data --co...  24 hours ago  Up 5 seconds  0.0.0.0:9000-9001->9000-9001/tcp                                        minio-storage
d0311857c285  docker.io/library/postgres:16                         postgres              24 hours ago  Up 5 seconds  0.0.0.0:5432->5432/tcp                                                  postgres-db
386fdad9f4cb  docker.io/amusarra/quarkus-graphql-quickstart:latest                        24 hours ago  Up 5 seconds  0.0.0.0:8080->8080/tcp, 0.0.0.0:8443->8443/tcp, 0.0.0.0:9090->9090/tcp  quarkus-graphql
```

Below is an asciinema recording showing the execution of the application via Docker Compose.

[![asciicast](https://asciinema.org/a/oYeT9m9EThWaskjfFlNzY3M5x.svg)](https://asciinema.org/a/oYeT9m9EThWaskjfFlNzY3M5x)

### Running native mode
To run the application in native mode, you can use the `docker-compose-native-[arm64|amd64].yml` file. This file is a copy of the `docker-compose.yml` file, but with the image reference changed to the native mode image. To run the application in native mode, execute the commands:

```shell
# Export the environment variable to enable and enable the HTTPS protocol
export APP_KEYSTORE_PATH=$(pwd)/src/main/resources/app-keystore.p12
```

```shell
# Start the native application ARM64 via Podman Compose
# This command must be executed from the root directory of the project
podman compose -f src/main/docker/docker-compose-native-arm64.yml up
```

```shell
# Start the native application AMD64 via Podman Compose
# This command must be executed from the root directory of the project
podman compose -f src/main/docker/docker-compose-native-amd64.yml up
```

Below is an asciiinema recording showing the execution of the application in native mode via Podman Compose.

[![asciicast](https://asciinema.org/a/ex9EKhcENPFljm7AEJjLMw5IN.svg)](https://asciinema.org/a/ex9EKhcENPFljm7AEJjLMw5IN)

The Quarkus application will be started when the PostgreSQL and MinIO services are available (see the `depends_on` and the `healthcheck`).

## Guide to the services and extensions used

- ArC ([guide](https://quarkus.io/guides/cdi-reference)): Build time CDI dependency injection
- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
- SmallRye Health ([guide](https://quarkus.io/guides/smallrye-health)): Monitor service health
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - H2 ([guide](https://quarkus.io/guides/datasource)): Connect to the H2 database via JDBC
- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- GraphQL ([guide](https://quarkus.io/guides/smallrye-graphql)): Expose your services as a GraphQL endpoint
- MinIO ([guide](https://quarkus.io/extensions/io.quarkiverse.minio/quarkus-minio/)): Use MinIO as an Object Store S3
- OpenShift ([guide](https://quarkus.io/guides/deploying-to-openshift)): Generate OpenShift resources from annotations
- Using Podman with Quarkus ([guide](https://quarkus.io/guides/podman))
## Team Tools

[![alt tag](http://pylonsproject.org/img/logo-jetbrains.png)](https://www.jetbrains.com/?from=LiferayPortalSecurityAudit)

Antonio Musarra's Blog Team would like inform that JetBrains is helping by
provided IDE to develop the application. Thanks to its support program for
an Open Source projects!