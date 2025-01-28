# Quarkus GraphQL Quickstart con Supporto MinIO per lo Storage degli Oggetti

Questo progetto è una dimostrazione di un’applicazione Quarkus che espone dati attraverso una API RESTful (`quarkus-rest`) 
tradizionale e una API GraphQL (`quarkus-smallrye-graphql`). Il progetto utilizza Hibernate ORM con Panache 
(`quarkus-hibernate-orm-panache`) per la persistenza dei dati e include configurazioni per database H2 (per sviluppo), 
PostgreSQL (per profili di produzione) e MinIO come Object Store S3 (`io.quarkiverse.minio:quarkus-minio`).

MinIO è un server di storage degli oggetti ad alte prestazioni, distribuito e compatibile con Amazon S3 facilmente
integrabile con Quarkus per via dell'estensione `quarkus-minio` e dei DevService.

Il progetto segue la classica architettura a tre strati:

* Strato di Persistenza (ORM/Panache): utilizza Hibernate ORM con Panache per la gestione e la persistenza 
   dei dati nel database.
* Strato di Servizio/API (GraphQL e REST): espone i dati attraverso API GraphQL e API REST, fornendo 
   un’interfaccia per le interazioni.
* Strato di Presentazione (Opzionale): il progetto non include un livello di interfaccia utente dedicato.

Il diagramma mostrato a seguire evidenzia la stratificazione dell’applicazione.

![Stratificazione dell'applicazione](src/main/docs/resources/images/architettura-applicazione-quarkus-graphql-1.webp)

Figura 1 - Stratificazione dell'applicazione Quarkus GraphQL

## Caratteristiche

* Implementazione di un'API GraphQL con Quarkus.
* Utilizzo di Panache per la persistenza dei dati con Hibernate ORM.
* Schema GraphQL definito con tipi, query e mutation.
* Esempi di query per recuperare libri e autori.
* Esempi di mutation per creare, aggiornare ed eliminare libri e autori.
* Storage delle copertine dei libri su MinIO, sia in sviluppo (con Dev Services) che in produzione
* Test di esempio.

A seguire sono riportati i principali endpoint dell'applicazione.

| Endpoint       | Tipo    | Descrizione                                                              |
|----------------|---------|--------------------------------------------------------------------------|
| `/api/graphql` | GraphQL | Endpoint per l'API GraphQL                                               |
| `/api/books`   | REST    | Endpoint per l'API REST                                                  |
| `/api/s3/files`| REST    | Endpoint per l'API REST per il caricamento e il download di file su MinIO |
| `/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/schema-yaml` | Schema  | Endpoint dello schema OpenAPI in formato YAML      |
| `/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/schema-json` | Schema  | Endpoint dello schema OpenAPI in formato JSON      |
| `/q/dev-ui/io.quarkus.quarkus-smallrye-graphql/graphql-schema` | Schema  | Endpoint dello schema GraphQL                      |

Tabelle 1 - Endpoint principali dell'applicazione Quarkus GraphQL

## Requisiti

Per eseguire o sviluppare il progetto, assicurati di avere installati i seguenti strumenti:

* Git 2.33+
* JDK 21+, GraalVM 21+ (per la build nativa)
* tool per i container come Docker o Podman
* Apache Maven 3.9.9 (opzionale nel caso di uso del wrapper Maven integrato con il progetto di esempio);
* Quarkus CLI 3.17 (opzionale, ma consigliato);

## Quickstart

Per eseguire il progetto in modalità sviluppo (o dev) e testare le funzionalità GraphQL e REST, devi seguire i seguenti
passaggi:

1. clonazione del repository git del progetto;
2. avvio dell'applicazione in modalità sviluppo;
3. test delle API GraphQL e REST.
4. test delle API MinIO per il caricamento e il download di file.

Per clonare il repository git del progetto, esegui il comando:

```shell
# Clona il repository git del progetto
git clone https://github.com/amusarra/quarkus-graphql-quickstart.git
```

Per avviare l'applicazione in modalità sviluppo, esegui il comando:

```shell
# Avvia l'applicazione in modalità sviluppo
# Tramite il wrapper Maven integrato
./mvnw quarkus:dev

# Tramite il comando Quarkus CLI
quarkus dev
```

Per testare le API GraphQL e REST, apri il browser e visita i seguenti URL:

* API GraphQL usando la UI GraphiQL: <http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-graphql/graphql-ui>
* API REST usando la Swagger UI: <http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/swagger-ui>

Tramite la UI GraphiQL, puoi eseguire query e mutation per recuperare e modificare i dati del database.
A seguire è riportata una demo di esempio su come eseguire query GraphQL per recuperare e creare libri, autori ed editori
oltre a creare recuperare oggetti S3 dall'Object Store MinIO.

![Demo di esempio su come eseguire query GraphQL](src/main/docs/resources/images/demo-graphql-ui.gif)

A seguire le query di esempio utilizzate nella demo:

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

Tramite la Swagger UI, puoi eseguire richieste REST per recuperare, creare, aggiornare ed eliminare libri e autori oltre che caricare e scaricare file su MinIO.

![OpenAPI SwaggerUI per eseguire richieste REST](src/main/docs/resources/images/openapi-ui.jpg)


This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-graphql-quickstart-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- JDBC Driver - PostgreSQL ([guide](https://quarkus.io/guides/datasource)): Connect to the PostgreSQL database via JDBC
- SmallRye Health ([guide](https://quarkus.io/guides/smallrye-health)): Monitor service health
- Hibernate ORM with Panache ([guide](https://quarkus.io/guides/hibernate-orm-panache)): Simplify your persistence code for Hibernate ORM via the active record or the repository pattern
- JDBC Driver - H2 ([guide](https://quarkus.io/guides/datasource)): Connect to the H2 database via JDBC
- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
